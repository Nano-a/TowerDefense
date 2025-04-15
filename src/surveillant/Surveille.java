package surveillant;

import java.util.ArrayList;
import java.util.List;

public abstract class Surveille {

    List<Gardien> observers;

    public Surveille() {
        observers = new ArrayList<>();

    }

    public void addObserver(Gardien o) {
        observers.add(o);
    }

    public void notifyObservers(String msg) {
        for (Gardien o : observers) {
            o.update(this, msg);
        }
    }
}
