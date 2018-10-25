package Observers;

public interface StatusObserver {

    enum StatusEvent
    {
        UPDATED_HEALTH,
        UPDATED_DRAGOONS,
        UPDATED_POTIONS,
        UPDATED_LOCATION,
        UPDATED_WEAPONS
    }

    void onNotify(StatusEvent event, final int value);

}
