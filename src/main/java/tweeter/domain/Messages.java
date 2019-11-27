/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Messages  extends AbstractPersistable<Long> {
    private LocalDateTime messageTime = LocalDateTime.now();
    private String message;
    
    @ManyToOne
    private Account account;
    
    @OneToMany(mappedBy = "messages")
    private List<Likes> likes = new ArrayList<>();
    
    @OneToMany(mappedBy = "messages")
    private List<Comments> comments = new ArrayList<>();
}
