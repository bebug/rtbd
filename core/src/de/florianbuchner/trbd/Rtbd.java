package de.florianbuchner.trbd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.florianbuchner.trbd.screen.GameScreen;

public class Rtbd extends Game {

    public SpriteBatch spriteBatch;

    @Override
    public void create() {
        this.spriteBatch = new SpriteBatch();

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
        this.spriteBatch.dispose();
    }
}
