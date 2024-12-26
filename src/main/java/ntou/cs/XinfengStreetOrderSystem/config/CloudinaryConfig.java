package ntou.cs.XinfengStreetOrderSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "db6ov5ugz",        // 請確認這是正確的 Cloud Name
                "api_key", "794558441897121",    // 請確認這是正確的 API Key
                "api_secret", "1_7Q-R-16u7h9SPkNQH238-u-t4"  // 替換為正確的 API Secret
        ));
    }
}
