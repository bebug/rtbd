package de.florianbuchner.trbd.core;

import com.badlogic.ashley.core.Entity;

import java.util.List;

public interface CheckDamageHandler {
    void dealDamage(Entity damageSource, List<Entity> entitiesToCheck);
}
