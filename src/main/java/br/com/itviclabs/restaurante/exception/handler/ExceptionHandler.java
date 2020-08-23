package br.com.itviclabs.restaurante.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.itviclabs.restaurante.exception.PedidoRequestException;
import br.com.itviclabs.restaurante.exception.ProdutosInvalidosException;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(ProdutosInvalidosException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ErrorResponse handleProdutosInvalidos(ProdutosInvalidosException ex) {
		return ErrorResponse.builder()
					.statusCode(HttpStatus.ACCEPTED.value())
					.description(ex.getMessage())
					.values(ex.getProdutosInvalidos())
					.build();
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(PedidoRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlePedidoRequest(PedidoRequestException ex) {
		return ErrorResponse.builder()
					.statusCode(HttpStatus.BAD_REQUEST.value())
					.values(ex.getFields())
					.description(ex.getMessage())
					.build();
	}
}
