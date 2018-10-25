package Helpers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import javax.xml.soap.Text;

import Managers.MyAssetManager;
import Observers.StatusObserver;
import Observers.StatusSubject;

public class StatusUI extends Window {

    private Table healthBarTable;
    private TextureRegion heartLeft;
    private TextureRegion heartRight;
    private Stack health;
    private Stack dragoons;
    private Stack potions;
    private Skin skin;
    private Label dragoonLabel;
    private Label potionLabel;
    private Image potionImage;
    private Image dragoonImage;
    private Integer dragoonsCollected = 0;
    private Integer potionsCollected = 0;
    private int currentHealth = 120;
    private TextureAtlas atlas;



    public StatusUI(String title, Skin skin, MyAssetManager myAssetManager) {
        super(title, skin);

        this.skin = skin;
        atlas = new TextureAtlas(Gdx.files.internal("Skins/redbeardskin/redbeard-ui.atlas"));

        healthBarTable = new Table();

        heartLeft = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("heartLeft");
        heartRight = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("heartRight");


        //healthbar
        healthBarTable.add(new HealthBar(heartLeft));
        healthBarTable.add(new HealthBar(heartRight)).padRight(20);
        healthBarTable.add(new HealthBar(heartLeft));
        healthBarTable.add(new HealthBar(heartRight)).padRight(20);
        healthBarTable.add(new HealthBar(heartLeft));
        healthBarTable.add(new HealthBar(heartRight)).padRight(20);
        healthBarTable.add(new HealthBar(heartLeft));
        healthBarTable.add(new HealthBar(heartRight));

        Table dragoonTable = new Table();
        TextureRegion dragoon = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("Dragoon25");
        Image dragoonImage = new Image(dragoon);
        // dragoonImage.setSize(100,100);
        dragoonLabel = new Label(": " + Integer.toString(dragoonsCollected), this.skin);
        dragoonTable.add(dragoonImage).size(64, 64).pad(0, 20, 5, 0);
        dragoonTable.add(dragoonLabel).align(Align.left).prefWidth(dragoonLabel.getMinWidth());


        Table potionTable = new Table();
        TextureRegion potion = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("potion");

        // Gdx.app.log("potion texture region",""+(potion==null));


        Image potionImage = new Image(new TextureRegionDrawable(potion));
        // Gdx.app.log("potion Image before",""+potionImage.getImageWidth() +" "+potionImage.getImageWidth());
        // potionImage.setSize(15,potion.getRegionHeight());
        // Gdx.app.log("potion Image after",""+potionImage.getImageWidth() +" "+potionImage.getImageWidth());


        potionLabel = new Label(": " + Integer.toString(potionsCollected), this.skin);
        // potionTable.setFillParent(true);
        potionTable.add(potionImage).size(64, 64).pad(0, 20, 5, 0);
        potionTable.add(potionLabel).align(Align.left).prefWidth(potionLabel.getMinWidth());


        NinePatchDrawable potionBackground = new NinePatchDrawable(atlas.createPatch("select-box"));
        Image potionBackgroundImage = new Image(potionBackground);
        // potionBackgroundImage.setSize(potion.getRegionWidth(),potion.getRegionHeight());

        NinePatchDrawable healthBackground = new NinePatchDrawable(atlas.createPatch("select-box"));
        Image healthBackgroundImage = new Image(healthBackground);

        NinePatchDrawable dragoonBackground = new NinePatchDrawable(atlas.createPatch("select-box"));
        Image dragoonBackgroundImage = new Image(dragoonBackground);

        //potionTable.setPosition(3,6);


        potions = new Stack();
        potions.addActor(potionBackgroundImage);
        potions.addActor(potionTable);

        // Gdx.app.log("potions Stack",""+potions.getMinWidth()+" "+potions.getMinHeight());


        dragoons = new Stack();
        dragoons.addActor(dragoonBackgroundImage);
        dragoons.addActor(dragoonTable);

        health = new Stack();
        health.addActor(healthBackgroundImage);
        health.addActor(healthBarTable);

        defaults().expand().fill();

        this.pad(this.getPadTop() + 50, 10, 10, 10);
        this.row().pad(10, 10, 10, 20);
        // Gdx.app.log("STATUS UI","width "+ potionImage.getImageWidth()+" Height "+ potionImage.getImageHeight());
        this.add(potions).size(potionTable.getPrefWidth(), potionTable.getPrefHeight()).align(Align.left);


        //this.add(potionLabel).align(Align.left);


        this.add(dragoons).size(dragoonTable.getPrefWidth(), dragoonTable.getPrefHeight()).align(Align.left);
        // this.add(dragoonLabel).align(Align.left);
        this.row().padRight(20);
        // Gdx.app.log("STATUS UI","width "+ healthBarTable.getWidth()+" Height "+ healthBarTable.getHeight());
        this.add(health).size(healthBarTable.getPrefWidth() + 64, healthBarTable.getPrefHeight()).colspan(2).align(Align.center);


        this.pack();


    }

    public void resetStatusUI(){
        this.setHealth(120);
        this.potionsCollected = 0;
        this.dragoonsCollected = 0;
    }

    public Integer getDragoonsCollected() {
        return dragoonsCollected;
    }

    public void setDragoonsCollected(Integer dragoonsCollected) {
        this.dragoonsCollected = dragoonsCollected;
        dragoonLabel.setText(Integer.toString(this.dragoonsCollected));
    }

    public void addDragoonsCollected(Integer dragoonsCollected) {
        this.dragoonsCollected += dragoonsCollected;
        Gdx.app.log("STATUS UI",""+this.dragoonsCollected);
        dragoonLabel.setText(Integer.toString(this.dragoonsCollected));
    }

    public Integer getPotionsCollected() {
        return potionsCollected;
    }

    public void setPotionsCollected(Integer potionsCollected) {
        this.potionsCollected = potionsCollected;
        potionLabel.setText(Integer.toString(this.potionsCollected));
    }

    public void addPotionsCollected(Integer potionsCollected) {
        this.potionsCollected += potionsCollected;
        potionLabel.setText(Integer.toString(this.potionsCollected));
    }

    public void usePotion(){
        if(potionsCollected>0&&currentHealth<120) {
            potionsCollected--;
            potionLabel.setText(Integer.toString(this.potionsCollected));
            updateHealth(60);
        }
    }

    public void setHealth(int currentHealth){
        this.currentHealth = currentHealth;
        for(int i = 0; i<120;i+=15){
            if(this.currentHealth>i){
                healthBarTable.getCells().get(i/15).getActor().setVisible(true);
            }
            else{
                healthBarTable.getCells().get(i/15).getActor().setVisible(false);
            }

        }
    }

    public void updateHealth(int currentHealth){
        this.currentHealth += currentHealth;
        if(this.currentHealth>120){
            this.currentHealth=120;
        }
        for(int i = 0; i<120;i+=15){
            if(this.currentHealth>i){
                healthBarTable.getCells().get(i/15).getActor().setVisible(true);
            }
            else{
                healthBarTable.getCells().get(i/15).getActor().setVisible(false);
            }

        }
    }



}
