package com.game.projectoop;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class PlayerComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel animIdle;
    private final AnimationChannel animWalk;
    private final AnimationChannel animFly;
    private final AnimationChannel animLanding;
    private PhysicsComponent physics;
    private int jumps = 2;
    private boolean isJumping = false;
    private boolean landed = true;

    public PlayerComponent() {
        Image image = image("CharacterMovement.png");

        animIdle = new AnimationChannel(image, 8, 32, 32, Duration.seconds(1), 0, 2);
        animWalk = new AnimationChannel(image, 8, 32, 33, Duration.seconds(0.8), 8, 7 * 2);
        animFly = new AnimationChannel(image, 8, 32, 33, Duration.seconds(0.7), 10, 10);
        animLanding = new AnimationChannel(image, 8, 32, 33, Duration.seconds(0.2), 4, 8);
        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        FXGL.set("PlayerPosition", entity.getPosition());
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                jumps = 1;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        FXGL.set("PlayerPosition", entity.getPosition());
        if (!physics.isOnGround()) {
            isJumping = true;
        }

        if (isJumping) {
            if (physics.isOnGround()) {
                landed = false;
                texture.playAnimationChannel(animLanding);
                texture.setOnCycleFinished(() -> landed = true);
                isJumping = false;
            } else {
                texture.loopAnimationChannel(animFly);
            }
        } else {
            if (landed) {
                if (physics.isMovingX()) {
                    if (texture.getAnimationChannel() != animWalk) {
                        texture.loopAnimationChannel(animWalk);
                    }
                } else {
                    if (texture.getAnimationChannel() != animIdle) {
                        texture.loopAnimationChannel(animIdle);
                    }
                }
            }
        }
    }

    public void move(double acc, int scaleX) {
        getEntity().setScaleX(scaleX);
        FXGL.set("PlayerScaleX", scaleX);
        physics.setVelocityX(150 * acc + 25 * scaleX);
    }

    public void stop() {
        physics.setVelocityX(0);
    }

    public void jump() {
        if (jumps == 0 || !physics.isOnGround()) {
            return;
        }
        physics.setVelocityY(-500);
        jumps--;
    }
}