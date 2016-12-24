package de.florianbuchner.trbd.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Map;

public class WeaponButton {

    public enum WeaponIcon {
        Ready,
        Empty,
        Loading
    }

    private final Map<WeaponIcon, TextureRegion> weaponTextures;
    private final Rectangle position;
    private final TextureRegion weaponContainerLoading;
    private final TextureRegion weaponContainerReady;

    public WeaponButton(Map<WeaponIcon, TextureRegion> weaponTextures, final TextureRegion weaponContainerReady, final TextureRegion weaponContainerLoading, final Rectangle position) {
        this.weaponTextures = weaponTextures;
        this.position = position;
        this.weaponContainerLoading = weaponContainerLoading;
        this.weaponContainerReady = weaponContainerReady;
    }

    public void draw(final SpriteBatch spriteBatch, final Float energy) {
        final boolean ready = energy != null && energy >= 1f;
        this.drawCentered(spriteBatch, weaponTextures.get(ready ? WeaponIcon.Ready : WeaponIcon.Empty));
        if (!ready) {
            this.drawCentered(spriteBatch, weaponTextures.get(WeaponIcon.Loading), energy != null ? energy : 0f);
        }
        this.drawCentered(spriteBatch, ready ? this.weaponContainerReady : this.weaponContainerLoading);
    }

    private void drawCentered(final SpriteBatch spriteBatch, final TextureRegion textureRegion) {
        spriteBatch.draw(textureRegion,
                this.position.x + (this.position.width - textureRegion.getRegionWidth()) / 2f,
                this.position.y + (this.position.height - textureRegion.getRegionHeight()) / 2f);
    }

    private void drawCentered(final SpriteBatch spriteBatch, final TextureRegion textureRegion, final float percentage) {
        final int height = Math.round(textureRegion.getRegionHeight() * percentage);
        final TextureRegion slicedRegion = new TextureRegion(textureRegion.getTexture(), textureRegion.getRegionX(), textureRegion.getRegionY() + textureRegion.getRegionHeight() - height, textureRegion.getRegionWidth(), height);
        spriteBatch.draw(slicedRegion,
                this.position.x + (this.position.width - textureRegion.getRegionWidth()) / 2f,
                this.position.y + (this.position.height - textureRegion.getRegionHeight()) / 2f);
    }
}
