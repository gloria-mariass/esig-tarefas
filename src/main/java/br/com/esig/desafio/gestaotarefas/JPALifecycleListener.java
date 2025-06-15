package br.com.esig.desafio.gestaotarefas;

import java.util.logging.Logger;
import br.com.esig.desafio.gestaotarefas.util.JPAUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class JPALifecycleListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(JPALifecycleListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("JPALifecycleListener: Aplicação web inicializada. Iniciando JPA.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("JPALifecycleListener: Aplicação web sendo encerrada. Fechando JPA.");
        JPAUtil.fecharFactory();
    }
}
