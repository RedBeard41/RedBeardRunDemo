package Conversation;

public interface ConversationGraphObserver {

    enum ConversationCommandEvent {
        EXIT_CONVERSATION,
        ACCEPT_QUEST,
        DENY_QUEST,
        BOUGHT_POTION,
        BOUGHT_BOW,
        NONE
    }

    void onNotify(ConversationCommandEvent event, final ConversationGraph graph);
}
