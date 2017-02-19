package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class PositionSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    @SuppressWarnings("unchecked")
    public PositionSystem() {
        super(Family.all(PositionComponent.class).get());
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
        if (positionComponent.body != null) {
            positionComponent.body.setRotation(positionComponent.facing.angle());
            positionComponent.body.setPosition(positionComponent.position.x, positionComponent.position.y);
        }
    }
}