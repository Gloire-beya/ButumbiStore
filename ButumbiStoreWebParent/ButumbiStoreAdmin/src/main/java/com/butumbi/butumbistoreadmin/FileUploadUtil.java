package com.butumbi.butumbistoreadmin;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path upLoadPath = Paths.get(uploadDir);

        if (!Files.exists(upLoadPath)){
            Files.createDirectories(upLoadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = upLoadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ex){
            throw new IOException("Could not save file " + fileName, ex);
        }
    }

    public static void cleadDir(String dir){
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)){
                    try{
                     Files.delete(file);
                    }catch (IOException e){
                        System.out.println("Could not delete file  " + file);
                    }
                }
            });
        }catch (IOException e){
            System.out.println("Could not list directory " + dirPath);
        }
    }
}
