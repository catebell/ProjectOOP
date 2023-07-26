package com.game.projectoop;

import com.almasb.fxgl.app.services.FXGLAssetLoaderService;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.ui.FXGLTextFlow;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.game.projectoop.App.EntityType.*;

public class PlatformerFactory implements EntityFactory {
    @Spawns("background")
    public Entity newBackground(SpawnData data){
        return entityBuilder()
            .view(new ScrollingBackgroundView(texture("background/blackBackground.png").getImage(),getAppWidth(),getAppHeight()))
            .zIndex(-1) //depth -1 -> background
            .with(new IrremovableComponent())
            .build();
    }
    @Spawns("platform")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder(data)
                .type(PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16,32), BoundingShape.box(1,1)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(PLAYER)
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .zIndex(1)
                .bbox(new HitBox(new Point2D(6,4), BoundingShape.box(18, 26)))
                .build();

    }

    @Spawns("keyPrompt")
    public Entity newPrompt(SpawnData data) {
        return entityBuilder(data)
                .type(KEY_PROMPT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("button")
    public Entity newLeftButton(SpawnData data) {
        return entityBuilder(data)
                .type(BUTTON)
                .viewWithBBox(texture("KEYS/" + getInput().getAllBindings().get(getInput().getActionByName(data.get(
                        "Action"))).toString() + ".png"))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("dialoguePrompt")
    public Entity newDialogue(SpawnData data){
                return entityBuilder(data)
                .type(DIALOGUE_PROMPT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    /*@Spawns("dialogueText")
    public Entity newDialogueText(SpawnData data){
        *//*FXGLTextFlow text = new FXGLTextFlow(FXGL.getUIFactoryService());
        Text test1 = new Text("testoaaaaaaaaaaaaaaaaaaaa");
        text.setStyle("-fx-text-fill: red");
        text.append(test1);*//*
        FXGL.getGameScene().addUINode(new Text("TESTO TESTO"));

            return entityBuilder(data)
                    .type(TEXT)
                    .viewWithBBox(text)
                    .build();
    }*/

}


