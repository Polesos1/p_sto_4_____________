package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import java.util.Optional;

public interface UserService extends ReadWriteService<User, Long> {
    Optional<User> getById(Long id);

    Optional<User> getByEmail(String email);

    void changePassword(String password, User user);

    void deleteByName(String email);

    void deleteById(Long id);

}
