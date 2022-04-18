package rescogitans.codingtest.barkingDog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Problem2Test {

    private static final Map<TestCase, Integer> testCase = new HashMap<>();

    @RequiredArgsConstructor
    @Getter
    static class TestCase {
        private final int[] arr;
        private final int n;
    }

    @BeforeAll
    static void initTestCase() {
        testCase.put(new TestCase(new int[]{1, 52, 48}, 3), 1);
        testCase.put(new TestCase(new int[]{50, 42}, 2), 0);
        testCase.put(new TestCase(new int[]{4, 13, 63, 87}, 4), 1);
    }

    @Test
    void solution() {
        Set<TestCase> testCaseInputs = testCase.keySet();
        for (TestCase testCaseInput : testCaseInputs) {
            assertThat(Problem2.solution(testCaseInput.getArr(), testCaseInput.getN()))
                    .isEqualTo(testCase.get(testCaseInput));
        }
    }
}