package br.com.esig.desafio.gestaotarefas.dao;

import br.com.esig.desafio.gestaotarefas.modelo.Tarefa;
import br.com.esig.desafio.gestaotarefas.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;
import java.io.Serializable;

public class TarefaDAO implements Serializable {

    public void salvar(Tarefa tarefa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tarefa);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void atualizar(Tarefa tarefa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tarefa);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void remover(Long numero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Tarefa tarefa = em.find(Tarefa.class, numero);
            if (tarefa != null) {
                em.remove(tarefa);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Tarefa buscarPorId(Long numero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Tarefa.class, numero);
        } finally {
            em.close();
        }
    }

    public List<Tarefa> listar(Long numero, String tituloOuDescricao, String responsavel, String situacao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT t FROM Tarefa t WHERE 1=1 ");

            if (numero != null) {
                jpql.append("AND t.numero = :numero ");
            }
            if (tituloOuDescricao != null && !tituloOuDescricao.isEmpty()) {
                jpql.append("AND (LOWER(t.titulo) LIKE LOWER(:tituloOuDescricao) OR LOWER(t.descricao) LIKE LOWER(:tituloOuDescricao)) ");
            }
            if (responsavel != null && !responsavel.isEmpty()) {
                jpql.append("AND t.responsavel = :responsavel ");
            }
            if (situacao != null && !situacao.isEmpty()) {
                jpql.append("AND t.situacao = :situacao");
            }

            TypedQuery<Tarefa> query = em.createQuery(jpql.toString(), Tarefa.class);

            if (numero != null) {
                query.setParameter("numero", numero);
            }
            if (tituloOuDescricao != null && !tituloOuDescricao.isEmpty()) {
                query.setParameter("tituloOuDescricao", "%" + tituloOuDescricao + "%");
            }
            if (responsavel != null && !responsavel.isEmpty()) {
                query.setParameter("responsavel", responsavel);
            }
            if (situacao != null && !situacao.isEmpty()) {
                query.setParameter("situacao", br.com.esig.desafio.gestaotarefas.modelo.Situacao.valueOf(situacao));
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean excluir(Long numero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Tarefa tarefaParaExcluir = em.find(Tarefa.class, numero);

            if (tarefaParaExcluir == null) {
                em.getTransaction().rollback();
                return false;
            }

            em.remove(tarefaParaExcluir);
            em.getTransaction().commit();

            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public List<Tarefa> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT t FROM Tarefa t";
            TypedQuery<Tarefa> query = em.createQuery(jpql, Tarefa.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}