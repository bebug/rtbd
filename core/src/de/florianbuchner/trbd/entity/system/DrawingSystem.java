package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import de.florianbuchner.trbd.entity.component.AnimationComponent;
import de.florianbuchner.trbd.entity.component.DrawingComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawingSystem extends IteratingSystem {

    private SpriteBatch spriteBatch;
    private Map<PositionComponent.PositionLayer, List<Entity>> drawEntities;

    private ComponentMapper<DrawingComponent> drawingComponentComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentComponentMapper;
    private ComponentMapper<AnimationComponent> animationComponentComponentMapper;

    public DrawingSystem(SpriteBatch spriteBatch) {
        super(Family.all(PositionComponent.class).one(AnimationComponent.class, DrawingComponent.class).get());

        this.drawingComponentComponentMapper = ComponentMapper.getFor(DrawingComponent.class);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.animationComponentComponentMapper = ComponentMapper.getFor(AnimationComponent.class);

        this.drawEntities = new HashMap<PositionComponent.PositionLayer, List<Entity>>(PositionComponent.PositionLayer.values().length);
        for (PositionComponent.PositionLayer positionLayer : PositionComponent.PositionLayer.values()) {
            this.drawEntities.put(positionLayer, new ArrayList<Entity>());
        }
        this.spriteBatch = spriteBatch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
        if (positionComponent != null) {
            this.drawEntities.get(positionComponent.positionLayer).add(entity);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.spriteBatch.begin();

        // Draw entities in order
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Background));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Ground));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Enemy));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Explosion));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Foreground));

        for (PositionComponent.PositionLayer positionLayer : PositionComponent.PositionLayer.values()) {
            this.drawEntities.get(positionLayer).clear();
        }

        this.spriteBatch.end();
    }

    private void drawEntities(final List<Entity> entityList) {
        for (Entity entity : entityList) {
            DrawingComponent drawingComponent = this.drawingComponentComponentMapper.get(entity);
            if (drawingComponent == null) {
                drawingComponent = this.animationComponentComponentMapper.get(entity);
            }
            PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
            this.spriteBatch.draw(
                    drawingComponent.textureRegion,
                    positionComponent.position.x + drawingComponent.textureOffset.x,
                    positionComponent.position.y + drawingComponent.textureOffset.y,
                    -drawingComponent.textureOffset.x,
                    -drawingComponent.textureOffset.y,
                    drawingComponent.textureRegion.getRegionWidth(),
                    drawingComponent.textureRegion.getRegionHeight(),
                    1,
                    1,
                    positionComponent.facing.angle());
        }
    }
}
