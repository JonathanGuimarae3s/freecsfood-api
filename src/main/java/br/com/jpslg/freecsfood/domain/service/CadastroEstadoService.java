package br.com.jpslg.freecsfood.domain.service;

import br.com.jpslg.freecsfood.domain.exception.EntidadeEmUsoException;
import br.com.jpslg.freecsfood.domain.exception.EstadoNaoEncontradaException;
import br.com.jpslg.freecsfood.domain.model.Estado;
import br.com.jpslg.freecsfood.domain.repository.EstadoRepositorio;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroEstadoService {
    private final EstadoRepositorio repositorio;

    public CadastroEstadoService(EstadoRepositorio repositorio) { this.repositorio = repositorio; }

    @Transactional(readOnly = true)
    public Page<Estado> listar(Pageable pageable) { return repositorio.findAll(pageable); }

    @Transactional
    public Estado salvar(Estado estado) { return repositorio.save(estado); }

    @Transactional
    public void excluir(Long id) {
        Estado estado = buscarOuFalhar(id);
        try {
            repositorio.delete(estado);
            repositorio.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new EntidadeEmUsoException("Estado de código %d está em uso".formatted(id));
        }
    }

    @Transactional(readOnly = true)
    public Estado buscarOuFalhar(Long id) {
        return repositorio.findById(id).orElseThrow(() -> new EstadoNaoEncontradaException(id));
    }
}
