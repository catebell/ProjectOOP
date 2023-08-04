package com.game.projectoop;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;

public class SmokeComponent extends Component {
    private final AnimatedTexture texture;
    boolean ready = false;
    private final Image image1 = image("SmokeJump1Mod.png");
    private final Image image2 = image("SmokeJump2.png");
    private final Image image3 = image("SmokeJump3.png");

    ArrayList<AnimationChannel> animLandings = new ArrayList<>(List.of(
            new AnimationChannel(image1, 9, 52, 32, Duration.seconds(0.5), 0, 8),
            new AnimationChannel(image2, 9, 32, 32, Duration.seconds(0.5), 0, 8),
            new AnimationChannel(image3, 9, 32, 32, Duration.seconds(0.5), 0, 8)
    ));

    public SmokeComponent() {
        texture = new AnimatedTexture(animLandings.get(0));
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));

        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        Entity player = FXGL.getGameWorld().getEntitiesByType(App.EntityType.PLAYER).get(0);
        if(player.getComponent(PlayerComponent.class).isJumping()) {
            ready=true;
        }

        if(!player.getComponent(PlayerComponent.class).isJumping() && ready){
            //texture.playAnimationChannel(animLandings.get(random(0,2)));
            texture.playAnimationChannel(animLandings.get(0));
            ready=false;
        }
    }
}
