package org.erick.finance.service;

import java.util.List;

import org.erick.finance.domain.TypeRevenue;
import org.erick.finance.repository.TypeRevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeRevenueService {

	@Autowired
	private TypeRevenueRepository rep;
	
	public List<TypeRevenue> listAll() {
		return rep.findAll();
	}
	
	public TypeRevenue save(TypeRevenue typeRevenue) {
		return rep.save(typeRevenue);
	}
	
	public void delete(Long id) {
		rep.deleteById(id);
	}

	public TypeRevenue findById(Long id) {
		return rep.findById(id).orElse(null);
	}
	
}
