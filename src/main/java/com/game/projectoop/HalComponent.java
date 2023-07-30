package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class HalComponent extends Component {
    private final AnimatedTexture texture;

    public HalComponent() {
        Image image = image("HalMovement.png");

        AnimationChannel animHover = new AnimationChannel(image, 4, 32, 32, Duration.seconds(0.66), 0, 3);
        texture = new AnimatedTexture(animHover);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
