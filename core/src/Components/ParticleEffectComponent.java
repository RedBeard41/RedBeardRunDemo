package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ParticleEffectComponent implements Component, Poolable {

    private PooledEffect particleEffect;
    private boolean isAttached = false;
    private float xOffset = 0;
    private float yOffset = 0;
    private float timeTillDeath = 0.25f;
    private boolean isDead = false;
    private Body attachedBody;
    private boolean isStarted = false;

    public PooledEffect getParticleEffect() {
        return particleEffect;
    }

    public void setParticleEffect(PooledEffect particleEffect) {
        this.particleEffect = particleEffect;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        if(started) {
            particleEffect.start();
            isStarted = started;
        }
        else{
            isStarted = started;
        }
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean attached) {
        isAttached = attached;
    }

    public float getxOffset() {
        return xOffset;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }

    public float getTimeTillDeath() {
        return timeTillDeath;
    }

    public void setTimeTillDeath(float timeTillDeath) {
        this.timeTillDeath = timeTillDeath;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Body getAttachedBody() {
        return attachedBody;
    }

    public void setAttachedBody(Body attachedBody) {
        this.attachedBody = attachedBody;
    }

    @Override
    public void reset() {
        particleEffect.free();
        particleEffect = null;
        xOffset = 0;
        yOffset = 0;
        isAttached = false;
        isDead = false;
        attachedBody = null;
        timeTillDeath = 0.25f;
        isStarted = false;

    }
}
