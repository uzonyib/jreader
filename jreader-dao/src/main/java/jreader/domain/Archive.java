package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Cache
@Data
@NoArgsConstructor
public class Archive {

    @Id
    private Long id;
    @Load
    @Parent
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Ref<User> userRef;
    @Index
    private String title;
    @Index
    private int order;
    
    public Archive(final User user, final String title, final int order) {
        this.setUser(user);
        this.title = title;
        this.order = order;
    }

    public Archive(final Builder builder) {
        this.setId(builder.id);
        this.setUser(builder.user);
        this.setTitle(builder.title);
        this.setOrder(builder.order);
    }

    public User getUser() {
        return getUserRef() == null ? null : getUserRef().get();
    }
    
    public void setUser(final User user) {
        this.setUserRef(user == null ? null : Ref.create(user));
    }

    public static class Builder {

        private Long id;
        private User user;
        private String title;
        private int order;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder user(final User user) {
            this.user = user;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder order(final int order) {
            this.order = order;
            return this;
        }
        
        public Archive build() {
            return new Archive(this);
        }

    }

}
