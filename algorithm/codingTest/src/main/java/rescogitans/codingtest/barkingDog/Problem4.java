package rescogitans.codingtest.barkingDog;

import lombok.extern.slf4j.Slf4j;

/**
 * 시간복잡도: O(logN)
 */
@Slf4j
public class Problem4 {
    public static int solution(int n) {
        int answer = 0;
        for (int i = 0; i < n; i++) {
            int tmp = powOfTwo(i);
            if (tmp > n) {
                log.info("answer = {}", answer);
                return answer;
            }
            else {
                answer = tmp;
            }
        }
        return answer;
    }

    public static int refactorSolution(int n) {
        int val = 1;
        while (2*val <= n) {
            val *= 2;
        }
        return val;
    }

    private static int powOfTwo(int i) {
        return (int) Math.pow(2, i);
    }
}
