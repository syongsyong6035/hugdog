package spring.hugme.domain.image.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.hugme.global.error.image.ImageUploadException;
import spring.hugme.global.error.image.InvalidFileFormatException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

  private final Cloudinary cloudinary;

  public List<String> upload(List<MultipartFile> files) {
    if (files == null || files.isEmpty()) {
      throw new InvalidFileFormatException("업로드할 이미지가 없습니다.");
    }

    List<String> savedPaths = new ArrayList<>();


    for (MultipartFile file : files) {
      if (file.isEmpty()) {
        throw new InvalidFileFormatException("비어있는 파일이 포함되어 있습니다.");
      }

      // 파일 타입 검사 (이미지 형식만 허용)
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        throw new InvalidFileFormatException("이미지 파일만 업로드할 수 있습니다.");
      }
      try {
        Map uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "folder", "post_uploads",
                "resource_type", "auto"
            )
        );
        String secureUrl = (String) uploadResult.get("secure_url");
        savedPaths.add(secureUrl);
      } catch (IOException e) {
        throw new ImageUploadException("이미지 업로드 중 클라우드 오류가 발생했습니다.");
      }

    }

    return savedPaths;
  }

}
