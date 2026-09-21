package com.javanauta.cadastrousuario.api.converter;

import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioConverterTest {

    private static final LocalDateTime DATA_HORA = LocalDateTime.of(2026, 1, 15, 10, 15, 30);

    @Mock
    private Clock clock;

    @InjectMocks
    private UsuarioConverter usuarioConverter;

    private UsuarioEntity usuarioEntity;
    private UsuarioRequestDTO usuarioRequestDTO;

    @BeforeEach
    public void setup() {
        Clock fixedClock = Clock.fixed(DATA_HORA.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

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

        EnderecoEntity enderecoEntity = EnderecoEntity.builder()
                .rua("Rua Onofra Aurelina da Silva")
                .bairro("Jardim Imperial")
                .cep("33234166")
                .cidade("Lagoa Santa")
                .numero(26L)
                .complemento("Casa germinada")
                .build();
        usuarioEntity = UsuarioEntity.builder()
                .nome("Usuario")
                .documento("123456")
                .email("calebewerneck@hotmail.com")
                .dataCadastro(DATA_HORA)
                .endereco(enderecoEntity)
                .build();
    }

    @Test
    public void deveConverterParaUsuarioEntity() {
        UsuarioEntity entity = usuarioConverter.paraUsuarioEntity(usuarioRequestDTO);
        assertEquals(usuarioEntity, entity);
    }
}
