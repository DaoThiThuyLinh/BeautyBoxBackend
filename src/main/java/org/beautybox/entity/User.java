package org.beautybox.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails{
    @Id
        @GeneratedValue(strategy = GenerationType.UUID)
            @Column(length = 36)
    String id;
    @Column(columnDefinition = "varchar(50)", nullable = false)
    String name;
    @Column(columnDefinition = "varchar(100)", nullable = false)
    String password;
    @Column(columnDefinition = "varchar(10)", nullable = false)
    String gender;
    @Column(columnDefinition = "varchar(60)", unique = true, nullable = false)
    String email;
    @Column(columnDefinition = "varchar(12)", nullable = false)
    String phone;
    @ManyToOne
            @JoinColumn(name = "role_id")
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(role.getName())
        );
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
