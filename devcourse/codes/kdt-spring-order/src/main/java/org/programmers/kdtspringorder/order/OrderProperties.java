package org.programmers.kdtspringorder.order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kdt")
@Getter
@Setter
public class OrderProperties implements InitializingBean {

    private String version;

    private int minimumOrderAmount;

    private List<String> supportVendors;

    private String description;

    @Value("${JAVA_HOME}")
    private String javaHome;

    @Override
    public void afterPropertiesSet() throws Exception {
//        System.out.println(MessageFormat.format("[OrderProperties]version = {0}", version));
//        System.out.println(MessageFormat.format("[OrderProperties]minimumOrderAmount = {0}", minimumOrderAmount));
//        System.out.println(MessageFormat.format("[OrderProperties]supportVendors = {0}", supportVendors));
//        System.out.println(MessageFormat.format("[OrderProperties]javaHome = {0}", javaHome));
    }
}
