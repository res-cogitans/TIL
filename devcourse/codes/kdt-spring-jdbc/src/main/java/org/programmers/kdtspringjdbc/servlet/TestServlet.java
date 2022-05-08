package org.programmers.kdtspringjdbc.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@WebServlet(value = "/*", loadOnStartup = 1)    //요청 받기 전에 init 하는 옵션 (기본은 -1, 미리 init 안 하는 옵션임)
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var requestURI = req.getRequestURI();
        log.info("requestURI = {}", requestURI);

        resp.getWriter().println("Hello Servlet!");
    }
}