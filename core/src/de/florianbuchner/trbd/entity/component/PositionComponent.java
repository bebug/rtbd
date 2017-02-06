package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 position;
    public Vector2 facing;
    public PositionLayer positionLayer;

    public enum PositionLayer{
        Background,
        Ground,
        Enemy,
        Explosion,
        Foreground,
        Text
    }

    public PositionComponent(Vector2 position, Vector2 facing, PositionLayer positionLayer) {
        this.position = position;
        this.facing = facing;
        this.positionLayer = positionLayer;
    }
}
