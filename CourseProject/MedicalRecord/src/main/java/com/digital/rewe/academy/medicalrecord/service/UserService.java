package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findUserById(Long id);
}