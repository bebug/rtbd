package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;

public class DelayComponent implements Component {

    public interface DelayHandler{
        void onDelay();
    }

    public DelayHandler delayHandler;

    public float delayCountdown;

    public DelayComponent(DelayHandler delayHandler, float delayCountdown) {
        this.delayHandler = delayHandler;
        this.delayCountdown = delayCountdown;
    }
}
