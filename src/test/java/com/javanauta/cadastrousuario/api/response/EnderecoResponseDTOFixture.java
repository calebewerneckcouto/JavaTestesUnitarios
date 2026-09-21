package com.javanauta.cadastrousuario.api.response;

public class EnderecoResponseDTOFixture {

    public static EnderecoResponseDTO build(String rua,
                                            Long numero,
                                            String bairro,
                                            String complemento,
                                            String cidade,
                                            String cep) {
        return new EnderecoResponseDTO(rua, numero, bairro, complemento, cidade, cep);
    }
}
