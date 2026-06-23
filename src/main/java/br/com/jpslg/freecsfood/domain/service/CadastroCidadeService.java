package br.com.jpslg.freecsfood.domain.service;

import br.com.jpslg.freecsfood.domain.exception.CidadeNaoEncontradaException;
import br.com.jpslg.freecsfood.domain.exception.EntidadeEmUsoException;
import br.com.jpslg.freecsfood.domain.model.Cidade;
import br.com.jpslg.freecsfood.domain.repository.CidadeRepositorio;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroCidadeService {
    private final CidadeRepositorio repositorio;
    private final CadastroEstadoService cadastroEstadoService;

    public CadastroCidadeService(CidadeRepositorio repositorio, CadastroEstadoService cadastroEstadoService) {
        this.repositorio = repositorio;
        this.cadastroEstadoService = cadastroEstadoService;
    }

    @Transactional(readOnly = true)
    public Page<Cidade> listar(Pageable pageable) { return repositorio.findAll(pageable); }

    @Transactional
    public Cidade salvar(Cidade cidade) {
        cidade.setEstado(cadastroEstadoService.buscarOuFalhar(cidade.getEstado().getId()));
        return repositorio.save(cidade);
    }

    @Transactional
    public void excluir(Long id) {
        Cidade cidade = buscarOuFalhar(id);
        try {
            repositorio.delete(cidade);
            repositorio.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new EntidadeEmUsoException("Cidade de código %d está em uso".formatted(id));
        }
    }

    @Transactional(readOnly = true)
    public Cidade buscarOuFalhar(Long id) {
        return repositorio.findById(id).orElseThrow(() -> new CidadeNaoEncontradaException(id));
    }
}
