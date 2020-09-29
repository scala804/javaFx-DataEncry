package com.autoTest.javaFxDataEncry.util;

import com.autoTest.javaFxDataEncry.controller.JavaFxMain;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

import static com.autoTest.javaFxDataEncry.base.ConsField.*;

public class PopupUtil {

    /**
     * 成功提示框
     * @param javaFxMain
     * @param title
     * @param headerText
     * @param contentText
     */
    public static void SuccessHints(JavaFxMain javaFxMain, String title, String headerText, String contentText){

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(javaFxMain.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static Boolean alertConfirDialog(JavaFxMain javaFxMain,String header,String message){
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,message,new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));
              // 设置窗口的标题
             _alert.setTitle("确认");
             _alert.setHeaderText(header);
            // 设置对话框的 icon 图标，参数是主窗口的 stage
            _alert.initOwner(javaFxMain.getPrimaryStage());
           // showAndWait() 将在对话框消失以前不会执行之后的代码
           Optional<ButtonType> _buttonType = _alert.showAndWait();
          // 根据点击结果返回
         if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 编辑弹出框，输入数据不为空的验证
     * @param stage Window窗口
     * @param fieldName 字段名称
     * @param tips 提示信息
     */
    public static void dataIsEmptyStage(Stage stage,String fieldName,String tips) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle(fieldName);
        alert.setHeaderText(tips);
        alert.setContentText("");
        alert.showAndWait();
    }


    /**
     * 编辑弹出框，输入数据不为空的验证
     * @param stage Window窗口
     * @param fieldName 字段名称
     * @param tips 提示信息
     */
    public static void dataInvalidStage(Stage stage,String fieldName,String tips) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle(fieldName);
        alert.setHeaderText(tips);
        alert.setContentText("输入完成之后，加密步骤检查");
        alert.showAndWait();
    }

    /**
     * 成功提示框
     *
     */
    public static void SuccessHints(Stage stage,String fieldName,String tips){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.setTitle(fieldName);
        alert.setHeaderText(tips);
        alert.setContentText("加密数据成功！");
        alert.showAndWait();
    }


}
