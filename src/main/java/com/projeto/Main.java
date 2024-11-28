package com.projeto;

import com.projeto.dao.EnderecoDAO;
import com.projeto.dao.FornecedorDAO;
import com.projeto.model.Endereco;
import com.projeto.model.Fornecedor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Endereco endereco = new Endereco("SP", "São Paulo", "Rua Exemplo", 123);

        EnderecoDAO enderecoDAO = new EnderecoDAO();

        boolean enderecoInsertResult = enderecoDAO.insert(endereco);
        System.out.println("Endereco insert result: " + enderecoInsertResult);

        try {
            Fornecedor fornecedor = new Fornecedor("Fornecedor Exemplo", "12.345.678/0001-90", endereco, "email@exemplo.com", "(11) 1234-5678");
            Fornecedor fornecedor2 = new Fornecedor(1,"Fornecedor Exemplo", "12.345.678/0001-90", endereco, "novoemail@exemplo.com", "(11) 1234-5678");

            FornecedorDAO fornecedorDAO = new FornecedorDAO();

            boolean insertResult = fornecedorDAO.insert(fornecedor);
            System.out.println("Insert result: " + insertResult);

            boolean updateResult = fornecedorDAO.update(fornecedor2);
            System.out.println("Update result: " + updateResult);

            Fornecedor retrievedFornecedor = fornecedorDAO.get(fornecedor2.getId());
            System.out.println("Retrieved Fornecedor: " + retrievedFornecedor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}