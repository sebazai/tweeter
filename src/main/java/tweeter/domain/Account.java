/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
    
    @Column(unique=true)
    private String username;
    
    private String password;
    
    @Column(unique=true)
    private String nickname;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilepic;
    
   
    @OneToMany(mappedBy = "account")
    List<Followers> followers = new ArrayList<>();
    
    @OneToMany(mappedBy = "account")
    List<Messages> messages = new ArrayList<>();
    
    @OneToMany(mappedBy = "account")
    List<Photos> photos = new ArrayList<>();
    
    @OneToMany(mappedBy = "account")
    List<Likes> likes = new ArrayList<>();
    
    @OneToMany(mappedBy = "account")
    List<Comments> comments = new ArrayList<>();
    
//    @ElementCollection(fetch = FetchType.EAGER)
//    private List<String> authorities;

}
