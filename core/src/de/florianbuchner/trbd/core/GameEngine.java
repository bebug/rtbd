package de.florianbuchner.trbd.core;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.*;
import de.florianbuchner.trbd.background.BackgroundComposer;
import de.florianbuchner.trbd.entity.CircleMotionHandler;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.component.*;
import de.florianbuchner.trbd.entity.system.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameEngine implements EnemySpawner, KillHandler {

    private final static float TOWER_LENGTH = 35F;

    private GameData gameData;
    private Resources resources;
    private Engine entityEngine;
    private EntityFactory entityFactory;
    private BackgroundComposer backgroundComposer;

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;
    private ComponentMapper<AnimationComponent> animationComponentComponentMapper;
    private ComponentMapper<DamageComponent> damageComponentComponentMapper;
    private ComponentMapper<HealthComponent> healthComponentComponentMapper;
    private ComponentMapper<MotionComponent> motionComponentComponentMapper;

    private final Random randomizer;

    public GameEngine(GameData gameData, Resources resources, int length, int height) {
        this.gameData = gameData;
        this.resources = resources;
        this.entityFactory = new EntityFactory(resources);
        this.entityEngine = new Engine();
        this.randomizer = new Random(new Date().getTime());
        this.backgroundComposer = new BackgroundComposer(length, height, resources);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.animationComponentComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
        this.damageComponentComponentMapper = ComponentMapper.getFor(DamageComponent.class);
        this.healthComponentComponentMapper = ComponentMapper.getFor(HealthComponent.class);
        this.motionComponentComponentMapper = ComponentMapper.getFor(MotionComponent.class);

        this.createBaseEntities();
        this.createBaseSystems();

        this.restart();
    }

    private void createBaseEntities() {
        // Add background entities
        for (Entity entity : this.backgroundComposer.getEntities()) {
            this.entityEngine.addEntity(entity);
        }

        this.entityEngine.addEntity(this.entityFactory.createFoundation());
        final Entity towerEntity = this.entityFactory.createTower();
        // Data will be set by reference to gamedata
        this.gameData.towerPosition = this.positionComponentComponentMapper.get(towerEntity);
        this.gameData.towerAnimation = this.animationComponentComponentMapper.get(towerEntity);
        MotionComponent towerMotionComponent = this.motionComponentComponentMapper.get(towerEntity);
        if (towerMotionComponent.handler instanceof CircleMotionHandler) {
            this.gameData.towerMotionHandler = (CircleMotionHandler)towerMotionComponent.handler;
        }
        this.entityEngine.addEntity(towerEntity);
    }

    private void createBaseSystems() {
        this.entityEngine.addSystem(new DelaySystem());
        this.entityEngine.addSystem(new AnimationSystem());
        this.entityEngine.addSystem(new MotionSystem());
        this.entityEngine.addSystem(new PositionSystem());
        this.entityEngine.addSystem(new DamageSystem());
        this.entityEngine.addSystem(new TargetSystem(this.entityEngine));
        this.entityEngine.addSystem(new HealthSystem(this.entityFactory, this.entityEngine, this.backgroundComposer, this));
        this.entityEngine.addSystem(new EnemySystem(this));
        this.entityEngine.addSystem(new DrawingSystem(this.resources, this.gameData));
    }

    public void restart() {
        for (WeaponType weaponType : WeaponType.values()) {
            this.gameData.weaponEnergies.put(weaponType, new WeaponEnergie());
        }
        this.gameData.level = 1;

    }

    public void update(float delta) {
        this.entityEngine.update(delta);

        for (WeaponType weaponType : WeaponType.values()) {
            float energyGain = 0f;
            switch (weaponType) {
                case GUN:
                    energyGain = 0.95f;
                    break;
                case BLAST:
                    energyGain = 0.2f;
                    break;
                case LASER:
                    energyGain = 0.25f;
                    break;
                case BOMB:
                    energyGain = 0.4f;
                    break;
                default:
                    break;
            }

            this.gameData.weaponEnergies.get(weaponType).add(delta * energyGain);
        }
    }

    public void buttonPressed(WeaponType weaponType) {
        if (this.gameData.weaponEnergies.get(weaponType).getEnergy() < 1F) {
            return;
        }

        Vector2 shootFacing = this.gameData.towerPosition.facing.nor();

        switch (weaponType) {
            case GUN:
                this.entityEngine.addEntity(this.entityFactory.createGun(this.createBulletStartPosition(shootFacing), new Vector2(shootFacing), this.entityEngine, new DamageHandler() {
                    @Override
                    public void dealDamage(Entity damageSource, List<Entity> entitiesToCheck) {
                        GameEngine.this.dealDamageGun(damageSource, entitiesToCheck);
                    }
                }));
                this.gameData.weaponEnergies.get(WeaponType.GUN).reset();
                this.gameData.towerAnimation.resetAnimation();
                break;
            case LASER:
                List<Entity> entities = this.entityFactory.createLaser(this.gameData.towerPosition.position, shootFacing, this.entityEngine, TOWER_LENGTH, new DamageHandler() {
                    @Override
                    public void dealDamage(Entity damageSource, List<Entity> entitiesToCheck) {
                        GameEngine.this.dealDamageLaser(damageSource, entitiesToCheck);
                    }
                });
                for (Entity singleEntity : entities) {
                    this.entityEngine.addEntity(singleEntity);
                }
                this.gameData.weaponEnergies.get(WeaponType.LASER).reset();
                break;
            case BOMB:
                this.entityEngine.addEntity(this.entityFactory.createBomb(this.createBulletStartPosition(shootFacing), new Vector2(shootFacing), this.entityEngine, new DamageHandler() {
                    @Override
                    public void dealDamage(Entity damageSource, List<Entity> entitiesToCheck) {
                        GameEngine.this.dealDamageBomb(damageSource, entitiesToCheck);
                    }
                }));
                this.gameData.weaponEnergies.get(WeaponType.BOMB).reset();
                this.gameData.towerAnimation.resetAnimation();
                break;
            case BLAST:
                entities = this.entityFactory.createBlast(new Vector2(0, 0), this.entityEngine, new DamageHandler() {
                    @Override
                    public void dealDamage(Entity damageSource, List<Entity> entitiesToCheck) {
                        GameEngine.this.dealDamageBlast(damageSource, entitiesToCheck);
                    }
                });
                for (Entity singleEntity : entities) {
                    this.entityEngine.addEntity(singleEntity);
                }
                this.gameData.weaponEnergies.get(WeaponType.BLAST).reset();
                break;
        }
    }

    public void rotateScreen(float angle) {
        this.setScreenRoation(this.gameData.rotationAngle + angle);
    }

    public void setScreenRoation(float angle) {
        if (this.gameData.rotationAngle - angle > 180f) {
            this.gameData.rotationAngle -= 360;
        }
        if (this.gameData.rotationAngle - angle < -180f) {
            this.gameData.rotationAngle += 360;
        }

        this.gameData.rotationAngle = this.gameData.rotationAngle * 0.8f + angle * 0.2f;
    }

    private void dealDamageGun(Entity damageSource, List<Entity> entitiesToCheck) {
        DamageComponent damageComponent = this.damageComponentComponentMapper.get(damageSource);
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(damageSource);
        if (damageComponent == null || positionComponent == null || positionComponent.body == null) {
            return;
        }

        for (Entity entity : entitiesToCheck) {
            PositionComponent enemyPosition = this.positionComponentComponentMapper.get(entity);
            HealthComponent enemyHealthComponent = this.healthComponentComponentMapper.get(entity);

            if (enemyHealthComponent != null && enemyPosition != null && enemyPosition.body != null &&
                    Intersector.overlapConvexPolygons(positionComponent.body, enemyPosition.body)) {
                long gunDamage = this.getGunDamage();
                enemyHealthComponent.health -= gunDamage;
                this.entityEngine.addEntity(this.entityFactory.createDamageLabel(enemyPosition.position, gunDamage, FontType.NORMAL, this.entityEngine));
                this.entityEngine.removeEntity(damageSource);
                return;
            }
        }
    }

    private void dealDamageLaser(Entity damageSource, List<Entity> entitiesToCheck) {
        DamageComponent damageComponent = this.damageComponentComponentMapper.get(damageSource);
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(damageSource);
        if (damageComponent == null || damageComponent.lastFacing == null || positionComponent == null || positionComponent.facing == null) {
            return;
        }
        float startAngle = this.gameData.towerMotionHandler.getSpeed() < 0 ? damageComponent.lastFacing.angle() : positionComponent.facing.angle();
        float endAngle = this.gameData.towerMotionHandler.getSpeed() < 0 ? positionComponent.facing.angle() : damageComponent.lastFacing.angle();

        for (Entity entity : entitiesToCheck) {
            PositionComponent enemyPositionComponent = this.positionComponentComponentMapper.get(entity);
            HealthComponent enemyHealthComponent = this.healthComponentComponentMapper.get(entity);

            if (enemyPositionComponent != null && enemyHealthComponent != null) {
                float enemyAngle = enemyPositionComponent.position.angle();
                float lastEnemyAngle = enemyPositionComponent.lastPosition.angle();

                if (this.angleInRange(startAngle, enemyAngle, endAngle) || this.angleInRange(startAngle, lastEnemyAngle, endAngle)) {
                    long laserDamage = this.getLaserDamage();
                    enemyHealthComponent.health -= laserDamage;
                    this.entityEngine.addEntity(this.entityFactory.createDamageLabel(enemyPositionComponent.position, laserDamage, FontType.NORMAL, this.entityEngine));
                }
            }
        }
    }

    private boolean angleInRange(float startAngle, float enemyAngle, float endAngle) {
        return startAngle > enemyAngle && enemyAngle > endAngle ||
                startAngle < endAngle && (enemyAngle > endAngle || enemyAngle < startAngle);
    }

    private void dealDamageBomb(Entity damageSource, List<Entity> entitiesToCheck) {
        DamageComponent damageComponent = this.damageComponentComponentMapper.get(damageSource);
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(damageSource);
        if (damageComponent == null || positionComponent == null || positionComponent.body == null) {
            return;
        }

        boolean hit = false;
        List<Entity> entitiesToDamage = new LinkedList<Entity>();
        Circle damageRange = new Circle(positionComponent.position, this.getBombRange());
        for (Entity entity : entitiesToCheck) {
            PositionComponent enemyPosition = this.positionComponentComponentMapper.get(entity);

            if (enemyPosition != null && enemyPosition.body != null) {
                if(Intersector.overlapConvexPolygons(positionComponent.body, enemyPosition.body)) {
                    hit = true;
                    for (Entity explosionEntity : this.entityFactory.createExplosions(new Vector2(positionComponent.position), damageRange.radius, 12, 0.5f, this.entityEngine)) {
                        this.entityEngine.addEntity(explosionEntity);
                    }
                    this.entityEngine.removeEntity(damageSource);
                }

                if (damageRange.contains(enemyPosition.position)) {
                    entitiesToDamage.add(entity);
                }
            }
        }

        if (hit) {
            for (Entity entity : entitiesToDamage) {
                PositionComponent enemyPositionComponent = this.positionComponentComponentMapper.get(entity);
                HealthComponent enemyHealthComponent = this.healthComponentComponentMapper.get(entity);

                if (enemyHealthComponent != null) {
                    long bombDamage = this.getBombDamage();
                    enemyHealthComponent.health -= bombDamage;
                    this.entityEngine.addEntity(this.entityFactory.createDamageLabel(enemyPositionComponent.position, bombDamage, FontType.NORMAL, this.entityEngine));
                }
            }
        }
    }

    private void dealDamageBlast(Entity damageSource, List<Entity> entitiesToCheck) {
        this.entityEngine.removeEntity(damageSource);

        Circle damageRange = new Circle(0, 0, 100f);

        for (Entity entity : entitiesToCheck) {
            PositionComponent enemyPositionComponent = this.positionComponentComponentMapper.get(entity);
            HealthComponent enemyHealthComponent = this.healthComponentComponentMapper.get(entity);

            if (enemyPositionComponent != null && enemyHealthComponent != null && damageRange.contains(enemyPositionComponent.position)){
                long blastDamage = this.getBlastDamage();
                enemyHealthComponent.health -= blastDamage;
                this.entityEngine.addEntity(this.entityFactory.createDamageLabel(enemyPositionComponent.position, blastDamage, FontType.NORMAL, this.entityEngine));
            }
        }
    }

    private float getBombRange() {
        return 60f;
    }

    private long getGunDamage() {
        return 20;
    }

    private long getLaserDamage() {
        return 20;
    }

    private long getBlastDamage() {
        return 20;
    }

    private long getBombDamage() {
        return 20;
    }

    private Vector2 createBulletStartPosition(Vector2 shootFacing) {
        return new Vector2(shootFacing.x * TOWER_LENGTH, shootFacing.y * TOWER_LENGTH);
    }

    @Override
    public void spawnEnemies(int currentSize) {
        if (currentSize <= 0) {
            for (int i = 0; i < 3; i++) {
                float rnd = this.randomizer.nextFloat();
                final Entity entity;
                if (rnd < 0.1f) {
                    entity = this.entityFactory.createGreenScum(new Vector2(0, 230).setAngle(this.randomizer.nextFloat() * 360f), new Vector2(0, 0), 5F, 100L);
                } else if (rnd < 0.4) {
                    entity = this.entityFactory.createBigFuck(new Vector2(0, 230).setAngle(this.randomizer.nextFloat() * 360f), new Vector2(0, 0), 5F, 100L);
                } else {
                    entity = this.entityFactory.createRedDick(new Vector2(0, 230).setAngle(this.randomizer.nextFloat() * 360f), new Vector2(0, 0), 5F, 100L);
                }
                this.entityEngine.addEntity(entity);
                this.entityEngine.addEntity(this.entityFactory.createTargetArrow(this.positionComponentComponentMapper.get(entity), this.healthComponentComponentMapper.get(entity)));
            }
        }
    }

    @Override
    public void enemyKilled() {

    }
}
