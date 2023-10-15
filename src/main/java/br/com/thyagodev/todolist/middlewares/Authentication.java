package br.com.thyagodev.todolist.middlewares;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import br.com.thyagodev.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Authentication extends OncePerRequestFilter{

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        var serveletPath = request.getServletPath();
        if (!serveletPath.startsWith("/tasks/")) {
            filterChain.doFilter(request, response);
            return; 
        }

        var authorization = request.getHeader("Authorization");

        var authEncoded = authorization.substring("Basic".length()).trim();
        String authDecoded = new String(Base64.getDecoder().decode(authEncoded));
        String[] credentials = authDecoded.split(":"); 
        String username = credentials[0];
        String password = credentials[1];

        var user = this.iUserRepository.findByUsername(username);
        if (user == null) {
            response.sendError(401);
            return;
        }
        System.out.println("User finded");

        Result passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        if (!passwordVerified.verified){
            response.sendError(401);
            return;
        }
        System.out.println("Password verified");
        
        request.setAttribute("userId", user.getId());
        filterChain.doFilter(request, response);
    }
    
}
