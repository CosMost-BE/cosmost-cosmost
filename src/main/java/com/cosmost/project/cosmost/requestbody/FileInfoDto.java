package com.cosmost.project.cosmost.requestbody;

import com.cosmost.project.cosmost.util.MultipartUtil;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDto {
    private String id;
    private String name;
    private String format;
    private String localPath;
    private String remotePath;
    private long bytes;

    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    public static FileInfoDto multipartOf(MultipartFile multipartFile, String Path) {
        final String fileId = MultipartUtil.createFileUUID();
        final String format = MultipartUtil.getFormat(multipartFile.getContentType());

        return FileInfoDto.builder()
                .id(fileId)
                .name(multipartFile.getOriginalFilename())
                .format(format)
                .localPath(MultipartUtil.createLocalPath(fileId, format))
                .remotePath(MultipartUtil.createRemotePath(Path, fileId, format))
                .bytes(multipartFile.getSize())
                .build();
    }
}
