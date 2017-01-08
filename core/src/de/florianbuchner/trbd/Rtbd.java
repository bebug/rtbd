package de.florianbuchner.trbd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.screen.GameScreen;

public class Rtbd extends Game {

    private static int WIDTH = 400;

    private static float RATIO = 1.7f;

    private GameData gameData = new GameData();

    @Override
    public void create() {
        this.gameData.spriteBatch = new SpriteBatch();
        this.gameData.width = WIDTH;
        this.gameData.height = (int)(WIDTH / RATIO);
        this.gameData.camera = new OrthographicCamera(this.gameData.width, this.gameData.height);
        this.gameData.spriteBatch.setProjectionMatrix(this.gameData.camera.combined);

        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.gameData.spriteBatch.dispose();
    }

    public GameData getGameData() {
        return gameData;
    }
}
