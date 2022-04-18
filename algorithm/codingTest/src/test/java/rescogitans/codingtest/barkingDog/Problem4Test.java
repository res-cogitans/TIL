package rescogitans.codingtest.barkingDog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Problem4Test {

    private static final Map<Integer, Integer> testCase = new HashMap<>();

    @BeforeAll
    static void initTestCase() {
        testCase.put(5, 4);
        testCase.put(97615282, 67108864);
        testCase.put(1024, 1024);
    }

    @Test
    void solution() {
        Set<Integer> testCaseInputs = testCase.keySet();
        for (Integer testCaseInput : testCaseInputs) {
            assertThat(Problem4.solution(testCaseInput)).isEqualTo(testCase.get(testCaseInput));
        }
    }

    @Test
    void refactorSolution() {
        Set<Integer> testCaseInputs = testCase.keySet();
        for (Integer testCaseInput : testCaseInputs) {
            assertThat(Problem4.refactorSolution(testCaseInput)).isEqualTo(testCase.get(testCaseInput));
        }
    }

}