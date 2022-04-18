package rescogitans.codingtest.barkingDog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Problem3Test {

    private static final Map<Integer, Integer> testCase = new HashMap<>();

    @BeforeAll
    static void initTestCase() {
        testCase.put(9, 1);
        testCase.put(693953651, 0);
        testCase.put(756580036, 1);
    }

    @Test
    void solution() {
        Set<Integer> testCaseInputs = testCase.keySet();
        for (Integer testCaseInput : testCaseInputs) {
            assertThat(Problem3.solution(testCaseInput)).isEqualTo(testCase.get(testCaseInput));
        }
    }
}