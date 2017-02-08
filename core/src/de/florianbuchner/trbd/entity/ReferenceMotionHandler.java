package de.florianbuchner.trbd.entity;

import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class ReferenceMotionHandler implements MotionComponent.MotionHandler {

    private Vector2 position;

    private Vector2 facing;

    private float distance;

    public ReferenceMotionHandler(Vector2 position, Vector2 facing, float distance) {
        this.position = position;
        this.facing = facing;
        this.distance = distance;
    }

    @Override
    public void update(PositionComponent positionComponent, float deltaTime) {
        positionComponent.facing.set(this.facing.x, this.facing.y);
        positionComponent.position.set(this.position.x + this.distance * this.facing.x,
                this.position.y + this.distance * this.facing.y);
    }
}
