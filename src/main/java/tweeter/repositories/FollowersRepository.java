/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tweeter.domain.Followers;

/**
 *
 * @author sebserge
 */
public interface FollowersRepository extends JpaRepository<Followers, Long>{
    public List<Followers> findByThefollowerId(Long id);
    public Followers findByAccountIdAndThefollowerId(Long accountid, Long followerid);
}
