package de.florianbuchner.trbd.core;

import com.badlogic.ashley.core.Entity;

import java.util.List;

public interface DamageHandler {
    void dealDamage(Entity damageSource, List<Entity> entitiesToCheck);
}
