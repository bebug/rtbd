package de.florianbuchner.trbd.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.DrawingComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class EntityFactory {

    Texture foundationTexture;
    Texture towerTexture;

    public EntityFactory() {
        this.foundationTexture = new Texture(Gdx.files.internal("foundation.png"));
        this.towerTexture = new Texture(Gdx.files.internal("tower.png"));
    }

    public Entity createEntity(EntityType type) {
        switch(type) {
            case FOUNDATION:
                return this.createFoundation();
        }

        return null;
    }

    private Entity createFoundation() {
        Entity entity = new Entity();
        entity.add(new PositionComponent(new Vector2(0, 0), new Vector2(0, 0)));
        entity.add(new DrawingComponent(this.foundationTexture));
        return entity;
    }

    public void dispose() {
        this.foundationTexture.dispose();
        this.towerTexture.dispose();
    }
}
