package callofproject.dev.service.jwt.filter;


import callofproject.dev.service.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JWTTokenValidatorFilter extends OncePerRequestFilter
{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        try
        {
            var authenticationHeader = request.getHeader("Authorization");

            if (authenticationHeader != null && authenticationHeader.startsWith("Bearer "))
            {
                var token = authenticationHeader.substring(7);
                var username = JwtUtil.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    String authorities = JwtUtil.extractRoles(token);

                    if (JwtUtil.isTokenValid(token, username))
                    {
                        var authToken = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request, response);

        } catch (AccessDeniedException | BadCredentialsException exception)
        {
            response.setStatus(403);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + exception.getMessage() + "\"}");
        }

       /* try
        {
            String jwt = request.getHeader("Authorization");
            if (jwt == null || !jwt.startsWith("Bearer "))
            {
                throw new AccessDeniedException("Token not foundasd or user not authenticated!");
            }

            jwt = jwt.substring(7);
            String username = JwtUtil.extractUsername(jwt);
            System.err.println("username = " + username);
            String authorities = JwtUtil.extractRoles(jwt);
            System.err.println("authorities = " + authorities);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException | BadCredentialsException var8)
        {
            response.setStatus(403);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + var8.getMessage() + "\"}");
        }*/
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        String[] pathsToExclude = {"/api/auth/authenticate/register", "/api/auth/authenticate/login", "socket-endpoint",
                "/api/auth/admin/login"};

        return Arrays.stream(pathsToExclude).anyMatch(request.getServletPath()::matches);
    }

}