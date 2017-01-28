package de.florianbuchner.trbd.entity;

import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class SineMotionHandler implements MotionComponent.MotionHandler {

    private static float FREQUENCY = 0.02F;

    private static float AMPLITUDE = 70F;

    private float speed;

    private Vector2 facing;

    private Vector2 startPosition;

    private float accTime;

    public SineMotionHandler(float speed, Vector2 facing, Vector2 startPosition) {
        this.speed = speed;
        this.facing = facing;
        this.startPosition = startPosition;
        this.accTime = 0F;
    }

    @Override
    public void update(PositionComponent positionComponent, float deltaTime) {
        this.accTime += deltaTime;
        float distance = this.accTime * this.speed;
        Vector2 tempPosition = new Vector2(distance, AMPLITUDE * (float) Math.sin(distance * FREQUENCY)).rotate(facing.angle()).add(this.startPosition);
        positionComponent.position.set(tempPosition.x, tempPosition.y);
        Vector2 tempFacing = new Vector2(1, AMPLITUDE * FREQUENCY * (float)Math.cos(distance * FREQUENCY)).rotate(facing.angle()).nor();
        positionComponent.facing.set(tempFacing.x, tempFacing.y);
    }
}
