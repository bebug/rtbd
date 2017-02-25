package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import de.florianbuchner.trbd.core.EnemySpawner;
import de.florianbuchner.trbd.entity.component.EnemyComponent;

public class EnemySystem extends EntitySystem {

    private final EnemySpawner enemySpawner;
    private final Family family;
    private ImmutableArray<Entity> entities;

    public EnemySystem(EnemySpawner enemySpawner) {
        super();
        this.enemySpawner = enemySpawner;
        this.family = Family.all(EnemyComponent.class).get();
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

        if (entities.size() <= 0) {
            this.enemySpawner.spawnEnemies();
        }
    }
}
