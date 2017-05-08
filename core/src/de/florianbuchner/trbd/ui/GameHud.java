package de.florianbuchner.trbd.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import de.florianbuchner.trbd.core.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameHud {

    public interface WeaponHudHandler {
        void weaponButtonClicked(WeaponType type);
    }

    public static float BUTTON_SIZE = 70;

    private Map<WeaponType, WeaponButton> weaponButtons = new HashMap<WeaponType, WeaponButton>(WeaponType.values().length);

    private Map<WeaponType, Rectangle> weaponBounds = new HashMap<WeaponType, Rectangle>(WeaponType.values().length);

    private GameData gameData;

    private Resources resources;

    private WeaponHudHandler weaponHudHandler;

    private GameEngine gameEngine;

    private TextureRegion miniScoreIcon;

    private TextureRegion miniCrystalIcon;

    public GameHud(GameData gameData, Resources resources, WeaponHudHandler weaponHudHandler, GameEngine gameEngine) {
        this.gameData = gameData;
        this.weaponHudHandler = weaponHudHandler;
        this.resources = resources;
        this.gameEngine = gameEngine;

        this.createWeaponButtons();

        this.weaponBounds.put(WeaponType.GUN, new Rectangle(-this.gameData.width / 2F , -gameData.height / 2F, GameHud.BUTTON_SIZE, GameHud.BUTTON_SIZE));
        this.weaponBounds.put(WeaponType.LASER, new Rectangle(this.gameData.width / 2F - GameHud.BUTTON_SIZE, -gameData.height / 2F, GameHud.BUTTON_SIZE, GameHud.BUTTON_SIZE));
        this.weaponBounds.put(WeaponType.BLAST, new Rectangle(-this.gameData.width / 2F , gameData.height / 2F - GameHud.BUTTON_SIZE, GameHud.BUTTON_SIZE, GameHud.BUTTON_SIZE));
        this.weaponBounds.put(WeaponType.BOMB, new Rectangle(this.gameData.width / 2F - GameHud.BUTTON_SIZE, gameData.height / 2F - GameHud.BUTTON_SIZE, GameHud.BUTTON_SIZE, GameHud.BUTTON_SIZE));

        TextureRegion miniIconsTexture = this.resources.textureAtlas.createSprite("mini-icons");
        this.miniCrystalIcon = new TextureRegion(miniIconsTexture, 12, 0, 12, 12);
        this.miniScoreIcon = new TextureRegion(miniIconsTexture, 0, 0, 12, 12);
    }

    private void createWeaponButtons() {
        final TextureRegion weaponIconsTexture = this.resources.textureAtlas.createSprite("weapon-icons");
        final TextureRegion weaponContainerTexture = this.resources.textureAtlas.createSprite("weapon-container");

        TextureRegion weaponContainerLoading = new TextureRegion(weaponContainerTexture, 0, 0, 37, 43);
        TextureRegion weaponContainerReady = new TextureRegion(weaponContainerTexture, 38, 0, 37, 43);

        Map<WeaponButton.WeaponIcon, TextureRegion> weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 0, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 0, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 0, 64, 31, 31));
        this.weaponButtons.put(WeaponType.BLAST, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.BLAST)));

        weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 32, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 32, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 32, 64, 31, 31));
        this.weaponButtons.put(WeaponType.GUN, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.GUN)));

        weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 64, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 64, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 64, 64, 31, 31));
        this.weaponButtons.put(WeaponType.BOMB, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.BOMB)));

        weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 96, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 96, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 96, 64, 31, 31));
        this.weaponButtons.put(WeaponType.LASER, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.LASER)));
    }

    private Rectangle getPosition(final WeaponType weaponType) {
        return new Rectangle(
                (weaponType == WeaponType.GUN || weaponType == WeaponType.BLAST ?
                        0 - this.gameData.width / 2f : this.gameData.width / 2f - BUTTON_SIZE),
                (weaponType == WeaponType.GUN || weaponType == WeaponType.LASER ?
                        0 - this.gameData.height / 2f : this.gameData.height / 2f - BUTTON_SIZE),
                BUTTON_SIZE, BUTTON_SIZE);
    }

    public void drawHud() {
        for (Map.Entry<WeaponType, WeaponButton> weaponTypeWeaponButtonEntry : weaponButtons.entrySet()) {
            if (this.gameData.weaponEnergies.containsKey(weaponTypeWeaponButtonEntry.getKey())) {
                weaponTypeWeaponButtonEntry.getValue().draw(this.resources.spriteBatch, this.gameData.weaponEnergies.get(weaponTypeWeaponButtonEntry.getKey()).getEnergy());
            }
        }

        this.drawPoints();
        this.drawCrystals();
    }

    private void drawPoints() {
        this.resources.spriteBatch.draw(this.miniScoreIcon, - this.gameData.width / 2 + 65, this.gameData.height / 2 - 31);
        BitmapFont whiteFont = this.resources.fonts.get(FontType.NORMAL);
        whiteFont.draw(this.resources.spriteBatch, "X" + String.format(new Locale("en"), "%.2f", this.gameEngine.getMultiplier()), - this.gameData.width / 2 + 80, this.gameData.height / 2 - 36);
        whiteFont.draw(this.resources.spriteBatch, String.valueOf(this.gameEngine.getScore()), - this.gameData.width / 2 + 80, this.gameData.height / 2 - 48);
    }

    private void drawCrystals() {
        this.resources.spriteBatch.draw(this.miniCrystalIcon, 30, this.gameData.height / 2 - 31);
        BitmapFont whiteFont = this.resources.fonts.get(FontType.NORMAL);
        whiteFont.draw(this.resources.spriteBatch, String.valueOf(this.gameEngine.getCrystals()), 46, this.gameData.height / 2 - 36);
    }

    public void updateInput(float x, float y) {
        for (WeaponType weaponType : WeaponType.values()) {
            if (this.weaponBounds.get(weaponType).contains(x, y)) {
                this.weaponHudHandler.weaponButtonClicked(weaponType);
            }
        }
    }
}
