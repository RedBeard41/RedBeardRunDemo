package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerControlComponent implements Component, Poolable {

    public enum CONTROL{
        TAP, DOUBLETAP, HOLD, RANGE, NONE
    }

    private int tapCount = 0;
    private float timeBetweenRangeAttacks = 0.5f;
    private float timeBetweenMeleeAttacks = 0.3f;
    private float timeSinceLastRangeAttack = 0.0f;
    private float timeSinceLastMeleeAttack = 0.0f;
    private boolean inputProcessed = false;
    private CONTROL control;


    public CONTROL getControl() {
        return control;
    }

    public void setControl(CONTROL control) {
        this.control = control;
    }

    public int getTapCount() {
        return tapCount;
    }

    public void setTapCount(int tapCount) {
        this.tapCount = tapCount;
    }

    public boolean isInputProcessed() {
        return inputProcessed;
    }

    public void setInputProcessed(boolean inputProcessed) {
        this.inputProcessed = inputProcessed;
    }

    public float getTimeBetweenRangeAttacks() {
        return timeBetweenRangeAttacks;
    }

    public void setTimeBetweenRangeAttacks(float timeBetweenRangeAttacks) {
        this.timeBetweenRangeAttacks = timeBetweenRangeAttacks;
    }

    public float getTimeBetweenMeleeAttacks() {
        return timeBetweenMeleeAttacks;
    }

    public void setTimeBetweenMeleeAttacks(float timeBetweenMeleeAttacks) {
        this.timeBetweenMeleeAttacks = timeBetweenMeleeAttacks;
    }

    public float getTimeSinceLastRangeAttack() {
        return timeSinceLastRangeAttack;
    }

    public void setTimeSinceLastRangeAttack(float timeSinceLastRangeAttack) {
        this.timeSinceLastRangeAttack = timeSinceLastRangeAttack;
    }

    public float getTimeSinceLastMeleeAttack() {
        return timeSinceLastMeleeAttack;
    }

    public void setTimeSinceLastMeleeAttack(float timeSinceLastMeleeAttack) {
        this.timeSinceLastMeleeAttack = timeSinceLastMeleeAttack;
    }

    @Override
    public void reset() {
     tapCount = 0;
     inputProcessed = false;
     control = null;
     timeBetweenMeleeAttacks = 0.5f;
     timeBetweenRangeAttacks = 0.6f;
     timeSinceLastMeleeAttack = 0.0f;
     timeSinceLastRangeAttack = 0.0f;
    }
}
