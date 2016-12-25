package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    public double health;

    public HealthComponent(double health) {
        this.health = health;
    }
}
