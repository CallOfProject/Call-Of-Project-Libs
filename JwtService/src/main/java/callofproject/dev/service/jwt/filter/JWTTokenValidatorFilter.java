package callofproject.dev.service.jwt.filter;


import callofproject.dev.service.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter
{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        var jwt = request.getHeader("Authorization");
        System.out.println("0");
        if (null != jwt)
        {
            try
            {
                System.out.println("JWT is: " + jwt);
                jwt = jwt.substring(7);
                var username = JwtUtil.extractUsername(jwt);

                System.out.println("jwt service username: " + username);

                //var authorities = (String) claims.get("authorities");
                //System.out.println(authorities);
                var auth = new UsernamePasswordAuthenticationToken(username, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e)
            {
                throw new BadCredentialsException("Invalid Token received!");
            }

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        return request.getServletPath().equals("/auth");
    }

}