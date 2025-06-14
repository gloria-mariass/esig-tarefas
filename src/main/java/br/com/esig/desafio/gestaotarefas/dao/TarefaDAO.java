package br.com.esig.desafio.gestaotarefas.dao;

import br.com.esig.desafio.gestaotarefas.modelo.Colaborador;
import br.com.esig.desafio.gestaotarefas.modelo.Situacao;
import br.com.esig.desafio.gestaotarefas.modelo.Tarefa;
import br.com.esig.desafio.gestaotarefas.util.JPAUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class TarefaDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public void salvar(Tarefa tarefa) {
        if (tarefa.getDeadline() != null) {
            tarefa.setDeadline(
                    tarefa.getDeadline()
                            .withHour(12)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0)
            );
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tarefa);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar tarefa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void excluir(Long numero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Tarefa tarefa = em.find(Tarefa.class, numero);
            if (tarefa != null) {
                em.remove(tarefa);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao excluir tarefa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Tarefa buscarPorNumero(Long numero) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Tarefa.class, numero);
        } finally {
            em.close();
        }
    }

    public List<Tarefa> listarComFiltros(Long numero, String tituloOuDescricao, Colaborador responsavel, String situacaoStr) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT t FROM Tarefa t LEFT JOIN FETCH t.responsavel WHERE 1=1 ");

            if (numero != null) {
                jpql.append("AND t.numero = :numero ");
            }
            if (tituloOuDescricao != null && !tituloOuDescricao.trim().isEmpty()) {
                jpql.append("AND (LOWER(t.titulo) LIKE LOWER(:tituloOuDescriptor) OR LOWER(t.descricao) LIKE LOWER(:tituloOuDescricao)) ");
            }
            if (responsavel != null) {
                jpql.append("AND t.responsavel = :responsavel ");
            }
            if (situacaoStr != null && !situacaoStr.trim().isEmpty()) {
                jpql.append("AND t.situacao = :situacao ");
            }

            jpql.append("ORDER BY t.numero DESC");

            TypedQuery<Tarefa> query = em.createQuery(jpql.toString(), Tarefa.class);

            if (numero != null) {
                query.setParameter("numero", numero);
            }
            if (tituloOuDescricao != null && !tituloOuDescricao.trim().isEmpty()) {
                query.setParameter("tituloOuDescricao", "%" + tituloOuDescricao + "%");
            }
            if (responsavel != null) {
                query.setParameter("responsavel", responsavel);
            }
            if (situacaoStr != null && !situacaoStr.trim().isEmpty()) {
                query.setParameter("situacao", Situacao.valueOf(situacaoStr));
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Tarefa buscarTarefaComResponsavel(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT t FROM Tarefa t LEFT JOIN FETCH t.responsavel WHERE t.numero = :id", Tarefa.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}