package br.com.itviclabs.restaurante.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class ConfirmacaoPedido {
	
	private String retorno;
	private String tempoDeEspera;
	@Default
	private List<ProdutoResponse> produtos = new ArrayList<ProdutoResponse>();
	private Boolean entrega;
}
