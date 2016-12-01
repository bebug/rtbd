package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import de.florianbuchner.trbd.entity.component.DrawingComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

public class DrawingSystem extends IteratingSystem {

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Array<Entity> drawEntities;

    private ComponentMapper<DrawingComponent> drawingComponentComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    public DrawingSystem(SpriteBatch spriteBatch) {
        super(Family.all(DrawingComponent.class).get());

        this.drawingComponentComponentMapper = ComponentMapper.getFor(DrawingComponent.class);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);

        this.drawEntities = new Array<Entity>();
        this.spriteBatch = spriteBatch;
        this.camera = new OrthographicCamera(500, 300);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.drawEntities.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.spriteBatch.begin();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);

        for (Entity entity : this.drawEntities) {
            DrawingComponent drawingComponent = this.drawingComponentComponentMapper.get(entity);
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

        this.drawEntities.clear();
        this.spriteBatch.end();
    }
}
