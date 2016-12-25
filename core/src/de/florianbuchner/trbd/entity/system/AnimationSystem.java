package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.florianbuchner.trbd.entity.component.AnimationComponent;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<AnimationComponent> animationComponentComponentMapper;

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());

        this.animationComponentComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.animationComponentComponentMapper.get(entity).update(deltaTime);
    }
}
