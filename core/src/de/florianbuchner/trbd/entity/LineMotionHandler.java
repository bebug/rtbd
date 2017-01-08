package de.florianbuchner.trbd.entity;

import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class LineMotionHandler implements MotionComponent.MotionHandler {

    private float speed;

    private Vector2 facing;

    private Vector2 startPosition;

    private float accTime;

    public LineMotionHandler(float speed, Vector2 facing, Vector2 startPosition) {
        this.speed = speed;
        this.facing = facing;
        this.startPosition = startPosition;
        this.accTime = 0F;
    }

    @Override
    public void update(PositionComponent positionComponent, float deltaTime) {
        this.accTime += deltaTime;
        positionComponent.position.set(this.startPosition.x + this.facing.x * this.accTime * this.speed,
                this.startPosition.y + this.facing.y * this.accTime * this.speed);
    }
}
