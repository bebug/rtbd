package de.florianbuchner.trbd.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.florianbuchner.trbd.Rtbd;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.GameEngine;
import de.florianbuchner.trbd.core.Resources;
import de.florianbuchner.trbd.core.WeaponType;
import de.florianbuchner.trbd.ui.WeaponHud;

public class GameScreen implements Screen, WeaponHud.WeaponHudHandler, InputProcessor {

    private ScreenHandler screenHandler;
    private WeaponHud weaponHud;
    private GameEngine gameEngine;

    private Vector2 lastTouchPosition = new Vector2();
    private float touchSpeed = 0F;
    private boolean touchStarted = false;
    private int touchPointer = 0;

    private Resources resources;
    private GameData gameData;

    public GameScreen(ScreenHandler screenHandler, Resources resources, GameData gameData) {
        this.screenHandler = screenHandler;
        this.resources = resources;
        this.gameData = gameData;
        this.gameEngine = new GameEngine(gameData, resources, 25, 25);
        this.weaponHud = new WeaponHud(gameData, resources, this);
        Gdx.input.setInputProcessor(this);
    }

    private void drawGUI() {
        this.resources.spriteBatch.begin();
        this.weaponHud.drawHud();
        this.resources.spriteBatch.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.gameEngine.update(delta);
        this.drawGUI();

        if (this.touchSpeed > 0) {
            this.touchSpeed = Math.max(0, this.touchSpeed - delta * 10F);
        }
        else {
            this.touchSpeed = Math.min(0, this.touchSpeed + delta * 10F);
        }

        if (!this.gameEngine.getGameData().tendMotion) {
            this.gameEngine.rotateScreen(this.touchSpeed);
        }
        else if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
            if (Math.abs(Gdx.input.getAccelerometerX()) > 5f ||  Math.abs(Gdx.input.getAccelerometerY()) > 5f) {
                float rotationAngle = new Vector2(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY()).angle();
                this.gameEngine.setScreenRoation(rotationAngle, true);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.gameEngine.rotateScreen(delta * 60f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
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
        if (!this.touchStarted) {
            this.gameEngine.buttonPressed(type);
        }
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
        Vector3 projection = this.gameEngine.getResources().camera.unproject(new Vector3(screenX, screenY, 0));

        if (!this.touchStarted) {
            this.weaponHud.updateInput(projection.x, projection.y);
        }

        if (!this.touchStarted) {
            this.lastTouchPosition.set(projection.x, projection.y);

            if (Math.abs(projection.x) < this.gameEngine.getGameData().width / 2F - 60F) {
                this.touchSpeed = 0F;
                this.touchStarted = true;
                this.touchPointer = button;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.touchStarted = false;
        this.touchPointer = 0;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.touchStarted && this.touchPointer == pointer) {
            Vector3 projection = this.gameEngine.getResources().camera.unproject(new Vector3(screenX, screenY, 0));

            if (Math.abs(projection.x) < 10 && Math.abs(projection.y) < 10) {
                return true;
            }

            float startAngle = this.lastTouchPosition.angle();
            this.lastTouchPosition.set(projection.x, projection.y);

            this.touchSpeed = this.lastTouchPosition.angle() - startAngle;

            while(this.touchSpeed > 180) {
                this.touchSpeed -= 360;
            }
            while(this.touchSpeed < -180) {
                this.touchSpeed += 360;
            }

            this.touchSpeed = Math.min(10F, Math.max(-10F, this.touchSpeed));

            Gdx.app.log("TouchSpeed", String.valueOf(this.touchSpeed));
        }

        return true;
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
