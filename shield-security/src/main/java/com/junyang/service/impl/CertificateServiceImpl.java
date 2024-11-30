package com.junyang.service.impl;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.device.DeviceCertificateEntity;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.CertificateService;
import com.junyang.utils.ExcelUtils;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.RedisUtil;

@RestController
@Transactional
@CrossOrigin
public class CertificateServiceImpl extends BaseApiService implements CertificateService {

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@SysLogAnnotation(module = "证书管理", type = "GET", remark = "生成32位随机数")
	public ResponseBase randomNumber(String deviceSn) {
		byte[] randomBytes = new byte[32];
		new SecureRandom().nextBytes(randomBytes);
		String num = Base64.getEncoder().encodeToString(randomBytes);
		redisUtil.set(deviceSn, num);
		return setResultSuccess(num, Constants.SUCCESS);
	}

	@Override
	@SysLogAnnotation(module = "证书管理", type = "POST", remark = "验证签名")
	public ResponseBase verification(String deviceSn, String sign) {
		try {
			String num = redisUtil.get(deviceSn).toString();
			if(num != null) {
//				PublicKey publicKey = extractPublicKeyFromCertificate("E:\\test\\certificate.pem");
				Query query = new Query();
				query.addCriteria(Criteria.where("serialNumber").is(deviceSn));
				DeviceCertificateEntity certificateEntity = mongoTemplate.findOne(query, DeviceCertificateEntity.class);
				if(certificateEntity != null && certificateEntity.getCertificatePem() != null 
						&& certificateEntity.getCertificatePem().length() > 0) {
					PublicKey publicKey = extractPublicKeyFromPem(certificateEntity.getCertificatePem());
					byte[] numBytes = Base64.getDecoder().decode(num);
					byte[] signBytes = Base64.getDecoder().decode(sign);
					boolean isValid = verifySignature(numBytes, signBytes, publicKey);
					System.out.println(isValid);
					if (isValid) {
						return setResultSuccess("签名验证成功");
					} else {
						return setResultError("签名验证失败");
					}
				}else {
					return setResultError("签名验证失败");
				}
				
			}else {
				return setResultError("签名验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return setResultError("签名验证失败，异常发生：" + e.getMessage());
		}
	}

	// 读取证书文件并提取公钥
	public PublicKey extractPublicKeyFromCertificate(String certificateFilePath) throws Exception {
		FileInputStream certFile = new FileInputStream(certificateFilePath);
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(certFile);
		return certificate.getPublicKey(); // 提取公钥
	}
	
	// 从 PEM 格式的证书内容中提取公钥
    public static PublicKey extractPublicKeyFromPem(String certificatePem) throws Exception {
        String certPemCleaned = certificatePem.replace("-----BEGIN CERTIFICATE-----", "")
                                              .replace("-----END CERTIFICATE-----", "")
                                              .replaceAll("\\s", "");
        byte[] decodedCert = Base64.getDecoder().decode(certPemCleaned);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new java.io.ByteArrayInputStream(decodedCert));
        return certificate.getPublicKey();
    }

	// 验证签名
	public boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
		Signature signature = Signature.getInstance("SHA256withECDSA", new BouncyCastleProvider());
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(signatureBytes); // 验证签名是否有效
	}

	@Override
	@SysLogAnnotation(module = "证书管理", type = "POST", remark = "导入证书")
	public ResponseBase upload(@RequestParam("pemCertificateFile") MultipartFile pemCertificateFile,
			@RequestParam("serialNumber") String serialNumber) {
		try {
			// 解析 PEM 证书文件内容
			String pemContent = readPemFile(pemCertificateFile);
			X509CertificateHolder certHolder = parsePemCertificate(pemContent);
			// 创建设备证书实体
			DeviceCertificateEntity deviceCertificate = new DeviceCertificateEntity();
			deviceCertificate.setId(ObjectId.get().toString()); // 设置唯一ID
			deviceCertificate.setSerialNumber(serialNumber);
			deviceCertificate.setCertificatePem(pemContent);
			deviceCertificate.setFileSize(FileUploadUtil.GetFileSize(pemCertificateFile));
			deviceCertificate.setFileName(pemCertificateFile.getOriginalFilename());
			deviceCertificate.setValid(true); // 假设证书有效，具体可以根据实际验证结果
			GenericityUtil.setDate(deviceCertificate);
			// 保存证书到数据库
			mongoTemplate.insert(deviceCertificate);
			return setResultSuccess("证书导入成功");
		} catch (Exception e) {
			e.printStackTrace();
			return setResultError(Constants.ERROR);
		}
	}

