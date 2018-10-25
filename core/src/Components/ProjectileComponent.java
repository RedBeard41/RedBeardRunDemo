package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ProjectileComponent implements Component, Poolable {
    private float xVel = 0;
    private float yVel = 0;
    private boolean isDead = false;
    private Entity particleEffect;
    private float originX;
    private float originY;

    public float getOriginX() {
        return originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getOriginY() {
        return originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public Entity getParticleEffect() {
        return particleEffect;
    }

    public void setParticleEffect(Entity particleEffect) {
        this.particleEffect = particleEffect;
    }



    public float getxVel() {
        return xVel;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    @Override
    public void reset() {
        xVel = 0;
        yVel = 0;
        isDead = false;
        particleEffect = null;
        originX = 0;
        originY = 0;


    }
}
