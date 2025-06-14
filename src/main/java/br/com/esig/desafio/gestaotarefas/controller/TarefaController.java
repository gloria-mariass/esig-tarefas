package br.com.esig.desafio.gestaotarefas.controller;

import br.com.esig.desafio.gestaotarefas.dao.TarefaDAO;
import br.com.esig.desafio.gestaotarefas.modelo.Tarefa;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class TarefaController implements Serializable {

    @Inject
    private TarefaDAO tarefaDAO;

    private List<Tarefa> tarefas;

    @PostConstruct
    public void init() {
        // this.tarefas = tarefaDAO.listar(numero, tituloOuDescricao, responsavel, status);
        // Coloque aqui sua lógica inicial para carregar as tarefas
        this.tarefas = tarefaDAO.listarTodos(); // Exemplo
    }

    // Getters e Setters para 'tarefas'
    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public void excluir(Tarefa tarefa) {
        if (tarefa != null) {
            tarefaDAO.excluir(tarefa.getNumero());

            this.tarefas.remove(tarefa);
        }
    }
}