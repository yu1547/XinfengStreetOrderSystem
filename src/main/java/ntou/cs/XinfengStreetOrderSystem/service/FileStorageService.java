package ntou.cs.XinfengStreetOrderSystem.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String storeFile(MultipartFile file) throws IOException {
        
        // 檢查並創建上傳目錄
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        


        // 生成唯一文件名
        String imageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(imageName);

        // 儲存文件
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return imageName;
    }

    public void deleteFile(String image) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFile'");
    }
}
