package edu.fullsail.gabriel_walters;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Hashtable;

import Managers.MyAssetManager;
import Screens.CreditsScreen;
import Screens.EndGameScreen;
import Screens.LoadingScreen;
import Screens.MainGameScreen;
import Screens.MainMenuScreen;

public class RedBeardRun extends Game {

	private static final String TAG = RedBeardRun.class.getSimpleName();

	public SpriteBatch getBatch() {
		return batch;
	}
	public boolean newGame;
	public boolean wonGame;
	public boolean startedGame;
	public Preferences loadData;



	//Every screen needs access to
	private SpriteBatch batch;
	private AssetManager assetManager;
	public MyAssetManager myAssetManager;

	//creating screens
	private LoadingScreen loadingScreen;
	private MainMenuScreen mainMenuScreen;
	private MainGameScreen mainGameScreen;
	private EndGameScreen endGameScreen;
	private CreditsScreen creditsScreen;
	private Hashtable<SCREENTYPE,Screen> screenTable;

	public enum SCREENTYPE{
		LOAD, MENU, GAME, END, CREDITS
	}

	public void createScreen (SCREENTYPE type){
		Screen screen = null;
		switch(type){
			case LOAD:
				Gdx.app.log(TAG,"Created Loading Screen");

				if(loadingScreen == null){
					loadingScreen = new LoadingScreen(this, myAssetManager);
					screenTable.put(SCREENTYPE.LOAD,loadingScreen);
				}
				break;

			case MENU:
				Gdx.app.log(TAG,"Created Menu Screen");

				if(mainMenuScreen == null){
					mainMenuScreen = new MainMenuScreen(this, myAssetManager);
					screenTable.put(SCREENTYPE.MENU,mainMenuScreen);
				}
				break;

			case GAME:
				Gdx.app.log(TAG,"Created Game Screen");

				if(mainGameScreen == null){
					mainGameScreen = new MainGameScreen(this, myAssetManager);
					screenTable.put(SCREENTYPE.GAME,mainGameScreen);
				}
				break;

			case END:
				Gdx.app.log(TAG,"Created End Screen");

				if(endGameScreen == null){
					endGameScreen = new EndGameScreen(this, myAssetManager);
					screenTable.put(SCREENTYPE.END,endGameScreen);
				}
				break;

			case CREDITS:
				Gdx.app.log(TAG,"Created Credits Screen");

				if(creditsScreen == null){
					creditsScreen = new CreditsScreen(this, myAssetManager);
					screenTable.put(SCREENTYPE.CREDITS,creditsScreen);
				}
				break;

			default:
				break;
		}

	}

	public void setScreen(SCREENTYPE type){
		createScreen(type);
		setScreen(screenTable.get(type));

	}


	//Texture img;

	@Override
	public void create () {
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		batch = new SpriteBatch();
		assetManager = new AssetManager(new InternalFileHandleResolver());
		myAssetManager = new MyAssetManager(assetManager);
		screenTable = new Hashtable<SCREENTYPE, Screen>();
		setScreen(SCREENTYPE.LOAD);
		newGame = true;
		loadData = Gdx.app.getPreferences("SaveFile");
		wonGame = false;
		startedGame = false;
		//img = new Texture("badlogic.jpg");
	}



	@Override
	public void dispose () {

		for(Screen screen: screenTable.values()){
			screen.dispose();
		}
		batch.dispose();

		assetManager.dispose();
		//img.dispose();
	}



	//Getters and Setters

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
}
