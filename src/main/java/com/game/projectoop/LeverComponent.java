package com.game.projectoop;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class LeverComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animPull;
    private Music pullSound = getAssetLoader().loadMusic("leverPull.mp3");

    private boolean isPulled=false;

    public LeverComponent() {
        Image image = image("LeverMovement.png");

        animPull = new AnimationChannel(image, 5, 32, 32, Duration.seconds(0.5), 0, 4);
        texture = new AnimatedTexture(animPull);
        texture.stop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        isPulled = false;
        entity.getViewComponent().addChild(texture);
    }

    public void pull() {
        getAudioPlayer().playMusic(pullSound);
        runOnce(()->getAudioPlayer().stopMusic(pullSound),Duration.seconds(2));
        texture.playAnimationChannel(animPull);
        texture.setOnCycleFinished(() -> isPulled = true);
    }

    public boolean isPulled() {
        return isPulled;
    }
}
