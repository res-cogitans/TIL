package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CheckedTest {

    @Test
    @DisplayName("CheckedException 처리 - catch")
    void catchCheckedException() {
        var service = new Service();
        service.callCatch();
    }

    @Test
    @DisplayName("CheckedException 처리하지 못한 경우 - throw만 함")
    void throwCheckedException() {
        var service = new Service();
        assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception 상속 받은 예외는 CheckedException 된다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * CheckedException 경우
     * 예외를 잡아서 처리하거나, 던지거나 둘 중 하나를 필수로 수행해야 함
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                //예외 처리 로직
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * CheckedException 밖으로 던지는 코드
         * CheckedException 잡지 않고 밖으로 던지려면 "throws 해당 예외"를 메서드에 필수로 선언해야 한다.
         * @Throws MyCheckedException
         */
        void callThrow() throws MyCheckedException {
            repository.call();
        }

    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("CheckedException message");
        }
    }
}
