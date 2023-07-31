package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class ElevatorComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animActivation;
    private final AnimationChannel animOFF;
    private final AnimationChannel animON;

    private boolean isON=false;

    public ElevatorComponent() {
        Image image = image("ElevatorAnim.png");

        animActivation = new AnimationChannel(image, 5, 64, 64, Duration.seconds(1), 0, 4);
        animOFF = new AnimationChannel(image, 5, 64, 64, Duration.seconds(1), 0, 0);
        animON = new AnimationChannel(image, 5, 64, 64, Duration.seconds(1), 4, 4);

        texture = new AnimatedTexture(animOFF);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (isON) {
            texture.loopAnimationChannel(animON);
        }
    }

    public void activation() {
        texture.playAnimationChannel(animActivation);
        texture.setOnCycleFinished(() -> isON = true);
    }

    public boolean isON() {
        return isON;
    }
}
