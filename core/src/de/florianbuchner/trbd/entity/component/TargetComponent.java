package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;

public class TargetComponent  implements Component {
    public PositionComponent positionComponent;
    public HealthComponent healthComponent;

    public TargetComponent(PositionComponent positionComponent, HealthComponent healthComponent) {
        this.positionComponent = positionComponent;
        this.healthComponent = healthComponent;
    }
}
