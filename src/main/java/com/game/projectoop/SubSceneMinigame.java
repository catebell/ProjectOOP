package com.game.projectoop;

import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.input.MouseButton;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SubSceneMinigame extends SubScene {

        public SubSceneMinigame(){
                spawn("minigameBackground");
                //setLevelFromMap("Testmingame.tmx");
                /*getInput().addAction(new UserAction("Do") {
                        @Override
                        protected void onActionBegin() {
                                ;
                        }
                }, MouseButton.PRIMARY);*/

                //Level level = setLevelFromMap("Testmingame.tmx");
        }
}
