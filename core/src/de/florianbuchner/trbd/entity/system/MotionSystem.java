package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class MotionSystem extends IteratingSystem {

    private ComponentMapper<MotionComponent> motionComponentComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    @SuppressWarnings("unchecked")
    public MotionSystem() {
        super(Family.all(MotionComponent.class, PositionComponent.class).get());
        this.motionComponentComponentMapper = ComponentMapper.getFor(MotionComponent.class);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
        this.motionComponentComponentMapper.get(entity).handler.update(positionComponent, deltaTime);
    }
}
