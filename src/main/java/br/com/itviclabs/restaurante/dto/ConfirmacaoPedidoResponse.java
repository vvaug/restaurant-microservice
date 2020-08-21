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
public class ConfirmacaoPedidoResponse {
	
	private String tempoDeEspera;
	private Boolean entrega;
	@Default
	private List<ProdutoRequest> produtos = new ArrayList<ProdutoRequest>();
}
