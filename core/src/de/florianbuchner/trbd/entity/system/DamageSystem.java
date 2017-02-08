package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.core.DamageHandler;
import de.florianbuchner.trbd.entity.component.*;

import java.util.LinkedList;
import java.util.List;

public class DamageSystem extends IteratingSystem {

    private ComponentMapper<EnemyComponent> enemyComponentComponentMapper;
    private ComponentMapper<DamageComponent> damageComponentComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    private List<Entity> enemyEntities = new LinkedList<Entity>();
    private List<Entity> damageEntities = new LinkedList<Entity>();

    public DamageSystem() {
        super(Family.one(EnemyComponent.class, DamageComponent.class).get());

        this.enemyComponentComponentMapper = ComponentMapper.getFor(EnemyComponent.class);
        this.damageComponentComponentMapper = ComponentMapper.getFor(DamageComponent.class);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (this.enemyComponentComponentMapper.has(entity)) {
            this.enemyEntities.add(entity);
        }
        if (this.damageComponentComponentMapper.has(entity)) {
            this.damageEntities.add(entity);
        }
    }

    @Override
    public void update(float deltaTime) {
        this.enemyEntities.clear();
        this.damageEntities.clear();
        super.update(deltaTime);

        for (Entity damageEntity : damageEntities) {
            DamageComponent damageComponent =  this.damageComponentComponentMapper.get(damageEntity);
            damageComponent.damageHandler.dealDamage(damageEntity, this.enemyEntities);

            PositionComponent positionComponent = this.positionComponentComponentMapper.get(damageEntity);
            if (positionComponent != null) {
                damageComponent.lastPosition = new Vector2(positionComponent.position);
                damageComponent.lastFacing = new Vector2(positionComponent.facing);
            }
        }
    }
}
