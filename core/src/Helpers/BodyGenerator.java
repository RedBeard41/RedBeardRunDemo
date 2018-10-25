package Helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class BodyGenerator {

    private static final String TAG = BodyGenerator.class.getSimpleName();
    private World world;

    public BodyGenerator(World world) {
        this.world = world;
    }

    public Body generateBody(Entity owner, float x, float y, float dimension, FileHandle handle, FixtureDef fdef){
        return bodyHelper(owner, new Vector2(x, y), new Vector2(dimension, dimension), handle, fdef);
    }

    public Body generateBody(Entity owner, float x, float y, float dimension, FixtureDef fdef){
        return bodyHelper(owner, new Vector2(x,y), new Vector2(dimension, dimension), fdef);
    }

    public Body generateArrow(Entity owner, float x, float y, float width, float height, float xOffset, float yOffset, FixtureDef fdef){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 0;

        bodyDef.position.set((x+width/2)/Figures.PPM, (y + height/2 )/Figures.PPM);

        body = world.createBody(bodyDef);
        Shape shape = new PolygonShape();
        ((PolygonShape)shape).setAsBox(width/2/Figures.PPM,height/2/Figures.PPM,
                new Vector2(body.getLocalCenter().x+(xOffset/Figures.PPM),
                        body.getLocalCenter().y+(yOffset/Figures.PPM)),0f);
        FixtureDef fixtureDef = fdef;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(owner);
        shape.dispose();

        return body;


    }

    public Body generateBrick(Entity owner, float x, float y, float width, float height, float xOffset, float yOffset, FixtureDef fdef){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.bullet = false;
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 0;

        bodyDef.position.set((x+width/2)/Figures.PPM, (y + height/2 )/Figures.PPM);

        body = world.createBody(bodyDef);
        Shape shape = new PolygonShape();
        ((PolygonShape)shape).setAsBox(width/2/Figures.PPM,height/2/Figures.PPM,
                new Vector2(body.getLocalCenter().x+xOffset/Figures.PPM,
                        body.getLocalCenter().y+yOffset/Figures.PPM),0f);
        FixtureDef fixtureDef = fdef;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(owner);
        shape.dispose();

        return body;


    }

    public Body generateSword(Entity owner, float x, float y, float width, float height, float xOffset, float yOffset, FixtureDef fdef){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 0;

        bodyDef.position.set((x+width/2)/Figures.PPM, (y + height/2 )/Figures.PPM);

        body = world.createBody(bodyDef);
        Shape shape = new PolygonShape();
        ((PolygonShape)shape).setAsBox(width/2/Figures.PPM,height/2/Figures.PPM,
                new Vector2(body.getLocalCenter().x+xOffset/Figures.PPM,
                        body.getLocalCenter().y+yOffset/Figures.PPM),0f);
        FixtureDef fixtureDef = fdef;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(owner);
        shape.dispose();

        return body;


    }

    private Body bodyHelper(Entity owner, Vector2 position, Vector2 dimensions, FixtureDef fdef){
        Body body;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = false;
        bodyDef.fixedRotation = true;
        bodyDef.gravityScale = 0;

        bodyDef.position.set((position.x+dimensions.x/2)/Figures.PPM,
                (position.y + dimensions.y/2 )/Figures.PPM);

        body = world.createBody(bodyDef);
        Shape shape = new CircleShape();
        shape.setRadius(dimensions.x/Figures.PPM);
        ((CircleShape)shape).setPosition(new Vector2(
                (body.getLocalCenter().x /Figures.PPM),
                (body.getLocalCenter().y /Figures.PPM)));

       // FixtureDef fixtureDef = new FixtureDef();
        FixtureDef fixtureDef = fdef;
        fixtureDef.shape = shape;
        //fixtureDef.isSensor = true;
        //fixtureDef.density = 1f;



        body.createFixture(fixtureDef).setUserData(owner);
        shape.dispose();


        return body;
    }

   private Body bodyHelper(Entity owner, Vector2 position, Vector2 dimensions, FileHandle handle, FixtureDef fdef){
        Body body;

        String rawJson = handle.readString();
        JsonReader jsonReader = new JsonReader();
        JsonValue root = jsonReader.parse(rawJson);

       /* short maskingBits = (short) ((PhysicsManager.FRIENDLY_BITS |
                PhysicsManager.ENEMY_BITS | PhysicsManager.NEUTRAL_BITS |
                PhysicsManager.LEVEL_BITS) ^ filterCategory);*/

        BodyDef bodyDef = new BodyDef();

        String bodytype = root.get("BodyDef").getString("type");

        if(bodytype.equalsIgnoreCase("DynamicBody")){
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }
        else if(bodytype.equalsIgnoreCase("KinematicBody")){
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        }
        else if(bodytype.equalsIgnoreCase("StaticBody")){
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        else{
            //Gdx.app.log(TAG, "Entity Box2d body type undefined: "+ filterCategory);
        }
        JsonValue jsonBody = root.get("BodyDef");

        bodyDef.bullet = jsonBody.getBoolean("bullet");
        bodyDef.fixedRotation = jsonBody.getBoolean("fixedRotation");
        bodyDef.gravityScale = jsonBody.getFloat("gravityScale");

        bodyDef.position.set((position.x+dimensions.x/2)/Figures.PPM,
                (position.y + dimensions.y/2 )/Figures.PPM);

        body = world.createBody(bodyDef);

        JsonValue fixtures = root.get("Fixtures");
        for(JsonValue fixture: fixtures){
            String fixtureType = fixture.getString("type");
            Shape shape;

            if(fixtureType.equalsIgnoreCase("PolygonShape")){
                shape = new PolygonShape();
                ((PolygonShape) shape)
                        .setAsBox(fixture.getFloat("width")/2/Figures.PPM,
                                fixture.getFloat("height")/2/Figures.PPM,
                                new Vector2((body.getLocalCenter().x +fixture.getFloat("x"))/Figures.PPM,
                                        (body.getLocalCenter().y + fixture.getFloat("y"))/Figures.PPM),0f);
            }
            else if(fixtureType.equalsIgnoreCase("CircleShape")){
                shape = new CircleShape();
                shape.setRadius(fixture.getFloat("radius")/Figures.PPM);
                ((CircleShape)shape).setPosition(new Vector2(
                        (body.getLocalCenter().x + fixture.getFloat("x"))/Figures.PPM,
                        (body.getLocalCenter().y + fixture.getFloat("y"))/Figures.PPM
                ));
            }
            else {
                Gdx.app.log(TAG, "Generated body shape was invalid: " + fixtureType);
                continue;
            }

            boolean isSensor = fixture.getBoolean("isSensor");

            //FixtureDef fixtureDef = new FixtureDef();
            FixtureDef fixtureDef = fdef;
            fixtureDef.shape = shape;
            fixtureDef.isSensor = isSensor;
            fixtureDef.density = fixture.getFloat("density");
            if(isSensor){
                //fixtureDef.filter.categoryBits = (short) (filterCategory << fixture.getShort("bitShifts"));
            }
            else{
                fixtureDef.friction = fixture.getFloat("friction");
               /* fixtureDef.filter.categoryBits = filterCategory;
                fixtureDef.filter.maskBits  = maskingBits;*/

            }

            body.createFixture(fixtureDef).setUserData(owner);
            shape.dispose();

        }
        return body;
    }
}

