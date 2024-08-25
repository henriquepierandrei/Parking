package com.api.Parking.Auth;

import com.api.Parking.Dto.LoginDto;
import com.api.Parking.Dto.RegisterDto;
import com.api.Parking.Dto.ResponseDto;
import com.api.Parking.Infra.Security.TokenService;
import com.api.Parking.Model.UserModel;
import com.api.Parking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){
        UserModel userModel = this.userRepository.findByEmail(loginDto.email()).orElseThrow(() -> new RuntimeException("User Not Found"));
        if (passwordEncoder.matches(loginDto.password(),userModel.getPassword())){
            String token = this.tokenService.generateToken(userModel);
            return ResponseEntity.ok(new ResponseDto(userModel.getName(),userModel.getEmail(),token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        Optional<UserModel> userModelOptional = this.userRepository.findByEmail(registerDto.email());
        if (userModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use!");
        }


        UserModel model = new UserModel();
        model.setName(registerDto.name());
        model.setEmail(registerDto.email());
        model.setPassword(registerDto.password());
        String token = this.tokenService.generateToken(model);

        this.authService.saveUser(model);
        return ResponseEntity.status(HttpStatus.OK).body("Registered\nTOKEN: "+token);


    }

}
