package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedAppTest {

    @Test
    void unCheckedExceptionApp() {
        var controller = new Controller();
        assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();

        void request() {
            service.businessLogic();
        }
    }

    @Test
    @DisplayName("예외 표시")
    void printException() {
        var controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
//            e.printStackTrace();  안 좋은 방법임. 로그를 남겨야 함
            log.error("예외 발생, 메시지: {}, ", e.getMessage(), e);
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetWorkClient netWorkClient = new NetWorkClient();

        void businessLogic() {
            repository.call();
            netWorkClient.call();
        }
    }

    static class NetWorkClient {
        void call() {
            try {
                throw new ConnectException("연결 실패");
            } catch (ConnectException e) {
                throw new RuntimeConnectException(e);
            }
        }
    }

    static class Repository {
        void call() {
            try {
                throw new SQLException("SQL 에러");
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(Throwable cause) {
            super(cause);
        }
    }
}
