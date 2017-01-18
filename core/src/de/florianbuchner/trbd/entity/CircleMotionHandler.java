package de.florianbuchner.trbd.entity;

import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class CircleMotionHandler implements MotionComponent.MotionHandler {

    private float speed;

    private float startAngle;

    private Vector2 centerPosition;

    private float distance;

    private float accTime;

    public CircleMotionHandler(Vector2 startFacing, Vector2 centerPosition, float distance, float speed) {
        this.startAngle = startFacing.angle();
        this.centerPosition = centerPosition;
        this.distance = distance;
        this.speed = speed;
        this.accTime = 0F;
    }

    @Override
    public void update(PositionComponent positionComponent, float deltaTime) {
        this.accTime += this.speed * deltaTime;
        positionComponent.facing.setAngle(startAngle + this.accTime);
        positionComponent.position.set(this.centerPosition.x + positionComponent.facing.x * distance,
                this.centerPosition.y + positionComponent.facing.y * distance);
    }
}
