package com.javanauta.cadastrousuario.business;

import com.javanauta.cadastrousuario.api.converter.UsuarioConverter;
import com.javanauta.cadastrousuario.api.converter.UsuarioMapper;
import com.javanauta.cadastrousuario.api.converter.UsuarioUpdateMapper;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTO;
import com.javanauta.cadastrousuario.api.request.EnderecoRequestDTOFixture;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTO;
import com.javanauta.cadastrousuario.api.request.UsuarioRequestDTOFixture;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTO;
import com.javanauta.cadastrousuario.api.response.EnderecoResponseDTOFixture;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTO;
import com.javanauta.cadastrousuario.api.response.UsuarioResponseDTOFixture;
import com.javanauta.cadastrousuario.infrastructure.entities.EnderecoEntity;
import com.javanauta.cadastrousuario.infrastructure.entities.UsuarioEntity;
import com.javanauta.cadastrousuario.infrastructure.exceptions.BusinessException;
import com.javanauta.cadastrousuario.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioUpdateMapper usuarioUpdateMapper;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private UsuarioConverter usuarioConverter;

    UsuarioEntity usuarioEntity;

    EnderecoEntity enderecoEntity;

    EnderecoRequestDTO enderecoRequestDTO;

    UsuarioRequestDTO usuarioRequestDTO;

    EnderecoResponseDTO enderecoResponseDTO;

    UsuarioResponseDTO usuarioResponseDTO;

    LocalDateTime dataHora;

    String email;
    @BeforeEach
    public void setup(){

        dataHora = LocalDateTime.of(2026,9,21,13,45,45);
        enderecoEntity = EnderecoEntity.builder().rua("Onofra Aurelina da Silva").bairro("Jardim Imperial").cep("33234166").cidade("Lagoa Santa").numero(26L).complemento("Casa Germinada").build();
        usuarioEntity = UsuarioEntity.builder().nome("Calebe").documento("15161615").email("calebewerneck@hotmail.com").dataCadastro(dataHora).endereco(enderecoEntity).build();
        enderecoRequestDTO = EnderecoRequestDTOFixture.build("Rua Onofra aurelina da Silva",26L,"Jardim Imperial","Casa Germinada","Lagoa Santa","33234166");
        usuarioRequestDTO = UsuarioRequestDTOFixture.build("Calebe","calebewerneck@hotmail.com","324552352352",enderecoRequestDTO);
        enderecoResponseDTO = EnderecoResponseDTOFixture.build("Rua Onofra aurelina da Silva",26L,"Jardim Imperial","Casa Germinada","Lagoa Santa","33234166");
        usuarioResponseDTO = UsuarioResponseDTOFixture.build(1L,"Calebe","calebewerneck@hotmail.com","41516615",enderecoResponseDTO);
        email = "calebewerneck@hotmail.com";
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);

        UsuarioEntity entity = usuarioService.salvaUsuario(usuarioEntity);

        assertEquals(usuarioEntity, entity);
        verify(usuarioRepository, times(1)).saveAndFlush(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository);
    }


    @Test
    void deveGravarUsuarioComSucesso(){
        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.gravarUsuarios(usuarioRequestDTO);

        assertEquals(usuarioResponseDTO, dto);
        verify(usuarioConverter, times(1)).paraUsuarioEntity(usuarioRequestDTO);
        verify(usuarioRepository, times(1)).saveAndFlush(usuarioEntity);
        verify(usuarioMapper, times(1)).paraUsuarioResponseDTO(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter, usuarioMapper);
    }


    @Test
    void naoDeveSalvarUsuarioCasoUsuarioRequestDTONull(){
        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.gravarUsuarios(null));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));
        verifyNoInteractions(usuarioMapper, usuarioConverter, usuarioRepository);
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoGravarUsuario(){
        when(usuarioConverter.paraUsuarioEntity(usuarioRequestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenThrow(new RuntimeException("Falha ao gravar dados de usuário"));

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.gravarUsuarios(usuarioRequestDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Falha ao gravar dados de usuário"));
        verify(usuarioConverter).paraUsuarioEntity(usuarioRequestDTO);
        verify(usuarioRepository).saveAndFlush(usuarioEntity);
        verifyNoInteractions(usuarioMapper);
        verifyNoMoreInteractions(usuarioRepository, usuarioConverter);
    }


    @Test
    void deveAtualizarCadastroUsuarioComSucesso() {
        when(usuarioRepository.findByEmail(email)).thenReturn(usuarioEntity);
        when(usuarioUpdateMapper.updateUsuarioFromDTO(usuarioRequestDTO,usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioRepository.saveAndFlush(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO dto = usuarioService.atualizaCadastro(usuarioRequestDTO);

        assertEquals(usuarioResponseDTO,dto);
        verify(usuarioUpdateMapper).updateUsuarioFromDTO(usuarioRequestDTO,usuarioEntity);
        verify(usuarioRepository, times(1)).saveAndFlush(usuarioEntity);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        verifyNoMoreInteractions(usuarioRepository,usuarioConverter,usuarioMapper);
    }


    @Test
    void naoDeveAtualizarDadosDeUsuarioCasoUsuarioRequestDTONull(){
        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.atualizaCadastro(null));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getMessage(), is("Os dados do usuário são obrigatórios"));
        verifyNoInteractions(usuarioMapper, usuarioUpdateMapper, usuarioRepository);
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroAoAtualizarUsuario(){

        when(usuarioRepository.findByEmail(email)).thenThrow(new RuntimeException("Falha ao buscar dados de usuário"));

        BusinessException e = assertThrows(BusinessException.class, () -> usuarioService.atualizaCadastro(usuarioRequestDTO));

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao gravar dados de usuário"));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Falha ao buscar dados de usuário"));
        verify(usuarioRepository).findByEmail(email);
        verifyNoInteractions(usuarioMapper,usuarioUpdateMapper);
        verifyNoMoreInteractions(usuarioRepository);
    }

    @Test
    void deveBuscarDadosDeUsuarioComSucesso(){
        when(usuarioRepository.findByEmail(email)).thenReturn(usuarioEntity);
        when(usuarioMapper.paraUsuarioResponseDTO(usuarioEntity)).thenReturn(usuarioResponseDTO);
        UsuarioResponseDTO dto = usuarioService.buscaDadosUsuario(email);

        verify(usuarioRepository).findByEmail(email);
        verify(usuarioMapper).paraUsuarioResponseDTO(usuarioEntity);
        assertEquals(dto,usuarioResponseDTO);
    }


    @Test
    void deveRetornarNullCasoUsuarioNaoEncontrado(){
        when(usuarioRepository.findByEmail(email)).thenReturn(null);
        UsuarioResponseDTO dto = usuarioService.buscaDadosUsuario(email);
        assertEquals(dto,null);
        verify(usuarioRepository).findByEmail(email);
        verifyNoInteractions(usuarioMapper);

    }


    @Test
    void deveDeletarDadosDeUsuarioComSucesso(){
        doNothing().when(usuarioRepository).deleteByEmail(email);
        usuarioService.deletaDadosUsuario(email);
        verify(usuarioRepository).deleteByEmail(email);
    }



}
