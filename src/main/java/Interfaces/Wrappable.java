package Interfaces;

import org.hibernate.Session;

public interface Wrappable<T> {
    public T wrap(Session session);
}
