package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

public class CheckedAppTest {

    @Test
    void checkedExceptionApp() {
        var controller = new Controller();
        assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();

        void request() throws SQLException, ConnectException {
            service.businessLogic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetWorkClient netWorkClient = new NetWorkClient();

        void businessLogic() throws ConnectException, SQLException {
            repository.call();
            netWorkClient.call();
        }
    }

    static class NetWorkClient {
        void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        void call() throws SQLException {
            throw new SQLException("SQL 에러");
        }
    }
}
