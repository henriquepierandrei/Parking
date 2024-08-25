package com.api.Parking.Auth;

import com.api.Parking.Model.UserModel;
import com.api.Parking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;


    public void saveUser(UserModel userModel){
        this.userRepository.save(userModel);
    }
}
