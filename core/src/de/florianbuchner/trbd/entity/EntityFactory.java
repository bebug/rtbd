package de.florianbuchner.trbd.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class EntityFactory {

    Texture foundationTexture;
    Texture towerTexture;

    public EntityFactory() {
        this.foundationTexture = new Texture(Gdx.files.internal("foundation.png"));
        this.towerTexture = new Texture(Gdx.files.internal("tower.png"));
    }

    public GameEntity createEntity(EntityType type) {
        switch(type) {
            case FOUNDATION:
                return this.createFoundation();
        }

        return null;
    }

    private GameEntity createFoundation() {
        return new Foundation(this.foundationTexture);
    }

    public void dispose() {
        this.foundationTexture.dispose();
        this.towerTexture.dispose();
    }
}
