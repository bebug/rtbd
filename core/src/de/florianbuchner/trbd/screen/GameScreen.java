package de.florianbuchner.trbd.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.Rtbd;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.EntityType;
import de.florianbuchner.trbd.entity.component.PositionComponent;
import de.florianbuchner.trbd.entity.system.DrawingSystem;
import de.florianbuchner.trbd.entity.system.TowerSystem;

public class GameScreen implements Screen {

    private Texture crosshairTexture;

    private Rtbd rtbd;
    private EntityFactory entityFactory;

    private Engine engine;
    private Entity towerEntity;

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    public GameScreen(Rtbd rtbd) {
        this.rtbd = rtbd;
        this.entityFactory = new EntityFactory();
        this.engine = new Engine();

        this.createBaseEntities();
        this.createBaseSystems();

        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);

        this.crosshairTexture = new Texture(Gdx.files.internal("crosshair.png"));
    }

    private void createBaseEntities() {
        this.engine.addEntity(entityFactory.createEntity(EntityType.FOUNDATION));
        this.towerEntity = entityFactory.createEntity(EntityType.TOWER);
        this.engine.addEntity(this.towerEntity);
    }

    private void createBaseSystems() {
        this.engine.addSystem(new DrawingSystem(this.rtbd.spriteBatch));
        this.engine.addSystem(new TowerSystem());
    }

    private void drawGUI() {
        this.rtbd.spriteBatch.begin();
        this.drawCrosshair();
        this.rtbd.spriteBatch.end();
    }

    private void drawCrosshair() {
        PositionComponent positionComponent = this.positionComponentComponentMapper.get(this.towerEntity);
        Vector2 crosshairPosition = new Vector2(100, 0);
        crosshairPosition.setAngle(positionComponent.facing.angle());

        this.rtbd.spriteBatch.draw(this.crosshairTexture,
                crosshairPosition.x - this.crosshairTexture.getWidth() / 2,
                crosshairPosition.y - this.crosshairTexture.getHeight() / 2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.engine.update(delta);
        this.drawGUI();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.entityFactory.dispose();
    }
}
