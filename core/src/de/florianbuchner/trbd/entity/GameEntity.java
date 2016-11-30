package de.florianbuchner.trbd.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class GameEntity extends Sprite implements Component {

    private Vector2 position;
    private Vector2 facing;

    protected GameEntity(Vector2 position, Vector2 facing, TextureRegion textureRegion) {
        super(textureRegion);
        this.position = position;
        this.facing = facing;
    }

    protected GameEntity(Vector2 position, Vector2 facing, Texture texture) {
        super(texture);
        this.position = position;
        this.facing = facing;
    }

    public abstract void update(float deltaTime);

    public abstract void beforeDraw();

    @Override
    public void draw(Batch batch) {
        this.beforeDraw();
        super.draw(batch);
    }
}
