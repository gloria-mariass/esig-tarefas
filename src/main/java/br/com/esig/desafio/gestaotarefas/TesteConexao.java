package br.com.esig.desafio.gestaotarefas;

import br.com.esig.desafio.gestaotarefas.dao.TarefaDAO;
import br.com.esig.desafio.gestaotarefas.modelo.Prioridade;
import br.com.esig.desafio.gestaotarefas.modelo.Tarefa;
import java.time.LocalDate;

public class TesteConexao {

    public static void main(String[] args) {
        System.out.println("Iniciando teste de persistência...");

        TarefaDAO tarefaDAO = new TarefaDAO();
        Tarefa novaTarefa = new Tarefa();

        novaTarefa.setTitulo("Teste de Persistência");
        novaTarefa.setDescricao("Verificar se a conexão com o banco e o DAO estão funcionando.");
        novaTarefa.setResponsavel("Desenvolvedor(a)");
        novaTarefa.setPrioridade(Prioridade.ALTA);
        novaTarefa.setDeadline(LocalDate.now().plusDays(10));

        try {
            tarefaDAO.salvar(novaTarefa);
            System.out.println("Sucesso! Tarefa salva com o número: " + novaTarefa.getNumero());
            System.out.println("Verifique seu banco de dados para confirmar se a tabela 'tarefa' foi criada e o registro inserido.");
        } catch (Exception e) {
            System.err.println("Erro ao salvar a tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
