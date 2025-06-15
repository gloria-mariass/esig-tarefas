package br.com.esig.desafio.gestaotarefas.dao;

import br.com.esig.desafio.gestaotarefas.modelo.Colaborador;
import br.com.esig.desafio.gestaotarefas.util.JPAUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@ApplicationScoped
public class ColaboradorDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    public Colaborador buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Colaborador.class, id);
        } finally {
            em.close();
        }
    }

    public List<Colaborador> buscarPorNome(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Colaborador c WHERE LOWER(c.nome) LIKE LOWER(:query) ORDER BY c.nome";
            return em.createQuery(jpql, Colaborador.class)
                    .setParameter("query", "%" + query + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Colaborador buscarPorNomeExato(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Colaborador c WHERE LOWER(c.nome) = LOWER(:nome)";
            return em.createQuery(jpql, Colaborador.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Colaborador> buscarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Colaborador c ORDER BY c.nome";
            return em.createQuery(jpql, Colaborador.class).getResultList();
        } finally {
            em.close();
        }
    }

    //    No momento não é usado porque não existe uma funcionalidade específica para adicionar um colaborador
    public void salvar(Colaborador colaborador) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(colaborador);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar colaborador: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
