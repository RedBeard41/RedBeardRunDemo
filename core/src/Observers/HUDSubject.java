package Observers;

public interface HUDSubject {
    public void addObserver(HUDObserver observer);
    public void removeObserver(HUDObserver observer);
    public void removeAllObservers();
    public void notify(HUDObserver.HUDEvent event, final int value);
}
