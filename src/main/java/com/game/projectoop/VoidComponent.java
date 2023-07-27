package com.game.projectoop;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;

public class VoidComponent extends Component {
    Texture texture;
    public VoidComponent() {
        texture = new Texture(FXGL.image("Mask.png"));
    }
    public void onAdded() {
       entity.getViewComponent().addChild(texture);
       entity.getTransformComponent().setLocalAnchor(new Point2D(940,985));
       entity.setScaleOrigin(new Point2D(940,985));
       entity.setScaleX(1);
    }
    public void onUpdate(double tpf){
        entity.getTransformComponent().setAnchoredPosition(FXGL.geto("PlayerPosition"));
        entity.setScaleX(FXGL.geti("PlayerScaleX"));
    }
}
