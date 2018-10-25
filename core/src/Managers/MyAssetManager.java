package Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class MyAssetManager {
    private static final String TAG = MyAssetManager.class.getSimpleName();

    private AssetManager assetManager;

    public MyAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void unloadAsset (String assetFilepath){
        if(assetManager.isLoaded(assetFilepath)){
            assetManager.unload(assetFilepath);
        }
        else{
            Gdx.app.log(TAG,"Asset is not in memory"+ assetFilepath);
        }
    }

    public float loadCompleted(){
        return assetManager.getProgress();
    }

    public int numberAssetsQueued(){
        return assetManager.getQueuedAssets();
    }

    public boolean updateAssetLoading(){
        return assetManager.update();
    }

    public boolean isAssetLoaded(String fileName){
        return assetManager.isLoaded(fileName);
    }

    public void loadMapAsset (String mapfilepath){
        if(mapfilepath == null||mapfilepath.isEmpty()){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(mapfilepath).exists()){
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
            assetManager.load(mapfilepath,TiledMap.class);
            //assetManager.finishLoadingAsset(mapfilepath);
            Gdx.app.log(TAG,"Map Loaded: "+ mapfilepath);
        }
        else{
            Gdx.app.log(TAG,"Map doesn't Exist: "+ mapfilepath);
        }

    }

    public TiledMap getMapAsset (String mapfilepath){
        TiledMap map = null;

        if(assetManager.isLoaded(mapfilepath)){
            map = assetManager.get(mapfilepath,TiledMap.class);
        }
        else {
            Gdx.app.log(TAG, "Map is not loaded: " + mapfilepath);
        }
        return map;
    }

    public void loadTextureAsset(String textureFilePath){
        if(textureFilePath == null || textureFilePath.isEmpty()){
            return;
        }

        if(assetManager.isLoaded(textureFilePath)){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(textureFilePath).exists()){
            assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(assetManager.getFileHandleResolver()));
            assetManager.load(textureFilePath, TextureAtlas.class);
            // assetManager.finishLoadingAsset(textureFilePath);
            Gdx.app.log(TAG,"Texture Loaded: "+ textureFilePath);
        }
        else{
            Gdx.app.log(TAG,"Texture doesn't exist: "+ textureFilePath);
        }

    }

    public TextureAtlas getTextureAsset (String textureFilePath){
        TextureAtlas atlas = null;

        if (assetManager.isLoaded(textureFilePath)) {
            Gdx.app.log(TAG,textureFilePath + " is loaded");
            atlas = assetManager.get(textureFilePath,TextureAtlas.class);
        }
        else{
            Gdx.app.log(TAG, "Texture is not loaded: " + textureFilePath);
        }
        return atlas;
    }

    public void loadingLoadingTextureAtlasAsset(String textureFilePath){
        if(textureFilePath == null || textureFilePath.isEmpty()){
            return;
        }

        if(assetManager.isLoaded(textureFilePath)){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(textureFilePath).exists()){
            assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(assetManager.getFileHandleResolver()));
            assetManager.load(textureFilePath, TextureAtlas.class);
            assetManager.finishLoadingAsset(textureFilePath);
            Gdx.app.log(TAG,"Loading screen Loaded: "+ textureFilePath);
        }
        else{
            Gdx.app.log(TAG,"Loading screen asset doesn't exist: "+ textureFilePath);
        }
    }

    public void loadingFontAsset(String fontFilePath){
        if(fontFilePath == null || fontFilePath.isEmpty()){
            return;
        }

        if(assetManager.isLoaded(fontFilePath)){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(fontFilePath).exists()){

            assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assetManager.getFileHandleResolver()));
            assetManager.setLoader(BitmapFont.class, ".ttf",new FreetypeFontLoader(assetManager.getFileHandleResolver()));

            FreetypeFontLoader.FreeTypeFontLoaderParameter parms = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            parms.fontFileName = fontFilePath;    // path of .ttf file where that exist
            parms.fontParameters.size = 20;
            assetManager.load(fontFilePath, BitmapFont.class, parms);

            assetManager.finishLoadingAsset(fontFilePath);
            Gdx.app.log(TAG,"Font Loaded: "+ fontFilePath);
        }
        else{
            Gdx.app.log(TAG,"Font asset doesn't exist: "+ fontFilePath);
        }
    }

    public void loadingLoadingFontAsset(String fontFilePath){
        if(fontFilePath == null || fontFilePath.isEmpty()){
            return;
        }

        if(assetManager.isLoaded(fontFilePath)){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(fontFilePath).exists()){

            assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assetManager.getFileHandleResolver()));
            assetManager.setLoader(BitmapFont.class, ".ttf",new FreetypeFontLoader(assetManager.getFileHandleResolver()));

            FreetypeFontLoader.FreeTypeFontLoaderParameter parms = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            parms.fontFileName = fontFilePath;    // path of .ttf file where that exist
            parms.fontParameters.size = 30;
            assetManager.load(fontFilePath, BitmapFont.class, parms);

            assetManager.finishLoadingAsset(fontFilePath);
            Gdx.app.log(TAG,"Font Loaded: "+ fontFilePath);
        }
        else{
            Gdx.app.log(TAG,"Font asset doesn't exist: "+ fontFilePath);
        }
    }

    public BitmapFont getFontAsset (String fontfilePath){
        BitmapFont font= null;

        if (assetManager.isLoaded(fontfilePath)) {
            Gdx.app.log(TAG,fontfilePath + " is loaded");
            font = assetManager.get(fontfilePath,BitmapFont.class);
        }
        else{
            Gdx.app.log(TAG, "sound is not loaded: " + fontfilePath);
        }
        return font;
    }

    public void loadingSoundAsset(String soundfilePath){
        if(soundfilePath == null || soundfilePath.isEmpty()){
            return;
        }

        if(assetManager.isLoaded(soundfilePath)){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(soundfilePath).exists()){
            assetManager.setLoader(Sound.class, new SoundLoader(assetManager.getFileHandleResolver()));
            assetManager.load(soundfilePath, Sound.class);
            //assetManager.finishLoadingAsset(soundfilePath);
            //Gdx.app.log(TAG,"sound Loaded: "+ soundfilePath);
        }
        else{
            Gdx.app.log(TAG,"sound doesn't exist: "+ soundfilePath);
        }
    }

    public Sound getSoundAsset (String soungfilePath){
        Sound sound = null;

        if (assetManager.isLoaded(soungfilePath)) {
            Gdx.app.log(TAG,soungfilePath + " is loaded");
            sound = assetManager.get(soungfilePath,Sound.class);
        }
        else{
            Gdx.app.log(TAG, "sound is not loaded: " + soungfilePath);
        }
        return sound;
    }

    public void loadingMusicAsset(String musicfilePath){
        if(musicfilePath == null || musicfilePath.isEmpty()){
            return;
        }

        if(assetManager.isLoaded(musicfilePath)){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(musicfilePath).exists()){
            assetManager.setLoader(Music.class, new MusicLoader(assetManager.getFileHandleResolver()));
            assetManager.load(musicfilePath, Music.class);
            assetManager.finishLoadingAsset(musicfilePath);
            Gdx.app.log(TAG,"music Loaded: "+ musicfilePath);
        }
        else{
            Gdx.app.log(TAG,"music doesn't exist: "+ musicfilePath);
        }
    }

    public Music getMusicAsset (String musicfilePath){
        Music music = null;

        if (assetManager.isLoaded(musicfilePath)) {
            Gdx.app.log(TAG,musicfilePath + " is loaded");
            music = assetManager.get(musicfilePath,Music.class);
        }
        else{
            Gdx.app.log(TAG, "music is not loaded: " + musicfilePath);
        }
        return music;
    }

    public void loadParticleAsset(String particlefilePath){
        if(particlefilePath == null || particlefilePath.isEmpty()){
            return;
        }

        if(assetManager.isLoaded(particlefilePath)){
            return;
        }

        if(assetManager.getFileHandleResolver().resolve(particlefilePath).exists()){
            assetManager.setLoader(ParticleEffect.class, new ParticleEffectLoader(assetManager.getFileHandleResolver()));
            ParticleEffectLoader.ParticleEffectParameter effectParameter = new ParticleEffectLoader.ParticleEffectParameter();
            effectParameter.atlasFile = "sprites/Output/RedBeardTest.atlas";
            assetManager.load(particlefilePath, ParticleEffect.class,effectParameter);
            // assetManager.finishLoadingAsset(particlefilePath);
            Gdx.app.log(TAG,"particle Loaded: "+ particlefilePath);
        }
        else{
            Gdx.app.log(TAG,"particle doesn't exist: "+ particlefilePath);
        }
    }

    public ParticleEffect getParticleAsset (String particlefilePath){
        ParticleEffect particle = null;

        if (assetManager.isLoaded(particlefilePath)) {
            Gdx.app.log(TAG,particlefilePath + " is loaded");
            particle = assetManager.get(particlefilePath,ParticleEffect.class);
        }
        else{
            Gdx.app.log(TAG, "particle is not loaded: " + particlefilePath);
        }
        return particle;
    }


    public void dispose(){



    }
}
