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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class OrderTester {

    private static final Logger log = LoggerFactory.getLogger(OrderTester.class);

    public static void main(String[] args) throws IOException {
//        var ac = new AnnotationConfigApplicationContext(AppConfiguration.class);

        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        var ac = new AnnotationConfigApplicationContext();

        log.info("logger name => {}", log.getName());

        var resource = ac.getResource("application.yaml");
        var resourceWithOutJar = ac.getResource("file:sample");
        var resourceWithUrl = ac.getResource("https://stackoverflow.com/");

        //classpath 가 기본적으로 가져오는 위치임. 명시적으로 표현 가능
//        var resource = ac.getResource("classpath:application.yaml");


        //resource 어떤 클래스인지?
        log.info(MessageFormat.format(
                "resource -> {0}", resource.getClass().getCanonicalName()));
        log.info(MessageFormat.format(
                "resourceWithUrl -> {0}", resourceWithUrl.getClass().getCanonicalName()));

        //resource 출력해보기
        printResource(resource, "[application.yaml file reading...] = ");
        //실행 workingDirectory 기준으로 탐색한다.
        printResource(resourceWithOutJar, "[sample file reading...] = ");
//        printResource(resourceWithUrl, "[file at url reading...] = ");
        //파일이 아닌 url 경우
        prinUrlResource(resourceWithUrl);

        ac.register(AppConfiguration.class);
        ConfigurableEnvironment environment = ac.getEnvironment();
        environment.setActiveProfiles("local");
        ac.refresh();
        String[] activeProfiles = environment.getActiveProfiles();
        log.info(MessageFormat.format("[activeProfiles] = {0}", activeProfiles));

//        String property = environment.getProperty("kdt.description");
//        log.info("property = " + property);

        /*
        Properties 관련 내용0

        var orderProperties = ac.getBean(OrderProperties.class);
        printProperties(orderProperties);
*/

        OrderService orderService = ac.getBean(OrderService.class);
        VoucherRepository voucherRepository = ac.getBean(VoucherRepository.class);
        VoucherService voucherService = ac.getBean(VoucherService.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        log.info(MessageFormat.format(
                "is Jdbc Repo -> {0}", voucherRepository instanceof JdbcVoucherRepository));
        log.info(MessageFormat.format(
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

    private static void prinUrlResource(Resource resourceWithUrl) throws IOException {
        var readableByteChannel = Channels.newChannel(resourceWithUrl.getURL().openStream());
        var bufferedReader = new BufferedReader(Channels.newReader(readableByteChannel, UTF_8));
        var contents = bufferedReader.lines().collect(Collectors.joining("\n"));
        log.trace(contents);
    }

    private static void printResource(Resource resource, String x) throws IOException {
        var file = resource.getFile();
        var strings = Files.readAllLines(file.toPath());    // 개행 기준으로 String 나눔
        log.info(x
                + strings.stream().reduce("", (a, b) -> a + "\n" + b));
    }

    private static void printProperties(OrderProperties orderProperties) {
        log.info(MessageFormat.format("orderProperties.getVersion() = {0}",
                orderProperties.getVersion()));
        log.info(MessageFormat.format("orderProperties.getDescription() = {0}",
                orderProperties.getDescription()));
        log.info(MessageFormat.format("orderProperties.getVersion() = {0}",
                orderProperties.getVersion()));
        log.info(MessageFormat.format("orderProperties.getMinimumOrderAmount() = {0}",
                orderProperties.getMinimumOrderAmount()));
        log.info(MessageFormat.format("orderProperties.getJavaHome() = {0}",
                orderProperties.getJavaHome()));
        log.info(MessageFormat.format("orderProperties.getSupportVendors() = {0}",
                orderProperties.getSupportVendors()));

    }
}
