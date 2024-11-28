package com.projeto.model;

import java.util.Objects;

public class Endereco {
    private int id;
    private String UF;
    private String municipio;
    private String rua;
    private int numero;

    //Construtor para cadastrar um novo fornecedor (id gerado pelo BD)
    public Endereco (String UF, String municipio, String rua, int numero) {
        try {
            setUF(UF);
            setMunicipio(municipio);
            setRua(rua);
            setNumero(numero);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    //Construtor para buscar um fornecedor (recupera o id fornecido pelo BD)
    public Endereco (int id, String UF, String municipio, String rua, int numero) {
        this(UF,municipio,rua,numero);
        setId(id);
    }

    //GETTERS
    public String getUF() {
        return UF;
    }
    public String getMunicipio() {
        return municipio;
    }
    public String getRua() {
        return rua;
    }
    public int getNumero() {
        return numero;
    }
    private void setUF(String uF) {
        UF = uF;
    }
    public int getId() {
        return id;
    }

    //SETTERS
    private void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    private void setRua(String rua) {
        this.rua = rua;
    }
    private void setNumero(int numero) throws IllegalArgumentException {
        if(numero <= 0) throw new IllegalArgumentException("O numero nao pode ser negativo");
        this.numero = numero;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Endereco other = (Endereco) obj;
        return Objects.equals(UF, other.UF) && Objects.equals(municipio, other.municipio) && numero == other.numero
                && Objects.equals(rua, other.rua);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", UF='" + UF + '\'' +
                ", municipio='" + municipio + '\'' +
                ", rua='" + rua + '\'' +
                ", numero=" + numero +
                '}';
    }
}