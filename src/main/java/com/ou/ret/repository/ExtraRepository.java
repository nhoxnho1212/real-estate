package com.ou.ret.repository;

import com.ou.ret.domain.Extra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Extra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtraRepository extends JpaRepository<Extra, Long>, JpaSpecificationExecutor<Extra> {}
