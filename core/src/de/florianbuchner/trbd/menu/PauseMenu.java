package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;

public class PauseMenu extends AbstractMenu {

    public PauseMenu(GameData gameData, Resources resources, MenuCloseHandler menuCloseHandler) {
        super(50, 50, gameData, resources);
    }

    @Override
    public void renderMenu(SpriteBatch spriteBatch, float delta) {

    }

}
