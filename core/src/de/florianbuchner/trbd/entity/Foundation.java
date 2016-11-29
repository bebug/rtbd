package de.florianbuchner.trbd.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Foundation extends GameEntity {

    public Foundation(Texture texture) {
        super(new Vector2(0, 0), new Vector2(0, 0), texture);
    }

    @Override
    public void update(float deltaTime) {
        // do nothing
    }

    @Override
    public void beforeDraw() {
        // do nothing
    }
}
