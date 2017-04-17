package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import de.florianbuchner.trbd.core.FontType;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMenu implements Menu, InputProcessor {

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

    /**
     * Used by the MenuManager to remove menu from active list
     */
    private MenuCloseHandler menuCloseHandler;

    /**
     * Used if special action is necessary after close
     */
    private ActionHandler closeActionHandler;

    private List<MenuClickComponent> clickComponents = new LinkedList<MenuClickComponent>();

    private InputProcessor oldInputProcessor;

    private List<MenuButton> menuButtons = new LinkedList<MenuButton>();

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
        this.oldInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(this);
    }

    public void addClickComponent(MenuClickComponent menuClickComponent) {
        this.clickComponents.add(menuClickComponent);
    }

    public void setMenuCloseHandler(MenuCloseHandler menuCloseHandler) {
        this.menuCloseHandler = menuCloseHandler;
    }

    @Override
    public void close() {
        this.closing = true;
        this.animationTime = 0F;
    }

    @Override
    public void close(ActionHandler closeActionHandlers) {
        this.closeActionHandler = closeActionHandlers;
        this.close();
    }

    private void onClosed() {
        Gdx.input.setInputProcessor(this.oldInputProcessor);
        this.menuCloseHandler.onMenuClosed(this);

        if (this.closeActionHandler != null) {
            this.closeActionHandler.doAction();
        }
    }

    protected boolean isIdle() {
        return this.opening || this.closing || this.buttonsIdle();
    }

    private boolean buttonsIdle() {
        for (MenuButton menuButton : menuButtons) {
            if (menuButton.isIdle()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        this.time += delta;

        if (this.opening) {
            this.animationTime += delta;
            this.renderFrame(spriteBatch, (int) Math.min(this.width, 30F + ((this.animationTime * 3 * this.width) - 30F)),
                    (int) Math.min(this.height, 30F + ((this.animationTime * 3 * this.height) - 30F)));
            if (this.animationTime > 0.3F) {
                this.opening = false;
            }
        } else if (closing) {
            this.animationTime += delta;
            this.renderFrame(spriteBatch, (int) Math.max(30F, this.width - (this.animationTime * 3 * (this.width - 30))),
                    (int) Math.max(30F, this.height - (this.animationTime * 3 * (this.height - 30))));
            if (this.animationTime > 0.3F) {
                if (this.menuCloseHandler != null) {
                   this.onClosed();
                }
            }
        } else {
            this.renderFrame(spriteBatch, this.width, this.height);
            this.renderMenu(spriteBatch, delta);
        }
    }

    protected void renderMenu(SpriteBatch spriteBatch, float delta) {
        BitmapFont font = this.resources.fonts.get(FontType.NORMAL);
        for (MenuButton menuButton : menuButtons) {
            menuButton.render(this.resources.spriteBatch, font, delta);
        }
    }

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

    protected void addMenuButton(MenuButton menuButton) {
        this.menuButtons.add(menuButton);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 projection = this.resources.camera.unproject(new Vector3(screenX, screenY, 0));

        if (!this.isIdle()) {
            for (MenuButton menuButton : menuButtons) {
                if (menuButton.processInput(projection.x, projection.y)) {
                    break;
                }
            }

            for (final MenuClickComponent clickComponent : clickComponents) {
                if (clickComponent.rectangle.contains(projection.x, projection.y)) {
                    clickComponent.menuClickHanlder.onClick();
                }
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
