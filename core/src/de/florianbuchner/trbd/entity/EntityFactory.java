package de.florianbuchner.trbd.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.background.BackgroundComposer;
import de.florianbuchner.trbd.entity.component.AnimationComponent;
import de.florianbuchner.trbd.entity.component.DelayComponent;
import de.florianbuchner.trbd.entity.component.DrawingComponent;
import de.florianbuchner.trbd.entity.component.MotionComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;
import de.florianbuchner.trbd.entity.component.TowerComponent;

import java.util.ArrayList;
import java.util.List;

public class EntityFactory {

    private Texture foundationTexture;
    private Texture towerTexture;
    private Texture explosionTexture;
    private TextureRegion gunTextureRegion;
    private TextureRegion bombTextureRegion;
    private TextureRegion[] laserTextureRegions;

    public EntityFactory() {
        this.foundationTexture = new Texture(Gdx.files.internal("foundation.png"));
        this.towerTexture = new Texture(Gdx.files.internal("tower.png"));
        this.explosionTexture = new Texture(Gdx.files.internal("explosion.png"));
        Texture bulletsTexture = new Texture(Gdx.files.internal("bullets.png"));

        this.bombTextureRegion = new TextureRegion(bulletsTexture, 0, 3 , 13, 8);
        this.gunTextureRegion = new TextureRegion(bulletsTexture, 14, 3, 9, 9);
        this.laserTextureRegions = new TextureRegion[3];
        this.laserTextureRegions[0] = new TextureRegion(bulletsTexture, 24, 0, 22, 15);
        this.laserTextureRegions[1] = new TextureRegion(bulletsTexture, 0, 15, 22, 15);
        this.laserTextureRegions[2] = new TextureRegion(bulletsTexture, 24, 15, 22, 15);
    }

    public Entity createExplosion(Vector2 position, final Engine engine) {
        final Entity entity = new Entity();
        entity.add(new PositionComponent(position, new Vector2(1, 0), PositionComponent.PositionLayer.Explosion));

        TextureRegion[][] textureSplits = TextureRegion.split(this.explosionTexture, this.explosionTexture.getWidth() / 6, this.explosionTexture.getHeight());
        entity.add(new AnimationComponent(new Animation(0.1f, textureSplits[0]), false));
        entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(entity);
            }
        }, 0.6f));

        return entity;
    }

    public List<Entity> createExplosions(final Vector2 position, final float range, int amount, float time, final Engine engine, final BackgroundComposer test) {
        List<Entity> entities = new ArrayList<Entity>(amount);
        for (int i = 0; i < amount; i++) {
            final Vector2 rndVector = this.getRandomVector(range);
            final Entity explosion = this.createExplosion(new Vector2(position.x + rndVector.x - range / 2f,
                    position.y + rndVector.y - range / 2f), engine);
            final Entity insertEntity = new Entity();
            insertEntity.add(new DelayComponent(new DelayComponent.DelayHandler() {
                @Override
                public void onDelay() {
                    test.setTile(position.x + rndVector.x - range / 2f,
                            position.y + rndVector.y - range / 2f, BackgroundEntity.BackgroundType.SOIL);
                    engine.removeEntity(insertEntity);
                    engine.addEntity(explosion);
                }
            }, (float)Math.random() * time));
            entities.add(insertEntity);
        }
        return entities;
    }

    private Vector2 getRandomVector(float range) {
        return new Vector2(range * (float)Math.random(), range * (float)Math.random());
    }

    public Entity createTower() {
        Entity entity = new Entity();
        entity.add(new PositionComponent(new Vector2(0, 0), new Vector2(0, 1), PositionComponent.PositionLayer.Enemy));
        entity.add(new DrawingComponent(new TextureRegion(this.towerTexture), new Vector2(-this.towerTexture.getWidth() / 2 + 7, -(this.towerTexture.getHeight() / 2))));
        entity.add(new TowerComponent());
        return entity;
    }

    public Entity createFoundation() {
        Entity entity = new Entity();
        entity.add(new PositionComponent(new Vector2(0, 0), new Vector2(0, 1), PositionComponent.PositionLayer.Ground));
        entity.add(new DrawingComponent(this.foundationTexture));
        return entity;
    }

    public void dispose() {
        this.foundationTexture.dispose();
        this.towerTexture.dispose();
    }

    public Entity createGun(Vector2 startPosition, Vector2 facing, final Engine engine) {
        final Entity entity = new Entity();
        entity.add(new PositionComponent(startPosition, facing, PositionComponent.PositionLayer.Explosion));
        entity.add(new DrawingComponent(this.gunTextureRegion));
        entity.add(new MotionComponent(new LineMotionHandler(170F, facing, new Vector2(startPosition))));
        // Make sure entity is removed after screen is exited
        entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(entity);
            }
        },3F));
        return entity;
    }

    public Entity createBomb(Vector2 startPosition, Vector2 facing, final Engine engine) {
        final Entity entity = new Entity();
        entity.add(new PositionComponent(startPosition, facing, PositionComponent.PositionLayer.Explosion));
        entity.add(new DrawingComponent(this.bombTextureRegion));
        entity.add(new MotionComponent(new LineMotionHandler(100F, facing, new Vector2(startPosition))));
        // Make sure entity is removed after screen is exited
        entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(entity);
            }
        }, 4F));
        return entity;
    }
}
