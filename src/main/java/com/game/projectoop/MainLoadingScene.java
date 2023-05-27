package com.game.projectoop;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.LoadingScene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MainLoadingScene extends LoadingScene {
    public MainLoadingScene(){
        Rectangle backGround = new Rectangle(getAppWidth(),getAppHeight(), Color.BLACK);

        Text text = getUIFactoryService().newText("loading",Color.WHITE,25);
        centerText(text,getAppWidth()/2.0,getAppHeight()/2.0);

        HBox hBox = new HBox(5);

        for(int i=0;i<3;i++){
            Text dot = getUIFactoryService().newText(".",Color.WHITE,30);
            hBox.getChildren().add(dot);

            animationBuilder(this)
                    .autoReverse(true)
                    .delay(Duration.seconds(i*0.5))
                    .repeatInfinitely()
                    .fadeIn(dot)
                    .buildAndPlay();
        }

        hBox.setTranslateX(getAppWidth() / 2 - 15); //spostamento dei puntini di caricamento
        hBox.setTranslateY(getAppHeight() / 2);

        getContentRoot().getChildren().setAll(backGround,text,hBox);
    }
}
