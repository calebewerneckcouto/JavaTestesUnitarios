package com.javanauta.cadastrousuario.api.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javanauta.cadastrousuario.api.UsuarioController;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.business.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {
    @InjectMocks
    UsuarioController usuarioController;
    @Mock
    UsuarioService usuarioService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String url;

    EnderecoRequestDTO enderecoRequestDTO;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoResponseDTO enderecoResponseDTO;

    UsuarioResponseDTO usuarioResponseDTO;

    private String json;

    @BeforeEach
    void setup() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).alwaysDo(print()).build();
        url="/user";
        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua Onofra aurelina da Silva",26L,"Jardim Imperial","Casa Germinada","Lagoa Santa","33234166");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Calebe","calebewerneck@hotmail.com","324552352352",enderecoRequestDTO);
        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Rua Onofra aurelina da Silva",26L,"Jardim Imperial","Casa Germinada","Lagoa Santa","33234166");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(1L,"Calebe","calebewerneck@hotmail.com","41516615",enderecoResponseDTO);

        json = objectMapper.writeValueAsString(usuarioRequestDTO);
    }

    @Test
    void deveGravarDadosDeUsuarioComSucesso() throws Exception {
        when(usuarioService.gravarUsuarios(usuarioRequestDTO)).thenReturn(usuarioResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isOk());

        verify(usuarioService).gravarUsuarios(usuarioRequestDTO);
        verifyNoMoreInteractions(usuarioService);

    }


    @Test
    void naoDeveGravarDadosDeUsuarioCasoJsonNulo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("")).andExpect(MockMvcResultMatchers.status().isBadRequest());


        verifyNoMoreInteractions(usuarioService);

    }


    @Test
    void deveAtualizarDadosDeUsuarioComSucesso() throws Exception {
        when(usuarioService.atualizaCadastro(usuarioRequestDTO)).thenReturn(usuarioResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isOk());

        verify(usuarioService).atualizaCadastro(usuarioRequestDTO);
        verifyNoMoreInteractions(usuarioService);

    }


    @Test
    void naoDeveAtualizarDadosDeUsuarioCasoJsonNulo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("")).andExpect(MockMvcResultMatchers.status().isBadRequest());


        verifyNoMoreInteractions(usuarioService);

    }


    @Test
    void deveBuscarDadosDoUsuarioComSucesso() throws Exception {
        when(usuarioService.buscaDadosUsuario("calebewerneck@hotmail.com")).thenReturn(usuarioResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email","calebewerneck@hotmail.com")).andExpect(MockMvcResultMatchers.status().isOk());

        verify(usuarioService).buscaDadosUsuario("calebewerneck@hotmail.com");
        verifyNoMoreInteractions(usuarioService);
    }


    @Test
    void naoBuscarDadosDeUsuarioCasoJsonNulo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("")).andExpect(MockMvcResultMatchers.status().isBadRequest());


        verifyNoMoreInteractions(usuarioService);

    }


    @Test
    void deveDeletarDadosDoUsuarioComSucesso() throws Exception {
       doNothing().when(usuarioService).deletaDadosUsuario("calebewerneck@hotmail.com");
        mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email","calebewerneck@hotmail.com")).andExpect(MockMvcResultMatchers.status().isAccepted());

        verify(usuarioService).deletaDadosUsuario("calebewerneck@hotmail.com");
        verifyNoMoreInteractions(usuarioService);
    }

}
