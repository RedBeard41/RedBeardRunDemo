package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;



public class StateComponent implements Component, Poolable{

    public enum DIRECTION {
        UP,DOWN,LEFT,RIGHT,NONE
    }

    public enum STATE {
        IDLE, MOVING, TALKING, LEAVING, IMMOBILE, ATTACK, HIT, PREPARE_TO_ATTACK, ENTERING,FORCE_PUSH, PAUSE
    }

    private STATE state;
    private DIRECTION direction;


    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public void setDirection(DIRECTION direction){
        this.direction = direction;

    }

    public DIRECTION getDirection(){
        return direction;
    }



    @Override
    public void reset() {
        state = null;
        direction = null;



    }
}
