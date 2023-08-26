package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class MonitorComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animActivation;

    private boolean isON=false;

    public MonitorComponent() {
        Image image = image("HangingMonitor.png");

        animActivation = new AnimationChannel(image, 2, 32, 32, Duration.seconds(0.2), 0, 1);

        texture = new AnimatedTexture(animActivation);
        texture.stop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    public void activation() {
        texture.playAnimationChannel(animActivation);
        texture.setOnCycleFinished(() -> isON = true);
    }

    public boolean isON() {
        return isON;
    }
}