package de.florianbuchner.trbd.entity.component;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationComponent extends DrawingComponent {

    private Animation animation;

    private boolean looping;

    private float duration;


    public AnimationComponent(Animation animation, boolean looping) {
        this(animation, looping, 0F);
    }

    public AnimationComponent(Animation animation, boolean looping, float startTime) {
        super(animation.getKeyFrame(0f));
        this.animation = animation;
        this.looping = looping;
        this.duration = startTime;
    }

    public void update(float delta) {
        this.duration += delta;
        this.textureRegion = this.animation.getKeyFrame(this.duration, this.looping);
    }
}
