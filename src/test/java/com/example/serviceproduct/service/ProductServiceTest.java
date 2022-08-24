package com.example.serviceproduct.service;

import com.example.serviceproduct.entity.Category;
import com.example.serviceproduct.entity.Product;
import com.example.serviceproduct.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
class ProductServiceTest {
	@Mock
	private ProductRepository productRepository;

	private ProductService productService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		productService = new ProductServiceImpl(productRepository);

		Product computer = Product.builder()
			.id(1l)
			.name("computer")
			.category(Category.builder().id(1L).build())
			.description("")
			.stock(Double.parseDouble("10"))
			.price(Double.parseDouble("1240.99"))
			.status("Created")
			.createAt(new Date()).build();

		Mockito.when(productRepository.findById(computer.getId())).thenReturn(Optional.of(computer));
		Mockito.when(productRepository.save(computer)).thenReturn(computer);

	}

	@Test
	public void whenValidGetID_ThenReturnProduct(){
		Product actual=productService.getProduct(1L);

		Assertions.assertThat(actual.getName()).isEqualTo("computer");
	}

	@Test
	public void whenValidUpdateStock_ThenReturnNewStock(){
		Product actual=productService.updateStock(1L, Double.parseDouble("8"));

		Assertions.assertThat(actual.getStock()).isEqualTo(18);
	}

}