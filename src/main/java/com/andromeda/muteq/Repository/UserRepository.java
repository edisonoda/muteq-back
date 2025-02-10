package com.andromeda.muteq.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.andromeda.muteq.Entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    public UserDetails findByEmail(String email);
}
