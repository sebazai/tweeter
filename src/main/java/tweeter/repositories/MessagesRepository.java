/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tweeter.domain.Messages;

/**
 *
 * @author sebserge
 */
public interface MessagesRepository extends JpaRepository<Messages, Long>{
    
}
