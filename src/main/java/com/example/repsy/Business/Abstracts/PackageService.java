package com.example.repsy.Business.Abstracts;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.util.List;

public interface PackageService {
    void uploadPackage(String packageName, String version, List<MultipartFile> files) throws Exception;
    Resource downloadFile(String packageName, String version, String fileName) throws Exception;
}
