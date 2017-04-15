package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Menu {
    void render(SpriteBatch spriteBatch, float delta);
    void close();
    void setMenuCloseHandler(MenuCloseHandler menuCloseHandler);
}
