package Interfaces;

import org.hibernate.Session;

public interface WrappableWithStringArg<T> {
    public T wrap(Session session, String arg);
}
