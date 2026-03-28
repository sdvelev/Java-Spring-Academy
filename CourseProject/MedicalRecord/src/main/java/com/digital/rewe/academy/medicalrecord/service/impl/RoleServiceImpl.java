package com.digital.rewe.academy.medicalrecord.service.impl;

import com.digital.rewe.academy.medicalrecord.data.entity.Role;
import com.digital.rewe.academy.medicalrecord.data.repo.RoleRepository;
import com.digital.rewe.academy.medicalrecord.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByAuthority(name);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}