package org.erick.finance.service;

import java.util.List;

import org.erick.finance.domain.SpendingCategory;
import org.erick.finance.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpendingCategoryService {

	@Autowired
	private CategoryRepository rep;
	
	public List<SpendingCategory> listAll() {
		return rep.findAll();
	}
	
	public SpendingCategory save(SpendingCategory spending) {
		return rep.save(spending);
	}
	
	public void delete(Long id) {
		rep.deleteById(id);
	}

	public SpendingCategory findById(Long id) {
		return rep.findById(id).orElse(null);
	}
	
}
