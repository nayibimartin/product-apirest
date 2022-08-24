package com.example.serviceproduct.controller;

import com.example.serviceproduct.entity.Category;
import com.example.serviceproduct.entity.Product;
import com.example.serviceproduct.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<List<Product>> list(@RequestParam(name = "categoryId", required = false) Long categoryId){
		List<Product> products=new ArrayList<>();

		if(categoryId==null){
			products=productService.listAllProduct();
			if(products.isEmpty()){
				return ResponseEntity.noContent().build();
			}
		} else{
			products=productService.findByCategory(Category.builder().id(categoryId).build());
			if(products.isEmpty()){
				return ResponseEntity.notFound().build();
			}
		}


		 return ResponseEntity.ok(products);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> get(@PathVariable("id") Long id){
		Product product= productService.getProduct(id);
		if(product==null){
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(product);
	}

	@PostMapping
	public ResponseEntity<Product> create(@Valid @RequestBody Product product, BindingResult result){
		if(result.hasErrors()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
		}

		Product create=productService.createProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Product> update(@PathVariable("id") Long id, @RequestBody Product product){
		product.setId(id);
		Product update=productService.updateProduct(product);
		if(update==null){
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(update);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Product> delete(@PathVariable("id") Long id){
		Product product= productService.deleteProduct(id);
		if(product==null){
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(product);
	}

	@GetMapping(value = "/{id}/stock")
	public ResponseEntity<Product> updateStock(@PathVariable("id") Long id, @RequestParam(name = "quantity") Double quantity){
		Product update=productService.updateStock(id, quantity);
		if(update==null){
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(update);
	}

	private String formatMessage(BindingResult result){
		List<Map<String,String>> errors=result.getFieldErrors().stream()
			.map(err->{
				Map<String,String> error=new HashMap<>();
				error.put(err.getField(), err.getDefaultMessage());
				return error;
			}).collect(Collectors.toList());

		ErrorMessage errorMessage=ErrorMessage.builder()
			.code("01")
			.messages(errors).build();

		ObjectMapper objectMapper=new ObjectMapper();
		String jsonString="";
		try {
			jsonString= objectMapper.writeValueAsString(errorMessage);
		} catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;

	}
}
