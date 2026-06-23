package br.com.jpslg.freecsfood.api.exceptionhandler;

import br.com.jpslg.freecsfood.domain.exception.EntidadeEmUsoException;
import br.com.jpslg.freecsfood.domain.exception.EntidadeNaoEncontradaException;
import br.com.jpslg.freecsfood.domain.exception.NegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final String BASE_TYPE = "https://freecsfood.com.br/problems/";

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    ResponseEntity<ProblemDetail> entidadeNaoEncontrada(EntidadeNaoEncontradaException exception) {
        return response(HttpStatus.NOT_FOUND, "Recurso não encontrado", exception.getMessage(), "recurso-nao-encontrado");
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    ResponseEntity<ProblemDetail> entidadeEmUso(EntidadeEmUsoException exception) {
        return response(HttpStatus.CONFLICT, "Entidade em uso", exception.getMessage(), "entidade-em-uso");
    }

    @ExceptionHandler(NegocioException.class)
    ResponseEntity<ProblemDetail> negocio(NegocioException exception) {
        return response(HttpStatus.BAD_REQUEST, "Regra de negócio violada", exception.getMessage(), "regra-de-negocio");
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> erroInesperado(Exception exception) {
        LOGGER.error("Erro não tratado", exception);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro interno inesperado.", "erro-interno");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "Dados inválidos",
                "Um ou mais campos estão inválidos.", "dados-invalidos");
        List<Map<String, String>> fields = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getDefaultMessage()))
                .toList();
        problem.setProperty("fields", fields);
        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "Corpo inválido",
                "O corpo da requisição não pôde ser interpretado.", "corpo-invalido");
        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    private ResponseEntity<ProblemDetail> response(HttpStatus status, String title, String detail, String type) {
        return ResponseEntity.status(status).body(problem(status, title, detail, type));
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, String type) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create(BASE_TYPE + type));
        return problem;
    }
}
