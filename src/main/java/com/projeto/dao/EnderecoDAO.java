package com.projeto.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.projeto.factory.ConnectionFactory;
import com.projeto.model.Endereco;

public class EnderecoDAO implements Interface_DAO<Endereco> {

    @Override
    public boolean insert(Endereco object) {
        Connection con = ConnectionFactory.getConnection();
        String query = "INSERT INTO endereco (\"UF\", municipio, rua, numero) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, object.getUF());
            pstm.setString(2, object.getMunicipio());
            pstm.setString(3, object.getRua());
            pstm.setInt(4, object.getNumero());
            pstm.executeUpdate();
            ResultSet generated_id = pstm.getGeneratedKeys();
            if (generated_id.next()) {
                object.setId(generated_id.getInt(1));
            }
            generated_id.close();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*TODO:
         * Criar outras Exceptions para retornar False caso ocorra invalidação de input
         *
         * Perguntar por que nao pode botar um return false aqui********
         */
    }

    @Override
    public boolean delete(int id) {
        Connection con = ConnectionFactory.getConnection();
        String query = "DELETE FROM endereco WHERE id = ?";
        try {
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setInt(1, id);
            return pstm.execute();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /*TODO:
     * Criar um objeto temporario para fazer essa alteração
     */
    @Override
    public boolean update(Endereco object) {
        Connection con = ConnectionFactory.getConnection();
        String query = "UPDATE endereco SET \"UF\" = ?, municipio = ?, rua = ?, numero = ? WHERE id = ?";
        try {
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setString(1, object.getUF());
            pstm.setString(2, object.getMunicipio());
            pstm.setString(3, object.getRua());
            pstm.setInt(4, object.getNumero());
            pstm.setInt(5, object.getId());
            pstm.execute();
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public List<Endereco> list(int limit, int offset) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM endereco LIMIT ? OFFSET ?";
        try {
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setInt(1, limit);
            pstm.setInt(2, offset);
            ResultSet resultSet = pstm.executeQuery();
            List<Endereco> list = new ArrayList();
            Endereco endereco;
            while(resultSet.next()) {
                int id_fornecedor = resultSet.getInt("id");
                String uf = resultSet.getString("UF");
                String municipio = resultSet.getString("municipio");
                String rua = resultSet.getString("rua");
                int numero = resultSet.getInt("numero");
                endereco = new Endereco(id_fornecedor, uf, municipio, rua, numero);
                list.add(endereco);
            }
            return list;
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Endereco get(int id) {
        Connection con = ConnectionFactory.getConnection();
        String query = "SELECT * FROM endereco WHERE id = ?";
        try {
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setInt(1, id);
            ResultSet resultSet = pstm.executeQuery();
            if(resultSet.next()) {
                int id_fornecedor = resultSet.getInt("id");
                String uf = resultSet.getString("UF");
                String municipio = resultSet.getString("municipio");
                String rua = resultSet.getString("rua");
                int numero = resultSet.getInt("numero");
                Endereco endereco = new Endereco(id_fornecedor, uf, municipio, rua, numero);
                return endereco;
            }
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}