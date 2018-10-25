package Observers;

public interface NPCObserver {

    enum NPCEvent
    {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        EXIT_CONVERSATION,
        DENY_QUEST,
        PURCHASE_GENERATED,
        BOUGHT_BOW,
        NONE
    }

    void onNotify(NPCEvent event, final int value);

}
