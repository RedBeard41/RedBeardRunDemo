package Helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import Components.BodyComponent;
import Components.DataComponent;
import Components.InteractiveComponent;
import Components.PositionComponent;
import Components.TypeComponent;

public class LevelCollisionGenerator {
    public static final String TAG = LevelCollisionGenerator.class.getSimpleName();
    private World world;
    private PooledEngine engine;
    private Array<Body> bodies = new Array<Body>();
    private Array<Entity>entities = new Array();
    private static final String MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER";
    private static final String MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER";
    private static final String INTERACTIVE_LAYER = "INTERACTIVE_LAYER";
    private TiledMap map;
    public LevelCollisionGenerator(World world, PooledEngine engine) {
        this.world = world;
        this.engine = engine;
    }

    public void createPhysics(TiledMap map){
        this.map = map;
        //getting collision layer information
        MapLayer layer = map.getLayers().get(MAP_COLLISION_LAYER);

        for(MapObject object: layer.getObjects()){
            LevelGeometry geometry = null;

            if(object instanceof TextureMapObject){
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.awake = false;
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if(object instanceof RectangleMapObject){
                geometry = getRectangle((RectangleMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof PolylineMapObject){
                geometry = getPolyline((PolylineMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof PolygonMapObject){
                geometry = getPolygon((PolygonMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof CircleMapObject){
                geometry = getCircle((CircleMapObject)object);
                shape = geometry.getShape();
            }

            else{
                Gdx.app.log(TAG, "Unrecognized map shape "+ object.toString());
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = TypeComponent.COL_LEVEL;
            fixtureDef.filter.maskBits = TypeComponent.COL_PLAYER|TypeComponent.COL_FRIENDLY|
                    TypeComponent.COL_ENEMY|TypeComponent.COL_INTERACTIVE;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);

            Entity levelEntity = engine.createEntity();
            TypeComponent type = engine.createComponent(TypeComponent.class);
            type.setType(TypeComponent.COL_LEVEL);
            levelEntity.add(type);

            PositionComponent position = engine.createComponent(PositionComponent.class);
            position.setX(body.getLocalCenter().x);
            position.setY(body.getLocalCenter().y);
            levelEntity.add(position);

            body.createFixture(fixtureDef);
            BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
            bodyComponent.setBody(body);
            bodyComponent.getBody().setUserData(levelEntity);
            levelEntity.add(bodyComponent);




            engine.addEntity(levelEntity);
            entities.add(levelEntity);
            bodies.add(body);
            fixtureDef.shape=null;
            shape.dispose();




        }

        //getting Portal layer information

        layer = map.getLayers().get(MAP_PORTAL_LAYER);

        for(MapObject object: layer.getObjects()){
            LevelGeometry geometry = null;

            if(object instanceof TextureMapObject){
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.awake = false;
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if(object instanceof RectangleMapObject){
                geometry = getRectangle((RectangleMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof PolylineMapObject){
                geometry = getPolyline((PolylineMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof PolygonMapObject){
                geometry = getPolygon((PolygonMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof CircleMapObject){
                geometry = getCircle((CircleMapObject)object);
                shape = geometry.getShape();
            }

            else{
                Gdx.app.log(TAG, "Unrecognized map shape "+ object.toString());
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;


            fixtureDef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
            fixtureDef.filter.maskBits = TypeComponent.COL_PLAYER ;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);



            Entity portalEntity = engine.createEntity();
            TypeComponent type = engine.createComponent(TypeComponent.class);
            type.setType(TypeComponent.COL_INTERACTIVE);
            portalEntity.add(type);

            PositionComponent position = engine.createComponent(PositionComponent.class);
            position.setX(body.getLocalCenter().x);
            position.setY(body.getLocalCenter().y);
            portalEntity.add(position);

            body.createFixture(fixtureDef);
            BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
            bodyComponent.setBody(body);
            bodyComponent.getBody().setUserData(portalEntity);
            portalEntity.add(bodyComponent);






            DataComponent dataComponent = engine.createComponent(DataComponent.class);
            if(object.getProperties().get("Area", Float.class)!=null) {
                dataComponent.setData("area", object.getProperties().get("Area", Float.class));
            }

            dataComponent.setData("cameraX", object.getProperties().get("cameraX", Float.class));
            dataComponent.setData("cameraY", object.getProperties().get("cameraY", Float.class));
            dataComponent.setData("x", object.getProperties().get("X", Float.class));
            dataComponent.setData("y", object.getProperties().get("Y", Float.class));
            dataComponent.setData("current", object.getProperties().get("current", Float.class));
            dataComponent.setData("past", object.getProperties().get("past", Float.class));
            dataComponent.setData("lerp", object.getProperties().get("lerp", Float.class));


            portalEntity.add(dataComponent);

            InteractiveComponent interactiveComponent = engine.createComponent(InteractiveComponent.class);
            interactiveComponent.setType(InteractiveComponent.TYPE.PORTAL);

            portalEntity.add(interactiveComponent);

            engine.addEntity(portalEntity);
            entities.add(portalEntity);
            bodies.add(body);
            fixtureDef.shape=null;
            shape.dispose();




        }


       /* //For Interactive Items
        layer = map.getLayers().get(INTERACTIVE_LAYER);

        for(MapObject object: layer.getObjects()){
            LevelGeometry geometry = null;
            String interactiveType = object.getProperties().get("type", String.class);

            if(object instanceof TextureMapObject){
                continue;
            }

            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.awake = false;
            bodyDef.type = BodyDef.BodyType.StaticBody;

            if(object instanceof RectangleMapObject){
                geometry = getRectangle((RectangleMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof PolylineMapObject){
                geometry = getPolyline((PolylineMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof PolygonMapObject){
                geometry = getPolygon((PolygonMapObject)object);
                shape = geometry.getShape();
            }

            else if (object instanceof CircleMapObject){
                geometry = getCircle((CircleMapObject)object);
                shape = geometry.getShape();
            }

            else{
                Gdx.app.log(TAG, "Unrecognized map shape "+ object.toString());
                continue;
            }

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = false;
            fixtureDef.filter.categoryBits = TypeComponent.COL_DESTROYABLE;
            fixtureDef.filter.maskBits =  TypeComponent.COL_WEAPON | TypeComponent.COL_PLAYER;

            *//*fixtureDef.filter.categoryBits = TypeComponent.COL_PORTAL;
            fixtureDef.filter.maskBits = (short) (TypeComponent.COL_PLAYER);*//*

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);



            Entity interactiveEntity = engine.createEntity();
            TypeComponent type = engine.createComponent(TypeComponent.class);

            if(interactiveType.equalsIgnoreCase("plant")){
                type.setType(TypeComponent.COL_DESTROYABLE);
            }


            interactiveEntity.add(type);

            PositionComponent position = engine.createComponent(PositionComponent.class);
            position.setX(body.getLocalCenter().x);
            position.setY(body.getLocalCenter().y);
            interactiveEntity.add(position);

            body.createFixture(fixtureDef);
            BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
            bodyComponent.setBody(body);
            bodyComponent.getBody().setUserData(interactiveEntity);
            interactiveEntity.add(bodyComponent);


            engine.addEntity(interactiveEntity);
            entities.add(interactiveEntity);
            bodies.add(body);
            fixtureDef.shape=null;
            shape.dispose();
*/



      //  }
    }

    private LevelGeometry getRectangle(RectangleMapObject rectangleMapObject){
        Rectangle rectangle = rectangleMapObject.getRectangle();
        PolygonShape polygon = new PolygonShape();

        Vector2 size = new Vector2((rectangle.x+rectangle.width*0.5f)/Figures.PPM,
                (rectangle.y+rectangle.height*0.5f)/Figures.PPM);
        polygon.setAsBox(rectangle.width*0.5f/Figures.PPM,
                rectangle.height*0.5f/Figures.PPM,size,0.0f);

        return new LevelGeometry(polygon);
    }

    private LevelGeometry getPolygon(PolygonMapObject polygonMapObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            worldVertices[i] = vertices[i] / Figures.PPM;
        }
        polygon.set(worldVertices);
        return new LevelGeometry(polygon);
    }

    private LevelGeometry getPolyline(PolylineMapObject polylineMapObject) {

        float[] vertices = polylineMapObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length/2];

        for (int i = 0; i < vertices.length/2; i++) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / Figures.PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / Figures.PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);

        return new LevelGeometry(chain);
    }

    private LevelGeometry getCircle(CircleMapObject circleMapObject){
        Circle circle = circleMapObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius/Figures.PPM);
        circleShape.setPosition(new Vector2(circle.x/Figures.PPM,circle.y/Figures.PPM));
        return new LevelGeometry(circleShape);
    }


    public void dispose() {
        /*for(Body body: bodies){
            world.destroyBody(body);
        }*/
        // Gdx.app.log("Number of collision entities",""+entities.size);
        for(Entity entity: entities){
            BodyComponent bodyComponent = entity.getComponent(BodyComponent.class);
            if(bodyComponent.getBody()!=null) {
                world.destroyBody(bodyComponent.getBody());
            }
            engine.removeEntity(entity);

            //Gdx.app.log("Number of collision entities",""+entities.size);
        }
        entities.clear();
        // Gdx.app.log("Number of collision entities",""+entities.size);
    }


    public static class LevelGeometry{
        private Shape shape;

        public LevelGeometry(Shape shape) {
            this.shape = shape;
        }

        public Shape getShape(){
            return shape;
        }
    }
}

