package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class MenuButton {

    private String text;

    private boolean idle;

    private ActionHandler idleHandler;

    private float posX;

    private float posY;

    private float width;

    private float height;

    private float idleTime = 0F;

    public MenuButton(String text, float posX, float posY, float width, float height, ActionHandler idleHandler) {
        this.text = text;
        this.idleHandler = idleHandler;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void render(SpriteBatch spriteBatch, BitmapFont font, float delta) {
        if (this.idle) {
            idleTime += delta;
            if ((idleTime * 10F) % 2F < 1F) {
                font.draw(spriteBatch, text, this.posX, this.posY + this.height/2F - font.getLineHeight(), this.width, Align.center, true);
            }

            if (idleTime > 0.5F) {
                if (this.idleHandler != null) {
                    this.idleHandler.doAction();
                }
                this.idle = false;
            }
        }
        else {
            font.draw(spriteBatch, text, this.posX, this.posY + this.height/2F - font.getLineHeight(), this.width, Align.center, true);
        }
    }

    public boolean processInput(float x, float y) {
        if (x < this.posX + this.width && x > this.posX &&
                y < this.posY + this.height && y > this.posY) {
            this.idle = true;
            this.idleTime = 0F;
            return true;
        }
        return false;
    }

    public boolean isIdle() {
        return idle;
    }
}
