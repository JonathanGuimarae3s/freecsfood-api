package br.com.jpslg.freecsfood.domain.service;

import br.com.jpslg.freecsfood.domain.exception.EntidadeEmUsoException;
import br.com.jpslg.freecsfood.domain.exception.RestauranteNaoEncontradaException;
import br.com.jpslg.freecsfood.domain.model.FormaPagamento;
import br.com.jpslg.freecsfood.domain.model.Restaurante;
import br.com.jpslg.freecsfood.domain.repository.RestauranteRepositorio;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroRestauranteService {
    private final RestauranteRepositorio restauranteRepositorio;
    private final CadastroCozinhaService cadastroCozinhaService;

    public CadastroRestauranteService(RestauranteRepositorio restauranteRepositorio,
                                      CadastroCozinhaService cadastroCozinhaService,
                                      CadastroFormaPagamentoService cadastroFormaPagamentoService) {
        this.restauranteRepositorio = restauranteRepositorio;
        this.cadastroCozinhaService = cadastroCozinhaService;
    }

    @Transactional(readOnly = true)
    public Page<Restaurante> listar(Pageable pageable) {
        return restauranteRepositorio.findAll(pageable);
    }

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        restaurante.setCozinha(cadastroCozinhaService.buscarOuFalhar(restaurante.getCozinha().getId()));
        return restauranteRepositorio.save(restaurante);
    }

    @Transactional
    public void excluir(Long id) {
        Restaurante restaurante = buscarOuFalhar(id);
        try {
            restauranteRepositorio.delete(restaurante);
            restauranteRepositorio.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new EntidadeEmUsoException("Restaurante de código %d está em uso".formatted(id));
        }
    }

    @Transactional(readOnly = true)
    public Restaurante buscarOuFalhar(Long id) {
        return restauranteRepositorio.findById(id).orElseThrow(() -> new RestauranteNaoEncontradaException(id));
    }

    public Page<FormaPagamento> listarFormaPagamento(Pageable pageable, Long idRestaurante) {
        return restauranteRepositorio.listarFormaPagamentoPorRestaurante(pageable, idRestaurante);
    }
}
