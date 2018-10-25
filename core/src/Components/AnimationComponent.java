package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Pool.Poolable;

public class AnimationComponent implements Component, Poolable {

    public enum ANIMATIONSTATE{
        UP,DOWN,LEFT,RIGHT,TALKING,LEAVING,IMMOBILE, ENTERING, ATTACKING, HIT, PREPARING_TO_ATTACKING,FORCE_PUSH
    }
    private float time = 0.0f;
    private boolean isLooping = false;

    private ArrayMap<ANIMATIONSTATE, Animation> animations=
            new ArrayMap<ANIMATIONSTATE, Animation>();

    public AnimationComponent addAnimation(ANIMATIONSTATE stateName, Animation animation){
        this.animations.put(stateName,animation);
        return this;
    }
    public Animation getAnimation(ANIMATIONSTATE stateName){
        return animations.get(stateName);
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setLooping(boolean isLooping){
        this.isLooping = isLooping;

    }

    public boolean isLooping() {
        return isLooping;
    }

    @Override
    public void reset() {
        time = 0.0f;
        animations.clear();
        isLooping = false;

    }
}
