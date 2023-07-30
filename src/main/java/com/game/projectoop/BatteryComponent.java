package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class BatteryComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animActivation;
    private final AnimationChannel animOFF;
    private final AnimationChannel animON;

    public BatteryComponent(){
        Image image = image("BatteryMovement.png");

        animActivation = new AnimationChannel(image, 4, 32, 32, Duration.seconds(0.66), 0, 3);
        animOFF = new AnimationChannel(image,4,32,32,Duration.seconds(1),0,0);
        animON = new AnimationChannel(image,4,32,32,Duration.seconds(1),3,3);

        texture = new AnimatedTexture(animOFF);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        //[to do] texture.loopAnimationChannel(animON); e finito il minigioco mettere l'animazione
    }
}
