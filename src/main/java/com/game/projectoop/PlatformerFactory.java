package com.game.projectoop;

import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.game.projectoop.App.EntityType.PLATFORM;
import static com.game.projectoop.App.EntityType.PLAYER;

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
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16,38), BoundingShape.box(6,8)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(PLAYER)
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .bbox(new HitBox(new Point2D(5,5), BoundingShape.circle(12)))
                //.viewWithBBox(new Circle(5,5,12, Color.RED))
                .bbox(new HitBox(new Point2D(10,25), BoundingShape.box(10, 17)))
                //.viewWithBBox(new Rectangle(16,38,1,1))
                .build();
    }

}


