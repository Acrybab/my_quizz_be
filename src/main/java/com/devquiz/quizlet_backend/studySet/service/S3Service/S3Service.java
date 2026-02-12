package com.devquiz.quizlet_backend.studySet.service.S3Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;

    public S3Service() {
        this.s3Client = S3Client.builder().region(Region.US_EAST_1).build();
    }
public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try{
            PutObjectRequest putObjectAclRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName).contentType(file.getContentType()) // Đây chính là Metadata quyết định việc xem trực tiếp hay tải về
                   .build();
            s3Client.putObject(putObjectAclRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi tải file lên S3: " + e.getMessage());



        }
}

}
