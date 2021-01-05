package Interfaces;

import org.hibernate.Session;

public interface WrappableWithArg<T, A> {
    public T wrap(Session session, A arg);
}
