package org.erick.finance.resource;

import java.net.URI;
import java.util.List;

import org.erick.finance.domain.Card;
import org.erick.finance.domain.CreditCardBill;
import org.erick.finance.dto.CreditCardBillDTO;
import org.erick.finance.service.CardService;
import org.erick.finance.service.CreditCardBillService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/card")
public class CardResource {
	@Autowired
	private CardService cardService;
	@Autowired
	private CreditCardBillService creditCardBillService;
	
	@CrossOrigin(origins = "*")
	@GetMapping
	public ResponseEntity<List<Card>> listAll() {
		List<Card> cards = cardService.listAll();
		return ResponseEntity.ok(cards);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(value="/{id}")
	public ResponseEntity<Card> buscar(@PathVariable Long id){
		Card card = cardService.findById(id);
		return ResponseEntity.ok(card);
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping
	public ResponseEntity<Card> inserir(@RequestBody Card obj){
		cardService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build(); 
	}
	
	@CrossOrigin(origins = "*")
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@RequestBody Card obj, @PathVariable Long id) throws Exception{
		cardService.save(obj);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		cardService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("/creditCardBill")
	public ResponseEntity<String> saveCreditCardBill(@RequestBody CreditCardBillDTO obj) {
		CreditCardBill creditCardbill = creditCardBillService.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(creditCardbill.getId()).toUri();
		return ResponseEntity.ok(creditCardbill.getId().toString());
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("/upload")
	public void handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("idCreditBill") String idCreditBill, RedirectAttributes redirectAttributes) {
		cardService.importCreditCardBill(file, idCreditBill);
	}
}
