package de.florianbuchner.trbd.core;

public class GameEngine {

    private final GameData gameData;

    public GameEngine(final GameData gameData) {
        this.gameData = gameData;
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
}
