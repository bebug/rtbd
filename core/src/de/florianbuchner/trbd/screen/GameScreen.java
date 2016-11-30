package de.florianbuchner.trbd.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.florianbuchner.trbd.Rtbd;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.EntityType;
import de.florianbuchner.trbd.entity.GameEntity;
import de.florianbuchner.trbd.entity.system.DrawingSystem;

public class GameScreen implements Screen {

    private Rtbd rtbd;
    private EntityFactory entityFactory;

    private Engine engine;

    Entity foundation;

    public GameScreen(Rtbd rtbd) {
        this.rtbd = rtbd;
        this.entityFactory = new EntityFactory();
        this.engine = new Engine();

        foundation = entityFactory.createEntity(EntityType.FOUNDATION);

        this.createBaseEntities();
        this.createBaseSystems();
    }

    private void createBaseEntities() {
        this.engine.addEntity(foundation);
    }

    private void createBaseSystems() {
        this.engine.addSystem(new DrawingSystem(this.rtbd.spriteBatch));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.engine.update(delta);
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
