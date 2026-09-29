package com.aerocadet.portal.auth;

import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public AppUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String[] authorities = account.getRoles().stream()
                .map(role -> role.getName().name())
                .toArray(String[]::new);

        return User.withUsername(account.getEmail())
                .password(account.getPasswordHash())
                .disabled(!account.isEnabled())
                .authorities(authorities)
                .build();
    }
}

