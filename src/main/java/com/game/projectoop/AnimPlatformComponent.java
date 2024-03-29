package com.game.projectoop;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class AnimPlatformComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animActivation;
    private boolean isOn=false;
    private final List<Entity> list=new ArrayList<>();
    private boolean activated =false;
    public AnimPlatformComponent() {
        Image image = image("PlatformMovement.png");

        animActivation = new AnimationChannel(image, 4, 32, 32, Duration.seconds(0.66), 0, 3);
        texture = new AnimatedTexture(animActivation);
        texture.stop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if(!activated){
            list.add(getGameWorld().getClosestEntity(entity,(platform)->platform.isType(App.EntityType.PLATFORM)).get());
            list.get(0).getComponent(PhysicsComponent.class).overwritePosition(new Point2D(-150, -150));
            activated =true;
        }
    }

    public void activation(){
        texture.playAnimationChannel(animActivation);
        texture.setOnCycleFinished(()->isOn=true);
        for (Entity platform: list) {
            platform.getComponent(PhysicsComponent.class).overwritePosition(entity.getPosition());
        }
    }
}
