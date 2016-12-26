package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.florianbuchner.trbd.entity.component.DelayComponent;

public class DelaySystem extends IteratingSystem {

    private ComponentMapper<DelayComponent> deleayComponentComponentMapper;

    public DelaySystem() {
        super(Family.all(DelayComponent.class).get());
        this.deleayComponentComponentMapper = ComponentMapper.getFor(DelayComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DelayComponent delayComponent = this.deleayComponentComponentMapper.get(entity);

        delayComponent.delayCountdown -= deltaTime;

        if(delayComponent.delayCountdown <= 0f && delayComponent.delayHandler != null) {
            delayComponent.delayHandler.onDelay();
        }
    }
}
