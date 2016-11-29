package de.florianbuchner.trbd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.EntityType;
import de.florianbuchner.trbd.entity.GameEntity;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private EntityFactory entityFactory;

    GameEntity foundation;

    public GameScreen() {
        batch = new SpriteBatch();

        this.entityFactory = new EntityFactory();

        foundation = entityFactory.createEntity(EntityType.FOUNDATION);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        foundation.draw(batch);
        batch.end();
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
        this.batch.dispose();
        this.entityFactory.dispose();
    }
}
