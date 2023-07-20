package com.naren.account.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

	List<Account> findByCustomerId(String customerId);

}
