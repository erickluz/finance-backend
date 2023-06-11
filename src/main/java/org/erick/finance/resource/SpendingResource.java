package org.erick.finance.resource;

import java.net.URI;
import java.util.List;

import org.erick.finance.domain.Spending;
import org.erick.finance.dto.DateDTO;
import org.erick.finance.dto.SpendingDTO;
import org.erick.finance.service.SpendingService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin()
@RequestMapping("/spending")
@RestController
public class SpendingResource {

	@Autowired
	private SpendingService spendingService;
	
	@GetMapping
	public ResponseEntity<List<SpendingDTO>> listAll(@RequestParam String date) {
		List<SpendingDTO> spendings = spendingService.listSpendingToDTO(date);
		return ResponseEntity.ok(spendings);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<SpendingDTO> buscar(@PathVariable Long id){
		SpendingDTO spendingDTO = spendingService.getSpendingDTO(spendingService.findById(id));
		return ResponseEntity.ok(spendingDTO);
	}
	
	@PostMapping
	public ResponseEntity<Spending> inserir(@RequestBody SpendingDTO obj){
		spendingService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); 
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody SpendingDTO obj) throws Exception{
		spendingService.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		spendingService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/dates")
	public ResponseEntity<List<DateDTO>> getSpendingDates() {
		return ResponseEntity.ok(spendingService.getDatesSpending());
	}
	
}
