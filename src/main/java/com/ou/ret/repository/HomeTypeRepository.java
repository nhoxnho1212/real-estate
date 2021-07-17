package com.ou.ret.repository;

import com.ou.ret.domain.HomeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HomeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HomeTypeRepository extends JpaRepository<HomeType, Long>, JpaSpecificationExecutor<HomeType> {}
