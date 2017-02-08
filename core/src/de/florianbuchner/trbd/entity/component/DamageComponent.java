package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.core.DamageHandler;

public class DamageComponent implements Component {

    public DamageHandler damageHandler;

    public Vector2 lastPosition;

    public Vector2 lastFacing;

    public DamageComponent(DamageHandler damageHandler) {
        this.damageHandler = damageHandler;
    }
}
