package org.erick.finance.resource;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.erick.finance.domain.Budget;
import org.erick.finance.dto.BudgetDTO;
import org.erick.finance.service.BudgetService;
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

@RequestMapping("/budget")
@RestController
public class BudgetResource {

	@Autowired
	private BudgetService budgetService;
	
	@CrossOrigin(origins = "*")
	@GetMapping
	public ResponseEntity<List<BudgetDTO>> listAll() {
		List<BudgetDTO> budgets = getBudgetDTO();
		return ResponseEntity.ok(budgets);
	}

	private List<BudgetDTO> getBudgetDTO() {
		return budgetService
				.findAll()
				.stream()
				.map(b -> {
					return budgetDTO(b);
		}).collect(Collectors.toList());
	}

	private BudgetDTO budgetDTO(Budget b) {
		String formattedDate = b.getDate().format(DateTimeFormatter.ofPattern("MMM/yyyy"));
		String date = b.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		return new BudgetDTO(b.getId(), date, formattedDate, b.getValue());
	}
	
	@CrossOrigin
	@GetMapping(value="/{id}")
	public ResponseEntity<BudgetDTO> buscar(@PathVariable Long id){
		BudgetDTO budget = budgetDTO(budgetService.findById(id));
		return ResponseEntity.ok(budget);
	}
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<Budget> inserir(@RequestBody BudgetDTO obj){
		budgetService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); 
	}
	
	@CrossOrigin
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@RequestBody BudgetDTO obj, @PathVariable Long id){
		budgetService.save(obj);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		budgetService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
