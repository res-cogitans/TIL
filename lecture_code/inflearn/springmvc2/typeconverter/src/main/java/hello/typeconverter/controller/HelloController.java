package hello.typeconverter.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data");     //문자 타입으로 조회
        Integer intValue = Integer.valueOf(data);             //숫자 타입으로 조회
        System.out.println("intValue = " + intValue);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV1(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }
}
