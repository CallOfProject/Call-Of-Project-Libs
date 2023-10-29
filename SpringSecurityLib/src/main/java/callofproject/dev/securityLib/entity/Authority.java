package callofproject.dev.securityLib.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Table
@Entity(name = "authorities")
public class Authority implements GrantedAuthority
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    public int id;

    @Column(length = 50)
    public String authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    public User user;

    @Override
    public String getAuthority()
    {
        return authority;
    }

    public int getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public void setAuthority(String authority)
    {
        this.authority = authority;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
