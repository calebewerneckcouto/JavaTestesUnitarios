package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioUpdateMapperTest {

    private static final LocalDateTime DATA_CADASTRO = LocalDateTime.of(2026, 1, 15, 10, 15, 30);
    private static final LocalDateTime DATA_ATUALIZACAO = LocalDateTime.of(2026, 2, 20, 8, 0, 0);

    private UsuarioUpdateMapper mapper;
    private UsuarioEntity usuarioEntity;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UsuarioEntity usuarioEntityEsperado;

    @BeforeEach
    public void setup() {
        mapper = Mappers.getMapper(UsuarioUpdateMapper.class);

        EnderecoEntity enderecoEntity = EnderecoEntity.builder()
                .id(123L)
                .rua("Rua Antiga")
                .bairro("Bairro Antigo")
                .cep("00000000")
                .cidade("Cidade Antiga")
                .numero(1L)
                .complemento("Complemento antigo")
                .build();

        usuarioEntity = UsuarioEntity.builder()
                .id(1L)
                .nome("Usuario Antigo")
                .documento("000000")
                .email("antigo@email.com")
                .dataCadastro(DATA_CADASTRO)
                .dataAtualizacao(DATA_ATUALIZACAO)
                .endereco(enderecoEntity)
                .build();

        EnderecoRequestDTO enderecoRequestDTO = EnderecoRequestDTOFixture.build(
                "Rua Onofra Aurelina da Silva",
                26L,
                "Jardim Imperial",
                "Casa germinada",
                "Lagoa Santa",
                "33234166"
        );
        usuarioRequestDTO = UsuarioRequestDTOFixture.build(
                "Usuario",
                "calebewerneck@hotmail.com",
                "123456",
                enderecoRequestDTO
        );

        EnderecoEntity enderecoEsperado = EnderecoEntity.builder()
                .id(123L)
                .rua("Rua Onofra Aurelina da Silva")
                .bairro("Jardim Imperial")
                .cep("33234166")
                .cidade("Lagoa Santa")
                .numero(26L)
                .complemento("Casa germinada")
                .build();
        usuarioEntityEsperado = UsuarioEntity.builder()
                .id(1L)
                .nome("Usuario")
                .documento("123456")
                .email("calebewerneck@hotmail.com")
                .dataCadastro(DATA_CADASTRO)
                .dataAtualizacao(DATA_ATUALIZACAO)
                .endereco(enderecoEsperado)
                .build();
    }

    @Test
    public void deveAtualizarUsuarioEntityAPartirDoDTO() {
        UsuarioEntity atualizado = mapper.updateUsuarioFromDTO(usuarioRequestDTO, usuarioEntity);
        assertEquals(usuarioEntityEsperado, atualizado);
    }
}
