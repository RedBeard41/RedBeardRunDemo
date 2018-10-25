package Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Components.CollisionComponent;

public class CollisionManager implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("Contact","");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Gdx.app.log(fa.getBody().getType().toString()+" has hit "+ fb.getBody().getType().toString(),"");

        if(fa.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fa.getBody().getUserData();
            entityCollision(ent, fb);
            return;
        }
        else if(fb.getBody().getUserData() instanceof Entity){
            Entity ent = (Entity) fb.getBody().getUserData();
            entityCollision(ent, fa);
        }

    }

    private void entityCollision(Entity ent, Fixture fixture){
        if(fixture.getBody().getUserData() instanceof Entity){
            Entity colEnt = (Entity) fixture.getBody().getUserData();

            CollisionComponent col = ent.getComponent(CollisionComponent.class);
            CollisionComponent colb = colEnt.getComponent(CollisionComponent.class);

            if(col != null){
                col.setCollisionEntity(colEnt);
            }
            else if(colb != null){
                colb.setCollisionEntity(ent);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("Contact End","");

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
