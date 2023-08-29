package com.dpc.erp.service;

import com.dpc.erp.Request_Response.AuthenticationRequest;
import com.dpc.erp.Request_Response.AuthenticationResponse;
import com.dpc.erp.entity.User;
import com.dpc.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
  private final  JwtService jwtService;
  private final UserDetailsService userDetailsService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try {
            User user = userRepository.findByEmailIgnoreCase(request.getEmail());
            if(user==null) {
                throw new Exception("Invalid user");
            }

                if (!user.getEmail().equalsIgnoreCase(request.getEmail()) || !user.isAccountNonLocked()) {
                    throw new Exception("Invalid email");
                }
                if (!user.isEnabled()) {
                    throw new Exception("User is disabled");
                }

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    throw new Exception("Invalid password");
                }

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
                String jwtToken = jwtService.generateToken(userDetails);
                return new AuthenticationResponse(jwtToken, userDetails);


        } catch (AuthenticationException e) {
            throw new InternalAuthenticationServiceException("Authentication failed, Please try again!");
        } catch (Exception e) {
            throw new Exception(e.getMessage());

        }
    }
}
