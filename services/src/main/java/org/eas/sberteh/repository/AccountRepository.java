package org.eas.sberteh.repository;


import org.eas.sberteh.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author aesipov.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

}
