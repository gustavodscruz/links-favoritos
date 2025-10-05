package com.example.links.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.links.dto.RegisterEmailAndPassword;
import com.example.links.entity.CustomUser;
import com.example.links.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "user", key = "#email")
    @Transactional(readOnly = true)
    @Override
    public CustomUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email do usuário não encontrado"));
    }

    @Cacheable(value = "user-id", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    @Transactional(readOnly = true)
    public Long getUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserIdByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado!"));
    }

    @Cacheable(value = "user", key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    @Transactional(readOnly = true)
    public CustomUser getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email do usuário não encontrado"));
    }

    @Transactional()
    public CustomUser saveUser(RegisterEmailAndPassword dto) {
        CustomUser user = new CustomUser(dto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @CacheEvict(value = { "user", "user-id" })
    public void wipeCache() {
        System.out.println("Limpando cache...");
    }
}
