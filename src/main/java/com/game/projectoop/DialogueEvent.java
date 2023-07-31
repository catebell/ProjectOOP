package com.game.projectoop;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.TimerAction;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.despawnWithDelay;

public class DialogueEvent extends Event {

    public static final EventType<InteractionEvent> ANY = new EventType<>(Event.ANY, "Dialogue_Event");
    public static final EventType<InteractionEvent> DIALOGUE1 = new EventType<>(ANY, "Dialogue1");
    public static final EventType<InteractionEvent> DIALOGUE2 = new EventType<>(ANY, "Dialogue2");
    public static final EventType<InteractionEvent> DIALOGUE3 = new EventType<>(ANY, "Dialogue3");
    public static final EventType<InteractionEvent> DIALOGUE4 = new EventType<>(ANY, "Dialogue4");

    private HashMap<Integer, List<String>> dialogues() {
        HashMap<Integer, List<String>> dialogues = new HashMap<>();
        dialogues.put(1, List.of("first text", "second text"));
        dialogues.put(2, List.of("Lorem ipsum dolor sit amet,\n consectetur adipiscing elit,", "sed do eiusmod tempor\n" + "incididunt ut labore et dolore magna aliqua.", "Ut enim ad minim veniam,", "quis nostrud exercitation ullamco laboris nisi", "ut aliquip ex ea commodo consequat."));
        return dialogues;
    }
    private ArrayList<TimerAction> queue;
    public DialogueEvent(EventType<? extends Event> eventType, Optional<Entity> promptEnt,
                         Optional<ArrayList<TimerAction>> dialogueQueue) {
        super(eventType);
        this.queue=dialogueQueue.get();

        if(promptEnt.isPresent()) {

            if (eventType.equals(DIALOGUE1)) {
                startDialogue(1, promptEnt.get(), 0, 0);
                System.out.println(queue);
            }
            if (eventType.equals(DIALOGUE2)) {
                startDialogue(2, promptEnt.get(), 0, 0);
            }

            if (eventType.equals(DIALOGUE3)) {
                startDialogue(3, promptEnt.get(), 0, 0);
            }
            if (eventType.equals(DIALOGUE4)) {
                startDialogue(4, promptEnt.get(), 0, 0);
            }


        }
    }


    protected void startDialogue(int dialNumber, Entity prompt, int offsetX, int offsetY) {
        HashMap<Integer, List<String>> dial = dialogues();
        double time = 0.0;

        for (String s : dial.get(dialNumber)) {//first dialogue
            Entity dialogueEntity = getGameWorld().create("dialogueText", new SpawnData(prompt.getX()+offsetX, prompt.getY()+offsetY).put("Text", s));

            //effects in out for each sentence and/or spawn delay
            //System.out.println("s = " + s + " and element 0 = " + dial.get(1).get(0)); //DEBUG
            /*if(dial.get(dialNumber).get(0).equals(s)){ //element 0 spawn in 0 time
                runOnce(()->spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()),Duration.seconds(time));
                System.out.println("first element");
            }else{ //delay spawn
                runOnce(()->spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()),Duration.seconds(time+0.5));
            }*/

            queue.add(runOnce(() -> spawnWithScale(dialogueEntity, Duration.seconds(1),
                    Interpolators.ELASTIC.EASE_OUT()), Duration.seconds(time)));
            time += 0.2 * s.toCharArray().length;
            //System.out.println("length of " + s + " = " + s.toCharArray().length + " time = " + time); //DEBUG

            if (dial.get(dialNumber).get(dial.get(dialNumber).size() - 1).equals(s)) { /*oppure con una deque per avere direttamente last element*/
                runOnce(() -> despawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN()), Duration.seconds(time));
            } else {
                despawnWithDelay(dialogueEntity, Duration.seconds(time));
            }
        }
    }

}
