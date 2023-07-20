package com.game.projectoop;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.FXGLPane;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLScene;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


public class App extends GameApplication {
    public enum EntityType{
        PLAYER,PLATFORM,BUTTON
    }

    //? private LazyValue<LevelEndScene> levelEndScene = new LazyValue<>(() -> new LevelEndScene());
    private Entity player;
    private double accX=0;

    private void setLevel() {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
            player.setZIndex(Integer.MAX_VALUE);
        }
        Level level = setLevelFromMap("TestLvl2.tmx");
    }

    // [per più livelli, CONTROLLARE NOMI]
    private void setLevel(int levelNum) {
        if (player != null) {
            player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
            player.setZIndex(Integer.MAX_VALUE);
        }
        Level level = setLevelFromMap("tmx/level" + levelNum  + ".tmx");
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
        settings.setHeight(24*30);
        settings.setWidth(30*36);
        settings.setFullScreenAllowed(true);

        settings.setSceneFactory(new SceneFactory(){
            @Override
            public LoadingScene newLoadingScene() {
                return new MainLoadingScene();
            }
        });
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initInput() {
        //movement to the left
        getInput().addAction(new UserAction("Left"){
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left(accX);
                if(accX<1){ accX+=0.08;}
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
                accX=0;
            }
        }, KeyCode.A);

        //movement to the right
        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right(accX);
                if(accX<1){ accX+=0.08;}
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
                accX=0;
            }
        },KeyCode.D);

        //jump
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).jump();
            }
        },KeyCode.W);

        getInput().addAction(new UserAction("Use") {
            @Override
            protected void onActionBegin() {
                getGameWorld().getEntitiesByType(EntityType.BUTTON)
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
        },KeyCode.E);
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("acceleration",0.0);
        vars.put("level",1);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.25);
        //altre impostazioni del genere
        /* [what is this] loopBGM("BGM_dash_runner.wav");*/
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new PlatformerFactory());
        player = null;


        setLevel(); //nextlevel(); [vedi sotto]

        // player must be spawned after call to nextLevel, otherwise player gets removed
        // before the update tick _actually_ adds the player to game world
        player = spawn("player", 50, 50);
        set("player",player);

        spawn("background");

        Viewport viewport = getGameScene().getViewport();
        viewport.setZoom(1.5);
        viewport.setBounds(0,0,36*30,24*30);
        System.out.println(getAppHeight());
        viewport.bindToEntity(player,getAppWidth()/2.0,getAppHeight()/2.0);
        viewport.setLazy(true); //smoother camera movement
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0,1000);

        //GESTIONE COLLISIONI
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
