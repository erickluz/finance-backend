package org.erick.finance.repository;

import org.erick.finance.domain.SpendingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<SpendingCategory, Long>{

}
