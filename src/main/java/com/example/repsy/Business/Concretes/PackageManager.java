package com.example.repsy.Business.Concretes;

import com.example.repsy.Business.Abstracts.PackageService;
import com.example.repsy.DataAccess.Abstracts.PackageRepository;
import com.example.repsy.Entities.Concretes.PackageEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziftgny.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PackageManager implements PackageService {

    private final StorageServiceFactory storageServiceFactory;
    private final PackageRepository packageRepository;

    public PackageManager(StorageServiceFactory storageServiceFactory, PackageRepository packageRepository) {
        this.storageServiceFactory = storageServiceFactory;
        this.packageRepository = packageRepository;
    }

    @Override
    public void uploadPackage(String packageName, String version, List<MultipartFile> files) throws Exception {
        packageName = packageName.trim();
        version = version.trim();
        MultipartFile repFile = null;
        MultipartFile metaFile = null;
        if (files.size() != 2) {
            throw new IllegalArgumentException("Exactly 2 files (package.rep and meta.json) must be uploaded.");
        }

        for (MultipartFile file : files) {
            if (file.getOriginalFilename().endsWith(".rep")) {
                repFile = file;
            } else if (file.getOriginalFilename().equals("meta.json")) {
                metaFile = file;
            }
        }

        if (repFile == null || metaFile == null) {
            throw new IllegalArgumentException("Both package.rep and meta.json must be provided.");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(metaFile.getInputStream());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid meta.json file: not valid JSON.");
        }

        if (packageRepository.findByPackageNameAndVersion(packageName, version).isPresent()) {
            throw new IllegalArgumentException("This package name and version already exists. Please upload a new version.");
        }


        StorageService storageService = storageServiceFactory.getStorageService();

        String basePath = packageName + "/" + version;

        storageService.save(basePath + "/package.rep", repFile);
        storageService.save(basePath + "/meta.json", metaFile);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName(packageName);
        packageEntity.setVersion(version);
        packageEntity.setAuthor("unknown"); // Optional: Parse meta.json to get author
        packageEntity.setRepPath(basePath + "/package.rep");
        packageEntity.setMetaPath(basePath + "/meta.json");
        packageEntity.setUploadDate(LocalDateTime.now());
        packageRepository.save(packageEntity);
    }

    @Override
    public Resource downloadFile(String packageName, String version, String fileName) throws Exception {
        packageName = packageName.trim();
        version = version.trim();
        fileName = fileName.trim();

        StorageService storageService = storageServiceFactory.getStorageService();

        String fullPath = String.join("/", packageName, version, fileName);

        return storageService.load(fullPath);
    }
}
