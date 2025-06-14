package br.com.esig.desafio.gestaotarefas.controller;

import br.com.esig.desafio.gestaotarefas.dao.ColaboradorDAO;
import br.com.esig.desafio.gestaotarefas.modelo.Colaborador;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ColaboradorController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ColaboradorController.class.getName());

    @Inject
    private ColaboradorDAO colaboradorDAO;

    @PostConstruct
    public void init() {
        List<Colaborador> todosColaboradores;
        try {
            todosColaboradores = colaboradorDAO.buscarTodos();
        } catch (Exception e) {
            todosColaboradores = new ArrayList<>();
            logger.log(Level.SEVERE, "Erro ao buscar todos os colaboradores no init()", e);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro ao carregar colaboradores", e.getMessage()));
        }
    }

    public List<Colaborador> buscarColaboradores(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return colaboradorDAO.buscarTodos();
            }
            return colaboradorDAO.buscarPorNome(query);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar colaboradores com a query: " + query, e);
            return new ArrayList<>();
        }
    }
}
