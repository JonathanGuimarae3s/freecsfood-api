package br.com.jpslg.freecsfood.domain.service;

import br.com.jpslg.freecsfood.domain.exception.CozinhaNaoEncontradaException;
import br.com.jpslg.freecsfood.domain.exception.EntidadeEmUsoException;
import br.com.jpslg.freecsfood.domain.model.Cozinha;
import br.com.jpslg.freecsfood.domain.repository.CozinhaRepositorio;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCozinhaService {
    private final CozinhaRepositorio repositorio;

    public CadastroCozinhaService(CozinhaRepositorio repositorio) { this.repositorio = repositorio; }

    @Transactional(readOnly = true)
    public Page<Cozinha> listar(Pageable pageable) { return repositorio.findAll(pageable); }

    @Transactional
    public Cozinha salvar(Cozinha cozinha) { return repositorio.save(cozinha); }

    @Transactional
    public void excluir(Long id) {
        Cozinha cozinha = buscarOuFalhar(id);
        try {
            repositorio.delete(cozinha);
            repositorio.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new EntidadeEmUsoException("Cozinha de código %d está em uso".formatted(id));
        }
    }

    @Transactional(readOnly = true)
    public Cozinha buscarOuFalhar(Long id) {
        return repositorio.findById(id).orElseThrow(() -> new CozinhaNaoEncontradaException(id));
    }
}
