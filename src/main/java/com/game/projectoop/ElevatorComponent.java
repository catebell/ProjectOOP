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
    private final AnimationChannel animOpening;

    private boolean isON=false;
    private boolean isOpen=false;

    public ElevatorComponent() {
        Image image = image("elevator.png");

        animActivation = new AnimationChannel(image, 5, 72, 72, Duration.seconds(1), 0, 4);
        animOpening = new AnimationChannel(image,5,72,72,Duration.seconds(0.5),5,9);

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

    public void open() {
        texture.playAnimationChannel(animOpening);
        texture.setOnCycleFinished(() -> isOpen = true);
    }

    public boolean isON() {
        return isON;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
