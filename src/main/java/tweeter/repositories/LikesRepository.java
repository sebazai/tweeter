/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import tweeter.domain.Account;
import tweeter.domain.Likes;
import tweeter.domain.Messages;

/**
 *
 * @author sebserge
 */
public interface LikesRepository extends JpaRepository<Likes, Long>{
    public Likes findByAccountAndMessages(Account acc, Messages message);
}
