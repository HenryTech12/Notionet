package com.taskmanager.app.service;

import com.taskmanager.app.dto.UserData;
import com.taskmanager.app.mapper.UserMapper;
import com.taskmanager.app.model.UserModel;
import com.taskmanager.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    public void addUser(UserData userData) {
        if(!Objects.isNull(userData)) {
           if(userRepository.findByEmail(userData.getEmail()).isEmpty()) {
               UserModel userModel = userMapper.convertToModel(userData);
               userRepository.save(userModel);
           }
        }
    }

    public UserData extractUserDetails(OAuth2User oAuth2User) {
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String picture = oAuth2User.getAttribute("picture");
        LocalDate localDate = LocalDate.now();
        String joined = localDate.getMonth().name()+", "+localDate.getDayOfMonth()+" "+localDate.getYear();
        return new UserData(email,name,picture,joined);
    }

    public UserData findByEmail(String email) {
        return userMapper.convertToDTO(userRepository.findByEmail(email).orElse(new UserModel()));
    }
}
