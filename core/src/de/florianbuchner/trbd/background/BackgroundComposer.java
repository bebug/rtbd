package de.florianbuchner.trbd.background;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.core.Resources;
import de.florianbuchner.trbd.entity.BackgroundEntity;
import de.florianbuchner.trbd.entity.component.DrawingComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackgroundComposer implements BackgroundTileHandler {

    private final int TILESIZE = 19;

    private TextureRegion backgroundTexture;
    private BackgroundEntity[][] backgroundTile;
    private int length;
    private int height;

    private Map<TileCombination, TileTexture> textureRegionMap;

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;
    private ComponentMapper<DrawingComponent> drawingComponentComponentMapper;

    public BackgroundComposer(int length, int height, Resources resources) {
        this.height = height;
        this.length = length;
        this.backgroundTexture = resources.textureAtlas.createSprite("background");

        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.drawingComponentComponentMapper = ComponentMapper.getFor(DrawingComponent.class);

        this.initializeTextureMap();
        this.initializeBackground();

        this.setTile(20, 20, BackgroundEntity.BackgroundType.SOIL);
        this.setTile(0, 0, BackgroundEntity.BackgroundType.SOIL);
        this.setTile(-20, -20, BackgroundEntity.BackgroundType.SOIL);
        this.setTile(20, -20, BackgroundEntity.BackgroundType.SOIL);
        this.setTile(-20, 20, BackgroundEntity.BackgroundType.SOIL);

    }

    private void initializeTextureMap() {
        this.textureRegionMap = new HashMap<TileCombination, TileTexture>();

        // Create all possible ground combinations
        // If one value on the edges will make no difference it is always filled with GRASS
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 0, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL)
                , new TextureRegion(this.backgroundTexture, 80, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 120, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 20, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 40, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 0, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 40, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 0, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 80, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 60, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 80, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL)
                , new TextureRegion(this.backgroundTexture, 60, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 80, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL)
                , new TextureRegion(this.backgroundTexture, 60, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 40, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 20, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundEntity.BackgroundType.SOIL, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.SOIL,
                        BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS, BackgroundEntity.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 60, 19, 19, -19));
    }

    private void addWithRotation(TileCombination tileCombination, TextureRegion textureRegion) {
        if (!this.textureRegionMap.containsKey(tileCombination)) {
            this.textureRegionMap.put(tileCombination, new TileTexture(textureRegion, new Vector2(1, 0)));
        }
        tileCombination = tileCombination.rotate90();
        if (!this.textureRegionMap.containsKey(tileCombination)) {
            this.textureRegionMap.put(tileCombination, new TileTexture(textureRegion, new Vector2(0, -1)));
        }
        tileCombination = tileCombination.rotate90();
        if (!this.textureRegionMap.containsKey(tileCombination)) {
            this.textureRegionMap.put(tileCombination, new TileTexture(textureRegion, new Vector2(-1, 0)));
        }
        tileCombination = tileCombination.rotate90();
        if (!this.textureRegionMap.containsKey(tileCombination)) {
            this.textureRegionMap.put(tileCombination, new TileTexture(textureRegion, new Vector2(0, 1)));
        }
    }

    private void initializeBackground() {
        this.backgroundTile = new BackgroundEntity[this.length][this.height];

        int startX = -this.length * TILESIZE / 2;
        int startY = -this.height * TILESIZE / 2;

        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.height; j++) {
                BackgroundEntity entity = new BackgroundEntity(BackgroundEntity.BackgroundType.GRASS);
                entity.add(new PositionComponent(new Vector2(startX + TILESIZE / 2 + i * TILESIZE, startY + TILESIZE / 2 + j * TILESIZE), new Vector2(1, 0), PositionComponent.PositionLayer.Background));
                // Add randomly flowers
                double rnd = Math.random();
                if (rnd < 0.03) {
                    entity.add(new DrawingComponent(new TextureRegion(this.backgroundTexture, 20, 20, 19, 19)));
                } else if (rnd < 0.06) {
                    entity.add(new DrawingComponent(new TextureRegion(this.backgroundTexture, 100, 0, 19, 19)));
                } else if (rnd < 0.09) {
                    entity.add(new DrawingComponent(new TextureRegion(this.backgroundTexture, 100, 20, 19, 19)));
                } else {
                    entity.add(new DrawingComponent(new TextureRegion(this.backgroundTexture, 80, 40, 19, 19)));
                }
                this.backgroundTile[i][j] = entity;
            }
        }
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<Entity>(this.height * this.length);

        for (Entity[] entityList : this.backgroundTile) {
            for (Entity entity : entityList) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public void setTile(float positionX, float positionY, BackgroundEntity.BackgroundType type) {
        if (positionX < -this.length * TILESIZE / 2 ||
                positionY > this.length * TILESIZE / 2 ||
                positionY < -this.height * TILESIZE / 2 ||
                positionY > this.height * TILESIZE / 2) {
            return;
        }

        int xIndex = this.getXIndex(positionX);
        int yIndex = this.getYIndex(positionY);

        this.backgroundTile[xIndex][yIndex].setType(type);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                this.reprocessTexture(xIndex + i, yIndex + j);
            }
        }
    }

    private void reprocessTexture(int xIndex, int yIndex) {
        if (this.invalidIndex(xIndex, yIndex)) {
            return;
        }

        TileCombination tileCombination = new TileCombination(
                this.getBackgroundType(xIndex - 1, yIndex + 1),
                this.getBackgroundType(xIndex, yIndex + 1),
                this.getBackgroundType(xIndex + 1, yIndex + 1),
                this.getBackgroundType(xIndex - 1, yIndex),
                this.getBackgroundType(xIndex, yIndex),
                this.getBackgroundType(xIndex + 1, yIndex),
                this.getBackgroundType(xIndex - 1, yIndex - 1),
                this.getBackgroundType(xIndex, yIndex - 1),
                this.getBackgroundType(xIndex + 1, yIndex - 1)
        );

        // Because not important edges are filed with grass we will do it also for the hashmap access
        if (tileCombination.b2 == BackgroundEntity.BackgroundType.SOIL) {
            tileCombination.a1 = BackgroundEntity.BackgroundType.GRASS;
            tileCombination.a2 = BackgroundEntity.BackgroundType.GRASS;
            tileCombination.a3 = BackgroundEntity.BackgroundType.GRASS;
            tileCombination.b1 = BackgroundEntity.BackgroundType.GRASS;
            tileCombination.b3 = BackgroundEntity.BackgroundType.GRASS;
            tileCombination.c1 = BackgroundEntity.BackgroundType.GRASS;
            tileCombination.c2 = BackgroundEntity.BackgroundType.GRASS;
            tileCombination.c3 = BackgroundEntity.BackgroundType.GRASS;
        } else {
            if (tileCombination.a2 == BackgroundEntity.BackgroundType.SOIL) {
                tileCombination.a1 = BackgroundEntity.BackgroundType.GRASS;
                tileCombination.a3 = BackgroundEntity.BackgroundType.GRASS;
            }
            if (tileCombination.b1 == BackgroundEntity.BackgroundType.SOIL) {
                tileCombination.a1 = BackgroundEntity.BackgroundType.GRASS;
                tileCombination.c1 = BackgroundEntity.BackgroundType.GRASS;
            }
            if (tileCombination.b3 == BackgroundEntity.BackgroundType.SOIL) {
                tileCombination.a3 = BackgroundEntity.BackgroundType.GRASS;
                tileCombination.c3 = BackgroundEntity.BackgroundType.GRASS;
            }
            if (tileCombination.c2 == BackgroundEntity.BackgroundType.SOIL) {
                tileCombination.c1 = BackgroundEntity.BackgroundType.GRASS;
                tileCombination.c3 = BackgroundEntity.BackgroundType.GRASS;
            }
        }

        TileTexture tileTexture = this.textureRegionMap.get(tileCombination);

        this.positionComponentComponentMapper.get(this.backgroundTile[xIndex][yIndex]).facing = tileTexture.facing;
        this.drawingComponentComponentMapper.get(this.backgroundTile[xIndex][yIndex]).textureRegion = tileTexture.textureRegion;
    }

    private BackgroundEntity.BackgroundType getBackgroundType(int xIndex, int yIndex) {
        if (this.invalidIndex(xIndex, yIndex)) {
            return BackgroundEntity.BackgroundType.GRASS;
        }

        return this.backgroundTile[xIndex][yIndex].getType();
    }

    private boolean invalidIndex(int xIndex, int yIndex) {
        return xIndex < 0 || xIndex > this.backgroundTile.length - 1 ||
                yIndex < 0 || yIndex > this.backgroundTile[xIndex].length - 1;
    }

    private int getXIndex(float positionX) {
        int startX = -this.length * TILESIZE / 2;

        if (positionX < startX) {
            return 0;
        } else if (-positionX < startX) {
            return TILESIZE - 1;
        } else {
            return (int) ((positionX - startX) / TILESIZE);
        }
    }

    private int getYIndex(float positionY) {
        int startY = -this.height * TILESIZE / 2;

        if (positionY < startY) {
            return 0;
        } else if (-positionY < startY) {
            return TILESIZE - 1;
        } else {
            return (int) ((positionY - startY) / TILESIZE);
        }
    }

    @Override
    public void destroyTile(Vector2 position) {
        this.setTile(position.x, position.y, BackgroundEntity.BackgroundType.SOIL);
    }

    public class TileCombination {
        public BackgroundEntity.BackgroundType a1;
        public BackgroundEntity.BackgroundType a2;
        public BackgroundEntity.BackgroundType a3;
        public BackgroundEntity.BackgroundType b1;
        public BackgroundEntity.BackgroundType b2;
        public BackgroundEntity.BackgroundType b3;
        public BackgroundEntity.BackgroundType c1;
        public BackgroundEntity.BackgroundType c2;
        public BackgroundEntity.BackgroundType c3;

        public TileCombination(
                BackgroundEntity.BackgroundType a1,
                BackgroundEntity.BackgroundType a2,
                BackgroundEntity.BackgroundType a3,
                BackgroundEntity.BackgroundType b1,
                BackgroundEntity.BackgroundType b2,
                BackgroundEntity.BackgroundType b3,
                BackgroundEntity.BackgroundType c1,
                BackgroundEntity.BackgroundType c2,
                BackgroundEntity.BackgroundType c3

        ) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.b1 = b1;
            this.b2 = b2;
            this.b3 = b3;
            this.c1 = c1;
            this.c2 = c2;
            this.c3 = c3;
        }

        public TileCombination rotate90() {
            return new TileCombination(
                    this.c1, this.b1, this.a1,
                    this.c2, this.b2, this.a2,
                    this.c3, this.b3, this.a3
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TileCombination that = (TileCombination) o;

            if (a1 != that.a1) return false;
            if (a2 != that.a2) return false;
            if (a3 != that.a3) return false;
            if (b1 != that.b1) return false;
            if (b2 != that.b2) return false;
            if (b3 != that.b3) return false;
            if (c1 != that.c1) return false;
            if (c2 != that.c2) return false;
            return c3 == that.c3;

        }

        @Override
        public int hashCode() {
            int result = a1 != null ? a1.hashCode() : 0;
            result = 31 * result + (a2 != null ? a2.hashCode() : 0);
            result = 31 * result + (a3 != null ? a3.hashCode() : 0);
            result = 31 * result + (b1 != null ? b1.hashCode() : 0);
            result = 31 * result + (b2 != null ? b2.hashCode() : 0);
            result = 31 * result + (b3 != null ? b3.hashCode() : 0);
            result = 31 * result + (c1 != null ? c1.hashCode() : 0);
            result = 31 * result + (c2 != null ? c2.hashCode() : 0);
            result = 31 * result + (c3 != null ? c3.hashCode() : 0);
            return result;
        }
    }

    public class TileTexture {
        public TextureRegion textureRegion;
        public Vector2 facing;

        public TileTexture(TextureRegion textureRegion, Vector2 facing) {
            this.textureRegion = textureRegion;
            this.facing = facing;
        }
    }
}
