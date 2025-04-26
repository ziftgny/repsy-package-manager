package com.ziftgny;

import java.io.InputStream;
import java.net.URI;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service("object-storage")
public class ObjectStorageManager implements StorageService {
    private static final Logger logger = Logger.getLogger(ObjectStorageManager.class.getName());
    private final S3Client s3Client;
    @Value("${storage.bucket-name}")
    private String bucketName;

    public ObjectStorageManager(@Value("${storage.endpoint-url}") String endpointUrl, @Value("${storage.access-key}") String accessKey, @Value("${storage.secret-key}") String secretKey) {
        this.s3Client = (S3Client)((S3ClientBuilder)((S3ClientBuilder)((S3ClientBuilder)((S3ClientBuilder)S3Client.builder().endpointOverride(URI.create(endpointUrl))).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))).region(Region.US_EAST_1)).serviceConfiguration((S3Configuration)S3Configuration.builder().pathStyleAccessEnabled(true).build())).build();
    }

    public void save(String path, MultipartFile file) throws Exception {
        logger.info("Saving file to MinIO: " + path);
        PutObjectRequest putObjectRequest = (PutObjectRequest)PutObjectRequest.builder().bucket(this.bucketName).key(path).build();
        this.s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        logger.info("File saved to MinIO at path: " + path);
    }

    public Resource load(String path) throws Exception {
        logger.info("Loading file from MinIO: " + path);
        GetObjectRequest getObjectRequest = (GetObjectRequest)GetObjectRequest.builder().bucket(this.bucketName).key(path).build();
        InputStream objectStream = this.s3Client.getObject(getObjectRequest);
        return new InputStreamResource(objectStream);
    }
}