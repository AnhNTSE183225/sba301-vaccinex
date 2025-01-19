package com.vaccine.controller;

import com.vaccine.configuration.CustomUserDetail;
import com.vaccine.configuration.JWTAuthenticationFilter;
import com.vaccine.configuration.JWTToken;
import com.vaccine.dto.request.UserLoginRequest;
import com.vaccine.dto.response.ObjectResponse;
import com.vaccine.dto.response.TokenResponse;
import com.vaccine.entity.User;
import com.vaccine.enums.EnumTokenType;
import com.vaccine.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final JWTToken jwtToken;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/test")
    public String testne() {
        return "testne2";
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        String email = jwtToken.getEmailFromJwt(refreshToken, EnumTokenType.REFRESH_TOKEN);
        User user = userService.getUserByEmail(email);
        if (user != null) {
            if (StringUtils.hasText(refreshToken) && user.getRefreshToken().equals(refreshToken)) {
                if (jwtToken.validate(refreshToken, EnumTokenType.REFRESH_TOKEN)) {
                    CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(user);
                    if (customUserDetail != null) {
                        String newToken = jwtToken.generatedToken(customUserDetail);
                        user.setAccessToken(newToken);
                        userService.save(user);
                        return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Refresh token successfully", newToken, refreshToken));
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Refresh token failed", null, null));
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Refresh token failed", null, null));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Refresh token failed", null, null));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@Valid @RequestBody UserLoginRequest userLogin) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String token = jwtToken.generatedToken(userDetails);
            String refreshToken = jwtToken.generatedRefreshToken(userDetails);
            User user = userService.getUserByEmail(userDetails.getEmail());
            if (user != null) {
                user.setRefreshToken(refreshToken);
                user.setAccessToken(token);
                userService.save(user);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login successfully", token, refreshToken));
        } catch (Exception e) {
            log.error("Cannot login : {}", e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Login failed", null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse> getLogout(HttpServletRequest request) {
        try {
            String token = jwtAuthenticationFilter.getToken(request);
            String email = jwtToken.getEmailFromJwt(token, EnumTokenType.TOKEN);
            User user = userService.getUserByEmail(email);
            user.setAccessToken(null);
            user.setRefreshToken(null);
            userService.save(user);
        } catch (Exception e) {
            log.error("Error logout : {}", e.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Logout successfully", null));
    }

}