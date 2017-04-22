package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.florianbuchner.trbd.background.BackgroundTileHandler;
import de.florianbuchner.trbd.core.KillHandler;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.component.EnemyComponent;
import de.florianbuchner.trbd.entity.component.HealthComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class HealthSystem extends IteratingSystem {

    private ComponentMapper<HealthComponent> healthComponentComponentMapper;

    private ComponentMapper<PositionComponent> positionComponentMapper;

    private ComponentMapper<EnemyComponent> enemyComponentComponentMapper;

    private EntityFactory entityFactory;

    private Engine engine;

    private BackgroundTileHandler backgroundTileHandler;

    private KillHandler killHandler;

    public HealthSystem(EntityFactory entityFactory, Engine engine, BackgroundTileHandler backgroundTileHandler, KillHandler killHandler) {
        super(Family.all(HealthComponent.class).get());
        this.healthComponentComponentMapper = ComponentMapper.getFor(HealthComponent.class);
        this.positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.enemyComponentComponentMapper = ComponentMapper.getFor(EnemyComponent.class);
        this.entityFactory = entityFactory;
        this.engine = engine;
        this.killHandler = killHandler;
        this.backgroundTileHandler = backgroundTileHandler;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = this.healthComponentComponentMapper.get(entity);
        PositionComponent positionComponent = this.positionComponentMapper.get(entity);

        if (healthComponent.health <= 0L || healthComponent.death) {
            // enemy killed by player
            if (!healthComponent.death && this.enemyComponentComponentMapper.has(entity)) {
                this.killHandler.enemyKilled(positionComponent != null ? positionComponent.position : null);
            }

            // destroy animation on negative health
            if (healthComponent.health <= 0L) {

                if (positionComponent != null) {
                    this.backgroundTileHandler.destroyTile(positionComponent.position);
                    for (Entity explosionEntity : this.entityFactory.createExplosions(positionComponent.position, 40, 5, 0.3f, this.engine)) {
                        this.engine.addEntity(explosionEntity);
                    }
                }
            }
            healthComponent.death = true;
            this.engine.removeEntity(entity);
        }
    }
}
