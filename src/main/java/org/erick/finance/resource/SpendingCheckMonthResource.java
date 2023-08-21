package org.erick.finance.resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.erick.finance.dto.AssociationsIDSDTO;
import org.erick.finance.dto.SpendingCheckAssociationDTO;
import org.erick.finance.dto.SpendingCheckDTO;
import org.erick.finance.dto.SpendingCheckMonthDTO;
import org.erick.finance.service.SpendingCheckMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RequestMapping("/spendingCheckMonth")
@RestController
public class SpendingCheckMonthResource {
	
	@Autowired
	private SpendingCheckMonthService spendingCheckMonthService;
	
	@GetMapping
	public ResponseEntity<List<SpendingCheckMonthDTO>> listAll() {
		return ResponseEntity.ok(spendingCheckMonthService.listSpendingCheckMonthToDTO());
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<SpendingCheckMonthDTO> buscar(@PathVariable Long id){
		return ResponseEntity.ok(spendingCheckMonthService.toSpendingCheckMonthDTO(spendingCheckMonthService.findById(id)));
	}
//	
//	@PostMapping
//	public ResponseEntity<SpendingCheckMonthDTO> inserir(@RequestBody SpendingCheckMonthDTO obj){
//		spendingCheckMonthService.save(obj);
//		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
//		return ResponseEntity.created(uri).build(); 
//	}
	
//	@PutMapping
//	public ResponseEntity<Void> update(@RequestBody SpendingCheckMonthDTO obj) throws Exception{
//		spendingCheckMonthService.update(obj);
//		return ResponseEntity.noContent().build();
//	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		spendingCheckMonthService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/associations")
	public ResponseEntity<List<SpendingCheckAssociationDTO>> getSpendingCheckAssociations(
			@RequestParam String date, 
			@RequestParam String association, 
			@RequestParam String associable) {
		return ResponseEntity.ok(spendingCheckMonthService.getSpendingsCheckAssociation(date, association, associable));
	}
	
	@PostMapping("/check")
	public ResponseEntity<Void> checkSpending(@RequestBody SpendingCheckDTO spendingCheckDTO) {
		spendingCheckMonthService.checkSpending(spendingCheckDTO);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/removeCheck/{id}")
	public ResponseEntity<Void> removeCheckSpending(@PathVariable String id) {
		spendingCheckMonthService.removeCheckSpending(Long.valueOf(id));
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/associate")
	public ResponseEntity<Void> associate(@RequestBody AssociationsIDSDTO associationsIdsDTO) {
		spendingCheckMonthService.associate(associationsIdsDTO);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/desassociate")
	public ResponseEntity<Void> desassociate(@RequestBody AssociationsIDSDTO associationsIdsDTO) {
		spendingCheckMonthService.desassociate(associationsIdsDTO);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/auto")
	public ResponseEntity<Void> auto(@RequestBody String sdate) {
		LocalDateTime date = LocalDateTime.parse(sdate + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy[ HH:mm:ss]"));
		spendingCheckMonthService.autoAssociate(date);
		return ResponseEntity.noContent().build();
	}
	
}
