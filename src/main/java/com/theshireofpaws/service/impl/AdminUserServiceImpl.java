package com.theshireofpaws.service.impl;

import com.theshireofpaws.entity.AdminUser;
import com.theshireofpaws.exception.ResourceNotFoundException;
import com.theshireofpaws.repository.AdminUserRepository;
import com.theshireofpaws.security.AdminUserDetails;
import com.theshireofpaws.service.interfaces.AdminUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    
    private final AdminUserRepository adminUserRepository;
    
    public AdminUserServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Admin user not found with email: " + email));
        
        return new AdminUserDetails(adminUser);
    }
}