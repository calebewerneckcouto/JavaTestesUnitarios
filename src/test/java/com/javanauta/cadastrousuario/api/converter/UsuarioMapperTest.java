package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTO;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTOFixture;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTO;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioMapperTest {

    UsuarioMapper mapper;

    private UsuarioEntity usuarioEntity;
    private UsuarioResponseDTO usuarioResponseDTO;
  LocalDateTime dataHora;
    @BeforeEach
    public void setup() {
          mapper = Mappers.getMapper(UsuarioMapper.class);

        EnderecoEntity enderecoEntity = EnderecoEntity.builder()
                .id(123L)
                .rua("Rua Onofra Aurelina da Silva")
                .bairro("Jardim Imperial")
                .cep("33234166")
                .cidade("Lagoa Santa")
                .numero(26L)
                .complemento("Casa germinada")
                .build();
        usuarioEntity = UsuarioEntity.builder()
                .id(1L)
                .nome("Usuario")
                .documento("123456")
                .email("calebewerneck@hotmail.com")
                .dataCadastro(dataHora)
                .endereco(enderecoEntity)
                .build();

        EnderecoResponseDTO enderecoResponseDTO = EnderecoResponseDTOFixture.build(
                "Rua Onofra Aurelina da Silva",
                26L,
                "Jardim Imperial",
                "Casa germinada",
                "Lagoa Santa",
                "33234166"
        );
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(
                1L,
                "Usuario",
                "calebewerneck@hotmail.com",
                "123456",
                enderecoResponseDTO
        );
    }

    @Test
    public void deveConverterParaUsuarioResponseDTO() {
        UsuarioResponseDTO dto = mapper.paraUsuarioResponseDTO(usuarioEntity);
        assertEquals(usuarioResponseDTO, dto);
    }
}
