package callofproject.dev.securityLib.config;

import callofproject.dev.securityLib.util.SpringSecurityLibUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptConfig
{
    @Bean(SpringSecurityLibUtil.PASSWORD_ENCODER_BEAN_NAME)
    @Lazy
    public BCryptPasswordEncoder provideBCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}