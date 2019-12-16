/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.domain;


/**
 *
 * @author sebserge
 */
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Followers extends AbstractPersistable<Long>{
    private LocalDateTime startedFollowing = LocalDateTime.now();
    @ManyToOne
    private Account account;
    @ManyToOne
    private Account thefollower;
    private boolean blocked;
}
