package com.com.com.board;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManager {

    // 파일을 저장할 경로
    private static final String UPLOAD_PATH = "C:\\imgFile\\";

    public static String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // 저장할 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String savedFilename = generateUniqueFileName(originalFilename);

        // 파일 저장
        File destFile = new File(UPLOAD_PATH + savedFilename);
        file.transferTo(destFile);

        return savedFilename;
    }

    // 파일명 중복을 피하기 위해 유니크한 파일명 생성
    private static String generateUniqueFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString();
        return uniqueFilename + "." + extension;
    }

    // 파일 확장자 추출
    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return ""; // 확장자 없는 경우
        }
        return filename.substring(lastDotIndex + 1);
    }

    // 파일 삭제
    public static void deleteFile(String filename) {
        File file = new File(UPLOAD_PATH + filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
