package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.model.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
    private final UserServiceImpl userService;

    public CustomUserDetailsService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.warn("loadUserByUsername -> CustomUserDetailsService");
        Optional<UserModel> userModel = userService.findByLogin(username);
        if (userModel.isEmpty()) {
            log.warn("DENİYOR YİNE");
            throw new UsernameNotFoundException("Kullanıcı adı bulunamadı: " + username);
        }
        return User.builder()
                .username(userModel.get().getUsername())
                .password(userModel.get().getPassword())
                .roles(userModel.get().getRole().name())
                .build();
    }
}
