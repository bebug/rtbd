package de.florianbuchner.trbd.screen;

import com.badlogic.gdx.Screen;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;
import de.florianbuchner.trbd.menu.MainMenu;

public class MainScreen implements Screen {

    private Resources resources;

    private GameData gameData;

    private ScreenHandler screenHandler;

    public MainScreen(Resources resources, GameData gameData, ScreenHandler screenHandler) {
        this.resources = resources;
        this.gameData = gameData;
        this.screenHandler = screenHandler;
    }

    @Override
    public void show() {
        this.resources.menuManager.add(new MainMenu(this.gameData, this.resources));
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
