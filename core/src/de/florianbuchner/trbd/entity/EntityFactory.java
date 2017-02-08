package de.florianbuchner.trbd.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.core.DamageHandler;
import de.florianbuchner.trbd.core.EnemyType;
import de.florianbuchner.trbd.core.FontType;
import de.florianbuchner.trbd.core.Resources;
import de.florianbuchner.trbd.entity.component.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityFactory {

    private float ROTATIONSPEED = -120F;

    private TextureRegion foundationTexture;
    private Animation towerAnimation;
    private TextureRegion crosshairTexture;
    private TextureRegion gunTextureRegion;
    private TextureRegion bombTextureRegion;
    private TextureRegion[] explosionRegions;
    private Animation laserAnimation;
    private Map<EnemyType, Animation> enemyAnimations = new HashMap<EnemyType, Animation>(EnemyType.values().length);

    public EntityFactory(Resources resources) {
        this.foundationTexture = resources.textureAtlas.createSprite("foundation");

        TextureRegion explosionTexture = resources.textureAtlas.createSprite("explosion");
        this.crosshairTexture = resources.textureAtlas.createSprite("crosshair");
        TextureRegion bulletsTexture = resources.textureAtlas.createSprite("bullets");
        TextureRegion enemiesTexture = resources.textureAtlas.createSprite("enemies");
        TextureRegion towerTexture = resources.textureAtlas.createSprite("tower");

        this.bombTextureRegion = new TextureRegion(bulletsTexture, 0, 3, 13, 8);
        this.gunTextureRegion = new TextureRegion(bulletsTexture, 14, 3, 9, 9);
        this.laserAnimation = new Animation(0.1F,
                new TextureRegion(bulletsTexture, 24, 0, 22, 15),
                new TextureRegion(bulletsTexture, 0, 15, 22, 15),
                new TextureRegion(bulletsTexture, 24, 15, 22, 15));
        this.laserAnimation.setPlayMode(Animation.PlayMode.LOOP);
        enemyAnimations.put(EnemyType.BIG_FUCK, new Animation(0.1F,
                new TextureRegion(enemiesTexture, 0, 0, 52, 23),
                new TextureRegion(enemiesTexture, 52, 0, 52, 23)));
        enemyAnimations.put(EnemyType.GREEN_SCUM, new Animation(0.1F,
                new TextureRegion(enemiesTexture, 0, 32, 52, 29),
                new TextureRegion(enemiesTexture, 52, 32, 52, 29)));
        enemyAnimations.put(EnemyType.RED_DICK, new Animation(0.1F,
                new TextureRegion(enemiesTexture, 0, 64, 52, 23),
                new TextureRegion(enemiesTexture, 52, 64, 52, 23)));

        this.explosionRegions = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            this.explosionRegions[i] = new TextureRegion(explosionTexture, (explosionTexture.getRegionWidth() / 6) * i, 0, explosionTexture.getRegionWidth() / 6, explosionTexture.getRegionHeight());
        }

        this.towerAnimation = new Animation(0.1F,
                new TextureRegion(towerTexture, towerTexture.getRegionWidth() / 2, 0, towerTexture.getRegionWidth() / 2, towerTexture.getRegionHeight()),
                new TextureRegion(towerTexture, 0, 0, towerTexture.getRegionWidth() / 2, towerTexture.getRegionHeight()));
    }

    public Entity createExplosion(Vector2 position, final Engine engine) {
        final Entity entity = new Entity();
        entity.add(new PositionComponent(position, new Vector2(1, 0), PositionComponent.PositionLayer.Explosion));

        entity.add(new AnimationComponent(new Animation(0.1f, explosionRegions), false));
        entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(entity);
            }
        }, 0.6f));

        return entity;
    }

    public Entity createExplosion(Vector2 position, final Engine engine, float time) {
        final Entity explosion = this.createExplosion(position, engine);
        final Entity insertEntity = new Entity();
        insertEntity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(insertEntity);
                engine.addEntity(explosion);
            }
        }, time));
        return insertEntity;
    }

    public List<Entity> createExplosions(final Vector2 position, final float range, int amount, float time, final Engine engine) {
        List<Entity> entities = new ArrayList<Entity>(amount);
        for (int i = 0; i < amount; i++) {
            final Vector2 rndVector = this.getRandomVector(range);
            entities.add(this.createExplosion(new Vector2(position.x + rndVector.x - range / 2f,
                    position.y + rndVector.y - range / 2f), engine, (float) Math.random() * time));
        }
        return entities;
    }

    private Vector2 getRandomVector(float range) {
        return new Vector2(range * (float) Math.random(), range * (float) Math.random());
    }

    public Entity createTower() {
        Entity entity = new Entity();
        entity.add(new PositionComponent(new Vector2(0, 0), new Vector2(0, 1), PositionComponent.PositionLayer.Enemy));
        //entity.add(new DrawingComponent(this.towerTexture, new Vector2(-this.towerTexture.getRegionWidth() / 2 + 7, -(this.towerTexture.getRegionHeight() / 2))));
        AnimationComponent animationComponent = new AnimationComponent(this.towerAnimation, false, 1F);
        animationComponent.textureOffset.set(animationComponent.textureOffset.x + 6, animationComponent.textureOffset.y);
        entity.add(animationComponent);
        entity.add(new TowerComponent());
        entity.add(new MotionComponent(new CircleMotionHandler(new Vector2(0, 0), 0F, ROTATIONSPEED)));
        entity.add(new HealthComponent(200L, 30));
        return entity;
    }

    /**
     * @param facing will be set by reference
     */
    public Entity createCrossHair(Vector2 facing) {
        Entity entity = new Entity();
        entity.add(new DrawingComponent(this.crosshairTexture, new Vector2(80, -this.crosshairTexture.getRegionHeight() / 2F)));
        entity.add(new PositionComponent(new Vector2(0, 0), facing, PositionComponent.PositionLayer.Foreground));
        return entity;
    }

    public Entity createFoundation() {
        Entity entity = new Entity();
        entity.add(new PositionComponent(new Vector2(0, 0), new Vector2(0, 1), PositionComponent.PositionLayer.Ground));
        entity.add(new DrawingComponent(this.foundationTexture));
        return entity;
    }

    public Entity createGun(Vector2 startPosition, Vector2 facing, final Engine engine, final DamageHandler damageHandler) {
        final Entity entity = new Entity();
        entity.add(new PositionComponent(startPosition, facing.nor(), PositionComponent.PositionLayer.Explosion));
        entity.add(new DrawingComponent(this.gunTextureRegion));
        entity.add(new MotionComponent(new LineMotionHandler(170F)));
        entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(entity);
            }
        }, 3F));
        entity.add(new DamageComponent(damageHandler));
        return entity;
    }

    public List<Entity> createLaser(Vector2 positionReference, Vector2 facingReference, final Engine engine, float distance, final DamageHandler damageHandler) {
        final List<Entity> entities = new ArrayList<Entity>();

        for (int i = 0; i < 10; i++) {
            final Entity entity = new Entity();
            float centerDistance = distance + 8 + i * 22;
            entity.add(new PositionComponent(new Vector2(positionReference.x + centerDistance * facingReference.x,
                    positionReference.y + centerDistance * facingReference.y), new Vector2(facingReference), PositionComponent.PositionLayer.Explosion));
            entity.add(new MotionComponent(new ReferenceMotionHandler(positionReference, facingReference, centerDistance)));
            entity.add(new AnimationComponent(this.laserAnimation, true, i % this.laserAnimation.getKeyFrames().length * this.laserAnimation.getFrameDuration()));
            entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
                @Override
                public void onDelay() {
                    engine.removeEntity(entity);
                }
            }, 3F));
            entities.add(entity);
        }
        entities.get(0).add(new DamageComponent(damageHandler));

        return entities;
    }

    public Entity createBomb(Vector2 startPosition, Vector2 facing, final Engine engine, final DamageHandler damageHandler) {
        final Entity entity = new Entity();
        entity.add(new PositionComponent(startPosition, facing.nor(), PositionComponent.PositionLayer.Explosion));
        entity.add(new DrawingComponent(this.bombTextureRegion));
        entity.add(new MotionComponent(new LineMotionHandler(100F)));
        entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(entity);
            }
        }, 4F));
        entity.add(new DamageComponent(damageHandler));
        return entity;
    }

    public List<Entity> createBlast(Vector2 centerPosition, final Engine engine, final DamageHandler damageHandler) {
        final List<Entity> entities = new ArrayList<Entity>();

        entities.addAll(this.createBlastRing(centerPosition, engine, 0F, 40F, 0F));
        entities.addAll(this.createBlastRing(centerPosition, engine, 0.3F, 60F, 10F));
        entities.addAll(this.createBlastRing(centerPosition, engine, 0.6F, 80F, 0F));

        entities.get(0).add(new DamageComponent(damageHandler));

        return entities;
    }

    private List<Entity> createBlastRing(Vector2 centerPosition, final Engine engine, float startTime, float distance, float startDegree) {
        final List<Entity> entities = new ArrayList<Entity>();

        float degree = startDegree;
        Vector2 degreeVector = new Vector2(1, 0);

        while (degree < 360F) {
            degreeVector.setAngle(degree);
            entities.add(this.createExplosion(new Vector2(centerPosition.x + degreeVector.x * distance,
                    centerPosition.y + degreeVector.y * distance), engine, startTime));
            degree += 20F;
        }

        return entities;
    }

    public Entity createBigFuck(Vector2 startPosition, Vector2 endPosition, float speed, long livepoints) {
        return this.createLineMotionEnemy(startPosition, endPosition, speed, livepoints, EnemyType.BIG_FUCK);

    }

    public Entity createRedDick(Vector2 startPosition, Vector2 endPosition, float speed, long livepoints) {
        return this.createLineMotionEnemy(startPosition, endPosition, speed, livepoints, EnemyType.RED_DICK);
    }

    private Entity createLineMotionEnemy(Vector2 startPosition, Vector2 endPosition, float speed, long health, EnemyType enemyType) {
        final Entity entity = new Entity();
        final AnimationComponent animationComponent = new AnimationComponent(this.enemyAnimations.get(enemyType), true);
        animationComponent.textureOffset = new Vector2(-35, -12);
        entity.add(animationComponent);
        entity.add(new MotionComponent(new LineMotionHandler(speed)));
        entity.add(new PositionComponent(new Vector2(startPosition), endPosition.sub(startPosition).nor(), PositionComponent.PositionLayer.Enemy));
        entity.add(new HealthComponent(health, 20));
        entity.add(new EnemyComponent());

        return entity;
    }

    public Entity createGreenScum(Vector2 startPosition, Vector2 endPosition, float speed, long health) {
        final Entity entity = new Entity();
        final AnimationComponent animationComponent = new AnimationComponent(this.enemyAnimations.get(EnemyType.GREEN_SCUM), true);
        animationComponent.textureOffset = new Vector2(-35, -15);
        entity.add(animationComponent);
        entity.add(new MotionComponent(new SineMotionHandler(speed, endPosition.sub(startPosition).nor(), new Vector2(startPosition))));
        entity.add(new PositionComponent(new Vector2(startPosition), new Vector2(0, 0), PositionComponent.PositionLayer.Enemy));
        entity.add(new HealthComponent(health, 20));
        entity.add(new EnemyComponent());

        return entity;
    }

    public Entity createDamageLabel(Vector2 position, long damage, FontType fontType, final Engine engine) {
        final Entity entity = new Entity();
        entity.add(new TextComponent(String.valueOf(damage), fontType));
        entity.add(new PositionComponent(new Vector2(position), new Vector2(0, 1), null));
        entity.add(new DelayComponent(new DelayComponent.DelayHandler() {
            @Override
            public void onDelay() {
                engine.removeEntity(entity);
            }
        }, 1));
        entity.add(new MotionComponent(new LineMotionHandler(30)));
        return entity;
    }
}
