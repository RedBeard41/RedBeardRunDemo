package Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import Managers.MyAssetManager;

public class MapUI extends Window {

    private Skin skin;
    private TextureAtlas atlas;
    private MyAssetManager myAssetManager;
    private Stack melee;
    private Stack range;
    private Image mapImage;
    private Image location;
    private Table mapTable;
    private Array<Image> locations;


    public MapUI(String title, Skin skin,MyAssetManager myAssetManager) {
        super(title, skin);
        this.skin = skin;
        this.myAssetManager = myAssetManager;
        atlas = new TextureAtlas(Gdx.files.internal("Skins/redbeardskin/redbeard-ui.atlas"));

        TextureRegion map = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("mapBackgroun");






        mapImage = new Image(new TextureRegionDrawable(map));
        mapImage.setSize(448,174);


        TextureRegion mapLocation = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas")
                .findRegion("mapLocation");

        location = new Image(new TextureRegionDrawable(mapLocation));
        locations = new Array<Image>();


        NinePatchDrawable mapBackground = new NinePatchDrawable(atlas.createPatch("select-box"));
        Image mapBackgroundImage = new Image(mapBackground);
        //location.setPosition(16,16);

        mapTable = new Table();
        mapTable.setBackground(new TextureRegionDrawable(map));
     // mapTable.setSize(448,174);
       // mapTable.setFillParent(true);

        for(int i = 0;i<28;i++) {
            locations.add(new Image(new TextureRegionDrawable(mapLocation)));


        }

        mapTable.add(locations.get(0)).size(8,8).pad(24);
        mapTable.add(locations.get(1)).size(8,8).pad(24);
        mapTable.add(locations.get(2)).size(8,8).pad(24);
        mapTable.add(locations.get(3)).size(8,8).pad(24);
        mapTable.add(locations.get(4)).size(8,8).pad(24);
        mapTable.add(locations.get(5)).size(8,8).pad(24);
        mapTable.add(locations.get(6)).size(8,8).pad(24);
        mapTable.row();
        mapTable.add(locations.get(7)).size(8,8).pad(24);
        mapTable.add(locations.get(8)).size(8,8).pad(24);
        mapTable.add(locations.get(9)).size(8,8).pad(24);
        mapTable.add(locations.get(10)).size(8,8).pad(24);
        mapTable.add(locations.get(11)).size(8,8).pad(24);
        mapTable.add(locations.get(12)).size(8,8).pad(24);
        mapTable.add(locations.get(13)).size(8,8).pad(24);
        mapTable.row();
        mapTable.add(locations.get(14)).size(8,8).pad(24);
        mapTable.add(locations.get(15)).size(8,8).pad(24);
        mapTable.add(locations.get(16)).size(8,8).pad(24);
        mapTable.add(locations.get(17)).size(8,8).pad(24);
        mapTable.add(locations.get(18)).size(8,8).pad(24);
        mapTable.add(locations.get(19)).size(8,8).pad(24);
        mapTable.add(locations.get(20)).size(8,8).pad(24);
        mapTable.row();
        mapTable.add(locations.get(21)).size(8,8).pad(24);
        mapTable.add(locations.get(22)).size(8,8).pad(24);
        mapTable.add(locations.get(23)).size(8,8).pad(24);
        mapTable.add(locations.get(24)).size(8,8).pad(24);
        mapTable.add(locations.get(25)).size(8,8).pad(24);
        mapTable.add(locations.get(26)).size(8,8).pad(24);
        mapTable.add(locations.get(27)).size(8,8).pad(24);

        for(int i=0;i<mapTable.getCells().size;i++){
            mapTable.getCells().get(i).getActor().setVisible(false);
            if(i==14){
                mapTable.getCells().get(i).getActor().setVisible(true);
            }
        }



        //mapTable.getCells().get(1).getActor().setVisible(false);

        //mapTable.debugTable();

        Stack mapStack = new Stack();
        mapStack.setSize(mapTable.getWidth(),mapTable.getHeight());
       // mapStack.addActor(mapBackgroundImage);
        mapStack.addActor(mapImage);
        mapStack.addActor(mapTable);
        //mapStack.addActor(location);



        this.pad(this.getPadTop()+50,10,10,10);

        this.row().expandX().pad(0,80,0,80);

        this.add(mapTable).size(mapTable.getPrefWidth(),mapTable.getPrefHeight());


        //this.setClip(false);
        //this.setKeepWithinStage(true);
        this.pack();
       // this.debug();
    }

    public void changeLocation(float currentGroup){

        for(int i=0;i<mapTable.getCells().size;i++){
            mapTable.getCells().get(i).getActor().setVisible(false);
        }

        mapTable.getCells().get((int) currentGroup).getActor().setVisible(true);




    }

}
