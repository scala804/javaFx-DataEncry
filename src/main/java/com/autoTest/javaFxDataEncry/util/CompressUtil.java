package com.autoTest.javaFxDataEncry.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 生成压缩文件 （zip，rar 格式）
 */
public class CompressUtil {
    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(CompressUtil.class);
    /**
     * @param path   要压缩的文件路径
     * @param format 生成的格式（zip、rar）d
     */
    public static void generateFile(String path, String format) throws Exception {
        ZipParameters parameters = new ZipParameters();
        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            throw new Exception("路径 " + path + " 不存在文件，无法进行压缩...");
        }
        // 用于存放压缩文件的文件夹
        String generateFile = file.getParent() + File.separator + "CompressFile";
        File compress = new File(generateFile);
        // 如果文件夹不存在，进行创建
        if (!compress.exists()) {
            compress.mkdirs();
        }

        // 目的压缩文件
        String generateFileName = compress.getAbsolutePath() + File.separator + "AAA" + file.getName() + "." + format;

        // 输入流 表示从一个源读取数据
        // 输出流 表示向一个目标写入数据

        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);

        // 压缩输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));

        generateFile(zipOutputStream, file, "");

        System.out.println("源文件位置：" + file.getAbsolutePath() + "，目的压缩文件生成位置：" + generateFileName);
        // 关闭 输出流
        zipOutputStream.close();
    }

    /**
     * @param out  输出流
     * @param file 目标文件
     * @param dir  文件夹
     * @throws Exception
     */
    private static void generateFile(ZipOutputStream out, File file, String dir) throws Exception {
        ArrayList<File> filesToAdd = new ArrayList<File>();
        // 当前的是文件夹，则进行一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();

            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + "/"));

            dir = dir.length() == 0 ? "" : dir + "/";

            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                generateFile(out, files[i], dir + files[i].getName());
            }

        } else { // 当前是文件

            // 输入流
            FileInputStream inputStream = new FileInputStream(file);
            // 标记要打包的条目
            out.putNextEntry(new ZipEntry(dir));


            // 进行写操作
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            // 关闭输入流
            inputStream.close();
        }

    }

    public static ZipParameters setParam(String password){
        //设置压缩文件参数
        ZipParameters parameters = new ZipParameters();
        //设置压缩方法
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        //设置压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        //设置压缩文件是否加密
        parameters.setEncryptFiles(true);
        //设置aes加密强度
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        //设置加密方法
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        //设置密码
        parameters.setPassword(password.toCharArray());
        return parameters;
    }

    /**
     *
     * @Title: zipFilesAndEncrypt
     * @Description: 将指定路径下的文件压缩至指定zip文件，并以指定密码加密,若密码为空，则不进行加密保护
     * @param srcFileName 待压缩文件路径
     * @param zipFileName zip文件名
     * @param password 加密密码
     * @return
     * @throws Exception
     */
    public static void zipFilesAndEncrypt(String srcFileName,String zipFileName,String password,String fileType) throws Exception{

        System.out.println("进入测试类");
        if(StringUtils.isEmpty(srcFileName) || StringUtils.isEmpty(zipFileName)){
            logger.error("请求的压缩路径或者文件名有误");
            return;
        }
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            if(!StringUtils.isEmpty(password)){
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                parameters.setPassword(password);
            }
            ArrayList<File> filesToAdd = new ArrayList<File>();
            File file=new File(srcFileName);
            File[] files;
            if(file.isDirectory())
            {
                files = file.listFiles();
                for(int i=0;i<files.length;i++){
                    filesToAdd.add(new File(srcFileName+files[i].getName()));
                    System.out.println("文件名称："+files[i].getName());
                }
            }
            else {
                filesToAdd.add(new File(srcFileName+file.getName()));
            }
            ZipFile zipFile = new ZipFile(srcFileName+zipFileName+"."+fileType);
            zipFile.addFiles(filesToAdd, parameters);
        }
        catch (Exception e) {
            System.out.println("文件压缩出错");
            logger.error("文件压缩出错", e);
            throw e;
        }
    }


    public static Boolean writeTXT(String path,String title,String content){
        Boolean booleanTmep;
        try {
            // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 写入Txt文件 */
            File writename = new File(path);// 相对路径，如果没有则要建立一个新的output。txt文件
            if(!writename.exists()){
                writename.mkdirs();
            }
            writename = new File(path+"\\"+title+".txt");// 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(content); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
            booleanTmep=true;
        } catch (Exception e) {
            booleanTmep=false;
            e.printStackTrace();
        }
        return booleanTmep;
    }


    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"".<br />
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;<br />
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
     * @param src 要压缩的文件或文件夹路径
     * @param dest 压缩文件存放路径
     * @param enryFileName 是否在压缩文件里创建目录,仅在压缩文件为目录时有效.<br />
     * 如果为false,将直接压缩目录下文件到压缩文件.
     * @param passwd 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径,如果为null则说明压缩失败.
     */
    public static String zipFile(String src, String dest,String enryFileName, String passwd,String format) {
        boolean isCreateDir=true;
        File srcFile = new File(src);
        dest = buildDestinationZipFilePath(srcFile, dest,enryFileName,format);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);    // 压缩级别
        if (!StringUtils.isEmpty(passwd)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
            parameters.setPassword(passwd.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File [] subFiles = srcFile.listFiles();
                    ArrayList<File> temp = new ArrayList<File>();
                    Collections.addAll(temp, subFiles);
                    zipFile.addFiles(temp, parameters);
                    return dest;
                }
                zipFile.addFolder(srcFile, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (net.lingala.zip4j.exception.ZipException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 构建压缩文件存放路径,如果不存在将会创建
     * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
     * @param srcFile 源文件
     * @param destParam 压缩目标路径
     * @return 正确的压缩文件存放路径
     */
    private static String buildDestinationZipFilePath(File srcFile,String destParam,String enryFileName,String format) {
        if (StringUtils.isEmpty(destParam)) {
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator +enryFileName + "."+format;
            } else {
                String fileName = enryFileName;
                destParam = srcFile.getParent() + File.separator + fileName + "."+format;
            }
        } else {
            createDestDirectoryIfNecessary(destParam);  // 在指定路径不存在的情况下将其创建出来
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = enryFileName;
                } else {
                    fileName =enryFileName;
                }
                destParam += fileName + "."+format;
            }
        }
        System.out.println("压缩的路径为"+destParam);
        return destParam;
    }

    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     * @param destParam 指定的存放路径,有可能该路径并没有被创建
     */
    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }



    // 测试
    public static void main(String[] args) {

        String path = "D:\\百行技术\\CompressFile\\";
        String format = "7z";
        String src="D:\\百行技术\\CompressFile\\test\\基于边界值和等价类划分的测试用例生成工具V002.doc";
        Boolean isCreateDir=true;
        String passwd="123";
        String fileName="edd";
        try {
            zipFile(src, path, fileName,  passwd,format);
           /* zipFilesAndEncrypt(path,"147","123",format);*/
} catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }




}

