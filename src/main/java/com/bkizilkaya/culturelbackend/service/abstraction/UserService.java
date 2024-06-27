package com.bkizilkaya.culturelbackend.service.abstraction;

import com.bkizilkaya.culturelbackend.model.UserModel;

import java.util.Optional;

public interface UserService {
    Optional<UserModel> findByLogin(String username);
}