	/**
	 * 读取 PEM 文件内容
	 * 
	 * @param pemCertificateFile 上传的 PEM 文件
	 * @return 证书内容字符串
	 * @throws IOException 读取文件时可能出现的异常
	 */
	private String readPemFile(MultipartFile pemCertificateFile) throws IOException {
		StringBuilder pemContent = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(pemCertificateFile.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				pemContent.append(line).append("\n");
			}
		}
		return pemContent.toString();
	}

	/**
	 * 解析 PEM 格式证书
	 * 
	 * @param pemCertificate PEM 格式证书内容
	 * @return 解析后的 X509CertificateHolder
	 * @throws Exception 解析异常
	 */
	private X509CertificateHolder parsePemCertificate(String pemCertificate) throws Exception {
		try (PEMParser pemParser = new PEMParser(new StringReader(pemCertificate))) {
			Object parsedObject = pemParser.readObject();
			if (parsedObject instanceof X509CertificateHolder) {
				return (X509CertificateHolder) parsedObject;
			} else {
				throw new Exception("证书格式无效");
			}
		}
	}

	@Override
	@SysLogAnnotation(module = "证书管理", type = "POST", remark = "查询证书")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if (entity.getSerialNumber() != null && !entity.getSerialNumber().isEmpty()) {
				query.addCriteria(Criteria.where("serialNumber").is(entity.getSerialNumber()));
			}
			long totalCount = mongoTemplate.count(query, DeviceCertificateEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "setTime"));
			query.with(pageRequest);
			// 执行分页查询
			List<DeviceCertificateEntity> list = mongoTemplate.find(query, DeviceCertificateEntity.class);
			// 获取总记录数
			PageInfo<DeviceCertificateEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "证书管理", type = "GET", remark = "下载证书")
	public void downloadFile(String id, HttpServletResponse response) {
		try {
			// 从 MongoDB 查询到文件实体
			DeviceCertificateEntity entity = mongoTemplate.findById(id, DeviceCertificateEntity.class);
			if (entity == null || entity.getCertificatePem() == null || entity.getCertificatePem().isEmpty()) {
				throw new RuntimeException("没有相关数据");
			}
			// 获取文件名并进行 URL 编码
			String fileName = URLEncoder.encode(entity.getFileName(), StandardCharsets.UTF_8.toString()) + ".pem"; // 添加扩展名
			byte[] certificateBytes = entity.getCertificatePem().getBytes(StandardCharsets.UTF_8);
			// 设置响应头
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-pem-file"); // 设置 MIME 类型为 PEM 文件类型
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 写入文件内容到输出流
			try (ServletOutputStream outputStream = response.getOutputStream()) {
				outputStream.write(certificateBytes);
				outputStream.flush();
			} catch (IOException e) {
				throw new RuntimeException("文件下载失败", e);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("文件下载失败", ex);
		}
	}

	@Override
	@SysLogAnnotation(module = "证书管理", type = "POST", remark = "批量导入证书")
	public ResponseBase uploadExcle(MultipartFile file) {
		try {
			if(file != null) {
				List<DeviceCertificateEntity> list = ExcelUtils.importData(file, DeviceCertificateEntity.class);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query();
						if (list.get(i).getSerialNumber() != null && !list.get(i).getSerialNumber().isEmpty()) {
							query.addCriteria(Criteria.where("serialNumber").is(list.get(i).getSerialNumber()));
						}
						List<DeviceCertificateEntity> numList = mongoTemplate.find(query, DeviceCertificateEntity.class);
						if(numList != null && numList.size() > 0) {
							continue;
						}else {
							GenericityUtil.setDate(list.get(i));
							mongoTemplate.insert(list.get(i));
						}
					}
				}
			}
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(String id) {
		try {
	        Query query = new Query(Criteria.where("_id").is(id));
	        mongoTemplate.remove(query, DeviceCertificateEntity.class);
	        return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
}
