package br.com.esig.desafio.gestaotarefas.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {

    private static final EntityManagerFactory FACTORY;

    static {
        EntityManagerFactory factoryTemp;

        String databaseUrl = System.getenv("DATABASE_URL"); // Railway-style

        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            try {
                // Caso use o railway:
                String rawUrl = databaseUrl.replace("postgresql://", "");
                String[] parts = rawUrl.split("@");
                String[] userInfo = parts[0].split(":");
                String[] hostDb = parts[1].split("/");

                String user = userInfo[0];
                String password = userInfo[1];
                String hostPort = hostDb[0];
                String dbName = hostDb[1];

                String jdbcUrl = "jdbc:postgresql://" + hostPort + "/" + dbName + "?sslmode=require";

                Map<String, String> props = new HashMap<>();
                props.put("javax.persistence.jdbc.url", jdbcUrl);
                props.put("javax.persistence.jdbc.user", user);
                props.put("javax.persistence.jdbc.password", password);
                props.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
                props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                props.put("hibernate.hbm2ddl.auto", "update");
                props.put("hibernate.show_sql", "true");
                props.put("hibernate.format_sql", "true");

                factoryTemp = Persistence.createEntityManagerFactory("desafio-esig", props);
            } catch (Exception e) {
                throw new ExceptionInInitializerError("Erro ao configurar JPA com DATABASE_URL: " + e);
            }
        } else {
            // fallback: usa persistence.xml
            try {
                factoryTemp = Persistence.createEntityManagerFactory("desafio-esig");
            } catch (Exception e) {
                throw new ExceptionInInitializerError("Erro ao criar EntityManagerFactory com persistence.xml: " + e);
            }
        }

        FACTORY = factoryTemp;
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
