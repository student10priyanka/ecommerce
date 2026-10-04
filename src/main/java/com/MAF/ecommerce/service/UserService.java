package com.MAF.ecommerce.service;

import com.MAF.ecommerce.model.User;
import com.MAF.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    private final UserRepository userRepository;
   // private List<User> userList=new ArrayList<>();
    private Long nextId=1L;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> fetchAllUsers() {
       // return userList;
        return userRepository.findAll();
    }
    public User fetchUser(Long id) {
//        for(User user:userList){
//            if(user.getId().equals(id)){
//                return user;
//            }
//        }
//        return null;
        return userRepository.findById(id).orElse(null);
    }
//    public List<User> addUser(User user) {
//        userList.add(user);
//        user.setId(nextId++);
//        return userList;
//    }
    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }
}
