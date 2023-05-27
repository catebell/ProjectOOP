package com.game.projectoop;

import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class PlatformerFactory implements EntityFactory {
    @Spawns("background")
    public Entity newBackground(SpawnData data){
        return entityBuilder()
            .view(new ScrollingBackgroundView(texture("background/forest.png").getImage(),getAppWidth(),getAppHeight()))
            .zIndex(-1) //piano profondità -1->background
            .with(new IrremovableComponent())
            .build();
    }

}


