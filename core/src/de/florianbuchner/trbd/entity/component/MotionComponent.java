package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;

public class MotionComponent implements Component {

    public interface MotionHandler{
        void update(PositionComponent positionComponent, float deltaTime);
    }

    public MotionHandler handler;

    public MotionComponent(MotionHandler handler) {
        this.handler = handler;
    }
}
