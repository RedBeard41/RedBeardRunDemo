package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BodyComponent implements Component, Poolable{

    private Body body;
    private boolean Dead = false;


    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isDead() {
        return Dead;
    }

    public void setDead(boolean dead) {
        Dead = dead;
    }

    public void setActive(boolean active){
        body.setActive(active);
    }

    public boolean isActive(){
        return body.isActive();
    }

    @Override
    public void reset() {
        body = null;
        Dead = false;

    }
}
