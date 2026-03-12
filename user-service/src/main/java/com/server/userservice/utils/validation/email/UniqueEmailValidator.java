package com.server.userservice.utils.validation.email;

import jakarta.validation.ConstraintValidator;
import com.server.userservice.domain.entity.User;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.userservice.repository.UserRepository;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (repository != null) {
            Optional<User> user = repository.findByEmail(email);
            return user == null;
        } else {
        }
        return true;
    }
}