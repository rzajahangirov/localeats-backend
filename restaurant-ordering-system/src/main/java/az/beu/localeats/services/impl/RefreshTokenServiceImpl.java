package az.beu.localeats.services.impl;

import az.beu.localeats.models.RefreshToken;
import az.beu.localeats.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import az.beu.localeats.repositories.RefreshTokenRepository;
import az.beu.localeats.repositories.UserRepository;
import az.beu.localeats.services.RefreshTokenService;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User can't find " + email));


        refreshTokenRepository.findByUserId(user.getId())
                .ifPresent(token -> refreshTokenRepository.delete(token));


        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000)) // 10 dəqiqə
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public boolean removeToken(String email) {
        return userRepository.findByEmail(email).map(user -> {
            refreshTokenRepository.findByUserId(user.getId())
                    .ifPresent(token -> refreshTokenRepository.delete(token));
            return true;
        }).orElse(false);
    }


    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}