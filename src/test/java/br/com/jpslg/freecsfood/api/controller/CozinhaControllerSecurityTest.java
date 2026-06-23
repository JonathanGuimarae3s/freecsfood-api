package br.com.jpslg.freecsfood.api.controller;

import br.com.jpslg.freecsfood.api.exceptionhandler.ApiExceptionHandler;
import br.com.jpslg.freecsfood.core.security.SecurityConfig;
import br.com.jpslg.freecsfood.domain.model.Cozinha;
import br.com.jpslg.freecsfood.domain.repository.UsuarioRepositorio;
import br.com.jpslg.freecsfood.domain.service.CadastroCozinhaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CozinhaController.class)
@Import({SecurityConfig.class, ApiExceptionHandler.class})
class CozinhaControllerSecurityTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private CadastroCozinhaService service;
    @MockitoBean private UsuarioRepositorio usuarioRepositorio;

    @Test
    void deveExigirAutenticacao() throws Exception {
        mockMvc.perform(get("/cozinhas")).andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    @WithMockUser
    void deveNegarEdicaoSemPermissao() throws Exception {
        mockMvc.perform(post("/cozinhas").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Brasileira\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "EDITAR_COZINHAS")
    void deveCriarCozinhaComContratoCorreto() throws Exception {
        when(service.salvar(any())).thenAnswer(invocation -> {
            Cozinha cozinha = invocation.getArgument(0);
            cozinha.setId(10L);
            return cozinha;
        });

        mockMvc.perform(post("/cozinhas").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Brasileira\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/cozinhas/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Brasileira"));
    }

    @Test
    @WithMockUser(authorities = "EDITAR_COZINHAS")
    void deveResponderProblemDetailParaDadosInvalidos() throws Exception {
        mockMvc.perform(post("/cozinhas").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.fields[0].field").value("nome"));
    }
}
