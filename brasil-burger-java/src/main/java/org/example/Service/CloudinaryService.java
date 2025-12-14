// Fichier: src/main/java/org/example/service/CloudinaryService.java
package org.example.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.config.CloudinaryConfig;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        this.cloudinary = CloudinaryConfig.getCloudinary();
    }

    public Map<String, Object> uploadImage(File imageFile, String folder) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageFile,
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    ));

            System.out.println("✓ Image uploadée avec succès sur Cloudinary");
            return uploadResult;
        } catch (IOException e) {
            System.err.println("✗ Erreur lors de l'upload de l'image: " + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> uploadBurgerImage(File imageFile) {
        return uploadImage(imageFile, CloudinaryConfig.getBurgerFolder());
    }

    public Map<String, Object> uploadMenuImage(File imageFile) {
        return uploadImage(imageFile, CloudinaryConfig.getMenuFolder());
    }

    public Map<String, Object> uploadComplementImage(File imageFile) {
        return uploadImage(imageFile, CloudinaryConfig.getComplementFolder());
    }

    public boolean deleteImage(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");

            if ("ok".equals(resultStatus)) {
                System.out.println("✓ Image supprimée avec succès de Cloudinary");
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("✗ Erreur lors de la suppression de l'image: " + e.getMessage());
            return false;
        }
    }

    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }

    public String getSecureImageUrl(String publicId) {
        return cloudinary.url().secure(true).generate(publicId);
    }
}