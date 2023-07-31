package com.game.projectoop;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.TimerAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;

public class App extends GameApplication {
    private final ArrayList<TimerAction> dialogue = new ArrayList<>();
    boolean tutorialOK = false;
    private Entity player;
    private Entity endlessVoid;
    private Entity flashlight;
    private double accX = 0;
    private boolean sx = false;
    private boolean dx = false;

    private HashMap<Integer, List<String>> dialogues() {
        HashMap<Integer, List<String>> dialogues = new HashMap<>();
        dialogues.put(1, List.of("first text", "second text"));
        dialogues.put(2, List.of("Lorem ipsum dolor sit amet,\n consectetur adipiscing elit,", "sed do eiusmod tempor\n" + "incididunt ut labore et dolore magna aliqua.", "Ut enim ad minim veniam,", "quis nostrud exercitation ullamco laboris nisi", "ut aliquip ex ea commodo consequat."));
        return dialogues;
    }

    public enum EntityType {
        PLAYER, PLATFORM, USE_PROMPT, BUTTON, DIALOGUE_PROMPT, TEXT, FLASHLIGHT_PROMPT, VOID, FLASHLIGHT, HAL, LEVER,
        BATTERY, PLATFORM_ANIM, EXIT, LIGHT, ELEVATOR
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setHeight(720);
        settings.setWidth(1280);
        settings.setFullScreenAllowed(true);
        settings.setTitle("OOP");
        settings.setFontUI("m5x7.ttf");
        settings.setFontText("m5x7.ttf");
        settings.setFontGame("m5x7.ttf");
        settings.setFontMono("m5x7.ttf");
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public LoadingScene newLoadingScene() {
                return new MainLoadingScene();
            }
        });
        //settings.setDeveloperMenuEnabled(true); //DEBUG
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.25);
        //altre impostazioni del genere
        /* [loop music] loopBGM("BGM_dash_runner.wav");*/
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        try {
            InputStream file = Files.newInputStream(Paths.get("src/main/resources/assets/ui/fonts/m5x7.ttf"));
            vars.put("font", Font.loadFont(file, 20));
        } catch (IOException e) {
            System.out.println("AHHHHHHHHHHHHHHHHHHHHHHHHHH");
        }
        vars.put("PlayerPosition", new Point2D(0, 0));
        vars.put("PlayerScaleX", 1);
        vars.put("level", 1);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());

        spawn("background");
        endlessVoid = spawn("void");
        flashlight = spawn("flashlight");
        flashlight.setVisible(false);
        player = null;
        // player must be spawned after call to nextLevel, otherwise player gets removed
        // before the update tick _actually_ adds the player to game world
        player = spawn("player", new Point2D(1015, 615));
        setLevel(); //nextlevel(); [vedi sotto]
        set("player", player);

        Viewport viewport = getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setZoom(1.4);
        viewport.setLazy(true); //smoother camera movement
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 1000);

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.USE_PROMPT, (player, prompt) -> {
            Entity useButton = getGameWorld().create("button", new SpawnData(prompt.getX(), prompt.getY()).put("Action", "Use"));
            spawnWithScale(useButton, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.FLASHLIGHT_PROMPT, (player, prompt) -> {
            Entity flashlightButton = getGameWorld().create("button", new SpawnData(prompt.getX(), prompt.getBottomY() - 65).put("Action", "Flashlight"));
            spawnWithScale(flashlightButton, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT());

            runOnce(() -> despawnWithScale(flashlightButton, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN()), Duration.seconds(5));
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.DIALOGUE_PROMPT, (player, prompt) -> {
            startDialogue(2, prompt);
            //startDialogue(1,prompt); //[partono in contemporanea se fatti andare troppo vicini]
        });
    }

    @Override
    protected void initInput() {
        //movement to the left
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                if (tutorialOK && !dx) {
                    sx = true;
                    if (accX > -1) {
                        accX -= 0.1;
                    }
                    player.getComponent(PlayerComponent.class).move(accX, -1);
                }
            }

            @Override
            protected void onActionEnd() {
                sx = false;
                if (!dx) {
                    player.getComponent(PlayerComponent.class).stop();
                    accX = 0;
                }
            }
        }, KeyCode.A);

        //movement to the right
        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                if (tutorialOK && !sx) {
                    dx = true;
                    if (accX < 1) {
                        accX += 0.1;
                    }
                    player.getComponent(PlayerComponent.class).move(accX, 1);
                }
            }

            @Override
            protected void onActionEnd() {
                dx = false;
                if (!sx) {
                    player.getComponent(PlayerComponent.class).stop();
                    accX = 0;
                }
            }
        }, KeyCode.D);

        //jump
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).jump();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Use") {
            @Override
            protected void onActionBegin() {
                getGameWorld().getEntitiesByType(EntityType.USE_PROMPT).stream().filter(prompt -> prompt.hasComponent(CollidableComponent.class) && player.isColliding(prompt)).forEach((prompt) -> {
                    if (prompt.getString("Use").equals("Tutorial") && !tutorialOK) { //only for
                        // tutorial
                        // actions
                        tutorialOK = true;
                        getEventBus().fireEvent(new InteractionEvent(InteractionEvent.TUTORIAL, Optional.of(prompt)));
                    }

                    if (prompt.getString("Use").equals("Minigame")) { //only for starting minigames
                        getEventBus().fireEvent(new InteractionEvent(InteractionEvent.MINIGAME, Optional.of(prompt)));
                    }

                    if (prompt.getString("Use").equals("Lever")) { //only for pulling levers
                        getEventBus().fireEvent(new InteractionEvent(InteractionEvent.LEVER, Optional.of(prompt)));
                    }
                });
            }
        }, KeyCode.E);

        getInput().addAction(new UserAction("Flashlight") {
            protected void onActionBegin() {
                if (flashlight.isVisible()) {
                    endlessVoid.setVisible(false);
                    flashlight.setVisible(false);
                } else {
                    flashlight.setVisible(true);
                    endlessVoid.setVisible(false);
                }
            }
        }, KeyCode.T);
    }

    protected void onUpdate(double tpf) {
        if (!getGameScene().getViewport().getVisibleArea().contains(player.getPosition())) {
            //player out of boundaries
            onPlayerDied();
        }
    }

    public void onPlayerDied() {
        getGameController().gotoLoading(this::resetLvl);
    }

    public void resetLvl() {
        setLevel();
        flashlight.setVisible(false);
        endlessVoid.setVisible(true);
        tutorialOK = false;
        dialogue.forEach((d) -> d.expire());
        tutorialOK = false;
    }

    private void setLevel() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(1015, 615));
        }
        Level level = setLevelFromMap("Cryo.tmx");
        List<Entity> layers = level.getEntities();
        int backgrounds = 0;
        for (Entity E : layers) {
            if (E.getTypeComponent().toString().equals("Type(TiledMapLayer)")) {
                if (backgrounds >= level.getProperties().getInt("backgrounds")) {
                    E.setZIndex(2);
                } else {
                    backgrounds++;
                    E.setZIndex(-1);
                }
            }
        }
        getGameScene().getViewport().setBounds(0, 0, level.getWidth(), level.getHeight() + 10);
    }

    // [vedi sopra]
    /*private void nextLevel() {
        if (geti("level") == MAX_LEVEL) {
            showMessage("You finished the demo!");
            return;
        }

        inc("level", +1);

        setLevel(geti("level"));
    }*/

    protected void startDialogue(int dialNumber, Entity prompt) {
        HashMap<Integer, List<String>> dial = dialogues();
        double time = 0.0;

        for (String s : dial.get(dialNumber)) {//first dialogue
            Entity dialogueEntity = getGameWorld().create("dialogueText", new SpawnData(prompt.getX(), prompt.getY()).put("Text", s));

            //effects in out for each sentence and/or spawn delay
            //System.out.println("s = " + s + " and element 0 = " + dial.get(1).get(0)); //DEBUG
            /*if(dial.get(dialNumber).get(0).equals(s)){ //element 0 spawn in 0 time
                runOnce(()->spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()),Duration.seconds(time));
                System.out.println("first element");
            }else{ //delay spawn
                runOnce(()->spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()),Duration.seconds(time+0.5));
            }*/

            dialogue.add(runOnce(() -> spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()), Duration.seconds(time)));
            time += 0.2 * s.toCharArray().length;
            //System.out.println("length of " + s + " = " + s.toCharArray().length + " time = " + time); //DEBUG

            if (dial.get(dialNumber).get(dial.get(dialNumber).size() - 1).equals(s)) { /*oppure con una deque per avere direttamente last element*/
                runOnce(() -> despawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_IN()), Duration.seconds(time));
            } else {
                despawnWithDelay(dialogueEntity, Duration.seconds(time));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
