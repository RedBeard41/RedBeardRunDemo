package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EnemyComponent implements Component, Poolable {

    public enum TYPE{
        SKELETON, SNAKE,DRAGON
    }
    private boolean dead = false;
    private int bored = 0;
    private int aggressive = 0;
    private int maxAggressive = 0;
    private int health = 0;
    private int maxHealth = 0;
    private float shootDelay = 1.5f;
    private float timeSinceLastShot = 0f;
    private boolean playedDyingSound = false;
    private Array<Entity> attachedEntities = new Array<>();

    public Array<Entity> getAttachedEntities() {
        return attachedEntities;
    }

    public void setAttachedEntities(Array<Entity> attachedEntities) {
        this.attachedEntities = attachedEntities;
    }

    private TYPE type;

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxAggressive() {
        return maxAggressive;
    }

    public void setMaxAggressive(int maxAggressive) {
        this.maxAggressive = maxAggressive;
    }

    public float getShootDelay() {
        return shootDelay;
    }

    public void setShootDelay(float shootDelay) {
        this.shootDelay = shootDelay;
    }

    public float getTimeSinceLastShot() {
        return timeSinceLastShot;
    }

    public void setTimeSinceLastShot(float timeSinceLastShot) {
        this.timeSinceLastShot = timeSinceLastShot;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getBored() {
        return bored;
    }

    public void setBored(int bored) {
        this.bored = bored;
    }

    public void addAggressive(int amount){
        if(this.aggressive + amount >maxAggressive){
            this.aggressive = maxAggressive;
        }
        else if(this.aggressive + amount<0){
            this.aggressive = 0;
        }
        else {
            this.aggressive += amount;
        }
    }

    public int getAggressive() {
        return aggressive;
    }

    public void setAggressive(int aggressive) {
        this.aggressive = aggressive;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public void addHealth(float amount){
        if(this.health + amount >maxHealth){
            this.health = maxHealth;
        }
        else if(this.health + amount<0) {
            this.health = 0;
            //this.setDead(true);
        } else{
            this.health += amount;
        }
    }

    public boolean isPlayedDyingSound() {
        return playedDyingSound;
    }

    public void setPlayedDyingSound(boolean playedDyingSound) {
        this.playedDyingSound = playedDyingSound;
    }

    @Override
    public void reset() {
        bored = 0;
        aggressive = 0;
        health = 0;
        type = null;
        dead = false;
        shootDelay = 0.5f;
        timeSinceLastShot = 0f;
        playedDyingSound = false;
        maxAggressive = 0;
        maxHealth = 0;
        attachedEntities.clear();

    }
}
