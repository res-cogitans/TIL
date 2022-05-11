package hello.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFile {

    private String uploadFileName;
    private String storeFileName;   //동일 uploadFileName 충분히 존재할 수 있기에 UUID 등으로 안 겹치게 만들어야
}
