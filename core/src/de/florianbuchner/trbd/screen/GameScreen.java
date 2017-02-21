package de.florianbuchner.trbd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
        this.gameEngine = new GameEngine(rtbd.getGameData(), rtbd.getResources(), 25, 25);
        this.weaponHud = new WeaponHud(rtbd.getGameData(), rtbd.getResources(), this);
    }

    private void drawGUI() {
        this.rtbd.getResources().spriteBatch.begin();
        this.weaponHud.drawHud();
        this.rtbd.getResources().spriteBatch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.updateInputs(delta);
        this.gameEngine.update(delta);
        this.drawGUI();
    }

    private void updateInputs(float delta) {
        this.weaponHud.updateInput();

        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            if (Math.abs(Gdx.input.getAccelerometerX()) > 5f ||  Math.abs(Gdx.input.getAccelerometerY()) > 5f) {
                float rotationAngle = new Vector2(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY()).angle();
                this.gameEngine.setScreenRoation(rotationAngle);
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            this.gameEngine.rotateScreen(delta * 60f);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            this.gameEngine.rotateScreen(delta * -60f);
        }
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

    @Override
    public void weaponButtonClicked(WeaponType type) {
        this.gameEngine.buttonPressed(type);
    }
}
