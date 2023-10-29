package callofproject.dev.securityLib.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    @Column(name = "username", length = 50, unique = true)
    private String username;
    @Column(name = "email", length = 100, unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column
    private boolean isEnabled;
    @Column
    private boolean isCredentialsNonExpired;
    @Column
    private boolean isAccountNonLocked;
    @Column
    private boolean isAccountNonExpired;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Authority> authorities;

    public UUID getUserId()
    {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities)
    {
        this.authorities = authorities;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired)
    {
        isAccountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked)
    {
        isAccountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired)
    {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean enabled)
    {
        isEnabled = enabled;
    }

    public void addAuthority(Authority authority)
    {
        authorities.add(authority);
    }
}
