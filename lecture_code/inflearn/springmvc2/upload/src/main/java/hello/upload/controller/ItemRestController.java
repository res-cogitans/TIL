package hello.upload.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemRestController {

    @Data
    static class TestForm {
        private String name;
        private MultipartFile attachFile;
        private NestedData data;
    }

    @Data
    static class NestedData {
        private int num;
        private String chara;

        public NestedData(int num, String chara) {
            this.num = num;
            this.chara = chara;
        }
    }

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/test")
    public List<String> save(List<String> req) {
        return req;
    }

    @PostMapping("/api/items/new")
    public String saveItem(@ModelAttribute TestForm form) throws IOException {
        System.out.println(form.getAttachFile().getBytes());
        System.out.println(form.getName());
        System.out.println(form.getData().chara);
        return "ok";
    }
}
