package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

import java.util.HashMap;

public class PlayerComponent implements Component, Poolable{
    private boolean dead = false;
    private boolean hold = false;
    private float currentArea = 14f;
    private float currentGroup = 14f;
    private float pastGroup = 0f;
    private float cameraX = 128f;
    private float cameraY = 264;
    private float lerp = 0f;
    private float x = 112f;
    private float y = 256;
    private boolean spawnPastGroupEntities = false;
    private float health=120;
    private Array<InteractiveComponent.TYPE> weapons = new Array<>();
    private float dragoons = 0;
    private float potions = 0;
    private boolean pause = false;
    private boolean playedDyingSound = false;
    private boolean firstTime = true;

    //data for inventory
    private float loadArea;
    private float loadGroup;
    private float loadCameraX;
    private float loadCameraY;
    private float loadLerp;
    private float loadX;
    private float loadY;
    private float loadHealth;
    private Array<InteractiveComponent.TYPE>loadWeapons;
    private float loadDragoons;


    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {

        this.health = health;
    }

    public void addHealth(float amount){
        if(this.health + amount >120){
            this.health = 120;
        }
        else if(this.health + amount<0) {
            this.health = 0;
            //this.setDead(true);
        } else{
            this.health += amount;
        }
    }

    public float getPotions() {
        return potions;
    }

    public void setPotions(float potions) {
        this.potions = potions;
    }

    public void addPotion(){
        this.potions++;
    }

    public Array<InteractiveComponent.TYPE> getWeapons() {
        return weapons;
    }

    public void setWeapons(Array<InteractiveComponent.TYPE> weapons) {
        this.weapons = weapons;
    }

    public void addWeapon(InteractiveComponent.TYPE type){
        weapons.add(type);
    }



    public boolean checkWeapon(InteractiveComponent.TYPE type){
        return weapons.contains(type,true);

    }

    public float getDragoons() {
        return dragoons;
    }

    public void addDragoons(float amount){
        this.dragoons+=amount;


    }

    public void setDragoons(float dragoons) {
        this.loadDragoons = this.dragoons;
        this.dragoons = dragoons;
    }

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public float getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(float currentArea) {
        this.loadArea = this.currentArea;
        this.currentArea = currentArea;
    }

    public float getCurrentGroup() {

        return currentGroup;
    }

    public void setCurrentGroup(float currentGroup) {

        this.loadGroup = this.currentGroup;
        this.currentGroup = currentGroup;
    }

    public float getPastGroup() {
        return pastGroup;
    }

    public void setPastGroup(float pastGroup) {
        this.pastGroup = pastGroup;
    }

    public float getCameraX() {
        return cameraX;
    }

    public void setCameraX(float cameraX) {
        this.loadCameraX = this.cameraX;
        this.cameraX = cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }

    public void setCameraY(float cameraY) {
        this.loadCameraY = this.cameraY;
        this.cameraY = cameraY;
    }

    public float getLerp() {
        return lerp;
    }

    public void setLerp(float lerp) {
        this.loadLerp = this.lerp;
        this.lerp = lerp;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.loadX = this.x;
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.loadY = this.y;
        this.y = y;
    }

    public boolean isSpawnPastGroupEntities() {
        return spawnPastGroupEntities;
    }

    public void setSpawnPastGroupEntities(boolean spawnedPastGroupEntities) {
        this.spawnPastGroupEntities = spawnedPastGroupEntities;
    }

    public float getLoadArea() {
        return loadArea;
    }

    public float getLoadGroup() {
        return loadGroup;
    }

    public float getLoadCameraX() {
        return loadCameraX;
    }

    public float getLoadCameraY() {
        return loadCameraY;
    }

    public float getLoadLerp() {
        return loadLerp;
    }

    public float getLoadX() {
        return loadX;
    }

    public float getLoadY() {
        return loadY;
    }

    public float getLoadHealth() {
        return loadHealth;
    }

    public Array<InteractiveComponent.TYPE> getLoadWeapons() {
        return loadWeapons;
    }

    public void setLoadWeapons(Array<InteractiveComponent.TYPE> currentWeapons){
        loadWeapons.addAll(currentWeapons);
    }

    public void setLoadHealth (float currrentHealth){
        this.loadHealth = currrentHealth;
    }

    public boolean isPlayedDyingSound() {
        return playedDyingSound;
    }

    public void setPlayedDyingSound(boolean playedDyingSound) {
        this.playedDyingSound = playedDyingSound;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    @Override
    public void reset() {
        dead = false;
        hold = false;
        currentArea = 14f;
        currentGroup = 14f;
        pastGroup = 0f;
        cameraX = 128f;
        cameraY = 264;
        lerp = 0f;
        x = 112f;
        y = 256;
        spawnPastGroupEntities = false;
        health = 120;
        weapons.clear();
        dragoons = 0;
        pause = false;
        weapons.clear();

        potions = 0f;
        playedDyingSound = false;
        firstTime = true;
    }
}
