package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class InteractiveComponent implements Component, Poolable{

    public enum TYPE {
        DRAGOON10, DRAGOON25, POTION, LITESWORD, HEAVYSWORD, BOW, LSWORD, HSWORD, ARROW, HEART, TOUCH, PORTAL, DESTROYABLE, ORB, BRICK
    }

    private TYPE type;
    private boolean dead = false;

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

    @Override
    public void reset() {
        type = null;
        dead = false;

    }
}
