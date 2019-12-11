/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.domain;

import java.time.LocalDateTime;
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
public class Comments extends AbstractPersistable<Long> implements Comparable<Comments> {
    private LocalDateTime commentTime = LocalDateTime.now();
    private String comment;
    @ManyToOne
    private Messages messages;
    @ManyToOne
    private Photos photos;
    @ManyToOne
    private Account account;

    @Override
    public int compareTo(Comments t) {
        return this.getCommentTime().compareTo(t.getCommentTime());
    }
}
