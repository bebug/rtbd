package de.florianbuchner.trbd.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.AnimationComponent;
import de.florianbuchner.trbd.entity.component.DrawingComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;
import de.florianbuchner.trbd.entity.component.TowerComponent;

import javax.xml.soap.Text;

public class EntityFactory {

    private Texture foundationTexture;
    private Texture towerTexture;
    private Texture explosionTexture;

    public EntityFactory() {
        this.foundationTexture = new Texture(Gdx.files.internal("foundation.png"));
        this.towerTexture = new Texture(Gdx.files.internal("tower.png"));
        this.explosionTexture = new Texture(Gdx.files.internal("explosion.png"));
    }

    public Entity createExplosion(Vector2 position) {
        Entity entity = new Entity();
        entity.add(new PositionComponent(position, new Vector2(1, 0), PositionComponent.PositionLayer.Explosion));

        TextureRegion[][] textureSplits = TextureRegion.split(this.explosionTexture, this.explosionTexture.getWidth() / 6, this.explosionTexture.getHeight());
        entity.add(new AnimationComponent(new Animation(0.1f, textureSplits[0]), true));

        return entity;
    }

    public Entity createTower() {
        Entity entity = new Entity();
        entity.add(new PositionComponent(new Vector2(0, 0), new Vector2(0, 1), PositionComponent.PositionLayer.Background));
        entity.add(new DrawingComponent(new TextureRegion(this.towerTexture), new Vector2(-this.towerTexture.getWidth() / 2 + 7, -(this.towerTexture.getHeight() / 2))));
        entity.add(new TowerComponent());
        return entity;
    }

    public Entity createFoundation() {
        Entity entity = new Entity();
        entity.add(new PositionComponent(new Vector2(0, 0), new Vector2(0, 1), PositionComponent.PositionLayer.Background));
        entity.add(new DrawingComponent(this.foundationTexture));
        return entity;
    }

    public void dispose() {
        this.foundationTexture.dispose();
        this.towerTexture.dispose();
    }
}
