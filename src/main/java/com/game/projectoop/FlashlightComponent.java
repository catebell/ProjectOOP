package com.game.projectoop;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class FlashlightComponent extends Component {
    private final AnimatedTexture texture;
    private final AnimationChannel flashlightIdle;
    private final AnimationChannel flashlightFlip;
    private boolean isFlipping=false;

    public FlashlightComponent() {
        Image image = FXGL.image("MaskSheet-Sheet.png");
        flashlightIdle = new AnimationChannel(image,6,2000,2000,Duration.seconds(1),0,0);
        flashlightFlip = new AnimationChannel(image,6,2000,2000,Duration.seconds(0.2),0,5);
        texture = new AnimatedTexture(flashlightIdle);
    }
    public void onAdded() {
       entity.getViewComponent().addChild(texture);
       entity.getTransformComponent().setLocalAnchor(new Point2D(990,990));
       entity.setScaleOrigin(new Point2D(1000,990));
       entity.setRotationOrigin(new Point2D(1000,990));
       entity.setScaleX(1);
    }
    public void onUpdate(double tpf){
        entity.getTransformComponent().setAnchoredPosition(FXGL.geto("PlayerPosition"));
        if(entity.getScaleX()!=FXGL.geti("PlayerScaleX")){
            if (texture.getAnimationChannel() != flashlightFlip) {
                isFlipping=true;
                texture.playAnimationChannel(flashlightFlip);
                texture.setOnCycleFinished(()->{
                    isFlipping=false;
                    entity.setScaleX(FXGL.geti("PlayerScaleX"));
                });
            }
        }else{
            if(!isFlipping) {
                if (texture.getAnimationChannel() != flashlightIdle) {
                    texture.loopAnimationChannel(flashlightIdle);
                }
            }
        }
    }
}
