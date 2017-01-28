package de.florianbuchner.trbd.screen;

import com.badlogic.gdx.Screen;
import de.florianbuchner.trbd.Rtbd;
import de.florianbuchner.trbd.core.GameEngine;
import de.florianbuchner.trbd.core.WeaponType;
import de.florianbuchner.trbd.ui.WeaponHud;

public class GameScreen implements Screen, WeaponHud.WeaponHudHandler {

    private Rtbd rtbd;
    private WeaponHud weaponHud;
    private GameEngine gameEngine;

    public GameScreen(Rtbd rtbd) {
        this.rtbd = rtbd;
        this.gameEngine = new GameEngine(rtbd.getGameData(), 23, 15);
        this.weaponHud = new WeaponHud(rtbd.getGameData(), this);
    }

    private void drawGUI() {
        this.rtbd.getGameData().spriteBatch.begin();
        this.weaponHud.drawHud();
        this.rtbd.getGameData().spriteBatch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.updateInputs();
        this.gameEngine.update(delta);
        this.drawGUI();
    }

    private void updateInputs() {
        this.weaponHud.updateInput();
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
        this.gameEngine.dispose();
    }

    @Override
    public void weaponButtonClicked(WeaponType type) {
        this.gameEngine.buttonPressed(type);
    }
}
