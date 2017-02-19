package de.florianbuchner.trbd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.florianbuchner.trbd.core.FontType;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;
import de.florianbuchner.trbd.screen.GameScreen;

public class Rtbd extends Game {

    private static int WIDTH = 400;

    private static float RATIO = 1.7f;

    private GameData gameData = new GameData();

    private Resources resources = new Resources();

    @Override
    public void create() {
        this.resources.spriteBatch = new SpriteBatch();
        this.gameData.width = WIDTH;
        this.gameData.height = (int)(WIDTH / RATIO);
        this.resources.camera = new OrthographicCamera(this.gameData.width, this.gameData.height);
        this.resources.spriteBatch.setProjectionMatrix(this.resources.camera.combined);
        this.resources.textureAtlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));

        this.resources.fonts.put(FontType.NORMAL, new BitmapFont(Gdx.files.internal("font-white-export.fnt")));
        this.resources.fonts.put(FontType.WARN, new BitmapFont(Gdx.files.internal("font-red-export.fnt")));
        this.resources.fonts.put(FontType.INFO, new BitmapFont(Gdx.files.internal("font-yellow-export.fnt")));

        this.resources.shapeRenderer = new ShapeRenderer();
        this.resources.shapeRenderer.setProjectionMatrix(this.resources.camera.combined);

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
        this.resources.spriteBatch.dispose();
        this.resources.textureAtlas.dispose();
    }

    public GameData getGameData() {
        return this.gameData;
    }

    public Resources getResources() {
        return this.resources;
    }
}
