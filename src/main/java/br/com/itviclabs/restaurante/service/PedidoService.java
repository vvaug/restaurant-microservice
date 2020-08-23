package br.com.itviclabs.restaurante.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.itviclabs.restaurante.client.EntregadorClient;
import br.com.itviclabs.restaurante.domain.Pedido;
import br.com.itviclabs.restaurante.domain.Produto;
import br.com.itviclabs.restaurante.dto.ConfirmacaoPedido;
import br.com.itviclabs.restaurante.dto.ConfirmacaoPedidoResponse;
import br.com.itviclabs.restaurante.dto.PedidoRequest;
import br.com.itviclabs.restaurante.dto.ProdutoRequest;
import br.com.itviclabs.restaurante.dto.ProdutoResponse;
import br.com.itviclabs.restaurante.enums.TipoPedido;
import br.com.itviclabs.restaurante.exception.PedidoRequestException;
import br.com.itviclabs.restaurante.exception.ProdutosInvalidosException;
import br.com.itviclabs.restaurante.exception.handler.Campo;
import br.com.itviclabs.restaurante.repository.PedidoRepository;
import br.com.itviclabs.restaurante.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {

	@Autowired
	EntregadorClient entregadorClient;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	
	public ConfirmacaoPedido efetuarPedido(PedidoRequest pedidoRequest) throws ProdutosInvalidosException {	
		
		log.info("Efetuando Pedido");
		
		Pedido pedido = buildPedido(pedidoRequest);
		
		if (pedidoRequest.getEntrega()) {
			
			log.info("Pedido para entrega");
			
			if (isPedidoValidoParaEntrega(pedidoRequest)) {
				
				pedido.setTipoPedido(TipoPedido.ENTREGA);
				
				pedidoRepository.save(pedido);
				
				return enviarParaEntrega(pedidoRequest);
			}
		}
		
		log.info("Pedido para retirada");
		
		pedido.setTipoPedido(TipoPedido.RETIRADA);
		
		pedidoRepository.save(pedido);
		
		return ConfirmacaoPedido.builder()
					.entrega(false)
					.produtos(getProdutosById(getProdutosId(pedidoRequest.getProdutos())))
					.tempoDeEspera("40 minutos")
					.build();
	}

	protected Pedido buildPedido(PedidoRequest pedidoRequest) throws ProdutosInvalidosException {
		
		List<ProdutoRequest> produtosInvalidos = new ArrayList<>();
		List<Produto> produtos = new ArrayList<>();
		
		pedidoRequest.getProdutos()
					.stream()
					.forEach(produto -> {
							
						Optional<Produto> exists = produtoRepository.findById(produto.getId());
							
						if (exists.isPresent()) {
							produtos.add(exists.get());
						}
						else {
							produtosInvalidos.add(produto);
						}
 					});
		
		
		if (! produtosInvalidos.isEmpty()) {
			throw new ProdutosInvalidosException(produtosInvalidos, "Os produtos informados não existem!");
		}
		
		return Pedido.builder()
					.produtos(produtos)
					.valorTotal(getValorTotal(produtos))
					.build();
					
	}
	
	protected BigDecimal getValorTotal(List<Produto> produtos) {
		
		BigDecimal valorTotal = BigDecimal.ZERO;
		
		for (Produto p : produtos) {
			valorTotal = valorTotal.add(p.getPreco());
		}
		
		return valorTotal;
	}
	
	protected List<ProdutoResponse> getProdutosById(List<Long> ids){
		return ids.stream()
				.map(id -> new ProdutoResponse(produtoRepository.findById(id).get()))
				.collect(Collectors.toList());
	}
	
	protected List<Long> getProdutosId(List<ProdutoRequest> produtos) {
		return produtos.stream()
					.map(produto -> produto.getId())
					.collect(Collectors.toList());
	}
	
	@HystrixCommand(fallbackMethod = "entregaFallback",
					threadPoolKey = "enviarParaEntregaThreadPool")
	public ConfirmacaoPedido enviarParaEntrega(PedidoRequest pedidoRequest) {
		log.info("Realizando solicitação de entrega");
		ConfirmacaoPedidoResponse response = entregadorClient.realizarEntrega(pedidoRequest);
		return ConfirmacaoPedido.builder()
					.entrega(true)
					.produtos(getProdutosById(getProdutosId(response.getProdutos())))
					.tempoDeEspera(response.getTempoDeEspera())
					.build();
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected ConfirmacaoPedido entregaFallBack() {
		return ConfirmacaoPedido.builder()
					.entrega(null)
					.produtos(null)
					.tempoDeEspera(null)
					.retorno("Houve uma falha ao comunicar-se com o serviço de entrega")
					.build();
	}

	protected Boolean isPedidoValidoParaEntrega(PedidoRequest request){
		
		List<Campo> invalidFields = new ArrayList<>();
		
		if ( request.getLogradouro() == null ) {
			invalidFields.add(new Campo("logradouro", "vazio"));
		}
		if ( request.getNomeCliente() == null ) {
			invalidFields.add(new Campo("nomeCliente", "vazio"));
		}
		if ( request.getNumero() == null || request.getNumero().equals("0") ) {
			invalidFields.add(new Campo("numero", "vazio"));
		}
		
		if ( ! invalidFields.isEmpty() ) {
			
			throw new PedidoRequestException("Informações inválidas para entrega", invalidFields);
		}
		
		return true;
	}
	
	public Page<Pedido> findAll(Pageable pageable) {
		return pedidoRepository.findAll(pageable);
	}
	
}
