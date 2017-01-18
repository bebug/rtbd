package de.florianbuchner.trbd.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import de.florianbuchner.trbd.Rtbd;
import de.florianbuchner.trbd.background.BackgroundComposer;
import de.florianbuchner.trbd.core.GameEngine;
import de.florianbuchner.trbd.core.WeaponType;
import de.florianbuchner.trbd.entity.EntityFactory;
import de.florianbuchner.trbd.entity.component.PositionComponent;
import de.florianbuchner.trbd.entity.system.AnimationSystem;
import de.florianbuchner.trbd.entity.system.DelaySystem;
import de.florianbuchner.trbd.entity.system.DrawingSystem;
import de.florianbuchner.trbd.entity.system.MotionSystem;
import de.florianbuchner.trbd.ui.WeaponHud;

public class GameScreen implements Screen, WeaponHud.WeaponHudHandler {

    private Rtbd rtbd;
    private EntityFactory entityFactory;
    private BackgroundComposer backgroundComposer;
    private WeaponHud weaponHud;
    private GameEngine gameEngine;

    private Engine entityEngine;
    private Entity towerEntity;

    private ComponentMapper<PositionComponent> positionComponentComponentMapper;

    public GameScreen(Rtbd rtbd) {
        this.rtbd = rtbd;
        this.entityFactory = new EntityFactory();
        this.entityEngine = new Engine();
        this.backgroundComposer = new BackgroundComposer(23, 15);
        this.gameEngine = new GameEngine(rtbd.getGameData(), this.entityEngine, this.entityFactory);
        this.positionComponentComponentMapper = ComponentMapper.getFor(PositionComponent.class);

        this.createBaseEntities();
        this.createBaseSystems();

        this.weaponHud = new WeaponHud(rtbd.getGameData(), this);
    }

    private void createBaseEntities() {
        // Add background entities
        for (Entity entity : this.backgroundComposer.getEntities()) {
            this.entityEngine.addEntity(entity);
        }

        this.entityEngine.addEntity(this.entityFactory.createFoundation());
        this.towerEntity = this.entityFactory.createTower();
        this.entityEngine.addEntity(this.towerEntity);
        this.entityEngine.addEntity(this.entityFactory.createCrossHair(this.positionComponentComponentMapper.get(this.towerEntity).facing));
    }

    private void createBaseSystems() {
        this.entityEngine.addSystem(new DelaySystem());
        this.entityEngine.addSystem(new AnimationSystem());
        this.entityEngine.addSystem(new DrawingSystem(this.rtbd.getGameData().spriteBatch));
        this.entityEngine.addSystem(new MotionSystem());
    }

    private void drawGUI() {
        this.rtbd.getGameData().spriteBatch.begin();
        //this.drawCrosshair();
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
        this.entityEngine.update(delta);
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
        this.entityFactory.dispose();
    }

    @Override
    public void weaponButtonClicked(WeaponType type) {
        this.gameEngine.buttonPressed(type, this.positionComponentComponentMapper.get(this.towerEntity).facing.nor());
    }
}
