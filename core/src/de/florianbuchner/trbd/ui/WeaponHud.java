package de.florianbuchner.trbd.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import de.florianbuchner.trbd.core.GameData;
import de.florianbuchner.trbd.core.WeaponType;

import java.util.HashMap;
import java.util.Map;

public class WeaponHud {

    public static float BUTTON_SIZE = 70;

    private Map<WeaponType, WeaponButton> weaponButtons = new HashMap<WeaponType, WeaponButton>(WeaponType.values().length);

    private GameData gameData;

    public WeaponHud(final GameData gameData) {
        this.gameData = gameData;

        this.createWeaponButtons();
    }

    private void createWeaponButtons() {
        final Texture weaponIconsTexture = new Texture(Gdx.files.internal("weapon-icons.png"));
        final Texture weaponContainerTexture = new Texture(Gdx.files.internal("weapon-container.png"));

        TextureRegion weaponContainerLoading = new TextureRegion(weaponContainerTexture, 0, 0, 37, 43);
        TextureRegion weaponContainerReady = new TextureRegion(weaponContainerTexture, 38, 0, 37, 43);

        Map<WeaponButton.WeaponIcon, TextureRegion> weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 0, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 0, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 0, 64, 31, 31));
        this.weaponButtons.put(WeaponType.Blast, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.Blast)));

        weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 32, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 32, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 32, 64, 31, 31));
        this.weaponButtons.put(WeaponType.Gun, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.Gun)));

        weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 64, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 64, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 64, 64, 31, 31));
        this.weaponButtons.put(WeaponType.Bomb, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.Bomb)));

        weaponIconMap = new HashMap<WeaponButton.WeaponIcon, TextureRegion>(WeaponButton.WeaponIcon.values().length);
        weaponIconMap.put(WeaponButton.WeaponIcon.Ready, new TextureRegion(weaponIconsTexture, 96, 0, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Loading, new TextureRegion(weaponIconsTexture, 96, 32, 31, 31));
        weaponIconMap.put(WeaponButton.WeaponIcon.Empty, new TextureRegion(weaponIconsTexture, 96, 64, 31, 31));
        this.weaponButtons.put(WeaponType.Laser, new WeaponButton(weaponIconMap, weaponContainerReady, weaponContainerLoading, this.getPosition(WeaponType.Laser)));
    }

    private Rectangle getPosition(final WeaponType weaponType) {
        return new Rectangle(
                (weaponType == WeaponType.Gun || weaponType == WeaponType.Blast ?
                        0 - this.gameData.width / 2f : this.gameData.width / 2f - BUTTON_SIZE),
                (weaponType == WeaponType.Gun || weaponType == WeaponType.Laser ?
                        0 - this.gameData.height / 2f : this.gameData.height / 2f - BUTTON_SIZE),
                BUTTON_SIZE, BUTTON_SIZE);
    }

    public void drawHud() {
        for (Map.Entry<WeaponType, WeaponButton> weaponTypeWeaponButtonEntry : weaponButtons.entrySet()) {
            if (this.gameData.weaponEnergies.containsKey(weaponTypeWeaponButtonEntry.getKey())) {
                weaponTypeWeaponButtonEntry.getValue().draw(this.gameData.spriteBatch, this.gameData.weaponEnergies.get(weaponTypeWeaponButtonEntry.getKey()).getEnergy());
            }
        }
    }

}
