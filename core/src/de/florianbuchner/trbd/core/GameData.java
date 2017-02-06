package de.florianbuchner.trbd.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.AnimationComponent;

import java.util.HashMap;
import java.util.Map;

public class GameData {

    public int width;

    public int height;

    public Map<WeaponType, WeaponEnergie> weaponEnergies = new HashMap<WeaponType, WeaponEnergie>(WeaponType.values().length);

    public int level;

    public Vector2 towerFacing;

    public AnimationComponent towerAnimation;
}
