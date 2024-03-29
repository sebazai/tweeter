/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeter.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
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
    @Size(min = 3, max = 15)
    private String username;
    
    @Size(min = 8, max = 100)
    private String password;
    
    private String passwordConfirm;
    
    @Column(unique=true)
    @Size(min = 3, max= 15)
    private String nickname;
    
    private Long profilePicId;
    
   
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
    
    public List<Messages> getMessages() {
        Collections.sort(messages, Collections.reverseOrder());
        return messages;
    }

}
