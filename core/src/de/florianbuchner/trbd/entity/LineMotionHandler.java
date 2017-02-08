package de.florianbuchner.trbd.entity;

import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class LineMotionHandler implements MotionComponent.MotionHandler {

    private float speed;

    public LineMotionHandler(float speed) {
        this.speed = speed;
    }

    @Override
    public void update(PositionComponent positionComponent, float deltaTime) {
        positionComponent.position.set(positionComponent.position.x + positionComponent.facing.x * deltaTime * this.speed,
                positionComponent.position.y + positionComponent.facing.y * deltaTime * this.speed);
    }
}
