package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;

import java.util.LinkedList;

public abstract class AbstractMenu implements Menu {

    protected float time = 0F;

    protected int width;

    protected int height;

    protected boolean opening;

    protected boolean closing;

    protected float animationTime;

    protected GameData gameData;

    protected Resources resources;

    private TextureRegion topLeft;

    private TextureRegion topRight;

    private TextureRegion bottomLeft;

    private TextureRegion bottomRight;

    private TextureRegion top;

    private TextureRegion bottom;

    private TextureRegion left;

    private TextureRegion right;

    private TextureRegion background;

    private MenuCloseHandler menuCloseHandler;

    private LinkedList<MenuClickComponent> clickComponents = new LinkedList<MenuClickComponent>();

    public AbstractMenu(int width, int height, GameData gameData, Resources resources) {
        this.width = width;
        this.height = height;
        this.gameData = gameData;
        this.resources = resources;

        Sprite menuSprite = this.resources.textureAtlas.createSprite("menu");
        topLeft = new TextureRegion(menuSprite, 0, 0, 6, 7);
        topRight = new TextureRegion(menuSprite, 26, 0, 6, 7);
        bottomLeft = new TextureRegion(menuSprite, 0, 27, 6, 7);
        bottomRight = new TextureRegion(menuSprite, 26, 27, 6, 7);
        top = new TextureRegion(menuSprite, 6, 0, 7, 7);
        bottom = new TextureRegion(menuSprite, 6, 27, 7, 7);
        left = new TextureRegion(menuSprite, 0, 7, 6, 6);
        right = new TextureRegion(menuSprite, 26, 7, 6, 6);
        background = new TextureRegion(menuSprite, 6, 7, 6, 6);

        this.opening = true;

    }

    public void addClickComponent(MenuClickComponent menuClickComponent) {
        this.clickComponents.add(menuClickComponent);
    }

    public void setMenuCloseHandler(MenuCloseHandler menuCloseHandler) {
        this.menuCloseHandler = menuCloseHandler;
    }

    public void close() {
        this.closing = true;
        this.animationTime = 0F;
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        this.time += delta;

        if (this.opening) {
            this.animationTime += delta;
            this.renderFrame(spriteBatch, (int) Math.min(this.width, 30F + (this.animationTime * 3 * this.width - 30F)),
                    (int) Math.min(this.height, 30F + (this.animationTime * 3 * this.height - 30F)));
            if (this.animationTime > 1F) {
                this.opening = false;
            }
        } else if (closing) {
            this.animationTime += delta;
            this.renderFrame(spriteBatch, (int) Math.max(30F, this.width - this.width * (1F - this.animationTime * 3)),
                    (int) Math.max(30F, this.height - this.height * (1F - this.animationTime * 3)));
            if (this.animationTime > 1F) {
                if (this.menuCloseHandler != null) {
                    this.menuCloseHandler.onMenuClosed(this);
                }
            }
        } else {
            this.renderFrame(spriteBatch, this.width, this.height);
            this.renderMenu(spriteBatch, delta);
        }
    }

    public abstract void renderMenu(SpriteBatch spriteBatch, float delta);

    protected void renderFrame(SpriteBatch spriteBatch, int width, int height) {
        float startX = -width / 2F;
        float startY = -height / 2F;

        // Draw edges
        spriteBatch.draw(bottomLeft, startX, startY);
        spriteBatch.draw(bottomRight, startX + width - topRight.getRegionWidth(), startY);
        spriteBatch.draw(topLeft, startX, startY + height - bottomLeft.getRegionHeight());
        spriteBatch.draw(topRight, startX + width - topRight.getRegionWidth(), startY + height - bottomLeft.getRegionHeight());

        // Draw border
        float drawWidth = width - this.bottomLeft.getRegionWidth() - this.bottomRight.getRegionWidth();
        float drawHeight = height - this.bottomLeft.getRegionHeight() - this.bottomRight.getRegionHeight();
        spriteBatch.draw(this.bottom, startX + 6, startY, drawWidth, 7);
        spriteBatch.draw(this.top, startX + 6, startY + 7 + drawHeight, drawWidth, 7);
        spriteBatch.draw(this.left, startX, startY + 7, 6, drawHeight);
        spriteBatch.draw(this.right, startX + drawWidth + 6, startY + 7, 6, drawHeight);

        // Draw background
        float drawnHeight = 0F;
        while (drawnHeight < drawHeight) {
            float drawnWidth = 0F;
            while (drawnWidth < drawWidth) {
                spriteBatch.draw(this.background, startX + drawnWidth + 6, startY + drawnHeight + 7,
                        Math.min(this.background.getRegionHeight(), drawWidth - drawnWidth),
                        Math.min(this.background.getRegionHeight(), drawHeight - drawnHeight));
                drawnWidth += this.background.getRegionWidth();
            }
            drawnHeight += this.background.getRegionHeight();
        }
    }

    @Override
    public void touchDown(float x, float y) {
        for (final MenuClickComponent clickComponent : clickComponents) {
            if (clickComponent.rectangle.contains(x, y)) {
                clickComponent.menuClickHanlder.onClick();
            }
        }
    }

}
