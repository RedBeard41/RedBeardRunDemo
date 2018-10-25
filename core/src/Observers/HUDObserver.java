package Observers;

public interface HUDObserver {

    enum HUDEvent
    {
        EXIT_CONVERSATION,
        ACCEPT_QUEST,
        DENY_QUEST,
        BOUGHT_POTION,
        BOUGHT_BOW,
        USE_POTION,
        PREPARE_FOR_OTHER_PURCHASES,
        NONE
    }

    void onNotify(HUDEvent event, final int value);

}
