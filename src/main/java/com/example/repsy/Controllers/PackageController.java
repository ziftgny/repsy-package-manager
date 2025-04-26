package com.example.repsy.Controllers;
import com.example.repsy.Business.Abstracts.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;

@RestController
@RequestMapping("/")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("{packageName}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("files") List<MultipartFile> files
    ) {
        packageName = packageName.trim();
        version = version.trim();
        try {
            packageService.uploadPackage(packageName, version, files);
            return ResponseEntity.ok("Package uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed: " + e.getMessage());
        }
    }
    @GetMapping("{packageName}/{version}/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName
    ) {
        try {
            Resource file = packageService.downloadFile(packageName, version, fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body( e.getMessage());
        }
    }

}
