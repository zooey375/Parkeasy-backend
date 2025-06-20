package com.example.demo.service;

import com.example.demo.model.dto.UserCert;

public interface CertService {

	/* 使用者登入驗證
	 * @Param username 帳號
	 * @Param password 密碼
	 * @return 驗證成功後的使用者憑證資訊
	 * @throws com.example.demo.exception.UserNotFoundException 使用者不存在
     * @throws com.example.demo.exception.PasswordInvalidException 密碼錯誤
	 */ 
	
	UserCert login(String username, String password);
	
}
