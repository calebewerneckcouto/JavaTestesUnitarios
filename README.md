# Testes unitários — Cadastro de Usuário

API Spring Boot de cadastro de usuário. Este README descreve **como os testes unitários estão organizados** e **como executá-los**.

## Stack de testes

| Ferramenta | Uso |
|---|---|
| JUnit 5 | Estrutura dos testes (`@Test`, `@BeforeEach`) |
| Mockito | Mocks de dependências (`@Mock`, `@InjectMocks`, `when`, `verify`) |
| MockMvc (standalone) | Testes da camada HTTP sem subir o servidor |
| Hamcrest | Asserções de exceção (`assertThat`, `is`) |
| Fixtures | Montagem de DTOs de request/response |

Os testes **não usam banco real**. Repositório, converter, mapper e service são mockados conforme a camada sob teste.

## Como rodar

Na pasta deste módulo:

```bash
gradlew.bat test
```

Um teste específico:

```bash
gradlew.bat test --tests com.javanauta.cadastrousuario.business.UsuarioServiceTest
```

No IntelliJ: abra a classe de teste e use o ícone verde ao lado do método (runner **JUnit**, não Gradle).

## Camadas testadas

```
api/response/UsuarioControllerTest    → controller (MockMvc)
business/UsuarioServiceTest           → regras de negócio
api/converter/UsuarioConverterTest    → DTO → Entity (Clock)
api/converter/UsuarioMapperTest       → Entity → ResponseDTO
api/converter/UsuarioUpdateMapperTest → atualização MapStruct (ignora null)
```

Fixtures em `src/test/java/.../api/request` e `src/test/java/.../api/response`.

## Padrão usado nos testes

1. **Arrange** — fixtures no `@BeforeEach` e `when(...)` nos mocks
2. **Act** — chama o método da classe sob teste
3. **Assert** — `assertEquals` / `assertThrows` / status HTTP
4. **Verify** — confirma quais mocks foram chamados (`times`, `verifyNoMoreInteractions`)

Exemplo (service):

```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @InjectMocks UsuarioService usuarioService;
    @Mock UsuarioRepository usuarioRepository;
    @Mock UsuarioConverter usuarioConverter;
    @Mock UsuarioMapper usuarioMapper;
}
```

- `@Mock` cria o falso colaborador
- `@InjectMocks` injeta os mocks no construtor da classe testada

## O que cada suíte cobre

### UsuarioServiceTest

- `deveSalvarUsuarioComSucesso` — `saveAndFlush`
- `deveGravarUsuarioComSucesso` — converter → save → mapper
- `naoDeveSalvarUsuarioCasoUsuarioRequestDTONull` — `BusinessException` se o DTO for `null`
- `deveGerarExcecaoCasoOcorraErroAoGravarUsuario` — falha no repositório vira `BusinessException`
- `deveAtualizarCadastroUsuarioComSucesso` — `findByEmail` → update mapper → save

O service usa `saveAndFlush` (não `saveAllAndFlush`). Para simular erro: `thenThrow(...)`, não `thenReturn(new RuntimeException())`.

### UsuarioConverterTest

O converter usa `Clock`. O teste mocka um relógio fixo para a `dataCadastro` ser previsível no `equals`.

### UsuarioMapperTest / UsuarioUpdateMapperTest

MapStruct (`componentModel = "spring"`). O update mapper **não sobrescreve** campos `null` (`NullValuePropertyMappingStrategy.IGNORE`) e preserva `id` e datas.

### UsuarioControllerTest

MockMvc `standaloneSetup` no endpoint `/user`.

- POST com JSON válido → **200**
- POST com body vazio (`""`) → **400** (Spring não lê o JSON; o service **não** é chamado)
- PUT / GET / DELETE conforme os mapeamentos do controller

**ObjectMapper deve ser real** (`new ObjectMapper()`), não `@Mock`. Mock devolve `null` em `writeValueAsString` e o `.content(json)` quebra com NPE.

O service mockado **não executa** `Assert.notNull`. JSON nulo no controller só vira 400 se o Spring rejeitar o body (string vazia / JSON ilegível), não se você reenviar o JSON válido do `setup()`.

## Teste de contexto

`CadastroUsuarioApplicationTests` sobe o contexto Spring com H2 (`src/test/resources/application.yaml`, porta aleatória) só para validar que a aplicação liga.

## Dicas

| Problema | Causa comum |
|---|---|
| `ClassNotFoundException` no Gradle | Rode pelo JUnit do IntelliJ ou `gradlew test` nesta pasta |
| `Status expected 400 but was 200` | O teste enviou JSON válido; use `.content("")` |
| `content is null` | `ObjectMapper` estava `@Mock` |
| `UnnecessaryStubbing` / verify falhou | Stub/verify de método que o código não chama (`saveAllAndFlush`) |
| Datas diferentes no converter | `LocalDateTime.now()` sem o mesmo `Clock` |
