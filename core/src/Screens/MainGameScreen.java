package Screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Components.BodyComponent;
import Components.DataComponent;
import Components.PlayerComponent;
import Components.PositionComponent;
import Components.SoundComponent;
import Components.TypeComponent;
import Helpers.CameraStyles;
import Helpers.GameInput;
import Helpers.Figures;
import Helpers.LevelCollisionGenerator;
import Helpers.Mappers;
import Helpers.PurchaseDialog;
import Managers.MyAssetManager;
import Managers.CollisionManager;
import Managers.EntityManager;
import Managers.PlayerHUD;
import Systems.AnimationSystem;
import Systems.B2bodyRenderSystem;
import Systems.BoundarySystem;
import Systems.CollisionSystem;
import Systems.ControlledMovementSystem;
import Systems.EnemySystem;
import Systems.InteractiveSystem;
import Systems.NPCSystem;
import Systems.ParticleEffectSystem;
import Systems.PositionSystem;
import Systems.ProjectileSystem;
import Systems.RenderSystem;
import Systems.RespawnSystem;
import Systems.SoundSystem;
import Systems.SteeringSystem;
import Systems.TouchRemovalSystem;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class MainGameScreen implements Screen {
    private static final String TAG = MainGameScreen.class.getSimpleName();

    public enum GAMESTATE {RUNNING, PAUSED}

    public static GAMESTATE gamestate;

    private RedBeardRun game;
    private MyAssetManager myAssetManager;
    private EntityManager entityManager;
    private Music song;

    private OrthogonalTiledMapRenderer mapRenderer = null;
    private TiledMap map;
    private OrthographicCamera gameCamera;
    private OrthographicCamera hudCamera;
    private Viewport gameViewport;
    private Viewport hudViewport;
    private Timer timer;

    public SpriteBatch batch;
    public GameInput gameInput;
    public GestureDetector gestureDetector;


    //Ashley ESC
    public PooledEngine engine;
    public ControlledMovementSystem controlledMovementSystem;
    public RenderSystem renderSystem;
    public AnimationSystem animationSystem;
    public PositionSystem positionSystem;
    public SteeringSystem steeringSystem;
    public CollisionSystem collisionSystem;
    public NPCSystem npcSystem;
    public EnemySystem enemySystem;
    public BoundarySystem boundarySystem;
    public RespawnSystem respawnSystem;
    public TouchRemovalSystem touchRemovalSystem;
    public InteractiveSystem interactiveSystem;
    public SoundSystem soundSystem;
    public ProjectileSystem projectileSystem;
    public ParticleEffectSystem particleEffectSystem;


    //Box2d debug Renderer for collision boundaries;
    public Box2DDebugRenderer box2DDebugRenderer;


    private LevelCollisionGenerator levelCollisionGenerator;


    public World world;
    public Entity player;
    public CollisionManager collisionManager;
    private Vector2 cameraTempVector;


    //HUD
    private PlayerHUD playerHUD;
    private InputMultiplexer multiplexer;
    private BitmapFont font;
   /* private Texture HUDtest;
    private Texture ScreenTest;

    public Stage hud;
    private Table hudBackground;
    private Table healthBarTable;
    private TextureRegion heartLeft;
    private TextureRegion heartRight;
    private int dragoonsCollected;
    private int potionsCollected;
    private float currentHealth;
    private BitmapFont hudFont;
    private Label dragoonAmount;
    private Label potionAmount;
    private Skin skin;
    private Dialog purchase;*/


    public MainGameScreen(RedBeardRun game, MyAssetManager myAssetManager) {
        this.game = game;
        this.myAssetManager = myAssetManager;
        engine = new PooledEngine(100, 1000,
                300, 1500);

        // skin = new Skin(Gdx.files.internal("Skins/neonskin/neon-ui.json"));


        // song  = myAssetManager.getMusicAsset("sounds/Credits song.wav");


        this.gamestate = GAMESTATE.PAUSED;

        init();

        initAshleyEntites();

        gameInput = new GameInput(gameViewport);


        gameInput.setPlayer(player);

        gestureDetector = new GestureDetector(gameInput);
        gestureDetector.setTapSquareSize(250);
        gestureDetector.setTapCountInterval(.25f);

        multiplexer = new InputMultiplexer();
        cameraTempVector = new Vector2(Vector2.Zero);
        timer = new Timer();

        initAshleySystems();




        playerHUD = new PlayerHUD(game,player, myAssetManager, gameViewport);
        collisionSystem.addObserver(playerHUD);
        playerHUD.addObserver(npcSystem);
        npcSystem.addObserver(playerHUD);
        multiplexer.addProcessor(playerHUD.getStage());
        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);

        box2DDebugRenderer = new Box2DDebugRenderer();


    }

    public void init() {
        gameCamera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();

        gameViewport = new FitViewport(Figures.VIRTUALWIDTH,
                Figures.VIRTUALHEIGHT, gameCamera);


        // hudViewport = new FitViewport(Figures.HUD_VIRTUAL_WIDTH,
        //   Figures.HUD_VIRTUAL_HEIGHT, hudCamera);


        // hudCamera.position.set(hudViewport.getWorldWidth()/2,hudViewport.getWorldHeight()/2,0);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        batch = game.getBatch();

        world = new World(new Vector2(Vector2.Zero), false);
        collisionManager = new CollisionManager();
        world.setContactListener(collisionManager);

        map = myAssetManager.getMapAsset("maps/Overworld.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, batch);

        /*engine = new PooledEngine(100,1000,
                300, 1500);*/

        entityManager = new EntityManager(game, world, myAssetManager, batch, engine);

        font = myAssetManager.getFontAsset("fonts/pixel_pirate.ttf");
        // hudFont = myAssetManager.getFontAsset("fonts/Pixeled English Font.ttf");

        // hud = new Stage(hudViewport); purchase = new Dialog("Purchase", skin);


        // setUpHud();
        // potionsCollected = 0;
        // dragoonsCollected = 0;

        // gamestate = GAMESTATE.RUNNING;

    }

    public void initAshleySystems() {


        steeringSystem = new SteeringSystem();
        controlledMovementSystem = new ControlledMovementSystem(game, gameInput, gestureDetector, world, entityManager, timer);
        renderSystem = new RenderSystem(batch, gameCamera);
        animationSystem = new AnimationSystem();
        positionSystem = new PositionSystem(world, engine, player);

        collisionSystem = new CollisionSystem(game, engine, world, entityManager, myAssetManager, player);
        npcSystem = new NPCSystem(game, player, world, entityManager,timer);
        enemySystem = new EnemySystem(player, world, timer, entityManager);
        //b2Render = new B2bodyRenderSystem(world, gameViewport);
        boundarySystem = new BoundarySystem(player);
        respawnSystem = new RespawnSystem(player, entityManager, map);
        //touchRemovalSystem = new TouchRemovalSystem();
        interactiveSystem = new InteractiveSystem();
        soundSystem = new SoundSystem(myAssetManager);
        projectileSystem = new ProjectileSystem();
        particleEffectSystem = new ParticleEffectSystem(engine, batch, gameCamera);


        engine.addSystem(animationSystem);
        engine.addSystem(steeringSystem);

        // engine.addSystem(boundarySystem);
        engine.addSystem(controlledMovementSystem);

        engine.addSystem(collisionSystem);
        engine.addSystem(npcSystem);
        engine.addSystem(enemySystem);
        engine.addSystem(interactiveSystem);
        engine.addSystem(respawnSystem);
        //engine.addSystem(touchRemovalSystem);
        engine.addSystem(positionSystem);
        engine.addSystem(projectileSystem);
        engine.addSystem(soundSystem);
        engine.addSystem(particleEffectSystem);
        engine.addSystem(renderSystem);

        //engine.addSystem(b2Render);


    }

    public void initAshleyEntites() {



            levelCollisionGenerator = new LevelCollisionGenerator(world, engine);
            levelCollisionGenerator.createPhysics(map);
            entityManager.spawnGroup(map, 8888);
            player = EntityManager.getPlayer();



        //entityManager.spawnEntities(map);

        //   entityManager.spawnGroup(map, 1);



    }

    public void reInitPlayer(){
        Body body = player.getComponent(BodyComponent.class).getBody();
        world.destroyBody(body);
        engine.removeEntity(player);
        entityManager.spawnEntity("Player", 8888,TypeComponent.COL_PLAYER,
                (int) game.loadData.getFloat("X", 128f),
                (int)game.loadData.getFloat("Y", 264f));
    }

    public void removeSystems(){
        engine.removeSystem(animationSystem);
        engine.removeSystem(steeringSystem);
        engine.removeSystem(controlledMovementSystem);

        engine.removeSystem(collisionSystem);
        engine.removeSystem(npcSystem);
        engine.removeSystem(enemySystem);
        engine.removeSystem(interactiveSystem);
        engine.removeSystem(respawnSystem);
        engine.removeSystem(touchRemovalSystem);
        engine.removeSystem(positionSystem);
        engine.removeSystem(projectileSystem);
        engine.removeSystem(soundSystem);
        engine.removeSystem(particleEffectSystem);
        engine.removeSystem(renderSystem);
    }

    public void removeEntities(){
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for(Body worldBody: bodies){
            world.destroyBody(worldBody);
        }
        engine.removeAllEntities();
    }


    @Override
    public void show() {
        Gdx.app.log(TAG, "Main Game Screen SHOW");



        //  HUDtest = new Texture("HUDScreenTest.png");
        // ScreenTest = new Texture("ScreenTest.png");


        //init();
        //entityManager.spawnGroup(map, 8888);


/*
        engine.removeSystem(animationSystem);
        engine.removeSystem(steeringSystem);
        engine.removeSystem(controlledMovementSystem);

        engine.removeSystem(collisionSystem);
        engine.removeSystem(npcSystem);
        engine.removeSystem(enemySystem);
        engine.removeSystem(interactiveSystem);
        engine.removeSystem(respawnSystem);
        engine.removeSystem(touchRemovalSystem);
        engine.removeSystem(positionSystem);
        engine.removeSystem(projectileSystem);
        engine.removeSystem(soundSystem);
        engine.removeSystem(particleEffectSystem);
        engine.removeSystem(renderSystem);

        engine.removeAllEntities();


        initAshleyEntites();
        initAshleySystems();


        gameInput.setPlayer(player);

        playerHUD = new PlayerHUD(game, player, myAssetManager, gameViewport);
        collisionSystem.addObserver(playerHUD);
        playerHUD.addObserver(npcSystem);
        npcSystem.addObserver(playerHUD);
        multiplexer.addProcessor(playerHUD.getStage());
        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);

        */
        box2DDebugRenderer = new Box2DDebugRenderer();



        //PurchaseDialog purchase = new PurchaseDialog("Purchase",skin);

        // song.setVolume(10);
        //song.setLooping(true);
        //song.play();

        if(!game.newGame){
            reInitPlayer();
        }

        if(game.newGame){
            collisionSystem.removeAllObservers();
            playerHUD.removeAllObservers();
            npcSystem.removeAllObservers();

            removeEntities();
            initAshleyEntites();

            removeSystems();
            initAshleySystems();

            collisionSystem.addObserver(playerHUD);
            playerHUD.addObserver(npcSystem);
            npcSystem.addObserver(playerHUD);

        }

        gamestate = GAMESTATE.RUNNING;
        playerHUD.show();
        Gdx.input.setInputProcessor(multiplexer);


    }


   /* private float getMapWidth() {
        MapProperties properties = map.getLayers().get("sldk").getProperties();
        //MapProperties properties = map.getProperties();
        int mapTileWidth = properties.get("width", Integer.class);
        int tilePixelWidth = properties.get("tilewidth", Integer.class);
        float mapPixelWidth = mapTileWidth * tilePixelWidth;
        return mapPixelWidth;
    }

    private float getMapHeight() {
        MapProperties properties = map.getProperties();
        int mapTileHeight = properties.get("height", Integer.class);
        int tilePixelHeight = properties.get("tileheight", Integer.class);
        float mapPixelHeight = mapTileHeight * tilePixelHeight;
        return mapPixelHeight;
    }*/

    public void update(float delta) {
        // gameCamera.position.set(player.getComponent(PositionComponent.class).getX(),player.getComponent(PositionComponent.class).getY(),0f);
        // camera.position.set(EntityManager.getPlayer().getComponent(PositionComponent.class).x,EntityManager.getPlayer().getComponent(PositionComponent.class).y,0f);

        //gameViewport.getCamera().update();
        // hudViewport.getCamera().update();


        CameraUpdate();
    }

    private void CameraUpdate() {

        float cameraX = player.getComponent(PlayerComponent.class).getCameraX();
        float cameraY = player.getComponent(PlayerComponent.class).getCameraY();
        float lerp = player.getComponent(PlayerComponent.class).getLerp();
        cameraTempVector.set(cameraX, cameraY);

        // Gdx.app.log(TAG, "Current Position "+ player.getComponent(PlayerComponent.class).getX()+", "+player.getComponent(PlayerComponent.class).getY());
        if (lerp == 1f) {

            CameraStyles.lerpToTarget(gameCamera, cameraTempVector, .1f);
            // Gdx.app.log(TAG,"Camera Position to set: ("+cameraX+", "+cameraY+")");
        } else {
            CameraStyles.lockOnTarget(gameCamera, cameraTempVector);
        }


        // dragoonsCollected = (int) player.getComponent(PlayerComponent.class).getDragoons();
        //  dragoonAmount.setText(": "+Integer.toString(dragoonsCollected));
        //potionsCollected = (int) player.getComponent(PlayerComponent.class);
      /*  currentHealth = player.getComponent(PlayerComponent.class).getHealth();
       // Gdx.app.log(TAG, "Player Health "+ currentHealth);

                for(int i = 0; i<120;i+=15){
                    if(currentHealth>i){
                        healthBarTable.getCells().get(i/15).getActor().setVisible(true);
                    }
                    else{
                        healthBarTable.getCells().get(i/15).getActor().setVisible(false);
                    }

                }
           // healthBarTable.getCells().get((int)(currentHealth/15)).getActor().setVisible(false);
*/

        gameCamera.update();
        //  hudCamera.update();


    }


    public void renderBottomTileLayers() {
        mapRenderer.getBatch().begin();
        for (int i = 0; i < 2; i++) {
            mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(i));

        }
        mapRenderer.getBatch().end();

    }

    public void renderTopTileLayers() {

        mapRenderer.getBatch().begin();
        for (int i = 2; i < 4; i++) {
            mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(i));

        }
        mapRenderer.getBatch().end();

    }


    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            Gdx.app.log(TAG, "Back key is pressed, RETURNING TO MAIN MENU");
            game.setScreen(RedBeardRun.SCREENTYPE.MENU);
        }


        //Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()*(11/16) );

        if (gamestate == GAMESTATE.RUNNING) {
            gameViewport.apply();

            mapRenderer.setView(gameCamera);
            mapRenderer.getBatch().enableBlending();
            mapRenderer.getBatch().setBlendFunction(
                    GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA
            );

            renderBottomTileLayers();
            // mapRenderer.render();

            // batch.setProjectionMatrix(gameCamera.combined);


            engine.update(delta);


            renderTopTileLayers();


            batch.setProjectionMatrix(gameCamera.combined);
            batch.begin();
            //font.draw(batch,Integer.toString(Gdx.graphics.getFramesPerSecond()),gameCamera.position.x,gameCamera.position.y);
            batch.end();


           // box2DDebugRenderer.render(world, gameCamera.combined.scl(Figures.PPM));

            playerHUD.render(delta);


            // hudViewport.apply();

           /* batch.setProjectionMatrix(hudCamera.combined);
            batch.begin();
            batch.draw(HUDtest, 0f, 0f);
            batch.end();*/

            //  purchase.text("hello").button("yes",true).button("No",false).show(hud);
            //  hud.act();
            // hud.draw();


        }


    }

    @Override
    public void resize(int width, int height) {
        // hudViewport.update(width,(height*5)/16);

        // hudViewport.setScreenBounds(Gdx.graphics.,height*(11/16),width,height*(5/16));

        //  gameViewport.update(width,(height*11)/16);
        gameViewport.update(width, height);
        // playerHUD.resize(width,height);
        //gameViewport.setScreenBounds(0,0,width,height*(11/16));

        //  hudViewport.setScreenY(hudViewport.getScreenHeight()+(height*6)/16);
        //gameViewport.getCamera().update();
        //hudViewport.getCamera().update();

        //  hudCamera.position.set(hudViewport.getWorldWidth()/2,hudViewport.getWorldHeight()/2,0);
        // gameCamera.position.set(gameViewport.getWorldWidth()/2,gameViewport.getWorldHeight()/2,0);
        //  hudCamera.update();
        //gameCamera.update();

    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "Main Game pause");
        gamestate = GAMESTATE.PAUSED;

    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "Main Game resume");
        gamestate = GAMESTATE.RUNNING;

        //  song.play();

    }

    @Override
    public void hide() {
        Gdx.app.log(TAG, "Main Game Hide");
        gamestate = GAMESTATE.PAUSED;
        Gdx.input.setInputProcessor(null);
        // song.pause();
        soundSystem.getDungeonSong().stop();
        soundSystem.getGameSong().stop();
        playerHUD.hide();



    }


    @Override
    public void dispose() {
        engine.clearPools();
        engine.removeAllEntities();

        //levelCollisionGenerator.dispose();
        // hud.clear();
//        hud.dispose();
        mapRenderer.dispose();
        Array <Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        Gdx.app.log("Main Game Screen","world Bodies: "+ bodies.size);
        for(Body body: bodies){
            if(!world.isLocked()){
                world.destroyBody(body);
            }

        }
        world.dispose();

        //song.dispose();

    }
}

   /* public void setUpHud(){
        *//*Pixmap pixmap = new Pixmap(hudViewport.getScreenWidth(),hudViewport.getScreenWidth(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));*//*
        Texture hudBackgroundImage = new Texture(Gdx.files.internal("9patch/hudBackground.9.png"));
        Texture mapBackgroundImage = new Texture(Gdx.files.internal("9patch/mapBackground.9.png"));
        Texture weaponBackgroundImage = new Texture(Gdx.files.internal("9patch/weaponBackground.9.png"));
        TextureRegion potion = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("potion");
        TextureRegion dragoon = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("Dragoon25");
        heartLeft =  myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("heartLeft");
        heartRight =  myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("heartRight");
        healthBarTable = new Table();
        Image potionImage = new Image(potion);
        potionImage.setSize(15,15);
        Image dragoonImage = new Image(dragoon);
        dragoonImage.setSize(15,15);




                NinePatchDrawable hudBackgrounds = new NinePatchDrawable(new NinePatch(
                new TextureRegion(hudBackgroundImage, 1, 1 ,
                        hudBackgroundImage.getWidth() - 2, hudBackgroundImage.getHeight() - 2)
                , 2, 2, 2, 2));

        NinePatchDrawable mapBackground =  new NinePatchDrawable(new NinePatch(
                new TextureRegion(mapBackgroundImage, 1, 1 ,
                        mapBackgroundImage.getWidth() - 2, mapBackgroundImage.getHeight() - 2)
                , 2, 2, 2, 2));

        NinePatchDrawable weaponBackground =  new NinePatchDrawable(new NinePatch(
                new TextureRegion(weaponBackgroundImage, 1, 1 ,
                        mapBackgroundImage.getWidth()-2 , mapBackgroundImage.getHeight()-2 )
                , 9, 9, 9, 9));

//powerLabel= new Label(String.format("%03d", power,"%"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//
        dragoonAmount = new Label(": "+Integer.toString(dragoonsCollected),new Label.LabelStyle(hudFont,Color.WHITE));
        potionAmount = new Label(": "+Integer.toString(potionsCollected),new Label.LabelStyle(hudFont,Color.WHITE));

        hudBackground = new Table();
        hudBackground.setDebug(true);
        hudBackground.setFillParent(true);
        hudBackground.setBackground(hudBackgrounds);

        Table map = new Table();

        //map.setFillParent(true);
        map.setBackground(mapBackground);

        Table inventory = new Table();
        //inventory.setFillParent(true);
        inventory.setBackground(mapBackground);

        healthBarTable = new Table();
        //healthBarTable.setFillParent(true);


        Table melee = new Table();
        //melee.setFillParent(true);
        melee.setBackground(mapBackground);

        Table range = new Table();
        //range.setFillParent(true);
        range.setBackground(mapBackground);

        healthBarTable.add(new HeartBarPart(heartLeft));
        healthBarTable.add(new HeartBarPart(heartRight)).padRight(2);
        healthBarTable.add(new HeartBarPart(heartLeft));
        healthBarTable.add(new HeartBarPart(heartRight)).padRight(2);
        healthBarTable.add(new HeartBarPart(heartLeft));
        healthBarTable.add(new HeartBarPart(heartRight)).padRight(2);
        healthBarTable.add(new HeartBarPart(heartLeft));
        healthBarTable.add(new HeartBarPart(heartRight)).padRight(2);



        inventory.add(potionImage).left().width(16);
        inventory.add(potionAmount).left().width(16);
        inventory.row();
        inventory.add(dragoonImage).width(16).left();
        inventory.add(dragoonAmount).width(16).left().expandX();
        inventory.row().left();
        inventory.add(healthBarTable).pad(0,2,4,2).colspan(2).prefWidth(80);



        map.add().fill();

       // melee.add().fill();

        range.add().fill();



        hudBackground.add(map).width(120).fillY().pad(4);
        hudBackground.add(inventory).expandX().fillY().pad(4,0,4,0);
        hudBackground.add(melee).width(32).fillY().pad(4);
        //hudBackground.add(range).width(16).fillY().pad(4,0,4,4);















        hud.addActor(hudBackground);


*/












       // pixmap.dispose();








/*class HeartBarPart extends Actor {


    private TextureRegion image;


    public HeartBarPart(TextureRegion image) {
        super();
        this.image = image;

        this.setWidth(8);
        this.setHeight(16);
        this.setVisible(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(image, getX(), getY(), 8, 16);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void act(float delta) {
        super.act(delta);


    }
}*/
