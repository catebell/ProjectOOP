package com.game.projectoop;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.minigames.MiniGame;
import com.almasb.fxgl.minigames.circuitbreaker.CircuitBreakerMiniGame;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.util.Duration;

import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.despawnWithScale;
import static com.game.projectoop.App.EntityType.*;

public class InteractionEvent extends Event {
    public static final EventType<InteractionEvent> ANY
            = new EventType<>(Event.ANY, "Interaction_Event");
    public static final EventType<InteractionEvent> TUTORIAL
            = new EventType<>(ANY, "TUTORIAL");
    public static final EventType<InteractionEvent> PICKUP
            = new EventType<>(ANY, "PICKUP");
    public static final EventType<InteractionEvent> MINIGAME
            =new EventType<>(ANY,"MINIGAME");

    public InteractionEvent(EventType<? extends Event> eventType, Optional<Entity> interactionEnt) {
        super(eventType);
        if(eventType.equals(TUTORIAL)){
            if (interactionEnt.isPresent()) {
                despawnWithScale(FXGL.getGameWorld().getSingleton((ent) -> ent.isType(App.EntityType.BUTTON) && ent.isColliding(interactionEnt.get())), Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
                getGameWorld().getEntitiesByType(VOID).get(0).setVisible(false);
                getGameWorld().getEntitiesByType(FLASHLIGHT).get(0).setVisible(true);
                runOnce(()->tutorialKeys(interactionEnt.get()),Duration.seconds(1));
            }
        }

        if(eventType.equals(MINIGAME)){
            //richiamo il minigame e gli do i parametri
            getMiniGameService().startCircuitBreaker(5,5,30,100,Duration.seconds(0.1),result -> {
                despawnWithScale(getGameWorld().getSingleton(FLASHLIGHT));});
           // getSceneService().pushSubScene(new SubSceneMinigame());
           // despawnWithScale(FXGL.getGameWorld().getSingleton((ent) -> ent.isType(App.EntityType.BUTTON) && ent
            // .isColliding(interactionEnt.get())), Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
        }
    }

    private void tutorialKeys(Entity prompt){
            Entity entityLeft = getGameWorld().create("button", new SpawnData(prompt.getX(), prompt.getY() + 17).put(
                    "Action", "Left"));
            Entity entityRight = getGameWorld().create("button",
                    new SpawnData(prompt.getX() + 17, prompt.getY() + 17).put("Action", "Right"));
            Entity entityJump = getGameWorld().create("button",
                    new SpawnData(prompt.getX() + 8.5, prompt.getY()).put("Action", "Jump"));

            spawnWithScale(entityLeft, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());
            spawnWithScale(entityRight, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());
            spawnWithScale(entityJump, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());

            runOnce(() -> {
                despawnWithScale(entityLeft, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
                despawnWithScale(entityRight, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
                despawnWithScale(entityJump, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN());
            }, Duration.seconds(5));
    }
}
