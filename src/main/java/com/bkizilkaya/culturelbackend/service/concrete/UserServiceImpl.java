package com.bkizilkaya.culturelbackend.service.concrete;

import com.bkizilkaya.culturelbackend.model.UserModel;
import com.bkizilkaya.culturelbackend.repo.UserRepository;
import com.bkizilkaya.culturelbackend.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<UserModel> findByLogin(String username) {
        return userRepository.findByUsername(username);

    }
}