package rescogitans.codingtest.barkingDog;

import lombok.extern.slf4j.Slf4j;

/**
 * 시간복잡도: O(N^2)
 */
@Slf4j
public class Problem2 {

    public static int solution(int[] arr, int n) {
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j <n; j++) {
                if (isSumOfTwoElementHundred(arr[i], arr[j])) {
                    log.info("Sum is 100, arr[i] = {}, arr[j] = {}", arr[i], arr[j]);
                    return 1;
                }
            }
        }
        return 0;
    }

    private static boolean isSumOfTwoElementHundred(int a, int b) {
        return (a+b==100);
    }
}
