package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.core.CheckDamageHandler;

public class DamageComponent implements Component {

    public CheckDamageHandler checkDamageHandler;

    public Vector2 lastPosition;

    public Vector2 lastFacing;

    public DamageComponent(CheckDamageHandler checkDamageHandler) {
        this.checkDamageHandler = checkDamageHandler;
    }
}
