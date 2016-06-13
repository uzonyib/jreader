package jreader.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Cache
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username;
    
    private Role role;
    
    private Long registrationDate;

}
