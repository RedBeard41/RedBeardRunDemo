package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Managers.MyAssetManager;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class LoadingScreen implements Screen {
    public static final String TAG = LoadingScreen.class.getSimpleName();

    private RedBeardRun game;
    private MyAssetManager myAssetManager;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private TextureRegion title;
    private Animation walkRight;
    private Stage stage;
    private Music music;
    private BitmapFont font;
    private String loadingFile;

    public final int IMAGE = 0;
    public final int FONT = 1;
    public final int SOUND = 2;
    public final int MUSIC = 3;
    public final int MAP = 4;
    public final int PARTICLE = 5;

    private int currentLoadingStage = 1;

    //timer for exiting loading screen
    public float countDown = 4f;
    private float stateTime;
    private TextureAtlas.AtlasRegion dash;
    private Table table;
    private Image titleImage;
    private Table loadingTable;
    private float volume;

    public LoadingScreen(RedBeardRun game, MyAssetManager myAssetManager) {
        this.game = game;
        this.myAssetManager = myAssetManager;

        stage = new Stage(new ScreenViewport());

        loadLoadingAssets();

        music = myAssetManager.getMusicAsset("sounds/Loading Screen Song.wav");
        font = myAssetManager.getFontAsset("fonts/pixel_pirate.ttf");
        batch = game.getBatch();
        volume = .8f;

    }

    private void loadLoadingAssets(){

        myAssetManager.loadingLoadingTextureAtlasAsset("sprites/Output/loading.atlas");
        myAssetManager.loadingMusicAsset("sounds/Loading Screen Song.wav");
        myAssetManager.loadingLoadingFontAsset("fonts/pixel_pirate.ttf");



        //get loading images
        atlas = myAssetManager.getTextureAsset("sprites/Output/loading.atlas");
        title = atlas.findRegion("RedBeard Run title");
        dash = atlas.findRegion("dash");

        walkRight = new Animation (0.25f, atlas.findRegions("RedBeardRight"), Animation.PlayMode.LOOP);

        //feed data into queue
        myAssetManager.loadTextureAsset("sprites/Output/RedBeardTest.atlas");
        loadingFile = "RedBeardTest.atlas";
    }


    @Override
    public void show() {
        music.setLooping(false);
        music.setVolume(volume);
        music.play();

        Gdx.app.log(TAG, "Loading Screen loaded");

        titleImage = new Image(title);

        table = new Table();
        table.setFillParent(true);
       // table.setDebug(true);

        loadingTable = new Table();
        loadingTable.add(new LoadingBarPart(dash, walkRight)).pad(50,0,0,20);
        loadingTable.add(new LoadingBarPart(dash, walkRight)).pad(50,0,0,20);
        loadingTable.add(new LoadingBarPart(dash, walkRight)).pad(50,0,0,20);
        loadingTable.add(new LoadingBarPart(dash, walkRight)).pad(50,0,0,20);
        loadingTable.add(new LoadingBarPart(dash, walkRight)).pad(50,0,0,20);


        table.add(titleImage).align(Align.center).pad(10,0,0,0).colspan(10);
        table.row();
        table.add(loadingTable).width(2512);
        table.row();







        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        //Gdx.app.log(TAG, "Loading Screen Render");
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if(myAssetManager.updateAssetLoading()){
            //Gdx.app.log("Current Loading",""+currentLoadingStage);


            if(currentLoadingStage <6){
                loadingTable.getCells().get((currentLoadingStage-1)).getActor().setVisible(true);

            }

            switch(currentLoadingStage){
                case FONT:
                    myAssetManager.loadingFontAsset("fonts/Pixeled English Font.ttf");
                    if(myAssetManager.isAssetLoaded("fonts/Pixeled English Font.ttf")) {
                        currentLoadingStage++;
                        // Gdx.app.log("TAG","CURRENT ASSET LOADING PROGRESS "+myAssetManager.loadCompleted());
                        loadingFile += "\nFONTS: ";
                        loadingFile += "Pixeled English Font..DONE";
                    }
                    break;
                case SOUND:

                    //todo need to plug in Utility to load sound files
                    myAssetManager.loadingSoundAsset("Blip_Select.wav");
                    myAssetManager.loadingSoundAsset("sounds/Coin_Drop.wav");
                    myAssetManager.loadingSoundAsset("sounds/Coin_Pickup.wav");
                    myAssetManager.loadingSoundAsset("sounds/enemy_Hit.wav");
                    myAssetManager.loadingSoundAsset("sounds/Health_Collect.wav");
                    myAssetManager.loadingSoundAsset("sounds/Hit_Hurt.wav");
                    myAssetManager.loadingSoundAsset("sounds/Sword_attack.wav");
                    myAssetManager.loadingSoundAsset("sounds/weird_sound.wav");
                    myAssetManager.loadingSoundAsset("sounds/destroyedBlock.wav");
                    myAssetManager.loadingSoundAsset("sounds/discovery.wav");
                    myAssetManager.loadingSoundAsset("sounds/dragonAttack.wav");
                    myAssetManager.loadingSoundAsset("sounds/dragonDeath.wav");
                    myAssetManager.loadingSoundAsset("sounds/dragonHit.wav");
                    myAssetManager.loadingSoundAsset("sounds/dragonRoar.wav");
                    myAssetManager.loadingSoundAsset("sounds/enemyDeath.wav");
                    myAssetManager.loadingSoundAsset("sounds/plant_destroyed.wav");
                    myAssetManager.loadingSoundAsset("sounds/playerDeath.wav");
                    myAssetManager.loadingSoundAsset("sounds/rangeAttack.wav");
                    myAssetManager.loadingSoundAsset("sounds/rovingLeaving.wav");
                    myAssetManager.loadingSoundAsset("sounds/rovingTalking.wav");
                    myAssetManager.loadingSoundAsset("sounds/staticLeaving.wav");
                    myAssetManager.loadingSoundAsset("sounds/staticTalking.wav");
                    myAssetManager.loadingSoundAsset("sounds/Sword_attack.wav");


                    if(myAssetManager.isAssetLoaded("Blip_Select.wav")){
                        loadingFile += "\nSOUND: ";
                        loadingFile += "..Blip_Select.wav";
                }
                    if(myAssetManager.isAssetLoaded("sounds/Coin_Drop.wav")) {
                        loadingFile += "..Coin_Drop";

                    }

                    if(myAssetManager.isAssetLoaded("sounds/Coin_Pickup.wav")){
                        loadingFile += "..Coin_Pickup";
                    }

                    if(myAssetManager.isAssetLoaded("sounds/weird_sound.wav")){
                        loadingFile += "..Player Hit";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/enemy_Hit.wav")){
                        loadingFile += "..enemy_Hit";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/Health_Collect.wav")){
                        loadingFile += "..Health_Collect";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/Hit_Hurt.wav")){
                        loadingFile += "..Hit_Hurt";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/destroyedBlock.wav")){
                        loadingFile += "..destroyed block";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/discovery.wav")){
                        loadingFile += "..discovery";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/dragonAttack.wav")){
                        loadingFile += "..dragon attack";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/dragonDeath.wav")){
                        loadingFile += "..dragon death";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/dragonHit.wav")){
                        loadingFile += "..dragon hit";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/dragonRoar.wav")){
                        loadingFile += "..dragon roar";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/enemyDeath.wav")){
                        loadingFile += "..enemy death";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/Health_Collect.wav")){
                        loadingFile += "..health collect";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/plant_destroyed.wav")){
                        loadingFile += "..plant destroyed";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/playerDeath.wav")){
                        loadingFile += "..player death";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/rangeAttack.wav")){
                        loadingFile += "..range attack";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/rovingLeaving.wav")){
                        loadingFile += "..roving leaving";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/rovingTalking.wav")){
                        loadingFile += "..roving talking";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/staticLeaving.wav")){
                        loadingFile += "..static leaving";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/staticTalking.wav")){
                        loadingFile += "..static Talking";
                    }
                    if(myAssetManager.isAssetLoaded("sounds/Sword_attack.wav")){
                        loadingFile += "..sword attack";
                    }


                   // Gdx.app.log("TAG","CURRENT ASSET LOADING PROGRESS "+myAssetManager.loadCompleted());

                    if(myAssetManager.isAssetLoaded("Blip_Select.wav")&&
                            myAssetManager.isAssetLoaded("sounds/Coin_Drop.wav")&&
                            myAssetManager.isAssetLoaded("sounds/Coin_Pickup.wav")&&
                            myAssetManager.isAssetLoaded("sounds/weird_sound.wav")&&
                            myAssetManager.isAssetLoaded("sounds/enemy_Hit.wav")&&
                            myAssetManager.isAssetLoaded("sounds/Health_Collect.wav")&&
                            myAssetManager.isAssetLoaded("sounds/Hit_Hurt.wav")&&
                            myAssetManager.isAssetLoaded("sounds/Sword_attack.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/destroyedBlock.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/discovery.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/dragonAttack.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/dragonDeath.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/dragonHit.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/dragonRoar.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/enemyDeath.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/Health_Collect.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/plant_destroyed.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/playerDeath.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/rangeAttack.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/rovingLeaving.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/rovingTalking.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/staticLeaving.wav")
                            &&
                            myAssetManager.isAssetLoaded("sounds/staticTalking.wav")) {

                        loadingFile += "..Done";
                        //Gdx.app.log(TAG, "sound Loaded: " + "Blip_Select.wav");
                        currentLoadingStage ++;
                    }


                    else {
                        myAssetManager.updateAssetLoading();
                        //Utility.assetManager.finishLoadingAsset("Blip_Select.wav");

                    }
                    break;
                case MUSIC:

                   // Gdx.app.log("TAG","CURRENT ASSET LOADING PROGRESS "+myAssetManager.loadCompleted());
                    myAssetManager.loadingMusicAsset("sounds/Game Song.wav");
                    myAssetManager.loadingMusicAsset("sounds/mainMenu.wav");
                    myAssetManager.loadingMusicAsset("sounds/Dungeon Song.wav");
                    myAssetManager.loadingMusicAsset("sounds/EndScreen Song.wav");
                    myAssetManager.loadingMusicAsset("sounds/WinGameSong.wav");

                    if(myAssetManager.isAssetLoaded("sounds/Credits song.wav")) {
                        loadingFile += "\nMUSIC: Game song";

                    }

                    if(myAssetManager.isAssetLoaded("sounds/mainMenu.wav")) {
                        loadingFile += "..main menu song";

                    }

                    if(myAssetManager.isAssetLoaded("sounds/Dungeon Song.wav")) {
                        loadingFile += "..Dungeon Song";

                    }

                    if(myAssetManager.isAssetLoaded("sounds/EndScreen Song.mp3")){
                        loadingFile +="..End Screen Song";
                    }

                    if(myAssetManager.isAssetLoaded("sounds/Game Song.wav")
                            &&myAssetManager.isAssetLoaded("sounds/mainMenu.wav")
                            &&myAssetManager.isAssetLoaded("sounds/Dungeon Song.wav")
                            &&myAssetManager.isAssetLoaded("sounds/EndScreen Song.wav")
                            &&myAssetManager.isAssetLoaded("sounds/WinGameSong.wav")){
                        loadingFile += "..DONE";
                        currentLoadingStage++;
                    }
                    break;

                case MAP:

                    myAssetManager.loadMapAsset("maps/Overworld.tmx");

                    if(myAssetManager.isAssetLoaded("maps/Overworld.tmx")) {
                        loadingFile += "\nMAP: Overworld..Cave1..Dungeon..???????..Done";
                        currentLoadingStage++;
                    }
                    break;
                case PARTICLE:

                    myAssetManager.loadParticleAsset("ParticleEffects/fire.party");
                    myAssetManager.loadParticleAsset("ParticleEffects/smoke.party");

                    if(myAssetManager.isAssetLoaded("ParticleEffects/fire.party")){
                        loadingFile += "Fire Particles";

                    }

                   if(myAssetManager.isAssetLoaded("ParticleEffects/smoke.party")){
                        loadingFile += "Smoke Particles";
                    }

                   if(myAssetManager.isAssetLoaded("ParticleEffects/smoke.party")&&
                            myAssetManager.isAssetLoaded("ParticleEffects/fire.party")) {
                        loadingFile += "\nParticles: ..........DONE";
                        currentLoadingStage++;
                    }
                    break;
                case 6:
                    //loading is done
                    //Gdx.app.log("TAG","CURRENT ASSET LOADING PROGRESS "+myAssetManager.loadCompleted());

                    currentLoadingStage ++;
                    break;
            }

            if (currentLoadingStage > 6){
                countDown -= delta;
                currentLoadingStage = 6;



                if(countDown < 0){
                    loadingFile +="\n FINISHED LOADING";
                    game.setScreen(RedBeardRun.SCREENTYPE.MENU);
                    music.pause();
                }
            }
        }

        //todo use like countdown with delta for it to decrease volume
        music.setVolume(volume-currentLoadingStage);

        batch.begin();
        font.getColor().set(Color.RED);

        font.draw(batch, loadingFile, 25,Gdx.graphics.getHeight()/3,Gdx.graphics.getWidth()-50,-1,true);
        batch.end();

        stage.act();
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true);
    }

    @Override
    public void pause() {
        if(music!=null) {
            if(music.isPlaying())
                music.stop();


            //music.release();
            music=null;
        }

    }

    @Override
    public void resume() {
        music = myAssetManager.getMusicAsset("Loading Screen Song.wav");

    }

    @Override
    public void hide() {
        music.stop();

    }

    @Override
    public void dispose() {
        //music.dispose();
        stage.dispose();
        //font.dispose();
       // music.dispose();


    }
}

class LoadingBarPart extends Actor {

    private AtlasRegion image;
    private Animation walkingRight;
    private float stateTime = 0f;
    private TextureRegion currentFrame;


    public LoadingBarPart(AtlasRegion image, Animation walkingRight) {
        super();
        this.image = image;
        this.walkingRight = walkingRight;
        this.setWidth(256);
        this.setHeight(256);
        this.setVisible(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(currentFrame, getX()-5, getY(), 256, 256);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;
        currentFrame = (TextureRegion) walkingRight.getKeyFrame(stateTime, true);
    }
}

