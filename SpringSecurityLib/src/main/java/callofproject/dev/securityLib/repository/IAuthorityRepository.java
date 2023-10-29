package callofproject.dev.securityLib.repository;

import callofproject.dev.securityLib.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.securityLib.util.SpringSecurityLibUtil.USER_REPOSITORY_BEAN_NAME;

@Repository(USER_REPOSITORY_BEAN_NAME)
@Lazy
public interface IAuthorityRepository extends CrudRepository<User, UUID>
{
}
