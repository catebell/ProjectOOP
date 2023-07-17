package com.game.projectoop;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import static com.almasb.fxgl.dsl.FXGL.*;


public class App extends GameApplication {
    public enum EntityType{
        PLAYER,PLATFORM
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(720);
        settings.setWidth(1280);
        settings.setFullScreenAllowed(true);
        //settings.setFullScreenFromStart(true);

        settings.setSceneFactory(new SceneFactory(){
            @Override
            public LoadingScene newLoadingScene() {
                return new MainLoadingScene();
            }
        });
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());
        spawn("background");
    }
    public static void main(String[] args) {
        launch(args);
    }
}
