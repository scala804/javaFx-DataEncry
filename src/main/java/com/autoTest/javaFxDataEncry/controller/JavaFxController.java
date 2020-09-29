package com.autoTest.javaFxDataEncry.controller;


import com.autoTest.javaFxDataEncry.util.Sm3Utils;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.autoTest.javaFxDataEncry.base.ConsField.*;
import static com.autoTest.javaFxDataEncry.util.CompressUtil.*;
import static com.autoTest.javaFxDataEncry.util.EasyExcelUtil.writeExcelByString;
import static com.autoTest.javaFxDataEncry.util.ExcelUtils.readExcelBySheetName;
import static com.autoTest.javaFxDataEncry.util.PopupUtil.*;
import static com.autoTest.javaFxDataEncry.util.encryUtils.getSHA256;
import static com.autoTest.javaFxDataEncry.util.encryUtils.stringToMD5;


public class JavaFxController {

    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(JavaFxController.class);
    @FXML
    private ChoiceBox ChoiceBoxEncryType;

    @FXML
    private Button local_changeDirButton;
    @FXML
    private Button result_changeDirButton;

    @FXML
    private JFXTextField encryFilePath;
    @FXML
    private JFXTextField encryFileResultPath;


    @FXML
    private RadioButton RadioButtonZip;
    @FXML
    private RadioButton RadioButtonRar;
    @FXML
    private RadioButton RadioButton7z;
    @FXML
    private ImageView  imageViewId;
    @FXML
    private TextField resultEncry;

    private Stage dialogStage;

    private final Desktop desktop = Desktop.getDesktop();

    final FileChooser fileChooser = new FileChooser();

