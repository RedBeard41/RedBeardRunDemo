package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class NPCComponent implements Component, Poolable{

    public enum TYPE{
        STATIC, ROVING
    }

    private TYPE type;
    private boolean dead = false;
    private int health = 0;
    private int maxHealth = 0;
    private boolean playedTalkingSound = false;
    private boolean playedLeavingSound = false;

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

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
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

    public boolean isPlayedTalkingSound() {
        return playedTalkingSound;
    }

    public void setPlayedTalkingSound(boolean playedTalkingSound) {
        this.playedTalkingSound = playedTalkingSound;
    }

    public boolean isPlayedLeavingSound() {
        return playedLeavingSound;
    }

    public void setPlayedLeavingSound(boolean playedLeavingSound) {
        this.playedLeavingSound = playedLeavingSound;
    }

    @Override
    public void reset() {
    type = null;
    health = 0;
    dead = false;
    playedLeavingSound = false;
    playedTalkingSound = false;
    maxHealth = 0;
    }
}
