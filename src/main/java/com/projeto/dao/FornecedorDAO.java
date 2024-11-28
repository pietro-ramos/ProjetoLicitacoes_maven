package com.projeto.dao;

import com.projeto.factory.ConnectionFactory;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.projeto.model.Endereco;
import com.projeto.model.Fornecedor;

/**
 * Classe que implementa as operações de banco de dados para a entidade Fornecedor.
 */
public class FornecedorDAO implements Interface_DAO<Fornecedor> {
    private static final String INSERT_QUERY = "INSERT INTO fornecedores (nome, cnpj, id_endereco, email, telefone) VALUES (?,?,?,?,?)";
    private static final String DELETE_QUERY = "DELETE FROM fornecedores WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE fornecedores SET nome = ?, cnpj = ?, email = ?, telefone = ? WHERE id = ?";
    private static final String SELECT_QUERY = "SELECT * FROM fornecedores f INNER JOIN endereco e ON f.id_endereco = e.id WHERE f.id = ?";
    private static final String LIST_QUERY = "SELECT * FROM fornecedores LIMIT ? OFFSET ?";
    private static final String UPDATE_ENDERECO_QUERY = "UPDATE endereco SET \"UF\" = ?, municipio = ?, rua = ?, numero = ? WHERE id = ?";


    /**
     * Insere um novo fornecedor no banco de dados.
     *
     * @param fornecedor O fornecedor a ser inserido.
     * @return true se a inserção for bem-sucedida, false caso contrário.
     */
    @Override
    public boolean insert(Fornecedor fornecedor) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, fornecedor.getNome());
            pstm.setString(2, fornecedor.getCnpj());
            pstm.setInt(3, fornecedor.getEndereco().getId());
            pstm.setString(4, fornecedor.getEmail());
            pstm.setString(5, fornecedor.getTelefone());
            pstm.executeUpdate();
            ResultSet generatedKeys = pstm.getGeneratedKeys();
            generatedKeys.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir fornecedor", e);
        }
    }

    /**
     * Exclui um fornecedor do banco de dados pelo ID.
     *
     * @param id O ID do fornecedor a ser excluído.
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    @Override
    public boolean delete(int id) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(DELETE_QUERY)) {
            pstm.setInt(1, id);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir fornecedor", e);
        }
    }

    /**
     * Atualiza as informações de um fornecedor no banco de dados.
     *
     * @param fornecedor O fornecedor com as informações atualizadas.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     */
    @Override
    public boolean update(Fornecedor fornecedor) {
        try (Connection con = ConnectionFactory.getConnection()) {
            // Atualiza o fornecedor
            try (PreparedStatement pstm = con.prepareStatement(UPDATE_QUERY)) {
                pstm.setString(1, fornecedor.getNome());
                pstm.setString(2, fornecedor.getCnpj());
                pstm.setString(3, fornecedor.getEmail());
                pstm.setString(4, fornecedor.getTelefone());
                pstm.setInt(5, fornecedor.getId());
                pstm.executeUpdate();
            }
            // Atualiza o endereço
            return updateEndereco(con, fornecedor.getEndereco());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar fornecedor", e);
        }
    }

    /**
     * Atualiza o endereço de um fornecedor no banco de dados.
     *
     * @param con      A conexão com o banco de dados.
     * @param endereco O endereço com as informações atualizadas.
     * @return true se a atualização for bem-sucedida, false caso contrário.
     */
    private boolean updateEndereco(Connection con, Endereco endereco) {
        try (PreparedStatement pstm = con.prepareStatement(UPDATE_ENDERECO_QUERY)) {
            pstm.setString(1, endereco.getUF());
            pstm.setString(2, endereco.getMunicipio());
            pstm.setString(3, endereco.getRua());
            pstm.setInt(4, endereco.getNumero());
            pstm.setInt(5, endereco.getId());
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar endereço", e);
        }
    }

    /**
     * Lista os fornecedores do banco de dados com paginação.
     *
     * @param limit  O número máximo de fornecedores a serem retornados.
     * @param offset O deslocamento inicial para a listagem.
     * @return Uma lista de fornecedores.
     */
    @Override
    public List<Fornecedor> list(int limit, int offset) {
        List<Fornecedor> fornecedores = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(LIST_QUERY)) {

            pstm.setInt(1, limit);
            pstm.setInt(2, offset);
            try (ResultSet resultSet = pstm.executeQuery()) {
                while (resultSet.next()) {
                    fornecedores.add(mapResultSetToFornecedor(resultSet));
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Erro ao listar fornecedores", e);
        }
        return fornecedores;
    }

    /**
     * Obtém um fornecedor do banco de dados pelo ID.
     *
     * @param id O ID do fornecedor a ser obtido.
     * @return O fornecedor correspondente ao ID, ou null se não encontrado.
     */
    @Override
    public Fornecedor get(int id) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(SELECT_QUERY)) {

            pstm.setInt(1, id);
            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToFornecedor(resultSet);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Erro ao obter fornecedor", e);
        }
        return null;
    }

    /**
     * Mapeia um ResultSet para um objeto Fornecedor.
     *
     * @param resultSet O ResultSet a ser mapeado.
     * @return O objeto Fornecedor correspondente.
     * @throws SQLException Se ocorrer um erro ao acessar o ResultSet.
     * @throws IOException  Se ocorrer um erro ao criar o objeto Fornecedor.
     */
    private Fornecedor mapResultSetToFornecedor(ResultSet resultSet) throws SQLException, IOException {
        int id = resultSet.getInt("id");
        String nome = resultSet.getString("nome");
        String cnpj = resultSet.getString("cnpj");
        String email = resultSet.getString("email");
        String telefone = resultSet.getString("telefone");
        int id_endereco = resultSet.getInt("id_endereco");
        String UF = resultSet.getString("UF");
        String municipio = resultSet.getString("municipio");
        String rua = resultSet.getString("rua");
        int numero = resultSet.getInt("numero");
        Endereco endereco = new Endereco(id_endereco, UF, municipio, rua, numero);
        return new Fornecedor(id, nome, cnpj, endereco, email, telefone);
    }
}