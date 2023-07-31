package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class LightsComponent extends Component {

    private final AnimatedTexture texture;
    private final AnimationChannel animOFF;
    private final AnimationChannel animON;

    private boolean isON=false;

    public LightsComponent() {
        Image image = image("Lights.png");

        animOFF = new AnimationChannel(image, 3, 32, 32, Duration.seconds(1), 0, 0);
        animON = new AnimationChannel(image, 3, 32, 32, Duration.seconds(1), 1, 1);

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
        texture.loopAnimationChannel(animON);
        System.out.println(texture);
        isON = true;
    }

    public boolean isON() {
        return isON;
    }
}
