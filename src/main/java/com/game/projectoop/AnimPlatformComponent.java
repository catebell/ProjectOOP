package com.game.projectoop;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class AnimPlatformComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animActivation;
    private final AnimationChannel animOFF;
    private final AnimationChannel animON;

    public AnimPlatformComponent(){
        Image image = image("PlatformMovement.png");

        animActivation = new AnimationChannel(image, 4, 32, 32, Duration.seconds(0.66), 3, 1);
        animOFF = new AnimationChannel(image,4,32,32,Duration.seconds(1),3,3);
        animON = new AnimationChannel(image,4,32,32,Duration.seconds(1),0,0);

        texture = new AnimatedTexture(animOFF);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        //[to do] texture.loopAnimationChannel(animON); e quando si attiva la leva mettere l'animazione
    }
}
