package com.game.projectoop;

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
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.game.projectoop.App.EntityType.*;

public class PlatformerFactory implements EntityFactory {
    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        Image bkg = texture("background/BGfinal.png").getImage();
        return entityBuilder()
                .view(new ScrollingBackgroundView(bkg, bkg.getWidth(), bkg.getHeight())).zIndex(-2)
                .at(-40, 0)
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

    @Spawns("exit")
    public Entity newExit(SpawnData data) {
        return entityBuilder(data)
                .type(EXIT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(6, 32), BoundingShape.box(18, 8)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder(data)
                .type(PLAYER)
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .zIndex(1)
                .bbox(new HitBox(new Point2D(6, 4), BoundingShape.box(18, 26)))
                .build();
    }

    @Spawns("usePrompt")
    public Entity newUsePrompt(SpawnData data) {
        return entityBuilder(data)
                .type(USE_PROMPT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("useSpawn")
    public Entity newUseSpawn(SpawnData data) {
        return entityBuilder(data)
                .type(USE_SPAWN)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .build();
    }

    @Spawns("button")
    public Entity newButton(SpawnData data) {
        return entityBuilder(data)
                .type(BUTTON)
                .viewWithBBox(texture("KEYS/" + getInput().getAllBindings().get(getInput().getActionByName(data.get("Action"))).toString() + ".png"))
                .with(new CollidableComponent(true))
                .zIndex(4)
                .build();
    }

    @Spawns("flashlightPrompt")
    public Entity newFlashlightPrompt(SpawnData data) {
        return entityBuilder(data)
                .type(FLASHLIGHT_PROMPT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("dialoguePrompt")
    public Entity newDialoguePrompt(SpawnData data) {
        return entityBuilder(data)
                .type(DIALOGUE_PROMPT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("dialogueSpawn")
    public Entity newDialogueSpawn(SpawnData data) {
        return entityBuilder(data)
                .type(DIALOGUE_SPAWN)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .build();
    }

    @Spawns("dialogueText")
    public Entity newDialogueText(SpawnData data) {
        //String m5x7 = "src/main/resources/assets/ui/fonts/m5x7.ttf";
        //Font.loadFont(Files.newInputStream(Paths.get(m5x7)),20)
        Text text = new Text(data.get("Text"));
        text.setFill(Color.WHITE);
        text.setFont(FXGL.geto("font"));
        return entityBuilder(data)
                .zIndex(4)
                .type(TEXT)
                .viewWithBBox(text)
                .build();
    }

    @Spawns("void")
    public Entity newVoid(SpawnData data) {
        return entityBuilder()
                .type(VOID)
                .view(new Texture(FXGL.image("background/blackBackground.png")))
                .scale(getAppWidth(), getAppHeight() * 1.5)
                .zIndex(3)
                .with(new IrremovableComponent())
                .build();
    }

    @Spawns("flashlight")
    public Entity newFlashlight(SpawnData data) {
        return entityBuilder()
                .type(FLASHLIGHT)
                .with(new FlashlightComponent())
                .zIndex(3)
                .with(new IrremovableComponent())
                .build();
    }

    @Spawns("hal")
    public Entity newHal(SpawnData data) {
        return entityBuilder(data)
                .type(HAL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new HalComponent())
                .zIndex(1)
                .build();
    }

    @Spawns("platforma")
    public Entity newPlatformA(SpawnData data) {
        return entityBuilder(data)
                .type(PLATFORM_ANIM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new AnimPlatformComponent())
                .zIndex(0)
                .build();
    }

    @Spawns("lever")
    public Entity newLever(SpawnData data) {
        return entityBuilder(data)
                .type(LEVER)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true)).with(new IrremovableComponent())
                .with(new LeverComponent())
                .zIndex(1)
                .build();
    }

    @Spawns("battery")
    public Entity newBattery(SpawnData data) {
        return entityBuilder(data)
                .type(BATTERY)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new BatteryComponent())
                .zIndex(0)
                .build();
    }

    @Spawns("light")
    public Entity newLight(SpawnData data) {
        return entityBuilder(data)
                .type(LIGHT)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new LightsComponent())
                .zIndex(0)
                .build();
    }

    @Spawns("elevator")
    public Entity newElevator(SpawnData data) {
        return entityBuilder(data)
                .type(ELEVATOR)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .with(new ElevatorComponent())
                .zIndex(0)
                .build();
    }

    @Spawns("notVisible")
    public Entity newNotVisible(SpawnData data){
        return entityBuilder(data)
                .type(NOT_VISIBLE)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }
    @Spawns("visible")
    public Entity newVisible(SpawnData data){
        return entityBuilder(data)
                .type(VISIBLE)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("smoke")
    public Entity newSmoke(SpawnData data){
        return entityBuilder(data)
                .type(SMOKE)
                .with(new SmokeComponent())
                .zIndex(1)
                .with(new IrremovableComponent())
                .build();
    }

    @Spawns("monitor")
    public Entity newMonitor(SpawnData data){
        return entityBuilder(data)
                .type(MONITOR)
                .with(new MonitorComponent())
                .zIndex(2)
                .build();
    }
}

