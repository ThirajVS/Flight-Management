package com.aerocadet.portal.auth;

import java.util.List;
import java.util.Locale;

import com.aerocadet.portal.common.ResourceConflictException;
import com.aerocadet.portal.user.CandidateProfile;
import com.aerocadet.portal.user.CandidateProfileRepository;
import com.aerocadet.portal.user.Role;
import com.aerocadet.portal.user.RoleName;
import com.aerocadet.portal.user.RoleRepository;
import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(
            UserAccountRepository userAccountRepository,
            RoleRepository roleRepository,
            CandidateProfileRepository candidateProfileRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            AppUserDetailsService userDetailsService,
            JwtService jwtService) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userAccountRepository.existsByEmailIgnoreCase(email)) {
            throw new ResourceConflictException("An account with this email already exists");
        }

        Role candidateRole = roleRepository.findByName(RoleName.ROLE_CANDIDATE)
                .orElseThrow(() -> new IllegalStateException("Candidate role is not configured"));
        UserAccount user = new UserAccount(
                request.fullName().trim(),
                email,
                passwordEncoder.encode(request.password()),
                candidateRole);
        userAccountRepository.save(user);

        CandidateProfile profile = new CandidateProfile(
                user,
                request.phone().trim(),
                request.dateOfBirth(),
                request.nationality().trim(),
                request.city().trim(),
                request.state().trim(),
                request.country().trim());
        candidateProfileRepository.save(profile);

        return createResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password()));
        UserAccount user = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated account no longer exists"));
        return createResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse.UserSummary currentUser(String email) {
        UserAccount user = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated account no longer exists"));
        return summary(user);
    }

    private AuthResponse createResponse(UserAccount user) {
        UserDetails details = userDetailsService.loadUserByUsername(user.getEmail());
        JwtService.TokenDetails token = jwtService.createToken(details);
        return new AuthResponse(token.value(), "Bearer", token.expiresAt(), summary(user));
    }

    private AuthResponse.UserSummary summary(UserAccount user) {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name().replace("ROLE_", ""))
                .sorted()
                .toList();
        return new AuthResponse.UserSummary(user.getId(), user.getFullName(), user.getEmail(), roles);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

