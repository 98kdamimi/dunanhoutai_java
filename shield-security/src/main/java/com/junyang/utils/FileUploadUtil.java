package com.junyang.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.junyang.base.FileResponse;
import com.junyang.constants.Constants;
import com.junyang.enums.PublicEnums;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@Slf4j
public class FileUploadUtil {
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replaceAll("-", "");
		return id;
	}

	public static Map<String, MultipartFile> getFilesByUniapp(HttpServletRequest request) {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> map = mulReq.getFileMap();
			return map;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param file      上传文件
	 * @param folder    上传磁盘地址
	 * @param fileFlode 文件夹名称
	 * @return
	 */
	public static FileResponse uploadFile(MultipartFile file, String folder, String fileFlode) {
		FileResponse fileResponse = new FileResponse();
		String url = "";
		String fileName = "";
		if (file != null) {
			if (!file.isEmpty()) {
				BufferedOutputStream stream = null;
				try {
					// 获取文件名
					fileName = file.getOriginalFilename();
					// 获取文件的后缀名
					String suffixName = fileName.substring(fileName.lastIndexOf("."));
					// 给新文件拼上时间毫秒，防止重名
					long nowfileName = System.currentTimeMillis();
					// 设置文件存储路径
					String filePath = folder + fileFlode + File.separator;
					File dir = new File(filePath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String path = filePath + nowfileName + suffixName;
					File dest = new File(path);

					byte[] bytes = file.getBytes();
					// 文件写入
					stream = new BufferedOutputStream(new FileOutputStream(dest));
					stream.write(bytes);
					stream.close();
					url = fileFlode + nowfileName + suffixName;
					if (FileUploadUtil.isImage(dest)) {
						fileResponse.setLable(PublicEnums.ONE.getIndex());
					} else {
						fileResponse.setLable(PublicEnums.TOW.getIndex());
					}
					fileResponse.setCode(Constants.HTTP_RES_CODE_200);
					fileResponse.setMsg(Constants.SUCCESS);
					fileResponse.setUrl(url);
					fileResponse.setPath(path);
					fileResponse.setFilename(fileName);
				} catch (Exception e) {
					stream = null;
					e.printStackTrace();
					fileResponse.setCode(Constants.HTTP_RES_CODE_500);
					fileResponse.setMsg(Constants.ERROR);
				}
			}
		} else {
			fileResponse.setCode(Constants.HTTP_RES_CODE_404);
			fileResponse.setMsg(Constants.NULL_CONTETN);
		}
		return fileResponse;
	}
	
	
	public static FileResponse uploadFile2(MultipartFile file, String folder, String fileFlode) {
		FileResponse fileResponse = new FileResponse();
		String url = "";
		String fileName = "";
		if (file != null) {
			if (!file.isEmpty()) {
				BufferedOutputStream stream = null;
				try {
					// 获取文件名
					fileName = file.getOriginalFilename();
					// 获取文件的后缀名
					String suffixName = fileName.substring(fileName.lastIndexOf("."));
					// 给新文件拼上时间毫秒，防止重名
					long nowfileName = System.currentTimeMillis();
					// 设置文件存储路径
					String filePath = folder + fileFlode;

					File dir = new File(filePath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String path = filePath + nowfileName + suffixName;
					File dest = new File(path);

					byte[] bytes = file.getBytes();
					// 文件写入
					stream = new BufferedOutputStream(new FileOutputStream(dest));
					stream.write(bytes);
					stream.close();
					url = fileFlode + nowfileName + suffixName;
					if (FileUploadUtil.isImage(dest)) {
						fileResponse.setLable(PublicEnums.ONE.getIndex());
					} else {
						fileResponse.setLable(PublicEnums.TOW.getIndex());
					}
					fileResponse.setCode(Constants.HTTP_RES_CODE_200);
					fileResponse.setMsg(Constants.SUCCESS);
					fileResponse.setUrl(url);
					fileResponse.setPath(path);
					fileResponse.setFilename(fileName);
				} catch (Exception e) {
					stream = null;
					e.printStackTrace();
					fileResponse.setCode(Constants.HTTP_RES_CODE_500);
					fileResponse.setMsg(Constants.ERROR);
				}
			}
		} else {
			fileResponse.setCode(Constants.HTTP_RES_CODE_404);
			fileResponse.setMsg(Constants.NULL_CONTETN);
		}
		return fileResponse;
	}

	
	public static FileResponse uploadFilesc(File file, String folder, String fileFlode) {
		FileResponse fileResponse = new FileResponse();
		String url = "";
		String fileName = "";
		if (file != null) {
			if (file != null) {
				BufferedOutputStream stream = null;
				try {
					// 获取文件名
					fileName = file.getName();
					// 获取文件的后缀名
					String suffixName = fileName.substring(fileName.lastIndexOf("."));
					// 给新文件拼上时间毫秒，防止重名
					long nowfileName = System.currentTimeMillis();
					// 设置文件存储路径
					String filePath = folder + fileFlode + File.separator;
					File dir = new File(filePath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String path = filePath + nowfileName + suffixName;
					File dest = new File(path);

					byte[] bytes = File2byte(file);
					// 文件写入
					stream = new BufferedOutputStream(new FileOutputStream(dest));
					stream.write(bytes);
					stream.close();
					url = fileFlode + nowfileName + suffixName;
					if (FileUploadUtil.isImage(dest)) {
						fileResponse.setLable(PublicEnums.ONE.getIndex());
					} else {
						fileResponse.setLable(PublicEnums.TOW.getIndex());
					}
					fileResponse.setCode(Constants.HTTP_RES_CODE_200);
					fileResponse.setMsg(Constants.SUCCESS);
					fileResponse.setUrl(url);
					fileResponse.setPath(path);
					fileResponse.setFilename(fileName);
				} catch (Exception e) {
					stream = null;
					e.printStackTrace();
					fileResponse.setCode(Constants.HTTP_RES_CODE_500);
					fileResponse.setMsg(Constants.ERROR);
				}
			}
		} else {
			fileResponse.setCode(Constants.HTTP_RES_CODE_404);
			fileResponse.setMsg(Constants.NULL_CONTETN);
		}
		return fileResponse;
	}

	public static void filedelete(String path) {
		if (StringUtils.isBlank(path))
			return;
		File file = new File(path);
		if (file.exists()) {
			if (file.delete()) {
				log.info("删除成功");
			} else {
				log.info("删除失败");
			}
		} else {
			log.info("文件不存在");
		}
	}


	/**
	 * 存储文件123.jpg
	 * 例如存储到 ” D:/wellBeing/headImg/ “文件夹下
	 *
	 * @param file       上传的文件
	 * @param parentFolder  父文件夹地址 D:/wellBeing/
	 * @param targetFolder 目标文件夹名称 headImg/
	 * @return
	 */
	public static FileResponse upload(MultipartFile file, String parentFolder, String targetFolder) {
		FileResponse fileResponse = new FileResponse();
		String url = "";
		String fileName = "";
		if (file != null) {
			if (!file.isEmpty()) {
				BufferedOutputStream stream = null;
				try {
					// 获取文件名
					fileName = file.getOriginalFilename();
					if (fileName==null)
						fileName= "";
					// 获取文件的后缀名
					int index = fileName.lastIndexOf(".");
					String suffixName;
					if (index > 0)
						suffixName = fileName.substring(index);
					else
						suffixName=".unknow";

					// 给新文件拼上时间毫秒，防止重名
					long nowfileName = System.currentTimeMillis();
					// 设置文件存储路径
					String filePath = parentFolder + targetFolder; // D:/wellBeing/headImg/
					File dir = new File(filePath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String path = filePath + nowfileName + suffixName;// D:/wellBeing/headImg/123.jpg
					File dest = new File(path);

					byte[] bytes = file.getBytes();
					// 文件写入
					stream = new BufferedOutputStream(Files.newOutputStream(dest.toPath()));
					stream.write(bytes);
					stream.close();
					url = targetFolder + nowfileName + suffixName;// headImg/123.jpg
					if (FileUploadUtil.isImage(dest)) {
						fileResponse.setLable(PublicEnums.ONE.getIndex());
					} else {
						if (FileUploadUtil.imgLocation(dest)) {
							fileResponse.setLable(PublicEnums.ZERO.getIndex());
						}
						fileResponse.setLable(PublicEnums.TOW.getIndex());
					}
					fileResponse.setCode(Constants.HTTP_RES_CODE_200);
					fileResponse.setMsg(Constants.SUCCESS);
					fileResponse.setUrl(url);
					fileResponse.setPath(path);
					fileResponse.setFilename(fileName);
				} catch (Exception e) {
					stream = null;
					e.printStackTrace();
					fileResponse.setCode(Constants.HTTP_RES_CODE_500);
					fileResponse.setMsg(Constants.ERROR);
				}
			}
		} else {
			fileResponse.setCode(Constants.HTTP_RES_CODE_404);
			fileResponse.setMsg(Constants.NULL_CONTETN);
		}
		return fileResponse;
	}

	public static void delete(String folder, String relativePath) {
		String path = folder + relativePath;
		filedelete(path);
	}


//	/**
//	 * 视频取帧
//	 *
//	 * @param videofile 视频地址
//	 * @param framefile 图片储存地址
//	 */
//	   @SuppressWarnings("resource")
//	public static void cutout(String videofile, String framefile) {
//	        try {
//	            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
//	            ff.start();
//	            int lenght = ff.getLengthInFrames();
//	            int i = 0;
//	            Frame f = null;
//	            while (i < lenght) {
//	                // 过滤前5帧，避免出现全黑的图片
//	                f = ff.grabFrame();
//	                if ((i > 5) && (f.image != null)) {
//	                    break;
//	                }
//	                i++;
//	            }
//	            int owidth = f.imageWidth;
//	            int oheight = f.imageHeight;
//	            // 对截取的帧进行等比例缩放
//	            int width = 800;
//	            int height = (int) (((double) width / owidth) * oheight);
//	            Java2DFrameConverter converter = new Java2DFrameConverter();
//	            BufferedImage fecthedImage = converter.getBufferedImage(f);
//	            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//	            bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
//	            File targetFile = new File(framefile);
//	            try {
//	                ImageIO.write(bi, "jpg", targetFile);
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	            }
//	            ff.stop();
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	    }
//	   
	public static boolean imgLocation(File file) throws IOException {
		String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
		Pattern p = Pattern.compile(reg);
		boolean boo = p.matcher(file.getName()).find();
		return boo;
	}

	/**
	 * @category 获取文件大小
	 * @param mulfile
	 * @return
	 */
	public static String GetFileSize(MultipartFile mulfile) {
		String size = "";
		long fileS = mulfile.getSize();
		DecimalFormat df = new DecimalFormat("#.00");
		if (fileS < 1024) {
			size = df.format((double) fileS) + "BT";
		} else if (fileS < 1048576) {
			size = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			size = df.format((double) fileS / 1048576) + "MB";
		} else {
			size = df.format((double) fileS / 1073741824) + "GB";
		}
		return size;
	}

	/**
	 * @category 获取文件大小
	 * @param file
	 * @return
	 */
	public static String findFileSize(File file) {
		String size = "";
		if (file.exists() && file.isFile()) {
			long fileS = file.length();
			DecimalFormat df = new DecimalFormat("#.00");
			if (fileS < 1024) {
				size = df.format((double) fileS) + "BT";
			} else if (fileS < 1048576) {
				size = df.format((double) fileS / 1024) + "KB";
			} else if (fileS < 1073741824) {
				size = df.format((double) fileS / 1048576) + "MB";
			} else {
				size = df.format((double) fileS / 1073741824) + "GB";
			}
		} else if (file.exists() && file.isDirectory()) {
			size = "";
		} else {
			size = "0BT";
		}
		return size;
	}

	/**
	 * 判断是否是图片
	 * 
	 * @param file
	 * @return
	 */
	private static boolean isImage(File file) {
		if (!file.exists()) {
			return false;
		}
		BufferedImage image = null;
		try {
			// 如果是spring中MultipartFile类型，则代码如下

			// image = ImageIO.read(file.getInputStream());
			image = ImageIO.read(file);
			if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将文件转换成byte数组
	 * 
//	 * @param filePath
	 * @return
	 */
	public static byte[] File2byte(File tradeFile) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(tradeFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	//判断是否是excel格式文件
	@SuppressWarnings("unused")
	public static boolean isExcelFile(MultipartFile file) {
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	public static boolean isImage(MultipartFile file) {
	    String contentType = file.getContentType();
	    return contentType != null && contentType.startsWith("image/");
	}
	

}