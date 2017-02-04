package de.florianbuchner.trbd.core;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.background.BackgroundComposer;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.component.AnimationComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;
import de.florianbuchner.trbd.entity.system.AnimationSystem;
import de.florianbuchner.trbd.entity.system.DelaySystem;
import de.florianbuchner.trbd.entity.system.DrawingSystem;
import de.florianbuchner.trbd.entity.system.MotionSystem;

import java.util.List;

public class GameEngine {

    private final static float TOWER_LENGTH = 35F;

    private GameData gameData;
    private Engine entityEngine;
    private EntityFactory entityFactory;
    private BackgroundComposer backgroundComposer;

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;
    private ComponentMapper<AnimationComponent> animationComponentComponentMapper;


    public GameEngine(GameData gameData, int length, int height) {
        this.gameData = gameData;
        this.entityFactory = new EntityFactory();
        this.entityEngine = new Engine();
        this.backgroundComposer = new BackgroundComposer(length, height);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.animationComponentComponentMapper = ComponentMapper.getFor(AnimationComponent.class);

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
        // Facing will be set by reference to gamedata
        this.gameData.towerFacing = this.positionComponentComponentMapper.get(towerEntity).facing;
        this.gameData.towerAnimation = this.animationComponentComponentMapper.get(towerEntity);
        this.entityEngine.addEntity(towerEntity);
        this.entityEngine.addEntity(this.entityFactory.createCrossHair(this.gameData.towerFacing));

        this.entityEngine.addEntity(this.entityFactory.createGreenScum(new Vector2(100,100), new Vector2(0, 0), 5F, 1F));
        this.entityEngine.addEntity(this.entityFactory.createBigFuck(new Vector2(-150, 100), new Vector2(0, 0), 5F, 1F));
    }

    private void createBaseSystems() {
        this.entityEngine.addSystem(new DelaySystem());
        this.entityEngine.addSystem(new AnimationSystem());
        this.entityEngine.addSystem(new DrawingSystem(this.gameData.spriteBatch));
        this.entityEngine.addSystem(new MotionSystem());
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
            this.gameData.weaponEnergies.get(weaponType).add(delta * 0.1f);
        }
    }

    public void buttonPressed(WeaponType weaponType) {
        if (this.gameData.weaponEnergies.get(weaponType).getEnergy() < 1F) {
            return;
        }

        Vector2 shootFacing = this.gameData.towerFacing.nor();

        switch (weaponType) {
            case GUN:
                this.entityEngine.addEntity(this.entityFactory.createGun(this.createBulletStartPosition(shootFacing), new Vector2(shootFacing), this.entityEngine));
                this.gameData.weaponEnergies.get(WeaponType.GUN).reset();
                this.gameData.towerAnimation.resetAnimation();
                break;
            case LASER:
                List<Entity> entities = this.entityFactory.createLaser(new Vector2(0, 0), new Vector2(shootFacing), this.entityEngine, TOWER_LENGTH);
                for (Entity entity : entities) {
                    this.entityEngine.addEntity(entity);
                }
                this.gameData.weaponEnergies.get(WeaponType.LASER).reset();
                break;
            case BOMB:
                this.entityEngine.addEntity(this.entityFactory.createBomb(this.createBulletStartPosition(shootFacing), new Vector2(shootFacing), this.entityEngine));
                this.gameData.weaponEnergies.get(WeaponType.BOMB).reset();
                this.gameData.towerAnimation.resetAnimation();
                break;
            case BLAST:
                entities = this.entityFactory.createBlast(new Vector2(0, 0), this.entityEngine);
                for (Entity entity : entities) {
                    this.entityEngine.addEntity(entity);
                }
                this.gameData.weaponEnergies.get(WeaponType.BLAST).reset();
                this.gameData.towerAnimation.resetAnimation();
                break;
        }
    }

    private Vector2 createBulletStartPosition(Vector2 shootFacing) {
        return new Vector2(shootFacing.x * TOWER_LENGTH, shootFacing.y * TOWER_LENGTH);
    }

    public void dispose() {
        this.entityFactory.dispose();
    }
}
