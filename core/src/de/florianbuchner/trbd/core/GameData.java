package de.florianbuchner.trbd.core;

import de.florianbuchner.trbd.entity.CircleMotionHandler;
import de.florianbuchner.trbd.entity.component.AnimationComponent;
import de.florianbuchner.trbd.entity.component.HealthComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

import java.util.HashMap;
import java.util.Map;

public class GameData {

    public int width;

    public int height;

    public Map<WeaponType, WeaponEnergie> weaponEnergies = new HashMap<WeaponType, WeaponEnergie>(WeaponType.values().length);

    public int level;

    public PositionComponent towerPosition;

    public AnimationComponent towerAnimation;

    public CircleMotionHandler towerMotionHandler;

    public HealthComponent healthComponent;

    public float rotationAngle = 0f;

    public boolean tendMotion;
}
