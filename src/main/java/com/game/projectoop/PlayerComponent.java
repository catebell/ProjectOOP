package com.game.projectoop;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class PlayerComponent extends Component {
    private PhysicsComponent physics;
    private AnimatedTexture texture;
    private AnimationChannel animIdle;
    private AnimationChannel animWalk;
    private int jumps = 2;

    public PlayerComponent(){
        Image image = image("player.png");

        animIdle = new AnimationChannel(image,4,32,42, Duration.seconds(1),1,1);
        animWalk = new AnimationChannel(image,4,32,42, Duration.seconds(0.66),0,3);
        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16,21));
        entity.getViewComponent().addChild(texture);

        physics.onGroundProperty().addListener((obs,old,isOnGround)->{
            if(isOnGround){
                jumps = 2;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {

        if(physics.isMovingX()){
            if(texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        }
        else{
            if(texture.getAnimationChannel() != animIdle){
                texture.loopAnimationChannel(animIdle);
            }
        }
    }

    public void move(double acc,int scaleX){
        getEntity().setScaleX(scaleX);
        physics.setVelocityX(150*acc+25*scaleX);
    }

    public void stop(){
        //if(physics.isOnGround()){
            physics.setVelocityX(0);
        //}
    }
    public void jump(){
        if(jumps==0){
            return;
        }
        physics.setVelocityY(-450);
        jumps--;
    }
}