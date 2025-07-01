package com.example.demo.service;

import com.example.demo.model.dto.UserCert;

public interface CertService {
	
	UserCert login(String username, String password);
	
}
