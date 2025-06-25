package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 建立 Server 與 Client 在傳遞資料上的統一結構與標準(含錯誤)
@Data
@NoArgsConstructor
public class ApiResponse<T> {
	private boolean success;
	private int status;     // 狀態 例如: 200, 400
	private String message; // 訊息 例如: 查詢成功, 新增成功, 請求錯誤
	private T data; 	    // payload 實際資料
	
	// 成功回應
	 public static <T> ApiResponse<T> success(String message, T data) {
	        ApiResponse<T> res = new ApiResponse<>();
	        res.success = true;
	        res.status = 200;
	        res.message = message;
	        res.data = data;
	        return res;
	    }
	
	// 失敗回應
	 public static <T> ApiResponse<T> error(int status, String message) {
	        ApiResponse<T> res = new ApiResponse<>();
	        res.success = false;
	        res.status = status;
	        res.message = message;
	        res.data = null;
	        return res;
	    }
}