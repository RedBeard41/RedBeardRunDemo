package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import Managers.MyAssetManager;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class CreditsScreen implements Screen
{

    private RedBeardRun game;
    private MyAssetManager myAssetManager;

    private static String CREDITS_PATH = "credits.txt";
    private Stage stage;
    private ScrollPane scrollPane;
    private Skin skin;
    private Music music;

    public CreditsScreen(RedBeardRun game, MyAssetManager myAssetManager) {
        this.game = game;
        this.myAssetManager = myAssetManager;

        stage = new Stage();

        skin = new Skin(Gdx.files.internal("Skins/redbeardskin/redbeard-ui.json"));
        music = myAssetManager.getMusicAsset("sounds/EndScreen Song.wav");



    }

    @Override
    public void show() {

        FileHandle file = Gdx.files.internal(CREDITS_PATH);
        String textString;
        if(game.startedGame) {

            if (game.wonGame) {
                textString = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nCongratulations!\n\n\n\nYou have defeated  DARGON  the dragon and made it home!\n\n\n\n\n\n\n\n";
                music = myAssetManager.getMusicAsset("sounds/WinGameSong.wav");
            } else {
                textString = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nYOU HAVE DIED!\n\n\n\n\n\n\n\n\n";
                //music = myAssetManager.getMusicAsset("sounds/EndScreen Song.wav");
            }
            textString += file.readString();
        }
        else{
            textString = file.readString();
        }

        Label text = new Label(textString, skin);
        text.setAlignment(Align.top | Align.center);
        text.setWrap(true);

        scrollPane = new ScrollPane(text);


        scrollPane.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                scrollPane.setScrollY(0);
                scrollPane.updateVisualScroll();
                //game.setScreen(game.getScreenType(RedBeardRun.ScreenType.MainMenu));
            }
        });

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.defaults().width(Gdx.graphics.getWidth());
        table.add(scrollPane);

        stage.addActor(table);


        scrollPane.setVisible(true);
        Gdx.input.setInputProcessor(stage);
        music.setLooping(true);
        music.setVolume(1.0f);
        music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            Gdx.app.log("CREDIT SCREEN", "Back key is pressed, RETURNING TO MAIN MENU");
            game.setScreen(RedBeardRun.SCREENTYPE.MENU);
        }

        stage.act(delta);
        stage.draw();

        scrollPane.setScrollY(scrollPane.getScrollY()+delta * 90);

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width,height);
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
        music = myAssetManager.getMusicAsset("sounds/EndScreen Song.wav");
        music.setVolume(1.0f);
        music.play();
        music.setLooping(true);

    }

    @Override
    public void hide() {
        scrollPane.setVisible(false);
        scrollPane.setScrollY(0);
        scrollPane.updateVisualScroll();
        Gdx.input.setInputProcessor(null);
        music.stop();

    }

    @Override
    public void dispose() {
        stage.clear();
        scrollPane = null;
        stage.dispose();
        skin.dispose();
       // music.dispose();

    }
}
