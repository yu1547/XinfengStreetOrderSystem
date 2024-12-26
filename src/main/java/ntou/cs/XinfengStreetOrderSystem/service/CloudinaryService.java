package ntou.cs.XinfengStreetOrderSystem.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // 上傳檔案到 Cloudinary 並返回圖片的 URL
    public String uploadFile(MultipartFile file) throws IOException {
        // 上傳檔案到 Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        // 返回圖片的 URL
        return uploadResult.get("url").toString();
    }
}
