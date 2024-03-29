package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class LightsComponent extends Component {

    private final AnimatedTexture texture;
    private final AnimationChannel anim;

    private boolean isON=false;

    public LightsComponent() {
        Image image = image("Lights.png");

        anim = new AnimationChannel(image, 2, 32, 32, Duration.seconds(1), 0, 1);

        texture = new AnimatedTexture(anim);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    public void activation() {
        texture.playAnimationChannel(anim);
        isON = true;
    }

    public boolean isON() {
        return isON;
    }
}
