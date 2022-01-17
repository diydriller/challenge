package com.project.programmers_order.users;

import java.util.Optional;

public interface UserRepository {

    void update(User user);

    Optional<User> findById(long id);

    Optional<User> findByEmail(Email email);

}
