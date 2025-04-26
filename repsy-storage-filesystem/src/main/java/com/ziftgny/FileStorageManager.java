package com.ziftgny;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

@Service("file-system")
public class FileStorageManager implements StorageService {
    private static final Logger logger = Logger.getLogger(FileStorageManager.class.getName());
    @Value("${storage.base-path:file-storage}")
    private String basePath;
    @Override
    public void save(String path, MultipartFile file) throws Exception {
        Path fullPath = Paths.get(basePath, path.replace("\\", "/").split("/"));

        Files.createDirectories(fullPath.getParent());
        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File saved to: " + fullPath);
    }

    @Override
    public Resource load(String path) throws Exception {
        Path fullPath = Paths.get(basePath, path.replace("\\", "/").split("/"));


        if (!Files.exists(fullPath)) {
            throw new RuntimeException("File not found: " + fullPath);
        }
        return new FileSystemResource(fullPath);
    }
}


