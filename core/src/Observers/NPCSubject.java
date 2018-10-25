package Observers;

public interface NPCSubject {
    public void addObserver(NPCObserver observer);
    public void removeObserver(NPCObserver observer);
    public void removeAllObservers();
    public void notify(NPCObserver.NPCEvent event, final int value);
}
