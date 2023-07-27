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
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
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
    public enum EntityType {
        PLAYER, PLATFORM, KEY_PROMPT, BUTTON, DIALOGUE_PROMPT, TEXT, VOID
    }

    //? private LazyValue<LevelEndScene> levelEndScene = new LazyValue<>(() -> new LevelEndScene());
    private Entity player;
    private Entity shadow;
    private double accX = 0;
    private boolean sx = false;
    private boolean dx = false;

    private void setLevel() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
        }
        Level level = setLevelFromMap("TestLvl3.tmx");
        List<Entity> layers = level.getEntities();
        int backgrounds = 0;
        for (Entity E : layers) {
            if (E.getTypeComponent().toString().equals("Type(TiledMapLayer)")) {
                if (backgrounds >= level.getProperties().getInt("backgrounds")) {
                    E.setZIndex(2);
                } else {
                    backgrounds++;
                    E.setZIndex(0);
                }
            }
        }

        Viewport viewport = getGameScene().getViewport();
        viewport.setZoom(1.4);
        viewport.setBounds(0, 0, level.getWidth(), level.getHeight());
    }

    private void setLevel(int levelNum) {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
            player.setZIndex(Integer.MAX_VALUE);
        }
        Level level = setLevelFromMap("TestLvl" + levelNum + ".tmx");
    }

    public void onPlayerDied() {
        setLevel(); // [DI PROVA, da togliere dopo e usare quello sotto]
        //setLevel(geti("level"));
    }

    protected void onUpdate(double tpf) {
        if (player.getY() > getAppHeight()) {
            setLevel(/*geti("level")*/);
        }
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
        //settings.setDeveloperMenuEnabled(true);
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        //movement to the left
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                sx = true;
                if (accX > -1) {
                    accX -= 0.1;
                }
                player.getComponent(PlayerComponent.class).move(accX, -1);
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
                dx = true;
                if (accX < 1) {
                    accX += 0.1;
                }
                player.getComponent(PlayerComponent.class).move(accX, 1);
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

        /*getInput().addAction(new UserAction("Use") {
            @Override
            protected void onActionBegin() {
                getGameWorld().getEntitiesByType(EntityType.KEY_PROMPT)
                        .stream()
                        .filter(btn -> btn.hasComponent(CollidableComponent.class)&& player.isColliding(btn))
                        .forEach(btn -> {
                            btn.removeComponent(CollidableComponent.class);

                            Entity keyEntity = btn.getObject("keyEntity");
                            keyEntity.setProperty("activated", true);
                            KeyView view = (KeyView) keyEntity.getViewComponent().getChildren().get(0);
                            view.setKeyColor(Color.RED);
                        });
            }
        },KeyCode.E);*/
    }

    @Override
    protected void initGameVars(Map<String, Object> vars){
        try{
        InputStream file = Files.newInputStream(Paths.get("src/main/resources/assets/ui/fonts/m5x7.ttf"));
        vars.put("font",Font.loadFont(file,20));
        }
        catch(IOException e){
            System.out.println("AHHHHHHHHHHHHHHHHHHHHHHHHHH");
        }
        vars.put("PlayerPosition",new Point2D(0,0));
        vars.put("PlayerScaleX",1);
        vars.put("level", 1);

    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.25);
        //altre impostazioni del genere
        /* [loop music] loopBGM("BGM_dash_runner.wav");*/
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());
        player = null;

        setLevel(); //nextlevel(); [vedi sotto]

        // player must be spawned after call to nextLevel, otherwise player gets removed
        // before the update tick _actually_ adds the player to game world
        player = spawn("player", 50, 50);
        set("player", player);

        spawn("background");
        spawn("void");
        Viewport viewport = getGameScene().getViewport();
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setLazy(true); //smoother camera movement
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 1000);

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.KEY_PROMPT, (player, prompt) -> {

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
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.DIALOGUE_PROMPT, (player, prompt) -> {
            startDialogue(2,prompt);
            //startDialogue(1,prompt); //[partono in contemporanea se fatti andare troppo vicini]
        });

        //la roba che spawna legata ai trigger, spawna dove sono fisicamente TUTTI i trigger? ce ne freghiamo perchè altrimenti bisogna fare spawn separati? Facciamo uno spawn per ogni "entità parlante"?
    }

private HashMap<Integer,List<String>> dialogues(){
        HashMap<Integer,List<String>> dialogues = new HashMap<>();
        dialogues.put(1,List.of("first text","second text"));
        dialogues.put(2,List.of("Lorem ipsum dolor sit amet,\n consectetur adipiscing elit,","sed do eiusmod tempor " +
                        "incididunt ut labore et dolore magna aliqua.",
                "Ut enim ad minim veniam,","quis nostrud exercitation ullamco laboris nisi","ut aliquip ex ea commodo consequat."));
        return dialogues;
}

protected void startDialogue(int dialNumber,Entity prompt){
    HashMap<Integer,List<String>> dial = dialogues();
    double time = 0.0;

    for(String s : dial.get(dialNumber)){ //first dialogue
        Entity dialogueEntity = getGameWorld().create("dialogueText", new SpawnData(prompt.getX(),
                prompt.getY()).put("Text",s));

        //se vogliamo tenere gli effetti anche in out e/o ritardare la comparsa delle frasi
        //System.out.println("s = " + s + " e elemento 0 = " + dial.get(1).get(0)); //DEBUG
        /*if(dial.get(dialNumber).get(0).equals(s)){ //element 0 spawn in 0 time
            runOnce(()->spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()),Duration.seconds(time));
            System.out.println("siamo al primo elemento");
        }else{ //delay spawn
            runOnce(()->spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()),Duration.seconds(time+0.5));
        }*/

        runOnce(()->spawnWithScale(dialogueEntity, Duration.seconds(1), Interpolators.ELASTIC.EASE_OUT()),Duration.seconds(time));
        time += 0.3 * s.toCharArray().length;
        //System.out.println("Lunghezza di " + s + " = " + s.toCharArray().length + " time = " + time); //DEBUG

        if(dial.get(dialNumber).get(dial.get(dialNumber).size() - 1).equals(s)){ /*oppure con una deque per avere direttamente last element*/
            runOnce(()->despawnWithScale(dialogueEntity,Duration.seconds(1),Interpolators.ELASTIC.EASE_IN()),Duration.seconds(time));
        }else{
            despawnWithDelay(dialogueEntity,Duration.seconds(time));
        }
    }
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

    public static void main(String[] args) {
        launch(args);
    }
}
