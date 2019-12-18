package edu.ucdavis.dss.datawarehouse.sync;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;

public class StatusLogger {
    static public void markIamLastAttempt(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        StatusItem statusItem = entityManager.find(StatusItem.class, "iam");
        if(statusItem == null) {
            statusItem = new StatusItem();
            statusItem.setUpstreamDb("iam");
        }

        statusItem.setLastAttempt(new Date());

        entityManager.getTransaction().begin();
        entityManager.merge(statusItem);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    static public void markIamLastSuccess(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        StatusItem statusItem = entityManager.find(StatusItem.class, "iam");
        if(statusItem == null) {
            statusItem = new StatusItem();
            statusItem.setUpstreamDb("iam");
        }

        statusItem.setLastSuccess(new Date());

        entityManager.getTransaction().begin();
        entityManager.merge(statusItem);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    static public void recordIamDuration(EntityManagerFactory entityManagerFactory, Integer duration) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        StatusItem statusItem = entityManager.find(StatusItem.class, "iam");
        if(statusItem == null) {
            statusItem = new StatusItem();
            statusItem.setUpstreamDb("iam");
        }

        statusItem.setDuration(duration);

        entityManager.getTransaction().begin();
        entityManager.merge(statusItem);
        entityManager.getTransaction().commit();

        entityManager.close();
    }
}
