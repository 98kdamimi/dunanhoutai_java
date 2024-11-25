package com.junyang.utils;
import java.io.*;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
 
/**
 * 压缩类
 * @author xrj
 * @date 2019/10/31
 */
public class ZipCompressor {
 
    static final int BUFFER = 8192;
 
    /**
     * 压缩的文件夹
     */
    private static File zipFile;
 
    public ZipCompressor(String pathName) {
        zipFile = new File(pathName);
    }
 
 
    /**
     * 遍历需要压缩文件集合
     * @param pathName
     * @throws IOException
     */
    public static void compress(String... pathName) throws IOException {
        ZipOutputStream out =null;
        FileOutputStream fileOutputStream=null;
        CheckedOutputStream cos=null;
        try {
            fileOutputStream = new FileOutputStream(zipFile);
            cos = new CheckedOutputStream(fileOutputStream,new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (int i=0;i<pathName.length;i++){
                compress(new File(pathName[i]), out, basedir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(out!=null){
                out.close();
            }
            if(fileOutputStream!=null){
                fileOutputStream.close();
            }
            if(cos!=null){
                cos.close();
            }
        }
    }
    
    /**
     * 压缩
     * @param file
     * @param out
     * @param basedir
     */
    private static void compress(File file, ZipOutputStream out, String basedir) throws IOException {
        // 判断是目录还是文件
        if (file.isDirectory()) {
           compressDirectory(file, out, basedir);
        } else {
           compressFile(file, out, basedir);
        }
    }
    
    /**
     * 压缩一个目录
     * */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) throws IOException {
        if (!dir.exists()){
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 递归
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }
    
    /**
     * 压缩一个文件
     *
     * */
    private static void compressFile(File file, ZipOutputStream out, String basedir) throws IOException {
        if (!file.exists()) {
            return;
        }
        BufferedInputStream bis =null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(bis!=null){
                bis.close();
            }
        }
    }
    
    
    
    /**
     * 下载压缩包
     * @param file
     * @param response
     * @return
     */
    public static HttpServletResponse downloadZip(File file,HttpServletResponse response) {
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            // 以流的形式下载文件。
             fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();
             toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
     
            //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            toClient.write(buffer);
            toClient.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            try {
                File f = new File(file.getPath());
                f.delete();
               if(fis!=null){
                   fis.close();
               }
               if(toClient!=null){
                   toClient.close();
               }
     
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }
    
    

    
    /**
     * @category zip解壓
     * @param zipFileName
     * @param outputDirectory
     * @throws Exception
     */
    public static void unZip(String zipFileName, String outputDirectory) throws Exception {
        ZipFile zipFile = new ZipFile(zipFileName);
        try {
            Enumeration<?> e = zipFile.entries();
            ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');
                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                    }
                    File f = new File(outputDirectory + File.separator + zipEntry.getName());
                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);
                    byte[] by = new byte[1024];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            zipFile.close();
        }
    }



    private static void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true) {
                fl.mkdir();
            } else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false) {
                    	  subFile.mkdir();
                    }
                      
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String readTxtFile(String filePath) throws IOException{
    	 InputStreamReader read = null;
    	 BufferedReader bufferedReader = null;
        try {
                String encoding="GBK";
                File file=new File(filePath);
               
                if(file.isFile() && file.exists()){ //判断文件是否存在
                	read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
					bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        return lineTxt;
                    }
                    
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }finally{
        	read.close();
            bufferedReader.close();
        }
		return null;

    }
   
   
}
