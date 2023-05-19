module com.game.projectoop {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.game.projectoop to javafx.fxml;
    exports com.game.projectoop;
}