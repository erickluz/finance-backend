package org.erick.finance.resource;

import java.net.URI;
import java.util.List;

import org.erick.finance.domain.TypeRevenue;
import org.erick.finance.service.TypeRevenueService;
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

@RequestMapping("/typeRevenue")
@RestController
public class TypeRevenueResource {

	@Autowired
	private TypeRevenueService typeRevenueService;
	
	@CrossOrigin
	@GetMapping
	public ResponseEntity<List<TypeRevenue>> listAll() {
		List<TypeRevenue> categories = typeRevenueService.listAll();
		return ResponseEntity.ok(categories);
	}
	
	@CrossOrigin
	@GetMapping(value="/{id}")
	public ResponseEntity<?> buscar(@PathVariable Long id){
		return ResponseEntity.ok(typeRevenueService.findById(id));
	}
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<TypeRevenue> inserir(@RequestBody TypeRevenue obj){
		typeRevenueService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); 
	}
	
	@CrossOrigin
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@RequestBody TypeRevenue obj, @PathVariable Long id){
		typeRevenueService.save(obj);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		typeRevenueService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
