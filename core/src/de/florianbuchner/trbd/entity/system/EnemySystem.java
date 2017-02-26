package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import de.florianbuchner.trbd.core.EnemySpawner;
import de.florianbuchner.trbd.entity.component.EnemyComponent;
import de.florianbuchner.trbd.entity.component.HealthComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class EnemySystem extends EntitySystem {

    private final EnemySpawner enemySpawner;
    private final Family family;
    private ImmutableArray<Entity> entities;

    private ComponentMapper<HealthComponent> healthComponentComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentMapper;

    public EnemySystem(EnemySpawner enemySpawner) {
        super();
        this.enemySpawner = enemySpawner;
        this.family = Family.all(EnemyComponent.class).get();
        this.healthComponentComponentMapper = ComponentMapper.getFor(HealthComponent.class);
        this.positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        entities = null;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.enemySpawner.spawnEnemies(entities.size());

        for (Entity entity : this.entities) {
            PositionComponent positionComponent = this.positionComponentMapper.get(entity);
            HealthComponent healthComponent = this.healthComponentComponentMapper.get(entity);

            if (Math.abs(positionComponent.position.x) > 300 ||
                    Math.abs(positionComponent.position.y) > 300) {
                healthComponent.death = true;
            }
        }
    }
}
