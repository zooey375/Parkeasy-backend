package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
            .securityContext(context -> context.requireExplicitSave(false))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                // CORS 預請求允許
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 註冊 / 登入 / 信箱驗證
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/verify").permitAll()
                .requestMatchers("/api/auth/me").permitAll()

                // 停車場資料查詢開放
                .requestMatchers(HttpMethod.GET, "/api/parkinglots").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/parkinglots/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/parkinglots/**").permitAll()

                // 停車場新增、修改、刪除 需 ADMIN
                .requestMatchers(HttpMethod.POST, "/api/parkinglots").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/parkinglots/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/parkinglots/**").hasRole("ADMIN")

                // 收藏功能需登入
                .requestMatchers("/api/favorites/**").authenticated()

                // 管理頁面功能需 ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 其他請求需登入
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/") // 登入後預設頁面（可視情況修改）
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // 登出後跳轉頁面（前端不用理會）
                .permitAll()
            )
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 回傳 401
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"請先登入\"}");
            });

        return http.build();
    }

    // 前端跨域允許設定
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // 你的前端網址
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 要讓前端能帶 session cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 密碼加密器
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

