package Conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class ConversationUI extends Window {
    private static final String TAG = ConversationUI.class.getSimpleName();

    private Skin skin;
    private Label dialogText;
    private List listItems;
    private ConversationGraph graph;
    private String currentEntityID;

    private TextButton closeButton;

    private Json json;

    public ConversationUI(String title, Skin skin) {
        super(title, skin);
        this.skin = skin;

        json = new Json();
        graph = new ConversationGraph();

        dialogText = new Label("", this.skin);
        dialogText.setWrap(true);
        dialogText.setAlignment(Align.center);

        listItems = new List<ConversationChoice>(this.skin);

        closeButton = new TextButton("X", this.skin);

        ScrollPane scrollPane = new ScrollPane(listItems, this.skin);
        scrollPane.setOverscroll(false,false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(true,false);
        scrollPane.setScrollBarPositions(false,true);

        this.pad(this.getPadTop()+50,10,10,10);
        this.add();
        this.add(closeButton);
        this.row();

        this.defaults().expand().fill();
        this.add(dialogText).pad(10);
        this.row();
        this.add(scrollPane).pad(10);
        this.debug();
        this.pack();

        /*listItems.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                ConversationChoice choice = (ConversationChoice) listItems.getSelected();
                if(choice == null){
                    return;
                }
                graph.notify(choice.getConversationCommandEvent(),graph);
                populateConversationDialog(choice.getDestinationId());
            }
        });*/
    }

    public List getListItems() {
        return listItems;
    }

    public void setListItems(List listItems) {
        this.listItems = listItems;
    }

    public void setGraph(ConversationGraph graph) {
        this.graph = graph;
    }

    public void loadConversation(int value){
        String filePath = "";
        this.getTitleLabel().setText("");

        clearDialog();

        switch(value){
            case 1:
                filePath = "scripts/Conversation1.json";
                currentEntityID = "Backstory Magic Girl";
                break;

            case 2:
                filePath = "scripts/Conversation2.json";
                currentEntityID = "Magic Girl";
                break;
            case 3:
                filePath = "scripts/Conversation3.json";
                currentEntityID = "The Collector";
                break;
                default:
                    break;
        }

        if(filePath.isEmpty() || !Gdx.files.internal(filePath).exists()){
            Gdx.app.log(TAG,"Conversation file does not exist!");
            return;
        }

        this.getTitleLabel().setText(currentEntityID);

        ConversationGraph graph = json.fromJson(ConversationGraph.class, Gdx.files.internal(filePath));
        setConversationGraph(graph);
    }

    public ConversationGraph getGraph() {
        return graph;
    }

    public void setConversationGraph(ConversationGraph graph) {
        if(graph != null){
            graph.removeAllObservers();
        }
        this.graph = graph;
        populateConversationDialog(graph.getCurrenConversationId());
    }

    public void populateConversationDialog(String conversationId){
        clearDialog();

        Conversation conversation = graph.getConversationById(conversationId);
        if(conversation == null){
            return;
        }
        graph.setCurrentConversation(conversationId);
        dialogText.setText(conversation.getDialog());
        Array<ConversationChoice> choices = graph.getCurrentChoices();
        if(choices == null){
            return;
        }
        listItems.setItems(choices);
        listItems.setSelectedIndex(-1);
    }

    public String getCurrentEntityID() {
        return currentEntityID;
    }



    public TextButton getCloseButton() {
        return closeButton;
    }

    private void clearDialog(){
        dialogText.setText("");
        listItems.clearItems();
    }


}
