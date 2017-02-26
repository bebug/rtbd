package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.florianbuchner.trbd.background.BackgroundTileHandler;
import de.florianbuchner.trbd.core.KillHandler;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.component.HealthComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class HealthSystem extends IteratingSystem {

    private ComponentMapper<HealthComponent> healthComponentComponentMapper;

    private ComponentMapper<PositionComponent> positionComponentMapper;

    private EntityFactory entityFactory;

    private Engine engine;

    private BackgroundTileHandler backgroundTileHandler;

    private KillHandler killHandler;

    public HealthSystem(EntityFactory entityFactory, Engine engine, BackgroundTileHandler backgroundTileHandler, KillHandler killHandler) {
        super(Family.all(HealthComponent.class).get());
        this.healthComponentComponentMapper = ComponentMapper.getFor(HealthComponent.class);
        this.positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.entityFactory = entityFactory;
        this.engine = engine;
        this.killHandler = killHandler;
        this.backgroundTileHandler = backgroundTileHandler;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = this.healthComponentComponentMapper.get(entity);

        if (healthComponent.health <= 0L || healthComponent.death) {
            if (!healthComponent.death) {
                this.killHandler.enemyKilled();
            }

            healthComponent.death = true;
            PositionComponent positionComponent = this.positionComponentMapper.get(entity);

            if (positionComponent != null) {
                this.backgroundTileHandler.destroyTile(positionComponent.position);
                for (Entity explosionEntity : this.entityFactory.createExplosions(positionComponent.position, 40, 5, 0.3f,  this.engine)) {
                    this.engine.addEntity(explosionEntity);
                }
            }
        }
    }
}
