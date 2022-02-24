public class Solution01 {

    public int solution(int[] arr) {
        int length = arr.length;

        int pre = 1001;
        int answer = -1;

        for (int i = 0; i <= 255; i++) {
            int gap = Math.abs(
                    length - 2*countLessThanLimit(arr, i)
            );
            if (gap < pre) {
                answer = i;
                pre = gap;
            }

        }
        return answer;
    }

    public int countLessThanLimit(int[] arr, int lim) {
        int cnt = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]<lim) {
                cnt++;
            }
        }
        return cnt;
    }
}
