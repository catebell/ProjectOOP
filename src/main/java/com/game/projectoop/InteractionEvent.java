package com.game.projectoop;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.TimerAction;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.game.projectoop.App.EntityType.*;

public class InteractionEvent extends Event {
    public static final EventType<InteractionEvent> ANY = new EventType<>(Event.ANY, "Interaction_Event");
    public static final EventType<InteractionEvent> TUTORIAL = new EventType<>(ANY, "TUTORIAL");
    public static final EventType<InteractionEvent> MINIGAME = new EventType<>(ANY, "MINIGAME");
    public static final EventType<InteractionEvent> LEVER = new EventType<>(ANY, "LEVER");

    public InteractionEvent(EventType<? extends Event> eventType, Optional<Entity> interactionEnt) {
        super(eventType);
        if (eventType.equals(TUTORIAL)) {
            if (interactionEnt.isPresent()) {
                despawnWithScale(FXGL.getGameWorld().getSingleton((ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(interactionEnt.get())), Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
                getGameWorld().getEntitiesByType(VOID).get(0).setVisible(false);
                getGameWorld().getEntitiesByType(FLASHLIGHT).get(0).setVisible(true);
                runOnce(() -> tutorialKeys(interactionEnt.get()), Duration.seconds(1));
            }
        }

        if (eventType.equals(MINIGAME)) {
            //call to minigame and passing parameters
            getMiniGameService().startCircuitBreaker(5, 5, 15, 80, Duration.seconds(0.1), result -> {
                if (result.isSuccess()) { //[to do] da cambiare
                    despawnWithScale(FXGL.getGameWorld().getSingleton((ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(interactionEnt.get())), Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
                    getGameWorld().getEntitiesByType(BATTERY)
                            .stream()
                            .filter((battery) -> battery.isColliding(interactionEnt.get()))
                            .forEach(battery -> {
                                                    if (!battery.getComponent(BatteryComponent.class).isON()) {
                                                        battery.getComponent(BatteryComponent.class).activation();
                                                    }
                                                });
                    if(getGameWorld().getClosestEntity(interactionEnt.get(),e->e.getType().equals(ELEVATOR)).isPresent()){
                        if (!getGameWorld().getClosestEntity(interactionEnt.get(),e->e.getType().equals(ELEVATOR)).get().getComponent(ElevatorComponent.class).isON()) {
                            getGameWorld().getClosestEntity(interactionEnt.get(),e->e.getType().equals(ELEVATOR)).get().getComponent(ElevatorComponent.class).activation();
                        }
                    }
                }
            });
        }

        if (eventType.equals(LEVER)) {
            if (interactionEnt.isPresent()) {
                getGameWorld().getEntitiesByType(App.EntityType.LEVER)
                        .stream().filter((lever) -> lever.isColliding(interactionEnt.get()))
                        .forEach(lever -> {
                                        if (!lever.getComponent(LeverComponent.class).isPulled()) {
                                            despawnWithScale(FXGL.getGameWorld().getSingleton((ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(interactionEnt.get())), Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
                                            lever.getComponent(LeverComponent.class).pull();
                                            FXGL.inc("Levers",-1);
                                            if(FXGL.geti("Levers")==0){
                                                unlockMinigame();
                                            }
                                            if(lever.getString("Activates").equals("platform")){
                                                getGameWorld().getEntitiesByType(PLATFORM_ANIM).forEach(platform->
                                                    platform.getComponent(AnimPlatformComponent.class).activation());

                                            }
                                        }
                                    });
                getGameWorld().getEntitiesByType(LIGHT).stream().filter((light) -> light.isColliding(interactionEnt.get()))
                        .forEach(light -> {
                            if (!light.getComponent(LightsComponent.class).isON()) {
                                light.getComponent(LightsComponent.class).activation();
                            }
                        });
            }
        }
    }

    private void tutorialKeys(Entity prompt) {
        Entity entityLeft = getGameWorld().create("button", new SpawnData(prompt.getX(), prompt.getY() + 17).put("Action", "Left"));
        Entity entityRight = getGameWorld().create("button", new SpawnData(prompt.getX() + 17, prompt.getY() + 17).put("Action", "Right"));
        Entity entityJump = getGameWorld().create("button", new SpawnData(prompt.getX() + 8.5, prompt.getY()).put("Action", "Jump"));

        spawnWithScale(entityLeft, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());
        spawnWithScale(entityRight, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());
        spawnWithScale(entityJump, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());

        runOnce(() -> {
            despawnWithScale(entityLeft, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
            despawnWithScale(entityRight, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
            despawnWithScale(entityJump, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
        }, Duration.seconds(5));
        runOnce(() ->{
            onCollisionEnd(App.EntityType.PLAYER, App.EntityType.NOT_VISIBLE, (player, prompto) -> {
                if(getGameWorld().getClosestEntity(prompt,
                        (ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(prompto)).isPresent()){
                    getGameWorld().getClosestEntity(prompt,
                            (ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(prompto)).get().setVisible(false);
                }

            });
            onCollisionBegin(App.EntityType.PLAYER, App.EntityType.VISIBLE, (player, prompto) -> {
                if(getGameWorld().getClosestEntity(prompt,
                        (ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(prompto)).isPresent()){
                    getGameWorld().getClosestEntity(prompt,
                            (ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(prompto)).get().setVisible(true);
                }
            });
        },Duration.seconds(8));

    }
    private void unlockMinigame(){
        Entity minigame = getGameWorld().getSingleton((entity)->entity.isType(USE_PROMPT) && entity.getString("Use").equals(
                        "Minigame"));
        Entity setters =
                getGameWorld().getSingleton((entity -> entity.isType(NOT_VISIBLE) && entity.isColliding(minigame)));
        setters.setAnchoredPosition(0,0);
        setters = getGameWorld().getSingleton((entity -> entity.isType(VISIBLE) && entity.isColliding(minigame)));
        setters.setAnchoredPosition(0,0);
        minigame.setAnchoredPosition(0,0);
    }
}
