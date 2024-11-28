package com.junyang.service;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.junyang.base.ResponseBase;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/certificate")
@Api(value = "证书管理",tags = "证书管理")
public interface CertificateService {
	
	@PostMapping("/randomNumber")
	@ApiOperation(value = "生成32位随机数",notes = "生成32位随机数",response = ResponseBase.class)
	ResponseBase randomNumber(String deviceSn);
	
	@PostMapping("/verification")
	@ApiOperation(value = "验证签名",notes = "验证签名",response = ResponseBase.class)
	ResponseBase verification(String deviceSn,String sign);
	
	@PostMapping("/upload")
	@ApiOperation(value = "导入证书",notes = "导入证书",response = ResponseBase.class)
	ResponseBase upload(MultipartFile pemCertificateFile, String serialNumber);
	
	@PostMapping("/findList")
	@ApiOperation(value = "查询证书",notes = "查询证书",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);
	
	@GetMapping("/downloadFile")
	@ApiOperation(value = "下载证书",notes = "查询证书",response = ResponseBase.class)
	void downloadFile(String id,HttpServletResponse response);
	
	@PostMapping("/uploadExcle")
	@ApiOperation(value = "批量导入证书",notes = "批量导入证书",response = ResponseBase.class)
	ResponseBase uploadExcle(MultipartFile file);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除证书",notes = "删除证书",response = ResponseBase.class)
	ResponseBase delete(String id);

}
