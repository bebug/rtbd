package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    public float health;

    public HealthComponent(float health) {
        this.health = health;
    }
}
