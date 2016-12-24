package de.florianbuchner.trbd.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

public class GameData {

    public int width;
    public int height;

    public SpriteBatch spriteBatch;

    public Map<WeaponType, WeaponEnergie> weaponEnergies = new HashMap<WeaponType, WeaponEnergie>(WeaponType.values().length);

    public int level;
}
