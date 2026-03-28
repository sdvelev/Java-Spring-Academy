package com.digital.rewe.academy.medicalrecord.service;

import com.digital.rewe.academy.medicalrecord.data.entity.Role;

public interface RoleService {

    Role getRoleByName(String name);

    Role saveRole(Role role);
}
