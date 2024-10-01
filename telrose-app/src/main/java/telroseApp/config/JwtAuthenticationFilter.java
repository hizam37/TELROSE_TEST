package telroseApp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import telroseApp.service.JwtService;
import telroseApp.service.UserService;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        request.getServletPath();
        var authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader,BEARER_PREFIX))
        {
            filterChain.doFilter(request,response);
            return;
        }

        var jwt = authHeader.substring(BEARER_PREFIX.length());
        var username = jwtService.extractUsername(jwt);
        if(StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails = userService.userDetailsService()
                    .loadUserByUsername(username);
            if(!jwtService.isTokenValid(jwt,userDetails))
            {
                SecurityContext context = SecurityContextHolder.getContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
