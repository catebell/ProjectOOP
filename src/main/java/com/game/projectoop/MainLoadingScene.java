package com.game.projectoop;

import com.almasb.fxgl.app.scene.LoadingScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getUIFactoryService;

public class MainLoadingScene extends LoadingScene {
    public MainLoadingScene(){
        Rectangle backGround = new Rectangle(getAppWidth(),getAppHeight(), Color.BLACK);

        Text text = getUIFactoryService().newText("loading",Color.WHITE,50);
        centerText(text,getAppWidth()/2.0,getAppHeight()/2.0);
    }
}
