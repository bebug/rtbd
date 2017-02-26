package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;


public class HealthComponent implements Component {

    public long maxHealth;

    public long health;

    public float yOffset;

    public boolean death = false;

    public HealthComponent(long health, float yOffset) {
        this.health = health;
        this.maxHealth = health;
        this.yOffset = yOffset;
    }
}
