package br.com.esig.desafio.gestaotarefas.converters;

import br.com.esig.desafio.gestaotarefas.dao.ColaboradorDAO;
import br.com.esig.desafio.gestaotarefas.modelo.Colaborador;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "colaboradorConverter", forClass = Colaborador.class)
public class ColaboradorConverter implements Converter<Colaborador> {

    private ColaboradorDAO colaboradorDAO = new ColaboradorDAO();

    @Override
    public Colaborador getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        // Se for um ID numérico, busca pelo ID
        if (value.matches("\\d+")) {
            return colaboradorDAO.buscarPorId(Long.valueOf(value));
        }

        // Se não for ID, tenta buscar pelo nome exato
        Colaborador colaboradorExistente = colaboradorDAO.buscarPorNomeExato(value);
        if (colaboradorExistente != null) {
            return colaboradorExistente;
        }

        // Se não existir, cria novo colaborador com o nome informado
        Colaborador novoColaborador = new Colaborador();
        novoColaborador.setNome(value);
        return novoColaborador;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Colaborador colaborador) {
        if (colaborador == null) return null;
        return (colaborador.getId() != null) ? String.valueOf(colaborador.getId()) : colaborador.getNome();
    }
}
