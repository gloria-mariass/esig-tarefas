package br.com.esig.desafio.gestaotarefas.controller;

import br.com.esig.desafio.gestaotarefas.dao.TarefaDAO;
import br.com.esig.desafio.gestaotarefas.modelo.Colaborador;
import br.com.esig.desafio.gestaotarefas.modelo.Prioridade;
import br.com.esig.desafio.gestaotarefas.modelo.Situacao;
import br.com.esig.desafio.gestaotarefas.modelo.Tarefa;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class TarefaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TarefaController.class.getName());

    @Inject
    private TarefaDAO tarefaDAO;

    private List<Tarefa> tarefas;
    private Tarefa tarefa;
    private Long idTarefa;
    private Tarefa tarefaSelecionada;
    private Long filtroNumero;
    private String filtroTituloDescricao;
    private Colaborador filtroResponsavel;
    private String filtroSituacao = Situacao.EM_ANDAMENTO.toString();

    @PostConstruct
    public void inicializarBean() {
        try {
            if (idTarefa != null) {
                this.tarefa = tarefaDAO.buscarPorNumero(idTarefa);
                if (this.tarefa == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Tarefa não encontrada para edição!"));
                }
            } else {
                this.tarefa = new Tarefa();
            }

            if (tarefas == null) {
                filtrar();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao inicializar o bean de tarefas", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar tarefas: " + e.getMessage()));
        }
    }

    public void carregarTarefaParaEdicao() {
        try {
            if (idTarefa != null) {
                this.tarefa = tarefaDAO.buscarTarefaComResponsavel(idTarefa);
                if (this.tarefa == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Tarefa não encontrada."));
                    FacesContext.getCurrentInstance().getExternalContext().redirect("listagem.xhtml");
                }
            } else {
                this.tarefa = new Tarefa();
                this.tarefa.setSituacao(Situacao.EM_ANDAMENTO);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao carregar tarefa para edição", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar tarefa: " + e.getMessage()));
        }
    }

    public String salvar() {
        try {
            if (tarefa.getNumero() == null) {
                tarefa.setSituacao(Situacao.EM_ANDAMENTO);
            }
            tarefaDAO.salvar(tarefa);
            return "listagem.xhtml?faces-redirect=true";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar tarefa", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar tarefa: " + e.getMessage()));
            return null;
        }
    }

    public Prioridade[] getPrioridades() {
        return Prioridade.values();
    }

    public String filtrar() {
        try {
            this.tarefas = tarefaDAO.listarComFiltros(filtroNumero, filtroTituloDescricao, filtroResponsavel, filtroSituacao);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao filtrar tarefas", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar tarefas: " + e.getMessage()));
        }
        return null;
    }

    public String excluir() {
        if (this.idTarefa != null) {
            try {
                tarefaDAO.excluir(this.idTarefa);
                this.tarefas.removeIf(t -> t.getNumero().equals(this.idTarefa));
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Tarefa excluída com sucesso!"));
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao excluir tarefa", e);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir tarefa: " + e.getMessage()));
            }
        }
        return null;
    }

    public String concluir() {
        if (this.idTarefa != null) {
            try {
                Tarefa tarefaParaConcluir = tarefaDAO.buscarPorNumero(this.idTarefa);
                if (tarefaParaConcluir != null) {
                    tarefaParaConcluir.setSituacao(Situacao.CONCLUIDA);
                    tarefaDAO.salvar(tarefaParaConcluir);
                    filtrar();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Tarefa concluída com sucesso!"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Tarefa não encontrada para concluir!"));
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao concluir tarefa", e);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao concluir tarefa: " + e.getMessage()));
            }
        }
        return null;
    }

    public String navegarParaCadastro() {
        this.idTarefa = null;
        this.tarefa = new Tarefa();
        return "cadastro_tarefas.xhtml?faces-redirect=true";
    }

    public void onRowSelect(SelectEvent<Tarefa> event) {
        this.tarefa = event.getObject();
    }

    // --- GETTERS E SETTERS ---

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Long getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(Long idTarefa) {
        this.idTarefa = idTarefa;
    }

    public Long getFiltroNumero() {
        return filtroNumero;
    }

    public void setFiltroNumero(Long filtroNumero) {
        this.filtroNumero = filtroNumero;
    }

    public String getFiltroTituloDescricao() {
        return filtroTituloDescricao;
    }

    public void setFiltroTituloDescricao(String filtroTituloDescricao) {
        this.filtroTituloDescricao = filtroTituloDescricao;
    }

    public Colaborador getFiltroResponsavel() {
        return filtroResponsavel;
    }

    public void setFiltroResponsavel(Colaborador filtroResponsavel) {
        this.filtroResponsavel = filtroResponsavel;
    }

    public String getFiltroSituacao() {
        return filtroSituacao;
    }

    public void setFiltroSituacao(String filtroSituacao) {
        this.filtroSituacao = filtroSituacao;
    }

    public Tarefa getTarefaSelecionada() {
        return tarefaSelecionada;
    }

    public void setTarefaSelecionada(Tarefa tarefaSelecionada) {
        this.tarefaSelecionada = tarefaSelecionada;
    }
}