package de.florianbuchner.trbd.entity.component;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationComponent extends DrawingComponent {

    private final Animation animation;

    private final boolean looping;

    private float duration;

    public AnimationComponent(Animation animation, boolean looping) {
        super(animation.getKeyFrame(0f));
        this.animation = animation;
        this.looping = looping;
        this.duration = 0f;
    }

    public void update(float delta) {
        this.duration += delta;
        this.textureRegion = this.animation.getKeyFrame(this.duration, this.looping);
    }
}
