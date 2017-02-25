package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.PositionComponent;
import de.florianbuchner.trbd.entity.component.TargetComponent;

public class TargetSystem extends IteratingSystem {

    private ComponentMapper<TargetComponent> targetComponentComponentMapper;

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    private Engine engine;

    @SuppressWarnings("unchecked")
    public TargetSystem(Engine engine) {
        super(Family.all(TargetComponent.class).get());
        this.targetComponentComponentMapper = ComponentMapper.getFor(TargetComponent.class);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TargetComponent targetComponent = this.targetComponentComponentMapper.get(entity);
        Vector2 newFacing = new Vector2(targetComponent.positionComponent.position.x, targetComponent.positionComponent.position.y).nor();

        this.positionComponentComponentMapper.get(entity).facing.set(newFacing);

        if (this.targetComponentComponentMapper.get(entity).healthComponent.health <= 0) {
            this.engine.removeEntity(entity);
        }
    }
}
