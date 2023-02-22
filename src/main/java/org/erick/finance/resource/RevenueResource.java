package org.erick.finance.resource;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.erick.finance.domain.Revenue;
import org.erick.finance.dto.RevenueDTO;
import org.erick.finance.service.RevenueService;
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

@RequestMapping("/revenue")
@RestController
public class RevenueResource {

	@Autowired
	private RevenueService revenueService;
	
	@CrossOrigin(origins = "*")
	@GetMapping
	public ResponseEntity<List<RevenueDTO>> listAll() {
		List<RevenueDTO> revenues = getRevenuesDTO();
		return ResponseEntity.ok(revenues);
	}

	private List<RevenueDTO> getRevenuesDTO() {
		List<RevenueDTO> revenues = revenueService
				.listAll()
				.stream()
				.map(revenue -> {
					String date = revenue.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					String value = "R$ " + revenue.getValue().toString();
					String type = revenue.getType().getName();					
					return new RevenueDTO(revenue.getId(), date, type, value);
				}).collect(Collectors.toList());
		return revenues;
	}
	
	@CrossOrigin
	@GetMapping(value="/{id}")
	public ResponseEntity<RevenueDTO> buscar(@PathVariable Long id){
		return ResponseEntity.ok(toRevenueDTO(revenueService.findById(id)));
	}
	
	@CrossOrigin
	@PostMapping
	public ResponseEntity<Revenue> inserir(@RequestBody RevenueDTO obj){
		revenueService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); 
	}
	
	@CrossOrigin
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@RequestBody Revenue obj, @PathVariable Long id){
		revenueService.save(obj);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		revenueService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	private RevenueDTO toRevenueDTO(Revenue revenue) {
		String date = revenue.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String value = revenue.getValue().toString();
		String type = revenue.getType().getId().toString();
		return new RevenueDTO(revenue.getId(), date, type, value);
	}
}
