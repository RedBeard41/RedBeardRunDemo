package Conversation;

import com.badlogic.gdx.utils.Array;

import java.util.Hashtable;

public class ConversationGraph implements ConversationGraphSubject {

    private Hashtable<String, Conversation> conversations;
    private Hashtable<String, Array<ConversationChoice>> associatedChoices;
    private String currentConversationId = null;

    private Array<ConversationGraphObserver> observers;

    public ConversationGraph() {
        observers = new Array<ConversationGraphObserver>();
    }

    public ConversationGraph(Hashtable<String, Conversation> conversations, String rootID) {
        setConversations(conversations);
        observers = new Array<ConversationGraphObserver>();
    }

    public void setConversations(Hashtable<String, Conversation> conversations){
        this.conversations = conversations;
        this.associatedChoices = new Hashtable<String, Array<ConversationChoice>>(conversations.size());

        for(Conversation conversation: conversations.values()){
            associatedChoices.put(conversation.getId(), new Array<ConversationChoice>());
        }
    }


    public void setCurrentConversation(String Id){
        Conversation conversation = getConversationById(Id);
        if(conversation == null){
            return;
        }
        if(currentConversationId == null ||
                currentConversationId.equalsIgnoreCase(Id)||
                isReachable(currentConversationId,Id)){
            currentConversationId = Id;
        }
    }

    public Conversation getConversationById(String id){
        if(!isValid(id)){
            return null;
        }
        return conversations.get(id);
    }

    public boolean isReachable(String sourceId, String syncId){
        if(!isValid(sourceId) || !isValid(syncId)){
            return false;
        }

        if(conversations.get(sourceId) == null){
            return false;
        }

        Array<ConversationChoice> list = associatedChoices.get(sourceId);
        if(list == null){
            return false;
        }
        for(ConversationChoice choice: list){
            if(choice.getSourceId().equalsIgnoreCase(sourceId)&&
                    choice.getDestinationId().equalsIgnoreCase(syncId)){
                return true;
            }
        }
        return false;
    }

    public boolean isValid(String id){
        Conversation conversation = conversations.get(id);
        if(conversation == null){
            return false;
        }
        return true;
    }

    public String displayCurrentConversation(){
        return conversations.get(currentConversationId).getDialog();
    }

    public String getCurrenConversationId(){
        return this.currentConversationId;
    }

    public Array<ConversationChoice> getCurrentChoices(){
        return associatedChoices.get(currentConversationId);
    }

    @Override
    public void addObserver(ConversationGraphObserver graphObserver) {
        observers.add(graphObserver);

    }

    @Override
    public void removeObserver(ConversationGraphObserver graphObserver) {
        observers.removeValue(graphObserver,true);

    }

    @Override
    public void removeAllObservers() {
        for(ConversationGraphObserver observer: observers){
            observers.removeValue(observer,true);
        }

    }

    @Override
    public void notify(ConversationGraphObserver.ConversationCommandEvent event, ConversationGraph graph) {
        for(ConversationGraphObserver observer: observers){
            observer.onNotify(event,graph);
        }
    }
}
