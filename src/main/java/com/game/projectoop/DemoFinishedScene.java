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

        /*HBox hBox = new HBox(5);

        for (int i = 0; i < 3; i++) {
            Text dot = getUIFactoryService().newText(".", Color.WHITE, 70);
            hBox.getChildren().add(dot);

            animationBuilder(this) //loading dots animations
                    .autoReverse(true)
                    .delay(Duration.seconds(i * 0.5))
                    .repeatInfinitely()
                    .fadeIn(dot)
                    .buildAndPlay();
        }

        hBox.setTranslateX(getAppWidth() / 2.0 - 25); //loading dots centered
        hBox.setTranslateY(getAppHeight() / 2.0);*/

        getContentRoot().getChildren().setAll(backGround, text);
    }

    public void onDemoFinish() {
        getSceneService().pushSubScene(this);
    }
}
