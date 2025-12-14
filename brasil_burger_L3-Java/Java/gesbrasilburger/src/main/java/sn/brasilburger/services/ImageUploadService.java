package sn.brasilburger.services;

import java.io.IOException;

public interface ImageUploadService {
    String uploadImage(String imagePath) throws IOException;
}