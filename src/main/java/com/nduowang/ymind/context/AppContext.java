package com.nduowang.ymind.context;

import com.nduowang.ymind.common.Constant;
import com.nduowang.ymind.common.Lang;
import com.nduowang.ymind.ui.DocTabPane;
import com.nduowang.ymind.ui.MainBorderPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AppContext {

    public boolean isDebug = false;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // appContext
    private static AppContext appContext = new AppContext();
    public static AppContext getInstance() {
        return appContext;
    }

    // docManager
    private DocManager docManager = new DocManager();
    public DocManager getDocManager() {
        return docManager;
    }

    // rightBox
    public StringProperty rightBoxContentProperty = new SimpleStringProperty();

    // main stage
    public Stage stage;

    // skin
    private String skin = "default";

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        BorderPane borderPane = new MainBorderPane();


        //center tabPane
        DocTabPane tabPane = new DocTabPane();
        borderPane.setCenter(tabPane);

        // scene
        Scene scene = new Scene(borderPane, 800, 600, Color.WHITE);

        loadStyle(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle(Lang.get("string.title"));

        //primaryStage.setMaximized(true);
        primaryStage.show();

        //bind
        docManager.bindDocList(tabPane);
        docManager.newDoc();

    }

    private void loadStyle(Scene scene) {

        File skinPath = new File(getClass().getResource("/style/" + skin).getFile());
        if (!skinPath.exists() || !skinPath.isDirectory()) {
            return;
        }

        String[] cssFiles = skinPath.list((dir, name) -> {
            if (name != null && name.endsWith(".css")) {
                return true;
            }
            return false;
        });

        log.info("load css dir:" + skinPath.getAbsoluteFile());
        if (cssFiles == null || cssFiles.length == 0) {
            return;
        }

        for (String css : cssFiles) {
            log.info("load css:" + css);
            scene.getStylesheets().add(
                    getClass().getResource("/style/" + skin + "/" + css)
                            .toExternalForm()
            );
        }


        scene.getStylesheets().add(
                getClass().getResource("/style/node/y-node-label-level.css")
                        .toExternalForm()
        );


    }
}
