package com.game.projectoop;

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
import javafx.geometry.Point2D;


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

    @Spawns("keyPrompt")
    public Entity newPrompt(SpawnData data) {
        return entityBuilder(data)
                .type(KEY_PROMPT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("LeftButton")
    public Entity newLeftButton(SpawnData data) {
        return entityBuilder(data)
                .type(BUTTON)
                .viewWithBBox(texture("KEYS/" + getInput().getAllBindings().get(getInput().getActionByName("Left")).toString() + ".png"))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("RightButton")
    public Entity newRightButton(SpawnData data) {
        return entityBuilder(data)
                .type(BUTTON)
                .viewWithBBox(texture("KEYS/" + getInput().getAllBindings().get(getInput().getActionByName("Right")).toString() + ".png"))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("JumpButton")
    public Entity newJumpButton(SpawnData data) {
        return entityBuilder(data)
                .type(BUTTON)
                .viewWithBBox(texture("KEYS/" + getInput().getAllBindings().get(getInput().getActionByName("Jump")).toString() + ".png"))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("UseButton")
    public Entity newUseButton(SpawnData data) {
        return entityBuilder(data)
                .type(BUTTON)
                .viewWithBBox(texture("KEYS/" + getInput().getAllBindings().get(getInput().getActionByName("Use")).toString() + ".png"))
                .with(new CollidableComponent(true))
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

}


