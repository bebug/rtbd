package de.florianbuchner.trbd.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.EntityFactory;

import java.util.List;

public class GameEngine {

    private final static float TOWER_LENGTH = 35F;

    private GameData gameData;
    private Engine entityEngine;
    private EntityFactory entityFactory;

    public GameEngine(GameData gameData, Engine entityEngine, EntityFactory entityFactory) {
        this.gameData = gameData;
        this.entityEngine = entityEngine;
        this.entityFactory = entityFactory;
        this.restart();
    }

    public void restart() {
        for (WeaponType weaponType : WeaponType.values()) {
            this.gameData.weaponEnergies.put(weaponType, new WeaponEnergie());
        }
        this.gameData.level = 1;

    }

    public void update(float delta) {
        for (WeaponType weaponType : WeaponType.values()) {
            this.gameData.weaponEnergies.get(weaponType).add(delta * 0.1f);
        }
    }

    public void buttonPressed(WeaponType weaponType, Vector2 shootFacing) {
        if (this.gameData.weaponEnergies.get(weaponType).getEnergy() < 1F) {
            return;
        }

        switch (weaponType) {
            case Gun:
                this.entityEngine.addEntity(this.entityFactory.createGun(this.createBulletStartPosition(shootFacing), new Vector2(shootFacing), this.entityEngine));
                this.gameData.weaponEnergies.get(WeaponType.Gun).reset();
                break;
            case Laser:
                List<Entity> entities = this.entityFactory.createLaser(new Vector2(0, 0), new Vector2(shootFacing), this.entityEngine, TOWER_LENGTH);
                for (Entity entity : entities) {
                    this.entityEngine.addEntity(entity);
                }
                this.gameData.weaponEnergies.get(WeaponType.Laser).reset();
                break;
            case Bomb:
                this.entityEngine.addEntity(this.entityFactory.createBomb(this.createBulletStartPosition(shootFacing), new Vector2(shootFacing), this.entityEngine));
                this.gameData.weaponEnergies.get(WeaponType.Bomb).reset();
                break;
            case Blast:
                entities = this.entityFactory.createBlast(new Vector2(0, 0), this.entityEngine);
                for (Entity entity : entities) {
                    this.entityEngine.addEntity(entity);
                }
                this.gameData.weaponEnergies.get(WeaponType.Blast).reset();
                break;
        }
    }

    private Vector2 createBulletStartPosition(Vector2 shootFacing) {
        return new Vector2(shootFacing.x * TOWER_LENGTH, shootFacing.y * TOWER_LENGTH);
    }
}
