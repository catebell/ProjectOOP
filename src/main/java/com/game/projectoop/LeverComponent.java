package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class LeverComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animPull;
    private final AnimationChannel animOFF;
    private final AnimationChannel animON;

    private boolean isPulled;

    public LeverComponent() {
        Image image = image("LeverMovement.png");

        animPull = new AnimationChannel(image, 5, 32, 32, Duration.seconds(0.5), 0, 4);
        animOFF = new AnimationChannel(image, 5, 32, 32, Duration.seconds(1), 0, 0);
        animON = new AnimationChannel(image, 5, 32, 32, Duration.seconds(1), 4, 4);
        texture = new AnimatedTexture(animOFF);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        isPulled = false;
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (isPulled) {
            texture.loopAnimationChannel(animON);
        }
    }

    public void pull() {
        texture.playAnimationChannel(animPull);
        texture.setOnCycleFinished(() -> isPulled = true);
    }

    public boolean isPulled() {
        return isPulled;
    }
}
