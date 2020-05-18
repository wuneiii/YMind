package com.nduowang.ymind.service;

import com.nduowang.ymind.common.Constant;
import com.nduowang.ymind.context.AppContext;
import com.nduowang.ymind.entity.Document;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TopBarService implements EventHandler<ActionEvent> {

    private Logger log = LoggerFactory.getLogger(getClass());
    private Map<String, Method> handlerMap = new HashMap<>();

    private String methodPrefix = "_handle";


    public TopBarService() {
        String name;
        Method[] methods = getClass().getMethods();
        for (Method m : methods) {
            name = m.getName();
            if (name.startsWith(methodPrefix)) {
                handlerMap.put(name, m);
                log.info("add button handle:" + name);
            }
        }
    }


    @Override
    public void handle(ActionEvent event) {
        Button button = (Button) event.getSource();
        String expectHandleName = methodPrefix + StringUtils.capitalize(button.getId());

        if (!handlerMap.containsKey(expectHandleName)) {
            log.error("expectHandleName:" + expectHandleName + "| not exists");
            return;
        }
        try {
            Method method = handlerMap.get(expectHandleName);
            method.invoke(this, event);
        } catch (Exception e) {
            log.error("invoke handle fail:" + expectHandleName);
            e.printStackTrace();
        }

        log.debug("button click:" + button.getId() + "|" + event);
    }

    public void _handleOntline(ActionEvent event) {
        var oldValue = AppContext.getInstance().rightBoxContentProperty.getValue();
        if ("outline".equals(oldValue)) {
            AppContext.getInstance().rightBoxContentProperty.setValue("");
        } else {
            AppContext.getInstance().rightBoxContentProperty.setValue("outline");
        }

    }

    public void _handleOpen(ActionEvent event) {
        AppContext.getInstance().getDocManager().openDoc();
    }

    public void _handleAbout(ActionEvent event) {


        HBox alertTop = new HBox();
        ImageView logo = new ImageView();
        {
            logo.setFitWidth(140d);
            logo.setPreserveRatio(true);
            logo.setImage(new Image(getClass().getResourceAsStream("/images/logo-long.png")));
        }
        Label aboutUs = new Label(Constant.ABOUTUS);
        {
            alertTop.getChildren().addAll(logo, aboutUs);
        }

        HBox alertBottom = new HBox();
        Button closeButton = new Button(" 关 闭 ");

        alertBottom.getChildren().add(closeButton);

        VBox alertMain = new VBox();
        alertMain.getChildren().addAll(alertTop, alertBottom);


        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(alertMain);
        dialogPane.getStyleClass().add("aboutUsAlert");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.initOwner(AppContext.getInstance().stage);
        alert.getDialogPane().setStyle("-fx-background-color: #ffffff");
        alert.setHeaderText(Constant.TITLE);
        alert.setGraphic(logo);
        alert.setContentText(Constant.ABOUTUS);
        alert.showAndWait();


    }

    public void _handleSnapshot(ActionEvent event) {
        Document activeDoc = AppContext.getInstance().getDocManager().getActiveDoc();


        if (activeDoc == null) {
            throw new RuntimeException("snapshot node is document");
        }

        File snapshotFile = null;
        Node node = AppContext.getInstance().getDocManager()
                .getActiveDoc().getBasePane();
        Color bgColor =(Color) ((Pane)node).getBackground().getFills().get(0).getFill();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存图片到");

        fileChooser.setInitialFileName(activeDoc.tabNameProperty.getValue() + Constant.FILE_EXT + ".jpg");
        snapshotFile = fileChooser.showSaveDialog(AppContext.getInstance().stage);
        if (snapshotFile == null) {
            log.info("截图取消");
            return;
        }


        //以下两句是设置截图的参数，具体细节还没有研究
        final SnapshotParameters params = new SnapshotParameters();
        params.setFill(bgColor);
        params.setDepthBuffer(true);
        params.setTransform(Transform.scale(8,8));

        //对Node进行截图，只会截取显示出来的部分，未显示出来的部分无法截图（没有火狐截图高级）
        WritableImage snapshot = node.snapshot(params, null);


        //将JavaFX格式的WritableImage对象转换成AWT BufferedImage 对象来进行保存
        final BufferedImage image
                = SwingFXUtils.fromFXImage(snapshot, null);
        try {

            BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = copy.createGraphics();
            g2d.setColor(java.awt.Color.WHITE); // Or what ever fill color you want...
            g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            Iterator<ImageWriter> iter = ImageIO
                    .getImageWritersByFormatName("jpeg");

            ImageWriter imageWriter = iter.next();
            ImageWriteParam iwp = imageWriter.getDefaultWriteParam();

            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(1f);

            FileImageOutputStream fileImageOutput = new FileImageOutputStream(snapshotFile);
            imageWriter.setOutput(fileImageOutput);
            IIOImage iio_image = new IIOImage(copy, null, null);
            imageWriter.write(null, iio_image, iwp);
            imageWriter.dispose();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void _handleSave(ActionEvent event) {
        AppContext.getInstance().getDocManager().saveFile();

    }
}