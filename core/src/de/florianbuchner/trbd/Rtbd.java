package de.florianbuchner.trbd;

import com.badlogic.gdx.Game;
import de.florianbuchner.trbd.screen.GameScreen;

public class Rtbd extends Game {

    @Override
    public void create() {
        this.setScreen(new GameScreen());
    }
}