    RadioButton radioButton;
    String encryPtionFilePathStr;
    String fileFormtStr;
    String fileName;
    Boolean successBoolean=true;
    String encryFilePathAndName;
    /**界面初始**/
    @FXML
    private void initialize() {
        /**初始化加密方式**/
        ChoiceBoxEncryType.getItems().addAll("SHA256", "MD5", "SM3");
        ChoiceBoxEncryType.getSelectionModel().selectFirst();
        System.out.println("加密类型: " +  ChoiceBoxEncryType.getSelectionModel().getSelectedIndex());

        /**监听初始化加密方式的选择类型**/
        ChoiceBoxEncryType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println(newValue.toString());
            }
        });

        /**初始化加密文件路径**/
        Stage stage=new Stage();
        local_changeDirButton.setOnAction(
                (final ActionEvent e) -> {
                    fileChooser.setTitle("选择加密文件路径");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xlsx Files", "*.xlsx"));
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xls Files", "*.xls"));
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv Files", "*.csv"));
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                     System.out.println(file.getAbsolutePath());
                        encryFilePath.setText(file.getAbsolutePath().trim());
                     }
                });
        /**初始化加密结果文件**/
        result_changeDirButton.setOnAction(
                (final ActionEvent e) -> {
                    fileChooser.setTitle("选择加密结果目录");
                    File  file;
                    if(encryPtionFilePathStr!=null){
                        file=new File(encryPtionFilePathStr);
                        fileChooser.setInitialDirectory(file);
                        if (file != null) {
                            openFile(file);
                        }
                    }else {
                        dataIsEmptyStage(dialogStage, "浏览加密文件", "还没有完成加密，没有加密文件路径");
                    }

                });

        /**初始化加密文件后的压缩格式**/
        ToggleGroup group = new ToggleGroup();
        RadioButtonZip.setToggleGroup(group);
        RadioButtonZip.setSelected(true);
        RadioButtonRar.setToggleGroup(group);
        RadioButton7z.setToggleGroup(group);
        /**监听初始化加密文件压缩格式选择**/
        radioButton= (RadioButton) group.getSelectedToggle();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (group.getSelectedToggle() != null) {
                    radioButton= (RadioButton) group.getSelectedToggle();
                    System.out.println("压缩格式: " + radioButton.getText());
                }
            }
        });

        ImageIcon imageIcon = new ImageIcon(JavaFxController.class.getResource("/icons/baihangCredit.png"));
        String tempImagePath=imageIcon.toString();
        Image image = new Image(String.valueOf(tempImagePath));
        imageViewId.setImage(image);
    }

   /**打开文件**/
    private void openFile(File file) {
        EventQueue.invokeLater(() -> {
            try {
                desktop.open(file);
                encryFileResultPath.setText(file.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(JavaFxController.
                        class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }


   /**加密操作**/
   @FXML
    public void handleEncry(){
        if(inputInvalid()){
            /**获取选择的加密类型**/
          int ChoiceBoxEncryTypeInt=ChoiceBoxEncryType.getSelectionModel().getSelectedIndex();
          String ChoiceBoxEncryTypeStr="SHA256";
          if(ChoiceBoxEncryTypeInt==0){
              ChoiceBoxEncryTypeStr="SHA256";
          }
          if(ChoiceBoxEncryTypeInt==1){
              ChoiceBoxEncryTypeStr="MD5";
          }
            if(ChoiceBoxEncryTypeInt==2){
                ChoiceBoxEncryTypeStr="SM3";
            }
          /**获取加密文件的路径**/
          String  encryFilePathStr=encryFilePath.getText().trim();
          /**获取加密后保存的压缩格式**/
          String radioButtonStr = radioButton.getText();
          /**获取加密的密码**/
          String resultEncryStr=resultEncry.getText();
          

          /**从Excel表中读取数据**/

            List<Map<String, Object>> mapList=readExcelBySheetName(encryFilePathStr,"");
            if(!successBoolean){
                dataInvalidStage(dialogStage, "读取数据失败", "读取数据失败，请检查加密数据的格式");
                return;
            }
          /**加密后生成的文件名及路径**/
            successBoolean=getEnyFilePath(encryFilePathStr);
            if(!successBoolean){
                dataInvalidStage(dialogStage, "获取生成文件名及路径", "获取生成文件名及路径失败，请检查生成文件的路径");
                return;
            }
            /**加密后生成的文件名及路径**/
            successBoolean=getEncryFilePathAndName();
            if(!successBoolean){
                dataInvalidStage(dialogStage, "加密后生成的文件名及路径", "加密后生成的文件名及路径失败，请检查生成文件的路径");
                return;
            }

            /**对excel表中的三元数姓名（需要加密）、手机号（需要加密）、电话（需要加密）进行加密**/
            successBoolean=encryExcelDate(ChoiceBoxEncryTypeStr,mapList);
            if(!successBoolean){
                dataInvalidStage(dialogStage, "加密字段内容", "加密字段内容失败，请检查加密的文件是否符合要求");
                return;
            }

            /**生成写入到excel表中的数据串**/
            List<List<String>> dataList=new ArrayList<>();
            successBoolean=getDataListBoolean(dataList,mapList);
            if(!successBoolean){
                dataIsEmptyStage(dialogStage, "获取加密后的字段内容", "获取加密后的字段内容失败");
                return;
            }
            /**写入到excel表中**/
            String output="output";
            successBoolean=writeExcelByString(encryFilePathAndName, output,dataList);
            if(!successBoolean){
                dataInvalidStage(dialogStage, "加密后写入到excel表中", "加密后写入到excel表中失败");
                return;
            }

            int fileNamePixInt=fileName.lastIndexOf("-") ;
            String zipFileName;
            if(fileNamePixInt>1){
                zipFileName=fileName.substring(0,fileNamePixInt+1)+"output";
            }else {
                zipFileName=fileName+"output";
            }
            try {
                zipFile(encryFilePathAndName, encryPtionFilePathStr+"\\", zipFileName,  resultEncryStr,radioButtonStr);
            } catch (Exception e) {
                successBoolean=false;
                dataInvalidStage(dialogStage, "压缩文件", "压缩加密文件失败！");
                return;
            }
            /**把密码写入到txt**/
            String title=zipFileName+"-解密密码";
            String content=title+":"+resultEncryStr;
            successBoolean=writeTXT( encryPtionFilePathStr, title, content);
            if(!successBoolean){
                dataInvalidStage(dialogStage, "压缩密码写入到txt", "压缩密码写入到txt失败！");
                return;
            }
            /**把压缩后的文件路径展现给用户**/
            encryFileResultPath.setText(encryPtionFilePathStr);
             if(!successBoolean){
                 dataInvalidStage(dialogStage, "加密数据", "加密数据失败");
             }else {
                 SuccessHints(dialogStage, "加密数据", "加密数据成功");
             }

        }else {
            System.out.println("加密文件输入框为空！");
        }
   }

    private Boolean getDataListBoolean(List<List<String>> dataList,List<Map<String, Object>> mapList) {
        int t=0;
       try {
           if(mapList.size()>0){
               if(!mapList.get(0).isEmpty()){
                   Map  map=mapList.get(0);
                   int m=0;
                   List<String> listStr = new ArrayList<>();
                   for (Object key : map.keySet()) {
                       listStr.add(m,key.toString());
                       m++;
                   }
                   dataList.add(t,listStr);
               }
               for(int i=0;i<mapList.size();i++){
                   Map  map=mapList.get(i);
                   int m=0;
                   t++;
                   List<String> listStr = new ArrayList<>();
                   for (Object key : map.keySet()) {
                       if(map.get(key)!=null){
                           listStr.add(m,map.get(key).toString());
                       }else {
                           listStr.add(m,"");
                       }
                       m++;
                   }
                   dataList.add(t,listStr);
               }
           }
       }catch (Exception e){
           successBoolean=false;
       }
       return successBoolean;
    }


    public  Boolean getEncryFilePathAndName(){
      try{
          int  indexTempInt=fileName.lastIndexOf("-");
          if(indexTempInt>0){
              String tempS=fileName.substring(0,indexTempInt+1);
              encryFilePathAndName=encryPtionFilePathStr+File.separator+tempS+"output."+fileFormtStr;
          }else {
              encryFilePathAndName=encryPtionFilePathStr+File.separator+"output."+fileFormtStr;
          }
          successBoolean=true;
      }catch (Exception e){
         successBoolean=false;
      }
     return successBoolean;
   }

    private Boolean encryExcelDate(String choiceBoxEncryTypeStr, List<Map<String, Object>> mapList) {
       try {
           for(int i=0;i<mapList.size();i++){
               Map map=mapList.get(i);
               Set<Map.Entry<String, String>> entryseSet=map.entrySet();
               for (Map.Entry<String, String> entry:entryseSet) {
                   switch (choiceBoxEncryTypeStr) {
                       case "SHA256":
                           if (entry.getKey().equals(FINAL_NAME)){
                               String value=getSHA256(entry.getValue());
                               entry.setValue(value);
                           }
                           if (entry.getKey().equals(FINAL_PID)){
                               String value=getSHA256(entry.getValue());
                               entry.setValue(value);
                           }
                           if (entry.getKey().equals(FINAL_PHOME)){
                               String value=getSHA256(entry.getValue());
                               entry.setValue(value);
                           }
                           break;
                       case "MD5":
                           if (entry.getKey().equals(FINAL_NAME)){
                               String value= stringToMD5(entry.getValue());
                               entry.setValue(value);
                           }
                           if (entry.getKey().equals(FINAL_PID)){
                               String value=stringToMD5(entry.getValue());
                               entry.setValue(value);
                           }
                           if (entry.getKey().equals(FINAL_PHOME)){
                               String value=stringToMD5(entry.getValue());
                               entry.setValue(value);
                           }
                           break;
                       case "SM3":
                           if (entry.getKey().equals(FINAL_NAME)){
                               String value= Sm3Utils.encrypt(entry.getValue());
                               entry.setValue(value);
                           }
                           if (entry.getKey().equals(FINAL_PID)){
                               String value=Sm3Utils.encrypt(entry.getValue());
                               entry.setValue(value);
                           }
                           if (entry.getKey().equals(FINAL_PHOME)){
                               String value=Sm3Utils.encrypt(entry.getValue());
                               entry.setValue(value);
                           }
                           break;
                       default:
                           break;
                   }
               }
           }
       }catch (Exception e){
           successBoolean=false;
           logger.error("对数据进行"+choiceBoxEncryTypeStr+"加密失败");
       }
       return successBoolean;
    }

    private Boolean getEnyFilePath(String encryFilePathStr)  {
       try {
           int intLast=encryFilePathStr.lastIndexOf("\\");
           encryPtionFilePathStr=encryFilePathStr.substring(0,intLast);
           int pixInt=encryFilePathStr.lastIndexOf(".");
           fileFormtStr=encryFilePathStr.substring(pixInt+1);
           fileName=encryFilePathStr.substring(intLast+1,pixInt);
           logger.info("生成的加密后的路径为："+encryFilePath);
       }catch (Exception e){
           successBoolean=false;
          logger.debug("获取加密后的路径失败");
       }
       return successBoolean;
    }

    private boolean inputInvalid() {
        Boolean booleanTemp=false;
        String encryFilePathS=encryFilePath.getText();
        if(StringUtils.isEmpty(encryFilePath.getText())){
            dataIsEmptyStage(dialogStage, "加密文件", "加密文件路径为空");
        }else {
           int indexPix=encryFilePathS.lastIndexOf(".");
           if(indexPix<1){
               dataIsEmptyStage(dialogStage, "加密文件", "文件为非法文件");
           }else {
               String subString=encryFilePathS.substring(indexPix+1);
               if(!FILE_TYPE.contains(subString)){
                   dataIsEmptyStage(dialogStage, "加密文件", "文件类型为非法文件");
               }
           }
        }
        if(StringUtils.isEmpty(resultEncry.getText())){
            dataIsEmptyStage(dialogStage, "结果文件加密密码", "结果文件加密密码为空");
        }else {
            booleanTemp=true;
        }
        return booleanTemp;
    }

    public void setDialogStage(Stage primaryStage) {
        this.dialogStage = dialogStage;
    }

}
