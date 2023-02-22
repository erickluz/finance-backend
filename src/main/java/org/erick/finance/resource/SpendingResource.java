package org.erick.finance.resource;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

@CrossOrigin
@RequestMapping("/spending")
@RestController
public class SpendingResource {

	@Autowired
	private SpendingService spendingService;
	
	
	@GetMapping
	public ResponseEntity<List<SpendingDTO>> listAll(@RequestParam String date) {
		List<SpendingDTO> spendings = listSpendingToDTO(date);
		return ResponseEntity.ok(spendings);
	}
	
	
	@GetMapping(value="/{id}")
	public ResponseEntity<SpendingDTO> buscar(@PathVariable Long id){
		SpendingDTO spendingDTO = getSpendingDTO(spendingService.findById(id));
		return ResponseEntity.ok(spendingDTO);
	}

	
	@PostMapping
	public ResponseEntity<Spending> inserir(@RequestBody SpendingDTO obj){
		spendingService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); 
	}
	
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@RequestBody Spending obj, @PathVariable Long id){
		spendingService.save(obj);
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
	
	private List<SpendingDTO> listSpendingToDTO(String dateParam) {
		List<SpendingDTO> spendings = spendingService.listByMonth(dateParam).stream()
				.map(spending -> {
					String date = spending.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					String value = "R$ " + spending.getValue();
					String categoria = spending.getCategory().getName();
					return new SpendingDTO(spending.getId(), spending.getName(), date, value, categoria);
				})
				.collect(Collectors.toList());
		return spendings;
	}
	
	
	private SpendingDTO getSpendingDTO(Spending spending) {
		String date = spending.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String value = spending.getValue().toString();
		String categoria = spending.getCategory().getId().toString();
		return new SpendingDTO(spending.getId(), spending.getName(), date, value, categoria);
	}
}
