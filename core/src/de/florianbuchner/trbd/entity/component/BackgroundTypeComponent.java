package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;

public class BackgroundTypeComponent implements Component {

    public enum BackgroundType {
        GRASS,
        SOIL
    }

    public BackgroundType type;

    public BackgroundTypeComponent(BackgroundType type) {
        this.type = type;
    }
}
