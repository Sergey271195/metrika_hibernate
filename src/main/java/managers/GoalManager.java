package managers;

import Interfaces.SessionManager;
import Interfaces.WrappableWithArg;
import components.SessionWrapper;
import models.Goal;
import models.Webpage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class GoalManager implements WrappableWithArg<List<Goal>, Webpage> {

    public static Goal createGoal(long goalId, String name, String type, Webpage webpage) {
        Goal newGoal = new Goal();
        newGoal.setGoalId(goalId);
        newGoal.setName(name);
        newGoal.setType(type);
        newGoal.setWebpage(webpage);
        return newGoal;
    }

    public static List<Goal> getAllGoalsFromDB(Session session) {
        Transaction tx = session.beginTransaction();
        List<Goal> goals = session
                .createQuery("FROM Goal")
                .getResultList();
        tx.commit();
        return goals;
    }

    public static List<Goal> getAllGoalsFromDBForCounter(SessionManager manager, Webpage webpage) {
        Session session = manager.startSession();
        Transaction tx = session.beginTransaction();
        List<Goal> goals = session
                .createQuery("FROM Goal goal where goal.webpage.pageId = '" + webpage.getPageId() + "'")
                .getResultList();
        tx.commit();
        session.close();
        return goals;
    }

    public static List<Goal> getAllGoalsFromDBForCounter(Session session, Webpage webpage) {
        List<Goal> goals = session
                .createQuery("FROM Goal goal where goal.webpage.pageId = '" + webpage.getPageId() + "'")
                .getResultList();
        return goals;
    }


    public static void saveGoalToDB(Session session, Goal goal) {session.persist(goal);}

    public List<Goal> getGoalsForCounterWrapped(Webpage webpage)  {
        return SessionWrapper.wrapWithArg(this::wrap, webpage);
    }

    @Override
    public List<Goal> wrap(Session session, Webpage webpage) {
        return getAllGoalsFromDBForCounter(session, webpage);
    }
}
