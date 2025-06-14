package br.com.esig.desafio.gestaotarefas.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory FACTORY;

    static {
        try {
            FACTORY = Persistence.createEntityManagerFactory("desafio-esig");
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Erro ao criar EntityManagerFactory: " + ex);
        }
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void fecharFactory() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
