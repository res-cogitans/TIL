package org.programmers.kdtspringorder;

import org.programmers.kdtspringorder.configuration.AppConfiguration;
import org.programmers.kdtspringorder.order.Order;
import org.programmers.kdtspringorder.order.OrderItem;
import org.programmers.kdtspringorder.order.OrderProperties;
import org.programmers.kdtspringorder.repository.JdbcVoucherRepository;
import org.programmers.kdtspringorder.repository.VoucherRepository;
import org.programmers.kdtspringorder.service.OrderService;
import org.programmers.kdtspringorder.service.VoucherService;
import org.programmers.kdtspringorder.voucher.FixedAmountVoucher;
import org.programmers.kdtspringorder.voucher.Voucher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.*;

public class OrderTester {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();

        var resource = ac.getResource("application.yaml");
        var resourceWithOutJar = ac.getResource("file:sample");
        var resourceWithUrl = ac.getResource("https://stackoverflow.com/");

        //classpath 가 기본적으로 가져오는 위치임. 명시적으로 표현 가능
//        var resource = ac.getResource("classpath:application.yaml");


        //resource 어떤 클래스인지?
        System.out.println(MessageFormat.format(
                "resource -> {0}", resource.getClass().getCanonicalName()));

        System.out.println(MessageFormat.format(
                "resourceWithUrl -> {0}", resourceWithUrl.getClass().getCanonicalName()));

        //resource 출력해보기
        printResource(resource, "[application.yaml file reading...] = ");
        //실행 workingDirectory 기준으로 탐색한다.
        printResource(resourceWithOutJar, "[sample file reading...] = ");
//        printResource(resourceWithUrl, "[file at url reading...] = ");

        //파일이 아닌 url 경우
        var readableByteChannel = Channels.newChannel(resourceWithUrl.getURL().openStream());
        var bufferedReader = new BufferedReader(Channels.newReader(readableByteChannel, UTF_8));
        var contents = bufferedReader.lines().collect(Collectors.joining("\n"));
        System.out.println(contents);

        ac.register(AppConfiguration.class);
        ConfigurableEnvironment environment = ac.getEnvironment();
        environment.setActiveProfiles("local");
        ac.refresh();

        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println(MessageFormat.format("[activeProfiles] = {0}", activeProfiles));

//        String property = environment.getProperty("kdt.description");
//        System.out.println("property = " + property);

        var orderProperties = ac.getBean(OrderProperties.class);
        printProperties(orderProperties);

        OrderService orderService = ac.getBean(OrderService.class);
        VoucherRepository voucherRepository = ac.getBean(VoucherRepository.class);
        VoucherService voucherService = ac.getBean(VoucherService.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        System.out.println(MessageFormat.format(
                "is Jdbc Repo -> {0}", voucherRepository instanceof JdbcVoucherRepository));
        System.out.println(MessageFormat.format(
                "is voucherRepository.class() -> {0}", voucherRepository.getClass()));

        List<OrderItem> orderItems = new ArrayList<>() {
            {
                add(new OrderItem(UUID.randomUUID(), 100L, 1));
            }
        };
        var customerId = UUID.randomUUID();
        Order order = orderService.createOrder(customerId, orderItems, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount() == 90L,
                MessageFormat.format("totalAmount {0} is not 90L", order.totalAmount()));

    }

    private static void printResource(Resource resource, String x) throws IOException {
        var file = resource.getFile();
        var strings = Files.readAllLines(file.toPath());    // 개행 기준으로 String 나눔
        System.out.println(x
                + strings.stream().reduce("", (a, b) -> a + "\n" + b));
    }

    private static void printProperties(OrderProperties orderProperties) {
        System.out.println(MessageFormat.format("orderProperties.getVersion() = {0}",
                orderProperties.getVersion()));
        System.out.println(MessageFormat.format("orderProperties.getDescription() = {0}",
                orderProperties.getDescription()));
        System.out.println(MessageFormat.format("orderProperties.getVersion() = {0}",
                orderProperties.getVersion()));
        System.out.println(MessageFormat.format("orderProperties.getMinimumOrderAmount() = {0}",
                orderProperties.getMinimumOrderAmount()));
        System.out.println(MessageFormat.format("orderProperties.getJavaHome() = {0}",
                orderProperties.getJavaHome()));
        System.out.println(MessageFormat.format("orderProperties.getSupportVendors() = {0}",
                orderProperties.getSupportVendors()));

    }
}
