package Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import Conversation.ConversationChoice;
import Conversation.ConversationGraph;
import Conversation.ConversationGraphObserver;
import Conversation.ConversationUI;
import Helpers.MapUI;
import Helpers.StatusUI;
import Helpers.WeaponUI;
import Observers.HUDObserver;
import Observers.HUDSubject;
import Observers.NPCObserver;
import Observers.StatusObserver;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class PlayerHUD implements Screen, StatusObserver, ConversationGraphObserver, HUDSubject, NPCObserver {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    private Viewport gameViewport;
    private OrthographicCamera camera;
    private Entity player;
    private MyAssetManager myAssetManager;
    private Skin skin;
    private int width;
    private int height;
    private RedBeardRun game;
    private boolean changeStatusUIColor;
    private boolean changeWeaponUIColor;
    private boolean changeMapUIColor;
    private float changeSpeed;
    private float changeTime;




    private Dialog messageBoxUI;
    private StatusUI statusUI;
    private WeaponUI weaponUI;
    private MapUI mapUI;
    private ConversationUI conversationUI;
    private Array<HUDObserver> observers;

    public Stage getStage() {
        return stage;
    }

    public PlayerHUD(RedBeardRun game, Entity player, MyAssetManager myAssetManager, Viewport gameScreenViewport) {
        this.player = player;
        this.myAssetManager = myAssetManager;
        this.gameViewport = gameScreenViewport;
        this.game = game;
        changeStatusUIColor = false;
        changeWeaponUIColor = false;
        changeMapUIColor = false;
        changeSpeed = 0.3f;
        changeTime = 0f;

        width = gameViewport.getLeftGutterWidth();
        height =gameViewport.getRightGutterWidth();
        Gdx.app.log(TAG,"gamescreen viewport "+ width+", "+ height);
        camera = new OrthographicCamera();
        skin = new Skin(Gdx.files.internal("Skins/redbeardskin/redbeard-ui.json"));
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        Gdx.app.log(TAG, viewport.getWorldWidth() + ", "+ viewport.getWorldHeight());
        camera.setToOrtho(false,viewport.getScreenWidth()/2,viewport.getScreenHeight()/2);
        stage = new Stage(viewport);
       // viewport.setCamera(camera);
        observers = new Array<HUDObserver>();

        statusUI = new StatusUI("-Stats-",skin,myAssetManager);
        statusUI.getTitleLabel().setAlignment(Align.center);
        statusUI.setVisible(true);
        statusUI.setPosition(0,0);
        statusUI.setKeepWithinStage(false);
        statusUI.setMovable(false);
        statusUI.sizeBy(100,0);
        statusUI.getColor().a = 0.5f;
        // statusUI.defaults().fill().expand();
        // statusUI.setSize(650,200);
        // statusUI.pack();

        weaponUI = new WeaponUI("-Weapons-", skin, myAssetManager);
        weaponUI.getTitleLabel().setAlignment(Align.center);
        weaponUI.setVisible(true);
        // weaponUI.getPrefWidth();
        // weaponUI.getPrefHeight();
        Gdx.app.log("WEAPON UI"," width "+ weaponUI.getColumnWidth(0));
        weaponUI.setPosition(Gdx.graphics.getWidth()-weaponUI.getWidth(),0);
        weaponUI.setKeepWithinStage(true);
        weaponUI.setMovable(false);
        weaponUI.getColor().a = 0.5f;

        //weaponUI.sizeBy(100,0);
        //weaponUI.setSize(300,300);
        //weaponUI.padLeft(100);

        mapUI = new MapUI("-Map-", skin, myAssetManager);
        mapUI.getTitleLabel().setAlignment(Align.center);
        mapUI.setVisible(true);

        mapUI.setPosition(Gdx.graphics.getWidth()-mapUI.getWidth(),Gdx.graphics.getHeight()-mapUI.getHeight());
        mapUI.setKeepWithinStage(true);
        mapUI.getColor().a = 0.5f;
        //mapUI.setFillParent(true);


        conversationUI = new ConversationUI("",skin);
        conversationUI.getTitleLabel().setAlignment(Align.center);
        conversationUI.setMovable(true);
        conversationUI.setVisible(false);
        conversationUI.setPosition(stage.getWidth()/2,0);
        conversationUI.setWidth(stage.getWidth()/2);
        conversationUI.setHeight(stage.getHeight());
        conversationUI.getColor().a = 0.5f;









        stage.addActor(statusUI);
        stage.addActor(weaponUI);
        stage.addActor(mapUI);
        stage.addActor(conversationUI);


        statusUI.validate();
        weaponUI.validate();
        mapUI.validate();
        conversationUI.validate();

        // statusUI.pack();


        conversationUI.getCloseButton().addListener(new ChangeListener(){


            @Override
            public void changed(ChangeEvent event, Actor actor) {
                conversationUI.setVisible(false);

                Gdx.app.log(TAG, "Clicked close button");
            }
        }
        );

        conversationUI.getListItems().addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                ConversationChoice choice = (ConversationChoice) conversationUI.getListItems().getSelected();
                if(choice == null){
                    return;
                }
               conversationUI.getGraph().notify(choice.getConversationCommandEvent(),conversationUI.getGraph());
                conversationUI.populateConversationDialog(choice.getDestinationId());
            }
        });




    }

    public void usedPotion(){
       // Gdx.app.log(TAG, "Used Potion sent message");
        notify(HUDObserver.HUDEvent.USE_POTION,60);
    }

    @Override
    public void show() {
        // statusUI.pack();
        /*if(!conversationUI.isVisible()&&conversationUI.getCloseButton().isPressed()){
            notify(HUDObserver.HUDEvent.EXIT_CONVERSATION,1);
        }*/

        statusUI.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                statusUI.usePotion();
                changeStatusUIColor = true;
                usedPotion();

            }
            //this.debug();


        });

        Gdx.app.log(TAG,"Show Method");
        Gdx.app.log(TAG, "New game is: "+ game.newGame);
        if(game.newGame){
            statusUI.resetStatusUI();
        }

        if(!game.newGame) {
            Gdx.app.log(TAG,"current Group: "+game.loadData.getFloat("currentGroup", 14f)+"");
            Gdx.app.log(TAG,"Dragoons: "+game.loadData.getFloat("dragoons", 0f)+"");
            Gdx.app.log(TAG,"litesword: "+game.loadData.getBoolean("liteSword", false)+"");
            Gdx.app.log(TAG,"heavysword: "+game.loadData.getBoolean("heavySword", false)+"");
            Gdx.app.log(TAG,"bow: "+game.loadData.getBoolean("bow", false)+"");
            Gdx.app.log(TAG,"health: "+game.loadData.getFloat("health", 120)+"");
            Gdx.app.log(TAG,"potions: " +game.loadData.getFloat("potions", 0f)+"");

            statusUI.setHealth((int)game.loadData.getFloat("health", 120));

            mapUI.changeLocation(game.loadData.getFloat("currentGroup", 14f));

            statusUI.addDragoonsCollected((int)game.loadData.getFloat("dragoons", 0f));

            if (game.loadData.getBoolean("heavySword", false)) {
                weaponUI.setSwordImage(2);

            }

            else if (game.loadData.getBoolean("liteSword", false)) {
                weaponUI.setSwordImage(1);
            }
            else{
                weaponUI.setSwordImage(0);
            }
            if (game.loadData.getBoolean("bow", false)) {
                weaponUI.setRangeImage(3);
            }
            else{
                weaponUI.setRangeImage(0);
            }

            statusUI.setPotionsCollected((int) game.loadData.getFloat("potions", 0f));
        }
    }

    public void updateHUDColor(float delta){


        if(changeStatusUIColor) {

            if(changeTime<=0) {
                changeTime -= delta;
                statusUI.getColor().a = 1;

                if(changeTime<-changeSpeed){
                    statusUI.getColor().a = 0.5f;
                    changeTime = 0;
                    changeStatusUIColor = false;
                   // Gdx.app.log(TAG,"change time Reset: "+ changeTime);

                }
            }
        }

        if(changeWeaponUIColor) {
            if(changeTime<=0) {
                changeTime -= delta;
                weaponUI.getColor().a = 1;

                if(changeTime<-changeSpeed){
                    weaponUI.getColor().a = 0.5f;
                    changeTime = 0;
                    changeWeaponUIColor = false;
                    // Gdx.app.log(TAG,"change time Reset: "+ changeTime);

                }
            }

        }

        if(changeMapUIColor) {
            if(changeTime<=0) {
                changeTime -= delta;
                mapUI.getColor().a = 1;

                if(changeTime<-changeSpeed){
                    mapUI.getColor().a = 0.5f;
                    changeTime = 0;
                    changeMapUIColor = false;
                    // Gdx.app.log(TAG,"change time Reset: "+ changeTime);

                }
            }

        }
    }

    @Override
    public void render(float delta) {
        updateHUDColor(delta);
        // statusUI.pack();
        viewport.apply(true);
        stage.act();
        stage.draw();

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

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();



    }

    @Override
    public void onNotify(StatusEvent event, int value) {
        switch(event){
            case UPDATED_HEALTH:
                statusUI.updateHealth(value);
                changeStatusUIColor = true;

                break;
            case UPDATED_POTIONS:
                statusUI.addPotionsCollected(value);
                changeStatusUIColor = true;
                break;
            case UPDATED_DRAGOONS:
                statusUI.addDragoonsCollected(value);
                changeStatusUIColor = true;
                break;
            case UPDATED_WEAPONS:
                if(value == 0){
                    weaponUI.setRangeImage(0);
                    weaponUI.setSwordImage(0);
                }
                else if(value == 1){
                    weaponUI.setSwordImage(1);
                    changeWeaponUIColor = true;
                }
                else if(value == 2){
                    weaponUI.setSwordImage(2);
                    changeWeaponUIColor = true;
                }
                else if(value == 3){
                    weaponUI.setRangeImage(3);
                    changeWeaponUIColor = true;
                }
                else{

                }
                break;
            case UPDATED_LOCATION:
                mapUI.changeLocation(value);
                changeMapUIColor = true;
            default:
                break;
        }

    }

    @Override
    public void onNotify(ConversationCommandEvent event, ConversationGraph graph) {
        switch (event){
            case NONE:
                break;

            case BOUGHT_BOW:
                if(statusUI.getDragoonsCollected()>300) {
                    notify(HUDObserver.HUDEvent.BOUGHT_BOW, 300);
                    statusUI.addDragoonsCollected(-300);

                }
                //todo generate bow for player to collect
                //todo subtract dragoons from players amount
                break;
            case DENY_QUEST:
                //todo send player to end game screen
                notify(HUDObserver.HUDEvent.DENY_QUEST,1);
                break;
            case ACCEPT_QUEST:
                notify(HUDObserver.HUDEvent.ACCEPT_QUEST,1);

                break;
            case BOUGHT_POTION:
                Gdx.app.log(TAG,"Conversation graph potion purchase notification");
                if(statusUI.getDragoonsCollected()>30){
                    Gdx.app.log(TAG, "player HUD bought potion");
                    notify(HUDObserver.HUDEvent.BOUGHT_POTION,-30);
                    statusUI.addDragoonsCollected(-30);
                }

                break;
                //subtract dragoons from player's amount
            case EXIT_CONVERSATION:
               // conversationUI.setVisible(false);
                Gdx.app.log(TAG,"End conversation command received "+ conversationUI.isVisible());
                notify(HUDObserver.HUDEvent.EXIT_CONVERSATION,1);
                //todo have npc that was talking disappear
                break;
                default:
                    break;
        }
    }

    @Override
    public void onNotify(NPCEvent event, int value) {
        switch (event){
            case PURCHASE_GENERATED:

            case LOAD_CONVERSATION:
                conversationUI.loadConversation(value);
                conversationUI.getGraph().addObserver(this);
                Gdx.app.log(TAG,"Load Conversation");
                break;
            case SHOW_CONVERSATION:
                conversationUI.setVisible(true);
                Gdx.app.log(TAG,"show Conversation");
                break;
            case EXIT_CONVERSATION:
                conversationUI.setVisible(false);


                Gdx.app.log(TAG,"Hide Conversation");
                break;
        }

    }

    @Override
    public void addObserver(HUDObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(HUDObserver observer) {
        observers.removeValue(observer, true);
    }

    @Override
    public void removeAllObservers() {
        for (HUDObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(HUDObserver.HUDEvent event, int value) {
        for (HUDObserver observer : observers) {
            observer.onNotify(event, value);
        }
    }


}
