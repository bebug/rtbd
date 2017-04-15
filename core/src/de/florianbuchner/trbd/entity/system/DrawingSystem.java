package de.florianbuchner.trbd.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;
import de.florianbuchner.trbd.entity.component.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DrawingSystem extends IteratingSystem {

    private final boolean showPolygons = true;

    private class HealthDrawingContainer {
        public PositionComponent positionComponent;
        public HealthComponent healthComponent;
    }

    private Resources resources;
    private GameData gameData;
    private Map<PositionComponent.PositionLayer, List<Entity>> drawEntities;
    private List<HealthDrawingContainer> healthEntities;
    private List<Entity> textEntities;

    private ComponentMapper<DrawingComponent> drawingComponentComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentComponentMapper;
    private ComponentMapper<AnimationComponent> animationComponentComponentMapper;
    private ComponentMapper<HealthComponent> healthComponentComponentMapper;
    private ComponentMapper<TextComponent> textComponentComponentMapper;

    private TextureRegion healthbarEmpty;
    private TextureRegion healthbar;

    public DrawingSystem(Resources resources, GameData gameData) {
        super(Family.all(PositionComponent.class).one(AnimationComponent.class, DrawingComponent.class, TextComponent.class).get());

        this.gameData = gameData;
        this.resources = resources;

        TextureRegion healthTextureRegion = resources.textureAtlas.createSprite("healthbar");
        this.healthbar = new TextureRegion(healthTextureRegion, 0, 0, healthTextureRegion.getRegionWidth() / 2, 4);
        this.healthbarEmpty = new TextureRegion(healthTextureRegion, healthTextureRegion.getRegionWidth() / 2, 0, healthTextureRegion.getRegionWidth() / 2, 4);

        this.drawingComponentComponentMapper = ComponentMapper.getFor(DrawingComponent.class);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.animationComponentComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
        this.healthComponentComponentMapper = ComponentMapper.getFor(HealthComponent.class);
        this.textComponentComponentMapper = ComponentMapper.getFor(TextComponent.class);

        this.drawEntities = new HashMap<PositionComponent.PositionLayer, List<Entity>>(PositionComponent.PositionLayer.values().length);
        for (PositionComponent.PositionLayer positionLayer : PositionComponent.PositionLayer.values()) {
            this.drawEntities.put(positionLayer, new LinkedList<Entity>());
        }

        this.textEntities = new LinkedList<Entity>();
        this.healthEntities = new LinkedList<HealthDrawingContainer>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
        if (positionComponent != null) {
            TextComponent textComponent = this.textComponentComponentMapper.get(entity);
            if (textComponent != null) {
                this.textEntities.add(entity);
            }
            else {
                this.drawEntities.get(positionComponent.positionLayer).add(entity);
                HealthComponent healthComponent = this.healthComponentComponentMapper.get(entity);
                if (healthComponent != null) {
                    HealthDrawingContainer healthDrawingContainer = new HealthDrawingContainer();
                    healthDrawingContainer.healthComponent = healthComponent;
                    healthDrawingContainer.positionComponent = positionComponent;
                    this.healthEntities.add(healthDrawingContainer);
                }
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        Matrix4 rotationMatrix = new Matrix4().rotate(0, 0, 1, this.gameData.rotationAngle);
        Matrix4 rotated = new Matrix4(this.resources.camera.combined).mul(rotationMatrix);
        this.resources.spriteBatch.setProjectionMatrix(rotated);

        this.healthEntities.clear();
        this.textEntities.clear();
        for (PositionComponent.PositionLayer positionLayer : PositionComponent.PositionLayer.values()) {
            this.drawEntities.get(positionLayer).clear();
        }

        super.update(deltaTime);

        this.resources.spriteBatch.begin();

        // Draw entities in order
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Background));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Ground));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Enemy));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Explosion));
        this.drawEntities(this.drawEntities.get(PositionComponent.PositionLayer.Foreground));

        if (this.gameData.tendMotion) {
            this.drawHealthEntities(this.healthEntities, null);
            this.drawText(this.textEntities, null);
            // reset projection
            this.resources.spriteBatch.setProjectionMatrix(this.resources.camera.combined);
        }
        else {
            // reset projection
            this.resources.spriteBatch.setProjectionMatrix(this.resources.camera.combined);
            this.drawHealthEntities(this.healthEntities, rotationMatrix);
            this.drawText(this.textEntities, rotationMatrix);
        }


        this.resources.spriteBatch.end();

        if (this.showPolygons) {
            this.resources.shapeRenderer.setProjectionMatrix(rotated);
            this.resources.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            this.resources.shapeRenderer.setColor(new Color(Color.WHITE));
            this.drawShapes(this.drawEntities.get(PositionComponent.PositionLayer.Enemy));
            this.resources.shapeRenderer.setColor(new Color(Color.RED));
            this.drawShapes(this.drawEntities.get(PositionComponent.PositionLayer.Explosion));

            this.resources.shapeRenderer.end();
            this.resources.shapeRenderer.setProjectionMatrix(this.resources.camera.combined);
        }
    }

    private void drawShapes(List<Entity> entityList) {
        for (Entity entity : entityList) {
            PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
            if (positionComponent != null && positionComponent.body != null) {
                this.resources.shapeRenderer.polygon(positionComponent.body.getTransformedVertices());
            }
        }
    }

    private void drawText(List<Entity> entityList, Matrix4 rotationMatrix) {
        for (Entity entity : entityList) {
            TextComponent textComponent = this.textComponentComponentMapper.get(entity);
            PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
            if (positionComponent.startPosition != null) {
                Vector2 rotated = (rotationMatrix != null ? this.rotate(positionComponent.startPosition, rotationMatrix) : positionComponent.startPosition).sub(new Vector2(positionComponent.startPosition).sub(positionComponent.position));
                this.resources.fonts.get(textComponent.fontType).draw(this.resources.spriteBatch, textComponent.text, rotated.x, rotated.y);
            }
            else {
                Vector2 rotated = (rotationMatrix != null ? this.rotate(positionComponent.position, rotationMatrix) : positionComponent.position);
                this.resources.fonts.get(textComponent.fontType).draw(this.resources.spriteBatch, textComponent.text, rotated.x, rotated.y);
            }
        }
    }

    private void drawEntities(List<Entity> entityList) {
        for (Entity entity : entityList) {
            DrawingComponent drawingComponent = this.drawingComponentComponentMapper.get(entity);
            if (drawingComponent == null) {
                drawingComponent = this.animationComponentComponentMapper.get(entity);
            }
            PositionComponent positionComponent = this.positionComponentComponentMapper.get(entity);
            this.resources.spriteBatch.draw(
                    drawingComponent.textureRegion,
                    positionComponent.position.x + drawingComponent.textureOffset.x,
                    positionComponent.position.y + drawingComponent.textureOffset.y,
                    -drawingComponent.textureOffset.x,
                    -drawingComponent.textureOffset.y,
                    drawingComponent.textureRegion.getRegionWidth(),
                    drawingComponent.textureRegion.getRegionHeight(),
                    1,
                    1,
                    (drawingComponent.disableRotation && !this.gameData.tendMotion) ? positionComponent.facing.angle() - this.gameData.rotationAngle : positionComponent.facing.angle());
        }
    }

    private void drawHealthEntities(final List<HealthDrawingContainer> drawEntities, Matrix4 rotationMatrix) {
        for (HealthDrawingContainer drawEntity : drawEntities) {
            Vector2 rotated = rotationMatrix != null ? this.rotate(drawEntity.positionComponent.position, rotationMatrix) : drawEntity.positionComponent.position;

            this.resources.spriteBatch.draw(this.healthbarEmpty, rotated.x - this.healthbarEmpty.getRegionWidth() / 2,
                    rotated.y + drawEntity.healthComponent.yOffset);
            this.resources.spriteBatch.draw(this.healthbar.getTexture(), rotated.x - this.healthbar.getRegionWidth() / 2,
                    rotated.y + drawEntity.healthComponent.yOffset,
                    this.healthbar.getRegionX(), this.healthbar.getRegionY(),
                    (int) ((this.healthbar.getRegionWidth() * Math.max(drawEntity.healthComponent.health, 0L)) / drawEntity.healthComponent.maxHealth),
                    this.healthbar.getRegionHeight());
        }
    }

    private Vector2 rotate(Vector2 source, Matrix4 rotation) {
        return new Vector2(source.x * rotation.val[Matrix4.M00] + source.y * rotation.val[Matrix4.M01], source.x * rotation.val[Matrix4.M10] + source.y * rotation.val[Matrix4.M11]);
    }
}
