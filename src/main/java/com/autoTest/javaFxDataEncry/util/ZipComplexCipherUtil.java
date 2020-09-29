package com.autoTest.javaFxDataEncry.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ZipComplexCipherUtil {
    /**
     * 加密1，winrar，好压2345可直接输入密码解压
     * 根据filepath读取文件并加密返回
     */
    public static void zipFileAndEncrypt(String filePath,String zipFileName,String password) {
        try {
            ZipParameters parameters = setParam(password);
            //压缩文件,并生成压缩文件
            ArrayList<File> filesToAdd = new ArrayList<File>();
            File file = new File(filePath);
            filesToAdd.add(file);

            ZipFile zipFile = new ZipFile(zipFileName);
            zipFile.addFiles(filesToAdd, parameters);//this line does works
            System.err.println("end");
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密1，winrar，好压2345可直接输入密码解压
     */
    public static void zipFileStream(InputStream is,String zipFileName,String password)
    {try {
        ZipParameters parameters = setParam(password);
        //addStream,多设置两个参数，缺一不可
        parameters.setFileNameInZip("yourfilename.xlsx");
        parameters.setSourceExternalStream(true);

        ZipFile zipFile = new ZipFile(zipFileName);
        zipFile.addStream(is, parameters);
    } catch (ZipException e) {
        e.printStackTrace();
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

    public static void main(String[] args) throws Exception {
        try{
            long l1 = System.currentTimeMillis();

//           //加密,addFile
//           ZipComplexCipherUtil.zipFileAndEncrypt("D:\\aaa\\zhmm_db.xlsx","D:\\aaa\\ccc.zip","123");
//          加密，addStream
            InputStream in = new FileInputStream("D:\\百行技术\\工具开发文档\\基于边界值和等价类划分的测试用例生成工具V002.doc");
            ZipComplexCipherUtil.zipFileStream(in,"D:\\百行技术\\CompressFile\\AAA工具开发文档001.zip","123");

            long l2 = System.currentTimeMillis();
            System.out.println((l2 - l1) + "毫秒.");
            System.out.println(((l2 - l1) / 1000) + "秒.");
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}