package com.project.BackendNBA_Notes.security.jwt;


import com.project.BackendNBA_Notes.models.TokenBlacklist;
import com.project.BackendNBA_Notes.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    public void blacklistToken(String token) {
        if (!tokenBlacklistRepository.existsByToken(token)) {
            tokenBlacklistRepository.save(new TokenBlacklist(token));
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
}
