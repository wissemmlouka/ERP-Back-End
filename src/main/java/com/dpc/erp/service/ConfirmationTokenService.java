package com.dpc.erp.service;


import com.dpc.erp.entity.ConfirmationToken;
import com.dpc.erp.entity.User;
import com.dpc.erp.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;


    public ConfirmationToken getTokenByToken(String token){
        return  confirmationTokenRepository.findByConfirmationToken(token);

    }

    public ConfirmationToken getTokenByUser(User user){
        return  confirmationTokenRepository.findByUser(user);
    }
    public ConfirmationToken updateToken(User user) {
        ConfirmationToken token = confirmationTokenRepository.findByUser(user);
        token.setCreationDate(LocalDateTime.now());
        return confirmationTokenRepository.save(token);
    }

    public ConfirmationToken saveToken(ConfirmationToken token) {
        return confirmationTokenRepository.save(token);
    }

    public boolean isTokenExist(String token) {
        return confirmationTokenRepository.existsByConfirmationTokenIgnoreCase(token);
    }

    public void deleteToken(ConfirmationToken token){
        confirmationTokenRepository.delete(token);

    }



}
