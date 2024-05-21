package com.epam.gym_app_main_mq.service;

import com.epam.gym_app_main_mq.model.Token;
import com.epam.gym_app_main_mq.model.User;
import com.epam.gym_app_main_mq.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
//@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    public Token findByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void delete(String token) {
        tokenRepository.deleteByToken(token);
    }

    public List<Token> findByUser(User user) {
        return tokenRepository.findByUser(user);
    }


    public String getValidTokenByUsername(User user) {
        log.info("\n\n++++++++++++++++++++ in TokenService getValidTokenByUsername() \n\n ");
        List<Token> tokens = tokenRepository.findByUser(user);
        log.info("\n\ntokens: {} \n\n", tokens.toString());

        if (tokens.isEmpty()) {
            throw new RuntimeException("Token not found with findByUserUsername(username)");
        }
        for (Token token : tokens) {
            log.info("token: " + token);
        }

        return tokens
                .stream()
                .filter(token -> !token.isRevoked() && !token.isExpired())
                .findFirst()
                .map(Token::getToken)
                .orElseThrow(() -> new RuntimeException("Token not found"));

    }
}
