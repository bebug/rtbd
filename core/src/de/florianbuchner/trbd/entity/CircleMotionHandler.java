package de.florianbuchner.trbd.entity;

import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class CircleMotionHandler implements MotionComponent.MotionHandler {

    private float speed;

    private Vector2 centerPosition;

    private float distance;

    public CircleMotionHandler(Vector2 centerPosition, float distance, float speed) {
        this.centerPosition = centerPosition;
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    public void update(PositionComponent positionComponent, float deltaTime) {
        positionComponent.facing.setAngle(positionComponent.facing.angle() + deltaTime * this.speed);
        positionComponent.position.set(this.centerPosition.x + positionComponent.facing.x * distance,
                this.centerPosition.y + positionComponent.facing.y * distance);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
