package br.com.esig.desafio.gestaotarefas.modelo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numero;

    private String titulo;
    private String descricao;
    private String responsavel;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private Situacao situacao = Situacao.EM_ANDAMENTO;

    // Getters e Setters
    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
}
