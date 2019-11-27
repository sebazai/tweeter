/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author sebserge
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Likes  extends AbstractPersistable<Long> {
    @ManyToOne
    private Messages message;
    @ManyToOne
    private Photos photo;
    @ManyToOne
    private Account account;
}
