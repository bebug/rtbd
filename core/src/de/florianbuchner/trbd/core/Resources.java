package de.florianbuchner.trbd.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;
import java.util.Map;

public class Resources {

    public SpriteBatch spriteBatch;

    public ShapeRenderer shapeRenderer;

    public OrthographicCamera camera;

    public TextureAtlas textureAtlas;

    public Map<FontType, BitmapFont> fonts = new HashMap<FontType, BitmapFont>();
}
