package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DBConnectionUtilTest {

    @Test
    @DisplayName("DB 커넥션 획득")
    void connection() {
        var connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
    }
}
