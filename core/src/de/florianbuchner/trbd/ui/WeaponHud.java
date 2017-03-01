package de.florianbuchner.trbd.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.Resources;
import de.florianbuchner.trbd.core.WeaponType;

import java.util.HashMap;
import java.util.Map;

public class WeaponHud {

    public interface WeaponHudHandler {
        void weaponButtonClicked(WeaponType type);
    }

    public static float BUTTON_SIZE = 70;

    private Map<WeaponType, WeaponButton> weaponButtons = new HashMap<WeaponType, WeaponButton>(WeaponType.values().length);

    private Map<WeaponType, Rectangle> weaponBounds = new HashMap<WeaponType, Rectangle>(WeaponType.values().length);

    private GameData gameData;

    private Resources resources;

    private WeaponHudHandler weaponHudHandler;

    public WeaponHud(GameData gameData, Resources resources, WeaponHudHandler weaponHudHandler) {
        this.gameData = gameData;
        this.weaponHudHandler = weaponHudHandler;
        this.resources = resources;

        this.createWeaponButtons();

        this.weaponBounds.put(WeaponType.GUN, new Rectangle(-this.gameData.width / 2F , -gameData.height / 2F, WeaponHud.BUTTON_SIZE, WeaponHud.BUTTON_SIZE));
        this.weaponBounds.put(WeaponType.LASER, new Rectangle(this.gameData.width / 2F - WeaponHud.BUTTON_SIZE, -gameData.height / 2F, WeaponHud.BUTTON_SIZE, WeaponHud.BUTTON_SIZE));
        this.weaponBounds.put(WeaponType.BLAST, new Rectangle(-this.gameData.width / 2F , gameData.height / 2F - WeaponHud.BUTTON_SIZE, WeaponHud.BUTTON_SIZE, WeaponHud.BUTTON_SIZE));
        this.weaponBounds.put(WeaponType.BOMB, new Rectangle(this.gameData.width / 2F - WeaponHud.BUTTON_SIZE, gameData.height / 2F - WeaponHud.BUTTON_SIZE, WeaponHud.BUTTON_SIZE, WeaponHud.BUTTON_SIZE));
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
    }

    public void updateInput() {
        if (Gdx.input.isTouched()) {
            Vector3 projection = this.resources.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (WeaponType weaponType : WeaponType.values()) {
                if (this.weaponBounds.get(weaponType).contains(projection.x, projection.y)) {
                    this.weaponHudHandler.weaponButtonClicked(weaponType);
                }
            }
        }
    }
}
