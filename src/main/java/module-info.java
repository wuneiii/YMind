module nduowang.Ymind {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.swing;
    requires org.slf4j;
    requires static lombok;
    requires java.base;
    requires commons.lang;
    requires java.desktop;

    opens com.nduowang.ymind to javafx.graphics, javafx.swing,junit;
    exports com.nduowang.ymind;
}