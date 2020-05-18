package com.nduowang.ymind;

import com.nduowang.ymind.context.AppContext;
import javafx.application.Application;
import javafx.stage.Stage;

public class YMind extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        AppContext.getInstance().start(primaryStage);
    }

}