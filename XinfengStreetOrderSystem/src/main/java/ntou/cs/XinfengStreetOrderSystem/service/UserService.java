package ntou.cs.XinfengStreetOrderSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ntou.cs.XinfengStreetOrderSystem.entity.User;
import ntou.cs.XinfengStreetOrderSystem.repository.UserRepository;

@Service
public class UserService {

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null; // 判斷用戶名是否已存在
    }
    

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // 調用 Repository 方法
    }

    // Registration logic
    public boolean register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        System.out.println("User registered: " + user.getUsername());
        return true;
    }

    // Login logic
    public boolean login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            System.out.println("User logged in: " + existingUser.getUsername());
            return true;
        }
        System.out.println("Login failed for username: " + user.getUsername());
        return false;
    }
}
