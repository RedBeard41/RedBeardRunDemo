package Conversation;

import com.badlogic.gdx.utils.Array;

public interface ConversationGraphSubject {



    public void addObserver(ConversationGraphObserver graphObserver);

    public void removeObserver(ConversationGraphObserver graphObserver);

    public void removeAllObservers();

    public void notify(ConversationGraphObserver.ConversationCommandEvent event, final ConversationGraph graph);


}
