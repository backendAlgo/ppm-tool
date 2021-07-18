package com.mprodev.ppmtool.web;

import com.mprodev.ppmtool.domain.User;
import com.mprodev.ppmtool.payload.JWTLoginSuccessResponse;
import com.mprodev.ppmtool.payload.LoginRequest;
import com.mprodev.ppmtool.security.JwtTokenProvider;
import com.mprodev.ppmtool.services.MapValidationErrorService;
import com.mprodev.ppmtool.services.UserService;
import com.mprodev.ppmtool.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.mprodev.ppmtool.security.SecurityConstants.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult result) {
        ResponseEntity<?> errorMap =
                mapValidationErrorService.mapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new JWTLoginSuccessResponse(true,
                TOKEN_PREFIX + tokenProvider.generateToken(authentication)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user,
                                          BindingResult result) {
        userValidator.validate(user, result);
        user.setConfirmPassword("");
        ResponseEntity<?> errorMap =
                mapValidationErrorService.mapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }
        User newUser = userService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
