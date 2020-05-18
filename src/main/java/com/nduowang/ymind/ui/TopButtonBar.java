package com.nduowang.ymind.ui;

import com.nduowang.ymind.common.Lang;
import com.nduowang.ymind.context.AppContext;
import com.nduowang.ymind.service.TopBarService;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.LinkedHashMap;
import java.util.Map;

public class TopButtonBar extends AnchorPane {

    private TopBarService topBarService = new TopBarService();
    private Map<String, String> leftButtons = new LinkedHashMap<>();
    private Map<String, String> rightButtons = new LinkedHashMap<>();

    public final static String BUTTON_SAVE = "save";
    public final static String BUTTON_OPEN = "open";
    public final static String BUTTON_SNAPSHOT = "snapshot";

    public final static String BUTTON_OUTLINE = "ontline";
    public final static String BUTTON_RECENT = "recent";
    public final static String BUTTON_ABOUT = "about";
    public final static String BUTTON_HELP = "help";

    TopButtonBar() {
        super();
        getStyleClass().add("TopButtonBar");

        leftButtons.put(BUTTON_SAVE, Lang.get("menu.save"));
        leftButtons.put(BUTTON_OPEN, Lang.get("menu.open"));
        leftButtons.put(BUTTON_SNAPSHOT, Lang.get("menu.snapshot"));


        leftButtons.put(BUTTON_ABOUT, Lang.get("menu.about"));
        leftButtons.put(BUTTON_HELP, Lang.get("menu.help"));

        rightButtons.put(BUTTON_OUTLINE, Lang.get("menu.outline"));
        //rightButtons.put(BUTTON_RECENT, "最近文件");
        initLeft();
        initRight();


    }

    private void initLeft() {

        HBox hBox = initButtons(leftButtons);
        setLeftAnchor(hBox, 0d);
        setTopAnchor(hBox, 0d);
    }


    private void initRight() {
        HBox hBox = initButtons(rightButtons);

        setRightAnchor(hBox, 0d);
        setTopAnchor(hBox, 0d);
    }


    private HBox initButtons(Map<String, String> leftButtons) {
        HBox hBox = new HBox();
        if (AppContext.getInstance().isDebug) {
            hBox.setStyle("-fx-border-width: 1px;-fx-border-color: green");
        }
        Button button;
        for (String id : leftButtons.keySet()) {
            button = new Button(leftButtons.get(id));
            button.setId(id);
            button.setOnAction(topBarService);
            hBox.getChildren().add(button);
        }
        getChildren().add(hBox);
        return hBox;
    }

}
