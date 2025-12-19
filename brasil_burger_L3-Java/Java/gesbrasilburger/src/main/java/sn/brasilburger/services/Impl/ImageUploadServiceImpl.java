package sn.brasilburger.services.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import sn.brasilburger.services.ImageUploadService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;

public class ImageUploadServiceImpl implements ImageUploadService {
    private Cloudinary cloudinary;
    private static ImageUploadServiceImpl instance = null;

    private ImageUploadServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public static ImageUploadServiceImpl getInstance(Cloudinary cloudinary) {
        if (instance == null) {
            instance = new ImageUploadServiceImpl(cloudinary);
        }
        return instance;
    }

    @Override
    public String uploadImage(String source) throws IOException {
        if (source == null || source.trim().isEmpty()) {
            throw new IOException("Le chemin ou l'URL de l'image ne peut pas être vide.");
        }

        System.out.println("Source reçue par uploadImage: " + source);

        try {
            // 1. Tente de créer un objet URL -> Si réussi, c'est une URL HTTP
            new URL(source);
            // Si on arrive ici, c'est bien une URL valide
            System.out.println("Détection: source est une URL. Upload depuis le web...");
            return uploadFromUrl(source);

        } catch (MalformedURLException e) {
            // 2. Si ce n'est pas une URL valide, on considère que c'est un chemin de fichier local
            System.out.println("Détection: source est un chemin de fichier local.");
            return uploadFromFile(source);
        }
    }

    // Méthode pour uploader depuis une URL distante
    private String uploadFromUrl(String imageUrl) throws IOException {
        try {
            System.out.println("Upload vers Cloudinary depuis URL: " + imageUrl);
            // Cloudinary peut directement uploader depuis une URL publique[citation:10]
            Map uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.emptyMap());
            String secureUrl = uploadResult.get("secure_url").toString();
            System.out.println("✅ Upload depuis URL réussi: " + secureUrl);
            return secureUrl;
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'upload depuis URL: " + e.getMessage());
            throw new IOException("Échec de l'upload depuis l'URL: " + e.getMessage(), e);
        }
    }

    // Méthode pour uploader depuis un fichier local (votre code original, légèrement amélioré)
    private String uploadFromFile(String filePath) throws IOException {
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                throw new IOException("Le fichier n'existe pas: " + filePath);
            }
            System.out.println("Upload vers Cloudinary depuis fichier local: " + filePath);

            // Lire les bytes du fichier
            byte[] bytes = Files.readAllBytes(path);
            Map uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
            String secureUrl = uploadResult.get("secure_url").toString();
            System.out.println("✅ Upload depuis fichier local réussi: " + secureUrl);
            return secureUrl;

        } catch (InvalidPathException e) {
            throw new IOException("Chemin de fichier invalide: " + filePath, e);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'upload depuis fichier: " + e.getMessage());
            throw new IOException("Échec de l'upload du fichier: " + e.getMessage(), e);
        }
    }
}