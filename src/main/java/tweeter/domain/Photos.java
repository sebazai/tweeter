/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.domain;

import org.hibernate.annotations.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
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
public class Photos extends AbstractPersistable<Long> {
    private String photoText;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)  
    @Type(type = "org.hibernate.type.BinaryType") //commented away for local, needed for heroku/pg
    private byte[] photo;
    
    @ManyToOne
    private Account account;
    
    @OneToMany(mappedBy = "photos", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Likes> likes = new ArrayList<>();
    
    @OneToMany(mappedBy = "photos", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comments> comments = new ArrayList<>();
    
    public List<Comments> getComments() {
        Collections.sort(comments, Collections.reverseOrder());
        if (comments.size() > 10) {
            return comments.subList(0, 10);
        }
        return comments;
    }
}
