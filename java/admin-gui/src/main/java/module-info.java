module org.mrowrpurr.demo.admingui {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.mrowrpurr.demo.admingui to javafx.fxml;
    exports org.mrowrpurr.demo.admingui;
}