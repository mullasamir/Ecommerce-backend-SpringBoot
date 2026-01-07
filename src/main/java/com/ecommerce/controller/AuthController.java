package com.ecommerce.controller;


import com.ecommerce.config.JwtProvider;
import com.ecommerce.exceptions.UserException;
import com.ecommerce.model.User;
import com.ecommerce.reponse.AuthResponse;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.services.CustomUserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private CustomUserServiceImpl customUserServiceImpl;

    AuthController(UserRepository userRepository , JwtProvider jwtProvider , PasswordEncoder passwordEncoder ,
                   CustomUserServiceImpl customUserServiceImpl){
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.customUserServiceImpl = customUserServiceImpl;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws UserException{

        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist != null){
            throw  new UserException("Email is Already used with another account");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);


        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail() , savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse(token , "SignUp Successfully");

        return new ResponseEntity<AuthResponse>(authResponse , HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest){


        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username , password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse(token , "Signin Successfully");

        return new ResponseEntity<AuthResponse>(authResponse , HttpStatus.CREATED);

    }


    private Authentication authenticate(String username , String password){
        UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
        if(userDetails == null){
            throw  new BadCredentialsException("Invalid Username or Password");
        }


        if(!passwordEncoder.matches(password , userDetails.getPassword())){
            throw  new BadCredentialsException("Invalid Username or Password");
        }


        return new UsernamePasswordAuthenticationToken(userDetails ,null, userDetails.getAuthorities());
    }


}
