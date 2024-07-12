module org.mrowrpurr.demo.game {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires com.almasb.fxgl.all;
    requires org.java_websocket;

    opens org.mrowrpurr.demo.game to javafx.fxml;
    exports org.mrowrpurr.demo.game;
}