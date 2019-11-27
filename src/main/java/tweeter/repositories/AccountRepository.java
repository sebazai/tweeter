/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.repositories;

import tweeter.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sebserge
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByUsername(String username);
}
