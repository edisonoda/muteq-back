package com.andromeda.muteq.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.muteq.Entity.User;
import com.andromeda.muteq.Repository.UserRepository;
import com.andromeda.muteq.Service.TokenService;

@RestController
public class LoginController {
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password);
        Authentication auth = authenticationManager.authenticate(user);
        
        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody LoginRequest signupRequest) {
        if (this.repository.findByEmail(signupRequest.username) != null)
            return ResponseEntity.badRequest().build();

        String encrypted = new BCryptPasswordEncoder().encode(signupRequest.password);
        User user = new User(signupRequest.username, encrypted);

        this.repository.save(user);

        return ResponseEntity.ok().build();
    }

    public record LoginRequest(String username, String password) { }
    public record LoginResponse(String token) { }
}
