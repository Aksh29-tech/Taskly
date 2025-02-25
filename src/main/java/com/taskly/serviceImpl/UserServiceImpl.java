package com.taskly.serviceImpl;

import com.taskly.entity.User;
import com.taskly.entity.VerificationToken;
import com.taskly.repository.UserRepository;
import com.taskly.repository.VerificationTokenRepository;
import com.taskly.service.EmailService;
import com.taskly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepository tokenRepository;

    @Override
    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()) {
            User foundUser = existingUser.get();
            if (!foundUser.isVerified()) {
                sendVerificationEmail(foundUser);
                throw new IllegalStateException("User exists, but not verified. A verification email has been sent.");
            }
            throw new IllegalStateException("Email already registered and verified.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        String verificationLink = "http://localhost:8080/verify?token=" + token;

        // Store the verification token in the database
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpirationTime(System.currentTimeMillis() + 3600000); // 1 hour expiration
        tokenRepository.save(verificationToken);

        emailService.sendEmail(user.getEmail(), "Verify your account", "Click the link to verify your account: " + verificationLink);
    }

    @Override
    public void verifyUser(String token) {
        Optional<VerificationToken> verificationTokenOpt = tokenRepository.findByToken(token);
        if (verificationTokenOpt.isPresent()) {
            VerificationToken verificationToken = verificationTokenOpt.get();

            if (System.currentTimeMillis() > verificationToken.getExpirationTime()) {
                throw new IllegalStateException("Verification token expired.");
            }

            User user = verificationToken.getUser();
            user.setVerified(true); // Mark the user as verified
            userRepository.save(user); // Save the updated user

            // Optionally, delete the token after successful verification
            tokenRepository.delete(verificationToken);
        } else {
            throw new IllegalStateException("Invalid or expired verification token.");
        }
    }
}

