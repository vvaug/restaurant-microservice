package br.com.itviclabs.restaurante.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.itviclabs.restaurante.domain.Produto;
import br.com.itviclabs.restaurante.service.ProdutoService;

@CrossOrigin("*")
@RestController
@RequestMapping("/produto")
public class ProdutoEndpoint {

	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping
	public List<Produto> findAll(){
		return produtoService.findAll();
	}
}
