package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DrawingComponent implements Component {
    public TextureRegion textureRegion;
    public Vector2 textureOffset;

    public DrawingComponent(Texture texture) {
        this(new TextureRegion(texture));
    }

    public DrawingComponent(TextureRegion textureRegion) {
        this(textureRegion, new Vector2(-textureRegion.getRegionWidth() / 2, -textureRegion.getRegionHeight() / 2));
    }

    public DrawingComponent(TextureRegion textureRegion, Vector2 textureOffset) {
        this.textureRegion = textureRegion;
        this.textureOffset = textureOffset;
    }
}
