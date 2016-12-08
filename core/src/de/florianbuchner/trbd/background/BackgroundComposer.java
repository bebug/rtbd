package de.florianbuchner.trbd.background;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.florianbuchner.trbd.entity.component.BackgroundTypeComponent;
import de.florianbuchner.trbd.entity.component.DrawingComponent;
import de.florianbuchner.trbd.entity.component.PositionComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackgroundComposer {

    private final int TILESIZE = 19;

    private Texture backgroundTexture;
    private Entity[][] backgroundTile;
    private int length;
    private int height;

    private Map<TileCombination, TileTexture> textureRegionMap;

    private ComponentMapper<BackgroundTypeComponent> backgroundTypeComponentComponentMapper;
    private ComponentMapper<PositionComponent> positionComponentComponentMapper;
    private ComponentMapper<DrawingComponent> drawingComponentComponentMapper;

    public BackgroundComposer(int length, int height) {
        this.height = height;
        this.length = length;
        this.backgroundTexture = new Texture(Gdx.files.internal("background.png"));

        this.backgroundTypeComponentComponentMapper = ComponentMapper.getFor(BackgroundTypeComponent.class);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);
        this.drawingComponentComponentMapper = ComponentMapper.getFor(DrawingComponent.class);

        this.initializeTextureMap();
        this.initializeBackground();

        this.setTile(20,20, BackgroundTypeComponent.BackgroundType.SOIL);
        this.setTile(0,0, BackgroundTypeComponent.BackgroundType.SOIL);
        this.setTile(-20,-20, BackgroundTypeComponent.BackgroundType.SOIL);
        this.setTile(20,-20, BackgroundTypeComponent.BackgroundType.SOIL);
        this.setTile(-20,20, BackgroundTypeComponent.BackgroundType.SOIL);

    }

    private void initializeTextureMap() {
        this.textureRegionMap = new HashMap<TileCombination, TileTexture>();

        // Create all possible ground combinations
        // If one value on the edges will make no difference it is always filled with GRASS
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 0, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 20, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 40, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 0, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 40, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 0, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 80, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 60, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 80, 0, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL)
                , new TextureRegion(this.backgroundTexture, 60, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 80, 20, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL)
                , new TextureRegion(this.backgroundTexture, 60, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 40, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
                , new TextureRegion(this.backgroundTexture, 20, 40, 19, 19));
        this.addWithRotation(
                new TileCombination(
                        BackgroundTypeComponent.BackgroundType.SOIL, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.SOIL,
                        BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS, BackgroundTypeComponent.BackgroundType.GRASS)
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
        this.backgroundTile = new Entity[this.length][this.height];

        int startX = -this.length * TILESIZE / 2;
        int startY = -this.height * TILESIZE / 2;

        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.height; j++) {
                Entity entity = new Entity();
                entity.add(new PositionComponent(new Vector2(startX + TILESIZE / 2 + i * TILESIZE, startY + TILESIZE / 2 + j * TILESIZE), new Vector2(1, 0)));
                entity.add(new BackgroundTypeComponent(BackgroundTypeComponent.BackgroundType.GRASS));
                // Add randomly flowers
                double rnd = Math.random();
                if (rnd < 0.1) {
                    entity.add(new DrawingComponent(new TextureRegion(this.backgroundTexture, 20, 20, 19, 19)));
                } else {
                    entity.add(new DrawingComponent(new TextureRegion(this.backgroundTexture, 80, 40, 19, 19)));
                }
                this.backgroundTile[i][j] = entity;
            }
        }
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<Entity>(this.height * this.length);

        for(Entity[] entityList : this.backgroundTile) {
            for(Entity entity : entityList) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public void setTile(float positionX, float positionY, BackgroundTypeComponent.BackgroundType type) {
        if (positionX < -this.length * TILESIZE / 2 ||
                positionY > this.length * TILESIZE / 2 ||
                positionY < -this.height * TILESIZE / 2 ||
                positionY > this.height * TILESIZE / 2) {
            return;
        }

        int xIndex = this.getXIndex(positionX);
        int yIndex = this.getYIndex(positionY);

        this.backgroundTypeComponentComponentMapper.get(this.backgroundTile[xIndex][yIndex]).type = type;

        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
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
        if (tileCombination.b2 == BackgroundTypeComponent.BackgroundType.SOIL) {
            tileCombination.a1 = BackgroundTypeComponent.BackgroundType.GRASS;
            tileCombination.a2 = BackgroundTypeComponent.BackgroundType.GRASS;
            tileCombination.a3 = BackgroundTypeComponent.BackgroundType.GRASS;
            tileCombination.b1 = BackgroundTypeComponent.BackgroundType.GRASS;
            tileCombination.b3 = BackgroundTypeComponent.BackgroundType.GRASS;
            tileCombination.c1 = BackgroundTypeComponent.BackgroundType.GRASS;
            tileCombination.c2 = BackgroundTypeComponent.BackgroundType.GRASS;
            tileCombination.c3 = BackgroundTypeComponent.BackgroundType.GRASS;
        } else {
            if (tileCombination.a2 == BackgroundTypeComponent.BackgroundType.SOIL) {
                tileCombination.a1 = BackgroundTypeComponent.BackgroundType.GRASS;
                tileCombination.a3 = BackgroundTypeComponent.BackgroundType.GRASS;
            }
            if (tileCombination.b1 == BackgroundTypeComponent.BackgroundType.SOIL) {
                tileCombination.a1 = BackgroundTypeComponent.BackgroundType.GRASS;
                tileCombination.c1 = BackgroundTypeComponent.BackgroundType.GRASS;
            }
            if (tileCombination.b3 == BackgroundTypeComponent.BackgroundType.SOIL) {
                tileCombination.a3 = BackgroundTypeComponent.BackgroundType.GRASS;
                tileCombination.c3 = BackgroundTypeComponent.BackgroundType.GRASS;
            }
            if (tileCombination.c2 == BackgroundTypeComponent.BackgroundType.SOIL) {
                tileCombination.c1 = BackgroundTypeComponent.BackgroundType.GRASS;
                tileCombination.c3 = BackgroundTypeComponent.BackgroundType.GRASS;
            }
        }

        TileTexture tileTexture = this.textureRegionMap.get(tileCombination);

        this.positionComponentComponentMapper.get(this.backgroundTile[xIndex][yIndex]).facing = tileTexture.facing;
        this.drawingComponentComponentMapper.get(this.backgroundTile[xIndex][yIndex]).textureRegion = tileTexture.textureRegion;
    }

    private BackgroundTypeComponent.BackgroundType getBackgroundType(int xIndex, int yIndex) {
        if (this.invalidIndex(xIndex, yIndex)) {
            return BackgroundTypeComponent.BackgroundType.GRASS;
        }

        return this.backgroundTypeComponentComponentMapper.get(this.backgroundTile[xIndex][yIndex]).type;
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
            return (int)((positionX - startX) / TILESIZE);
        }
    }

    private int getYIndex(float positionY) {
        int startY = -this.height * TILESIZE / 2;

        if (positionY < startY) {
            return 0;
        } else if (-positionY < startY) {
            return TILESIZE - 1;
        } else {
            return (int)((positionY - startY) / TILESIZE);
        }
    }
    
    public class TileCombination {
        public BackgroundTypeComponent.BackgroundType a1;
        public BackgroundTypeComponent.BackgroundType a2;
        public BackgroundTypeComponent.BackgroundType a3;
        public BackgroundTypeComponent.BackgroundType b1;
        public BackgroundTypeComponent.BackgroundType b2;
        public BackgroundTypeComponent.BackgroundType b3;
        public BackgroundTypeComponent.BackgroundType c1;
        public BackgroundTypeComponent.BackgroundType c2;
        public BackgroundTypeComponent.BackgroundType c3;
        
        public TileCombination(
                BackgroundTypeComponent.BackgroundType a1,
                BackgroundTypeComponent.BackgroundType a2,
                BackgroundTypeComponent.BackgroundType a3,
                BackgroundTypeComponent.BackgroundType b1,
                BackgroundTypeComponent.BackgroundType b2,
                BackgroundTypeComponent.BackgroundType b3,
                BackgroundTypeComponent.BackgroundType c1,
                BackgroundTypeComponent.BackgroundType c2,
                BackgroundTypeComponent.BackgroundType c3

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
