package com.javanauta.cadastrousuario.api.request;

public class UsuarioRequestDTOFixture {

    public static UsuarioRequestDTO build(String nome, String email, String documento, EnderecoRequestDTO endereco) {
        return new UsuarioRequestDTO(nome, email, documento, endereco);
    }
}
