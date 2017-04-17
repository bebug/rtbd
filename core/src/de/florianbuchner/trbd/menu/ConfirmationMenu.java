package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import de.florianbuchner.trbd.core.FontType;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;

public class ConfirmationMenu extends AbstractMenu {

    private ConfirmationMenuHanlder handler;

    private String text;

    public ConfirmationMenu(String text, GameData gameData, Resources resources, ConfirmationMenuHanlder handler) {
        super(160, 80, gameData, resources);
        this.handler = handler;
        this.text = text;

        this.addMenuButton(new MenuButton("YES", -70, -35, 70, 40, new ActionHandler() {
            @Override
            public void doAction() {
                ConfirmationMenu.this.close(new ActionHandler() {
                    @Override
                    public void doAction() {
                        ConfirmationMenu.this.handler.onConfirmation(true);
                    }
                });
            }
        }));

        this.addMenuButton(new MenuButton("NO", 0, -35, 70, 40, new ActionHandler() {
            @Override
            public void doAction() {
                ConfirmationMenu.this.close(new ActionHandler() {
                    @Override
                    public void doAction() {
                        ConfirmationMenu.this.handler.onConfirmation(false);
                    }
                });
            }
        }));
    }

    @Override
    protected void renderMenu(SpriteBatch spriteBatch, float delta) {
        super.renderMenu(spriteBatch, delta);
        BitmapFont font = this.resources.fonts.get(FontType.NORMAL);
        font.draw(spriteBatch, this.text, -75, font.getLineHeight(), 150, Align.center, true);
    }

    public interface ConfirmationMenuHanlder {
        void onConfirmation(boolean confirmed);
    }
}
