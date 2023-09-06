package com.game.projectoop;

import com.almasb.fxgl.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

public class DemoFinishedScene extends SubScene {
    public DemoFinishedScene() {
        Rectangle backGround = new Rectangle(getAppWidth(), getAppHeight(), Color.BLACK);

        Text text = getUIFactoryService().newText("DEMO FINISHED :)", Color.WHITE, 60);
        centerText(text, getAppWidth() / 2.0, getAppHeight() / 2.0);

        getContentRoot().getChildren().setAll(backGround, text);
        getAudioPlayer().stopAllMusic();
    }

    public void onDemoFinish() {
        getSceneService().pushSubScene(this);
    }
}
