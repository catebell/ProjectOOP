package com.game.projectoop;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.TimerAction;
import javafx.event.Event;
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

    private HashMap<Integer, List<String>> dialogues;
    private int maxWidth = 0;
    private int maxHeight = 0;
    boolean tutorialOK = false;
    private Entity player;
    private Entity endlessVoid;
    private Entity flashlight;
    private double accX = 0;
    private boolean sx = false;
    private boolean dx = false;
    private final ArrayList<TimerAction> dialogueQueue = new ArrayList<>();
    List<Boolean> dialogDone = new ArrayList<>(List.of(false,false,false,false));
    double startingY=0.0;
    double endingY=0.0;
    boolean wasFalling=false;

    public enum EntityType {
        PLAYER, PLATFORM, USE_PROMPT, BUTTON, DIALOGUE_PROMPT, DIALOGUE_SPAWN, TEXT, FLASHLIGHT_PROMPT, VOID, FLASHLIGHT, HAL, LEVER,
        BATTERY, PLATFORM_ANIM, LIGHT, ELEVATOR, VISIBLE, NOT_VISIBLE, EXIT, SMOKE
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
        settings.setAppIcon("SleepyGuy.png");
    }

    @Override
    protected void onPreInit() {
        dialogues = new HashMap<>();
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
        vars.put("Levers",2);
        vars.put("PlayerPosition", new Point2D(0, 0));
        vars.put("PlayerScaleX", 1);
        vars.put("level", 1);
        vars.put("dialPlaying",0);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());

        spawn("background");
        endlessVoid = spawn("void");
        flashlight = spawn("flashlight");
        flashlight.setVisible(false);
        player = null;
        spawn("smoke");
        setLevel(); //nextlevel(); [vedi sotto]
        Viewport viewport = getGameScene().getViewport();
        // player must be spawned after call to nextLevel, otherwise player gets removed
        // before the update tick _actually_ adds the player to game world
        player = spawn("player", new Point2D(905, 595));
        set("player", player);
        initMinigame();
        initExit();
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
            if(!dialogueQueue.isEmpty()){
                dialogueQueue.forEach(TimerAction::expire);
            }
            if (prompt.getInt("Number")==1 && !dialogDone.get(0)) {
                getEventBus().fireEvent(new DialogueEvent(DialogueEvent.DIALOGUE1,Optional.of(prompt),
                        Optional.of(dialogueQueue),dialogues));
                dialogDone.set(0,true);
            }

            if (prompt.getInt("Number")==2 && dialogDone.get(0)) {
                getEventBus().fireEvent(new DialogueEvent(DialogueEvent.DIALOGUE2,Optional.of(prompt),
                        Optional.of(dialogueQueue),dialogues));
                dialogDone.set(1,true);
            }

            if (prompt.getInt("Number")==3 && dialogDone.get(1)) {
                getEventBus().fireEvent(new DialogueEvent(DialogueEvent.DIALOGUE3,Optional.of(prompt),
                        Optional.of(dialogueQueue),dialogues));
                dialogDone.set(2,true);
            }

            if (prompt.getInt("Number")==4 && dialogDone.get(2)) {
                getEventBus().fireEvent(new DialogueEvent(DialogueEvent.DIALOGUE4,Optional.of(prompt),
                        Optional.of(dialogueQueue),dialogues));
                dialogDone.set(3,true);
            }
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
                    if (prompt.getString("Use").equals("Tutorial") && !tutorialOK) { //only for tutorial actions
                        tutorialOK = true;
                        getEventBus().fireEvent(new InteractionEvent(InteractionEvent.TUTORIAL, Optional.of(prompt)));
                    }

                    if (prompt.getString("Use").equals("Minigame")) { //only for starting
                        // minigames
                        getEventBus().fireEvent(new InteractionEvent(InteractionEvent.MINIGAME, Optional.of(prompt)));
                    }

                    if (prompt.getString("Use").equals("Lever")) { //only for pulling levers
                        getEventBus().fireEvent(new InteractionEvent(InteractionEvent.LEVER, Optional.of(prompt)));
                    }

                    if(prompt.getString("Use").equals("Elevator")) {
                        getEventBus().fireEvent(new InteractionEvent(InteractionEvent.EXIT,Optional.of(prompt)));
                    }
                });
            }
        }, KeyCode.E);

        getInput().addAction(new UserAction("Flashlight") {
            protected void onActionBegin() {
               // resetLvl();
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


    public void resetLvl() {
        getGameWorld().getEntitiesByType(App.EntityType.HAL).forEach(FXGL::despawnWithScale);
        getGameWorld().getEntitiesByType(App.EntityType.LEVER).forEach(FXGL::despawnWithScale);
        setLevel();
        flashlight.setVisible(false);
        endlessVoid.setVisible(true);
        tutorialOK = false;
        dialogueQueue.forEach(TimerAction::expire);
    }

    private void setLevel() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(905, 595));
        }
        Level level = setLevelFromMap("Cryo.tmx");
        maxWidth = level.getWidth();
        maxHeight = level.getHeight();
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
        getGameWorld().getEntitiesByType(EntityType.HAL)
                .forEach(hal->hal.setVisible(false));
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

    private void initMinigame(){
       Entity minigame =
               getGameWorld().getSingleton((entity -> entity.isType(EntityType.USE_PROMPT) && entity.getString("Use").equals("Minigame")));

       Entity setters = getGameWorld().getSingleton((entity -> entity.isType(EntityType.VISIBLE) && entity.isColliding(minigame)));
       setters.setLocalAnchor(new Point2D(-setters.getX(),-setters.getY()));
       setters.setAnchoredPosition(-750,0);

       setters = getGameWorld().getSingleton((entity -> entity.isType(EntityType.NOT_VISIBLE) && entity.isColliding(minigame)));
       setters.setLocalAnchor(new Point2D(-setters.getX(),-setters.getY()));
       setters.setAnchoredPosition(-750,0);

       minigame.setLocalAnchor(new Point2D(-minigame.getX(),-minigame.getY()));
       minigame.setAnchoredPosition(-750,0);
    }

    private void initExit(){
        Entity exit =
                getGameWorld().getSingleton((entity -> entity.isType(EntityType.USE_PROMPT) && entity.getString("Use").equals("Elevator")));

        Entity setters = getGameWorld().getSingleton((entity -> entity.isType(EntityType.VISIBLE) && entity.isColliding(exit)));
        setters.setLocalAnchor(new Point2D(-setters.getX(),-setters.getY()));
        setters.setAnchoredPosition(-750,0);

        setters = getGameWorld().getSingleton((entity -> entity.isType(EntityType.NOT_VISIBLE) && entity.isColliding(exit)));
        setters.setLocalAnchor(new Point2D(-setters.getX(),-setters.getY()));
        setters.setAnchoredPosition(-750,0);

        exit.setLocalAnchor(new Point2D(-exit.getX(),-exit.getY()));
        exit.setAnchoredPosition(-750,0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
