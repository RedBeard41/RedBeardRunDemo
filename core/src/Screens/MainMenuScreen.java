package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;



import Managers.MyAssetManager;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class MainMenuScreen implements Screen{
    public static final String TAG = MainMenuScreen.class.getSimpleName();

    private RedBeardRun game;
    private MyAssetManager myAssetManager;

    private Stage stage;
    private Sound blip;
    private TextureAtlas atlas;
    private TextureRegion title;
    private Image titleImage;
    private Skin skin;
    private Music song;
    private boolean backKeyPressed = false;

    public MainMenuScreen(RedBeardRun game, MyAssetManager myAssetManager) {
        this.game = game;
        this.myAssetManager = myAssetManager;

        this.stage = new Stage(new ScreenViewport());
        atlas = myAssetManager.getTextureAsset("sprites/Output/loading.atlas");
        title = atlas.findRegion("RedBeard Run title");
        song = myAssetManager.getMusicAsset("sounds/mainMenu.wav");

    }
    @Override
    public void show() {
        Gdx.app.log(TAG,"back Key is "+ backKeyPressed);
        blip = myAssetManager.getSoundAsset("Blip_Select.wav");
        song.setLooping(true);
        song.setVolume(.7f);
        song.play();
        Gdx.input.setInputProcessor(stage);
        Gdx.app.log(TAG, "Main Menu Screen loaded");
        Table table = new Table();
        table.setFillParent(true);
       // table.setDebug(true);

        titleImage = new Image(title);

        skin = new Skin(Gdx.files.internal("Skins/redbeardskin/redbeard-ui.json"));

        final TextButton newGameButton = new TextButton("New Game", skin);
        TextButton loadGameButton = new TextButton("Load Game", skin);
        TextButton creditsButton = new TextButton("Credits", skin);
        //TextButton exitButton = new TextButton("Exit", skin);


        //Layout
        table.add(titleImage).align(Align.center).pad(10,0,50,0).colspan(10);

        table.row().pad(20,450,30,100);
        table.add(newGameButton).fillX().uniformX();
        table.row().pad(20,450,30,100);
        table.add(loadGameButton).fillX().uniformX();
        table.row().pad(30,450,20,100);
        table.add(creditsButton).fillX().uniformX();





        //Listeners
        newGameButton.addListener(new ChangeListener() {
                                      @Override
                                      public void changed(ChangeEvent event, Actor actor) {
                                          blip.play(.4f);
                                          game.newGame = true;
                                          game.wonGame = false;
                                          game.startedGame = true;
                                          game.setScreen(RedBeardRun.SCREENTYPE.GAME);
                                          backKeyPressed=false;

                                      }
                                  }

        );
        loadGameButton.addListener(new ChangeListener() {
                                      @Override
                                      public void changed(ChangeEvent event, Actor actor) {
                                          blip.play(.4f);
                                          game.newGame = false;
                                          game.startedGame = true;
                                          game.wonGame = false;
                                          game.setScreen(RedBeardRun.SCREENTYPE.GAME);
                                          backKeyPressed = false;

                                      }
                                  }

        );

        creditsButton.addListener(new ChangeListener() {
                                      @Override
                                      public void changed(ChangeEvent event, Actor actor) {
                                          blip.play(.5f);

                                          game.setScreen(RedBeardRun.SCREENTYPE.CREDITS);
                                          backKeyPressed = false;
                                      }
                                  }

        );

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {

        if(!backKeyPressed) {
            if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                backKeyPressed = true;
                //
            }
        }else {
            if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                Gdx.app.log(TAG, "Back key is pressed, EXITING");
//            game.dispose();
                Gdx.app.exit();
            }

            }


        Gdx.gl.glClearColor(.6f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(),1/30f));
        stage.draw();
        //backKeyPressed++;

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        song.play();

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        blip.stop();
        song.pause();
        Gdx.app.log(TAG, "input Processor is "+ Gdx.input.getInputProcessor());

    }

    @Override
    public void dispose() {
        blip.dispose();
        stage.dispose();
        skin.dispose();
       // song.dispose();


    }
}
