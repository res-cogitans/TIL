package org.programmers.kdtspringjdbc.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Slf4j
public class KdtWepApplicationInitializer implements WebApplicationInitializer {
    @Override
    DispatcherServlet
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Starting Server...");
        var servletRegistration = servletContext.addServlet("test", new TestServlet());
        servletRegistration.addMapping("/*");
        servletRegistration.setLoadOnStartup(1);
    }
}
