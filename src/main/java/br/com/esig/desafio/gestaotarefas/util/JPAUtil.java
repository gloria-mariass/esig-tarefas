package br.com.esig.desafio.gestaotarefas.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("desafio-esig");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}
