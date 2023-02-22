package org.erick.finance.resource;

import java.net.URI;
import java.util.List;

import org.erick.finance.domain.SpendingCategory;
import org.erick.finance.service.SpendingCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/category")
@RestController
public class CategoryResource {

	@Autowired
	private SpendingCategoryService categoryService;
	
	@CrossOrigin(origins = "*")
	@GetMapping
	public ResponseEntity<List<SpendingCategory>> listAll() {
		System.out.println("GET");
		List<SpendingCategory> categories = categoryService.listAll();
		return ResponseEntity.ok(categories);
	}
	
	@CrossOrigin
	@GetMapping(value="/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id){
		System.out.println("GET");
		return ResponseEntity.ok(categoryService.findById(id));
	}
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<SpendingCategory> inserir(@RequestBody SpendingCategory obj){
		System.out.println("POST");
		categoryService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); 
	}
	
	@CrossOrigin
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@RequestBody SpendingCategory obj, @PathVariable Long id){
		System.out.println("UPDATE");
		categoryService.save(obj);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		System.out.println("DELETE");
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
