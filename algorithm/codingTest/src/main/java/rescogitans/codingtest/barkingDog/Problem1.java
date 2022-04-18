package rescogitans.codingtest.barkingDog;

import lombok.extern.slf4j.Slf4j;

/**
 * 시간복잡도: O(N)
 */
@Slf4j
public class Problem1 {

    public static long solution(int n) {
        long answer = 0;
        for (int i = 3; i <= n; i++) {
            if (isMultipleOfThree(i) || isMultipleOfFive(i)) {
                answer += i;
                log.info("added i = {}, answer becomes ={}", i, answer);
            }
        }
        return answer;
    }

    private static boolean isMultipleOfThree(int i) {
        return i%3==0;
    }

    private static boolean isMultipleOfFive(int i) {
        return i%5==0;
    }
}
