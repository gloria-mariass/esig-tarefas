package br.com.esig.desafio.gestaotarefas.controller;

import br.com.esig.desafio.gestaotarefas.dao.TarefaDAO;
import br.com.esig.desafio.gestaotarefas.modelo.Tarefa;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class AgendaController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AgendaController.class.getName());

    @Inject
    private TarefaDAO tarefaDAO;

    private ScheduleModel modeloAgenda;
    private List<Tarefa> tarefasSelecionadas = new ArrayList<>();
    private LocalDate dataSelecionada;

    @PostConstruct
    public void init() {
        LOGGER.info("AgendaController: Inicializando bean.");
        modeloAgenda = new DefaultScheduleModel();
        carregarEventos();
        LOGGER.info("AgendaController: Bean inicializado com sucesso.");
    }

    private void carregarEventos() {
        LOGGER.info("AgendaController: Carregando eventos do calendário.");
        List<Tarefa> tarefas = buscarTarefasComDeadline();

        if (tarefas == null || tarefas.isEmpty()) {
            LOGGER.info("AgendaController: Nenhuma tarefa com deadline encontrada.");
            return;
        }

        for (Tarefa tarefa : tarefas) {
            if (tarefa.getDeadline() != null) {
                LocalDate localDateDeadline = tarefa.getDeadline().toLocalDate();
                LocalDateTime startOfDayDateTime = localDateDeadline.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();

                ScheduleEvent<?> evento = DefaultScheduleEvent.builder()
                        .title("Entrega: " + tarefa.getTitulo())
                        .startDate(startOfDayDateTime)
                        .endDate(startOfDayDateTime)
                        .allDay(true)
                        .editable(false)
                        .build();

                modeloAgenda.addEvent(evento);
                LOGGER.fine("Evento adicionado: " + tarefa.getTitulo() + " em " + localDateDeadline);
            } else {
                LOGGER.warning("Tarefa com ID " + tarefa.getNumero() + " possui deadline nula e será ignorada.");
            }
        }

        LOGGER.info("AgendaController: Eventos do calendário carregados.");
    }

    public ScheduleModel getModeloAgenda() {
        return modeloAgenda;
    }

    private List<Tarefa> buscarTarefasComDeadline() {
        return tarefaDAO.buscarTarefasComDeadline();
    }

    private List<Tarefa> buscarTarefasPorData(LocalDate data) {
        return buscarTarefasComDeadline().stream()
                .filter(t -> t.getDeadline() != null && t.getDeadline().toLocalDate().equals(data))
                .collect(Collectors.toList());
    }

    public List<Tarefa> getTarefasSelecionadas() {
        return tarefasSelecionadas;
    }

    /**
     * Gera JSON manual de datas com deadline.
     */
    public String getDatasComDeadlineJson() {
        List<Tarefa> tarefas = buscarTarefasComDeadline();

        List<String> datas = tarefas.stream()
                .filter(t -> t.getDeadline() != null)
                .map(t -> t.getDeadline().toLocalDate().toString())
                .collect(Collectors.toList());

        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < datas.size(); i++) {
            jsonBuilder.append("\"").append(datas.get(i)).append("\"");
            if (i < datas.size() - 1) {
                jsonBuilder.append(", ");
            }
        }
        jsonBuilder.append("]");

        return jsonBuilder.toString();
    }

    public void onDataSelecionada() {
        System.out.println(">>> Data selecionada: " + dataSelecionada);
        this.tarefasSelecionadas = buscarTarefasPorData(dataSelecionada);
    }

    public Object getDataSelecionada() {
        return dataSelecionada;
    }

    public void setDataSelecionada(LocalDate dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }

    public Date getDataSelecionadaComoDate() {
        if (dataSelecionada == null) return null;
        return Date.from(dataSelecionada.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
