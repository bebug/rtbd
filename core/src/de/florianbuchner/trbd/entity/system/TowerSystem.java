package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.florianbuchner.trbd.entity.component.PositionComponent;
import de.florianbuchner.trbd.entity.component.TowerComponent;

public class TowerSystem extends IteratingSystem {

    private float ROTATIONSPEED = -120F;

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    public TowerSystem() {
        super(Family.all(TowerComponent.class).get());

        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);

        positionComponent.facing.setAngle(positionComponent.facing.angle() + ROTATIONSPEED * deltaTime);
    }
}
