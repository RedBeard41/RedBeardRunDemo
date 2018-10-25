package Helpers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import Components.InteractiveComponent;
import Managers.MyAssetManager;

public class WeaponUI extends Window {

    private Skin skin;
    private TextureAtlas atlas;
    private MyAssetManager myAssetManager;
    private Stack melee;
    private Stack range;
    private Image swordImage;
    private Image rangeImage;
    private boolean collectedHeavySword;



    public WeaponUI(String title, Skin skin, MyAssetManager myAssetManager) {
        super(title, skin);
        this.skin = skin;
        this.myAssetManager = myAssetManager;
        collectedHeavySword = false;
        atlas = new TextureAtlas(Gdx.files.internal("Skins/redbeardskin/redbeard-ui.atlas"));

        TextureRegion sword = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("emptyAsset");





        swordImage = new Image(new TextureRegionDrawable(sword));
        swordImage.setSize(128,128);


        NinePatchDrawable swordBackground = new NinePatchDrawable(atlas.createPatch("select-box"));
        Image swordBackgroundImage = new Image(swordBackground);


        TextureRegion bow = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("emptyAsset");

        rangeImage = new Image(new TextureRegionDrawable(bow));
        rangeImage.setSize(128,128);



       // rangeImage.setFillParent(true);


        NinePatchDrawable rangeBackground = new NinePatchDrawable(atlas.createPatch("select-box"));
        Image rangeBackgroundImage = new Image(rangeBackground);

        melee = new Stack();
        melee.addActor(swordBackgroundImage);
        melee.addActor(swordImage);
      //  melee.setSize(swordImage.getWidth(),swordImage.getHeight());

        range = new Stack();
        range.addActor(rangeBackgroundImage);
        range.addActor(rangeImage);

       // range.setSize(rangeImage.getWidth(),rangeImage.getHeight());


       // defaults().expand().fill();

        this.pad(this.getPadTop()+50,10,10,10);

        this.row().fillX().pad(0,80,0,50);

        this.add(melee).size(melee.getWidth(),melee.getHeight());
        this.add(range).size(range.getWidth(),range.getHeight());

        //this.setClip(false);

        this.pack();
       // this.debug();


    }


    public Image getSwordImage() {
        return swordImage;
    }

    public void setSwordImage(int value) {
        switch(value){
            case 0:
                melee.removeActor(swordImage);
                this.swordImage = new Image(new TextureRegionDrawable(myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                        .findRegion("emptyAsset")));
                melee.addActor(swordImage);
                break;
            case 1:
                if(!collectedHeavySword) {
                    melee.removeActor(swordImage);
                    this.swordImage = new Image(new TextureRegionDrawable(myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                            .findRegion("liteSwordUp")));
                    melee.addActor(swordImage);
                }

                break;
            case 2:
                melee.removeActor(swordImage);
                this.swordImage = new Image(new TextureRegionDrawable(myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                        .findRegion("heavySwordUp")));
                melee.addActor(swordImage);
                collectedHeavySword = true;
                break;
            default:
                melee.removeActor(swordImage);
                this.swordImage = new Image(new TextureRegionDrawable(myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                        .findRegion("emptyAsset")));
                melee.addActor(swordImage);
                break;
        }
    }

    public Image getRangeImage() {
        return rangeImage;
    }

    public void setRangeImage(int value) {
        switch(value){
            case 3:
                range.removeActor(rangeImage);
                this.rangeImage = new Image(new TextureRegionDrawable(myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                        .findRegion("Bow")));
                range.addActor(rangeImage);
                break;
            default:
                range.removeActor(rangeImage);
                this.rangeImage = new Image(new TextureRegionDrawable(myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                        .findRegion("emptyAsset")));
                range.addActor(rangeImage);
                break;
        }

    }
}
