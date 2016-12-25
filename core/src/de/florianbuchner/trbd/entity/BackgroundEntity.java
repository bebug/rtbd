package de.florianbuchner.trbd.entity;

import com.badlogic.ashley.core.Entity;

public class BackgroundEntity extends Entity {

    public enum BackgroundType {
        GRASS,
        SOIL
    }

    public BackgroundType type;

    public BackgroundEntity(BackgroundType type) {
        this.type = type;
    }

    public BackgroundType getType() {
        return type;
    }

    public void setType(BackgroundType type) {
        this.type = type;
    }
}
