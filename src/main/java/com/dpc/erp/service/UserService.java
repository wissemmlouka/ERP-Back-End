package com.dpc.erp.service;


import com.dpc.erp.Request_Response.RegistrationRequest;
import com.dpc.erp.Request_Response.UpdatePasswordRequest;
import com.dpc.erp.entity.ConfirmationToken;
import com.dpc.erp.entity.Role;
import com.dpc.erp.entity.User;
import com.dpc.erp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private  final RoleService roleService;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
  public List<User> getAllUsers() throws Exception {
      try {
          return userRepository.findAll();
      }catch (Exception e){
          throw new Exception(e.getMessage());      }
  }
    @Transactional
    public User addNewUser(RegistrationRequest request) throws Exception {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            User user = userRepository.findByEmailIgnoreCase(request.getEmail());
            if (user.isEnabled()&& user.isAccountNonLocked())
                throw new Exception("User is already exist!");

            ConfirmationToken token = confirmationTokenService.updateToken(user);
            //send email
            sendEmail(token);
            return user;
        } else {

             Set<Role> roles=new HashSet<>();
            roles.add(roleService.getRole(request.getRole()));
            User newUser = userRepository.save(
                    User.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .password("")
                            .roles(roles)
                            .isEnabled(false)
                            .isAccountNonLocked(true)
                            .build());

            ConfirmationToken token = confirmationTokenService.saveToken(new ConfirmationToken(newUser, "Set"));
            //send email
            sendEmail(token);
            return newUser;
        }

    }


    public boolean sendEmailToResetPassword(String email) throws Exception {
        try {
            if (!userRepository.existsByEmailIgnoreCase(email))
                throw new Exception("Invalid email!");
            User user = userRepository.findByEmailIgnoreCase(email);
            if (!user.isEnabled())
                throw new Exception("User is disabled!");
            ConfirmationToken token = new ConfirmationToken(user, "Reset");
            sendEmail(token);
            confirmationTokenService.saveToken(token);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void sendEmail(ConfirmationToken token) throws Exception {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(token.getUser().getEmail());

            message.setSubject(token.getAction() == "Set" ? "Account activation" : "Reset password");
            message.setText(
                    (token.getAction() == "Set" ? "To active your account" : "To reset your password")
                            + ", please click here : "
                            + "http://localhost:8083/confirm-account/" + token.getConfirmationToken()
            );

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String confirmToken(String token) throws Exception {
        try {
            if (!confirmationTokenService.isTokenExist(token))
                throw new Exception("Invalid token!");

            ConfirmationToken confirmationToken = confirmationTokenService.getTokenByToken(token);
            User user = userRepository.findByEmailIgnoreCase(confirmationToken.getUser().getEmail());
            if (confirmationToken.getCreationDate().plusMinutes(30).isBefore(LocalDateTime.now()))
                throw new Exception("Link is expired!");
            if (confirmationToken.getAction().equalsIgnoreCase("Set") && user.isEnabled())
                throw new Exception("Account is already activated!");
            return confirmationToken.getAction();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean updatePassword(UpdatePasswordRequest request) throws Exception {
        try {
            if (!confirmationTokenService.isTokenExist(request.getToken()))
                throw new Exception("Invalid token!");
            ConfirmationToken confirmationToken = confirmationTokenService.getTokenByToken(request.getToken());
            User user = userRepository.findByEmailIgnoreCase(confirmationToken.getUser().getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            if (confirmationToken.getAction().equalsIgnoreCase("Set"))
                user.setEnabled(true);
            userRepository.save(user);
            confirmationTokenService.deleteToken(confirmationToken);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
}
