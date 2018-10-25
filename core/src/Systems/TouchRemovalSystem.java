package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import Components.BodyComponent;
import Components.TouchComponent;
import Helpers.Mappers;
import Managers.EntityManager;

public class TouchRemovalSystem extends IteratingSystem {


    public TouchRemovalSystem() {
        super(Family.all(TouchComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TouchComponent touchComponent = Mappers.touch.get(entity);
        BodyComponent body = Mappers.body.get(entity);

        if(touchComponent.isDead()){
            body.setDead(true);
        }
    }
}
