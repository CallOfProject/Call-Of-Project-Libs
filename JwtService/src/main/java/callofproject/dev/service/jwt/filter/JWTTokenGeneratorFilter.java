package callofproject.dev.service.jwt.filter;

import callofproject.dev.service.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter
{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        try
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (null != authentication)
            {
                String claims = JwtUtil.populateAuthorities(authentication.getAuthorities());
                HashMap<String, Object> map = new HashMap<>();
                map.put("authorities", claims);
                String jwt = JwtUtil.generateToken(map, authentication.getName());
                response.setHeader("Authorization", jwt);
                filterChain.doFilter(request, response);
            }
            else
            {
                throw new AccessDeniedException("Token not found or user not authenticated!");
            }
        } catch (AccessDeniedException ex)
        {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        return !request.getServletPath().equals("/auth");
    }

}