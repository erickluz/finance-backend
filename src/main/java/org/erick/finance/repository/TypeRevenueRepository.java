package org.erick.finance.repository;

import org.erick.finance.domain.TypeRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRevenueRepository extends JpaRepository<TypeRevenue, Long>{

}
