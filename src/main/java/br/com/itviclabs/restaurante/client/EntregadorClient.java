package br.com.itviclabs.restaurante.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.itviclabs.restaurante.dto.ConfirmacaoPedidoResponse;
import br.com.itviclabs.restaurante.dto.PedidoRequest;

@FeignClient(name = "entregador")
public interface EntregadorClient {

	@PostMapping("/entregador/api/v1/entrega")
	public ConfirmacaoPedidoResponse realizarEntrega(@RequestBody PedidoRequest request);
}
