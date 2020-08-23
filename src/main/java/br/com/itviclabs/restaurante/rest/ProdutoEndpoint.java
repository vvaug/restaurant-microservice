package br.com.itviclabs.restaurante.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public Page<Produto> findAll(@PageableDefault(direction = Direction.ASC) Pageable pageable){
		return produtoService.findAll(pageable);
	}
	
	@GetMapping("/{id}")
	public Produto findById(@PathVariable("id") String id) {
		return produtoService.findById(id);
	}
}
