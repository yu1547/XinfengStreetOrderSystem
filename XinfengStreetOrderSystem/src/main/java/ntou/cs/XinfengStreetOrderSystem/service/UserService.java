package ntou.cs.XinfengStreetOrderSystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ntou.cs.XinfengStreetOrderSystem.entity.User;
import ntou.cs.XinfengStreetOrderSystem.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserById(String id) {
        System.out.println("Searching for user with ID: " + id);
        Optional<User> user = userRepository.findById(id);
        System.out.println("User found: " + user.isPresent());
        return user;
    }
    
    // 其他用戶相關的業務邏輯方法
}
