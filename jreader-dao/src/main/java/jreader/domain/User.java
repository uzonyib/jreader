package jreader.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class User {

    @Id
    private String username;
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

	public Role getRole() {
		return role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

}
