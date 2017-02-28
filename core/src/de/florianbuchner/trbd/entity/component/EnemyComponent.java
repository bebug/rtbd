package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
    public long damage;

    public EnemyComponent(long damage) {
        this.damage = damage;
    }
}
