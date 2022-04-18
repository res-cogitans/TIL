package rescogitans.codingtest.barkingDog;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class Problem1Test {

    private static final Map<Integer, Long> testCase = new HashMap<>();

    @BeforeAll
    static void initTestCase() {
        testCase.put(16, 60L);
        testCase.put(34567, 278812814L);
        testCase.put(27639, 178254968L);
    }

    @Test
    void solution() {
        Set<Integer> testCaseInputs = testCase.keySet();
        for (Integer testCaseInput : testCaseInputs) {
            assertThat(Problem1.solution(testCaseInput)).isEqualTo(testCase.get(testCaseInput));
        }
    }
}