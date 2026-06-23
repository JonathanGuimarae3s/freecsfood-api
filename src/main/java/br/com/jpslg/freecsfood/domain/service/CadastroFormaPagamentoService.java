package br.com.jpslg.freecsfood.domain.service;

import br.com.jpslg.freecsfood.domain.exception.FormaPagamentoNaoEncontradaException;
import br.com.jpslg.freecsfood.domain.model.FormaPagamento;
import br.com.jpslg.freecsfood.domain.repository.FormaPagamentoRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroFormaPagamentoService {


    private final FormaPagamentoRepositorio formaPagamentoRepositorio;

    public CadastroFormaPagamentoService(FormaPagamentoRepositorio formaPagamentoRepositorio) {
        this.formaPagamentoRepositorio = formaPagamentoRepositorio;
    }

    @Transactional(readOnly = true)
    public Page<FormaPagamento> listar(Pageable pageable) {
        return formaPagamentoRepositorio.findAll(pageable);
    }


    public FormaPagamento buscarOuFalhar(Long id) {
        return formaPagamentoRepositorio.findById(id).orElseThrow(() -> new FormaPagamentoNaoEncontradaException(id));
    }

    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento) {
        return formaPagamentoRepositorio.save(formaPagamento);
    }

    public void excluir(Long id) {
        FormaPagamento formaPagamento = buscarOuFalhar(id);

        formaPagamentoRepositorio.delete(formaPagamento);
        formaPagamentoRepositorio.flush();
    }
}
