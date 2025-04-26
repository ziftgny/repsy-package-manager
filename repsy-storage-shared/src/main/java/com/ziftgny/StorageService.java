package com.ziftgny;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void save(String path, MultipartFile file) throws Exception;
    Resource load(String path) throws Exception;
}

