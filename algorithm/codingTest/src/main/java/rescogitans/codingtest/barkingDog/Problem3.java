package rescogitans.codingtest.barkingDog;

import lombok.extern.slf4j.Slf4j;

/**
 * 시간복잡도: O(루트 N)
 */
@Slf4j
public class Problem3 {

    public static int solution(int n) {
        for (int i = 0; i < n; i++) {
            long squared = i*i;
            log.info("squared = {}, at value i = {}", squared, i);
            if (squared > n) {
                log.info("Squared value is over n");
                return 0;
            }
            if (squared==n) {
                log.info("Squared value is n");
                return 1;
            }
        }
        return 0;
    }
}