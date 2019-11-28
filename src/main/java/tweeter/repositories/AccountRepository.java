/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.repositories;

import java.util.Collection;
import java.util.List;
import tweeter.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author sebserge
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByUsername(String username);
    public Account findByNickname(String nickname);
    public List<Account> findByNicknameContaining(String contains);
}
