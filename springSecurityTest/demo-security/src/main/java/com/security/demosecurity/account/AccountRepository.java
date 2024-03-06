package com.security.demosecurity.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer>{

    Account findByUsername(String username);
    
}
