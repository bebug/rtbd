package de.florianbuchner.trbd.background;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.BackgroundTypeComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BackgroundComposer {

    private final int TILESIZE = 19;

    Texture backgroundTexture;
    private Entity[][] backgroundTile;
    private int length;
    private int width;

    Map<BackgroundTextureType, TextureRegion> textureRegionMap;

    public BackgroundComposer(int length, int width) {
        this.width = width;
        this.length = length;
        this.backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        this.initializeTextureMap();
        this.initializeBackground();
    }

    private void initializeTextureMap() {
        this.textureRegionMap = new EnumMap<BackgroundTextureType, TextureRegion>(BackgroundTextureType.class);

        this.textureRegionMap.put(BackgroundTextureType.EMPTY, new TextureRegion(this.backgroundTexture, 0, 0, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.SIDE_EMPTY, new TextureRegion(this.backgroundTexture, 20, 0, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.SURROUNDED_EMPTY, new TextureRegion(this.backgroundTexture, 40, 0, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.SIDES_EMPTY, new TextureRegion(this.backgroundTexture, 0, 20, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.FULL_ALTERNATIVE_1, new TextureRegion(this.backgroundTexture, 20, 20, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.SIDE_FULL, new TextureRegion(this.backgroundTexture, 40, 20, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.CORNER_EMPTY, new TextureRegion(this.backgroundTexture, 0, 40, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.FULL_ALTERNATIVE_2, new TextureRegion(this.backgroundTexture, 20, 40, 19, 19));
        this.textureRegionMap.put(BackgroundTextureType.FULL, new TextureRegion(this.backgroundTexture, 40, 40, 19, 19));
    }

    private void initializeBackground() {
        this.backgroundTile = new Entity[this.length][this.width];

        int startX = -this.length * TILESIZE / 2;
        int startY = -this.width * TILESIZE / 2;

        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                Entity entity = new Entity();
                entity.add(new PositionComponent(new Vector2(startX + i * TILESIZE, startY + j * TILESIZE), new Vector2(0, 1)));
                entity.add(new BackgroundTypeComponent(BackgroundTypeComponent.BackgroundType.GRASS));
            }
        }
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>(this.width * this.length);

        for(Entity[] entityList : this.backgroundTile) {
            for(Entity entity : entityList) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public int getXIndex(int positionX) {
        int startX = -this.length * TILESIZE / 2;

        if (positionX < startX) {
            return 0;
        } else {
            return (positionX - startX) / TILESIZE;
        }
    }

    public enum BackgroundTextureType {
        /**
         * OOO
         * OOO
         * OOO
         */
        FULL,
        FULL_ALTERNATIVE_1,
        FULL_ALTERNATIVE_2,
        /**
         * XXX
         * XXX
         * XXX
         */
        EMPTY,
        /**
         * XOX
         * XOX
         * XOX
         */
        SIDES_EMPTY,
        /**
         * XOO
         * OOO
         * OOO
         */
        CORNER_EMPTY,
        /**
         * XOO
         * XOO
         * XOO
         */
        SIDE_EMPTY,
        /**
         * XXX
         * XOX
         * XXX
         */
        SURROUNDED_EMPTY,
        /**
         * XXX
         * XOX
         * XOX
         */
        SIDE_FULL
    }
}
