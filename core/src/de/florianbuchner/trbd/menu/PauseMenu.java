package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;

public class PauseMenu extends AbstractMenu {

    private PauseMenuHandler handler;

    public PauseMenu(GameData gameData, Resources resources, PauseMenuHandler handler) {
        super(100, 90, gameData, resources);
        this.handler = handler;

        this.addMenuButton(new MenuButton("RESUME", -50, -0, 100, 40, new ActionHandler() {
            @Override
            public void doAction() {
                PauseMenu.this.close();
            }
        }));

        this.addMenuButton(new MenuButton("EXIT", -50, -40, 100, 40, new ActionHandler() {
            @Override
            public void doAction() {
                PauseMenu.this.resources.menuManager.add(new ConfirmationMenu("ARE YOU SURE ABOUT THAT?", PauseMenu.this.gameData, PauseMenu.this.resources, new ConfirmationMenu.ConfirmationMenuHanlder() {
                    @Override
                    public void onConfirmation(boolean confirmed) {
                        PauseMenu.this.onExitConfirmed(confirmed);
                    }
                }));
            }
        }));
    }

    private void onExitConfirmed(boolean confirmed) {
        if (confirmed) {
            this.handler.onExitGame();
        }
    }

    @Override
    public void renderMenu(SpriteBatch spriteBatch, float delta) {
        super.renderMenu(spriteBatch, delta);
    }

    public interface PauseMenuHandler {
        void onExitGame();
    }

}
