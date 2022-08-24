package com.example.serviceproduct.service;

import com.example.serviceproduct.entity.Category;
import com.example.serviceproduct.entity.Product;
import com.example.serviceproduct.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{


	private final ProductRepository productRepository;

	@Override
	public List<Product> listAllProduct() {
		return productRepository.findAll();
	}

	@Override
	public Product getProduct(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product createProduct(Product product) {
		product.setStatus("CREATED");
		product.setCreateAt(new Date());

		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Product product) {
		Product productDB= getProduct(product.getId());

		if(productDB==null){
			return null;
		}

		productDB.setName(product.getName());
		productDB.setDescription(product.getDescription());
		productDB.setStock(product.getStock());
		productDB.setPrice(product.getPrice());
		productDB.setCategory(product.getCategory());

		return productRepository.save(productDB);
	}

	@Override
	public Product deleteProduct(Long id) {
		Product productDB= getProduct(id);

		if(productDB==null){
			return null;
		}

		productDB.setStatus("DELETED");
		return productRepository.save(productDB);
	}

	@Override
	public List<Product> findByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	@Override
	public Product updateStock(Long id, Double quantity) {
		Product productDB= getProduct(id);

		if(productDB==null){
			return null;
		}
		productDB.setStock(productDB.getStock()+ quantity);

		return productRepository.save(productDB);
	}
}
