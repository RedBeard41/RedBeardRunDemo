package Observers;

public interface StatusSubject {
    public void addObserver(StatusObserver observer);
    public void removeObserver(StatusObserver observer);
    public void removeAllObservers();
    public void notify(StatusObserver.StatusEvent event, final int value);
}
