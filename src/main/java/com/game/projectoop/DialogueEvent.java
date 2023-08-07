package com.game.projectoop;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.TimerAction;
import javafx.event.Event;
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
        dialogues.put(1, List.of(
                "   Good Evening 666.\nPlease, follow my voice.",
                "As you must have guessed,\n    we're facing some...",
                "   technical difficulties.",
                " The ship's power's out.",
                " Now, keep going this way\nand reach the ground floor."));
        dialogues.put(2, List.of(
                "The main generator needs\n    manual rebooting;",
                "  Therefore, I took the\nliberty of waking you up.",
                "  Don't worry, just keep\nfollowing my instructions."));
        dialogues.put(3, List.of(
                "There are two levers down there.", "  You'll need to pull them both."));
        dialogues.put(4, List.of(
                "Good job. Now, fix this circuit\n   to activate the elevator."));

        return dialogues;
    }
    private final ArrayList<TimerAction> queue;
    public DialogueEvent(EventType<? extends Event> eventType, Optional<Entity> promptEnt,
                         Optional<ArrayList<TimerAction>> dialogueQueue) {
        super(eventType);
        this.queue=dialogueQueue.get();

        if(promptEnt.isPresent()) {

            if (eventType.equals(DIALOGUE1)) {
                startDialogue(1, promptEnt.get());
            }

            if (eventType.equals(DIALOGUE2)) {
                startDialogue(2, promptEnt.get());
            }

            if (eventType.equals(DIALOGUE3)) {
                startDialogue(3, promptEnt.get());
            }

            if (eventType.equals(DIALOGUE4)) {
                startDialogue(4, promptEnt.get());
            }
        }
    }

    protected void startDialogue(int dialNumber, Entity prompt) {
        HashMap<Integer, List<String>> dial = dialogues();
        double time = 0.0;

        Optional<Entity> spawnD = getGameWorld().getEntitiesByType(App.EntityType.DIALOGUE_SPAWN).stream()
                .filter(dialogueSpawn -> dialogueSpawn.getInt("Number")==dialNumber)
                .findFirst();

        getGameWorld().getEntitiesByType(App.EntityType.HAL).forEach(hal -> hal.setVisible(hal.getInt("Number") == dialNumber));

        for (String s : dial.get(dialNumber)) {//first dialogue

            Entity dialogueEntity = getGameWorld().create("dialogueText", new SpawnData(spawnD.get().getX(), spawnD.get().getY()).put("Text", s));

            queue.add(runOnce(() -> spawnWithScale(dialogueEntity, Duration.seconds(1),
                    Interpolators.ELASTIC.EASE_OUT()), Duration.seconds(time)));
            time += 0.1 * s.toCharArray().length;
            //System.out.println("length of " + s + " = " + s.toCharArray().length + " time = " + time); //DEBUG

            if (dial.get(dialNumber).get(dial.get(dialNumber).size() - 1).equals(s)) { /*oppure con una deque per avere direttamente last element*/
                runOnce(() -> despawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN()), Duration.seconds(time));
            } else {
                despawnWithDelay(dialogueEntity, Duration.seconds(time));
            }
        }
    }
}