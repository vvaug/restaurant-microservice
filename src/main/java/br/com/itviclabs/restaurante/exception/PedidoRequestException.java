package br.com.itviclabs.restaurante.exception;

import java.util.ArrayList;
import java.util.List;

import br.com.itviclabs.restaurante.exception.handler.Campo;

public class PedidoRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Campo> fields = new ArrayList<>();
	
	public PedidoRequestException(String msg, List<Campo> fields) {
		super(msg);
		this.fields = fields;
	}

	public List<Campo> getFields() {
		return fields;
	}

	public void setFields(List<Campo> fields) {
		this.fields = fields;
	}
	
	
}
