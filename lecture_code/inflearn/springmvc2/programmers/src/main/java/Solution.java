import java.util.ArrayList;
import java.util.List;

class Solution {

    static List<Integer> route = new ArrayList<>();
    static List<Integer> finalRoute = new ArrayList<>();
    static int maxCapacity = 0;
    static int length = 1;

    public int[] solution(int n, int[] passenger, int[][] train) {

        for (int i = 1; i < n; i++) {
            for (int j = i+1; j <= n; j++) {
                route.clear();
                route.add(1);
                length = 1;
                trainRoute(1, j, n, train);
                int capacity = 0;
                for (int k = 0; k < route.size(); k++) {
                    capacity += passenger[route.get(k)-1];
                }
                if (capacity >= maxCapacity) {
                    finalRoute = route;
                    maxCapacity = capacity;
                }
            }
        }

        int end = finalRoute.get(finalRoute.size()-1);
        int passengersOfDay = maxCapacity;
        int[] answer = {end, passengersOfDay};
        return answer;
    }

    private void trainRoute(int start, int end, int n, int[][] train) {
        for (int i = 0; i < n-1; i++) {
            if (train[i][0] == start) {
                if (route.contains(train[i][1])) {
                    continue;
                }
                route.add(train[i][1]);
                if (train[i][1]==end) {
                    return;
                }
                trainRoute(train[i][1], end, n, train);
                return;
            }
        }
        for (int i = 0; i < n-1; i++) {
            if (train[i][1] == start) {
                if (route.contains(train[i][0])) {
                    continue;
                }
                route.add(train[i][0]);
                if (train[i][0]==end) {
                    return;
                }
                trainRoute(train[i][0], end, n, train);
                return;
            }
        }
        // 모든 루트를 체크하지 못함. 짧은 루트라도 결승점을 만나면 멈춰버림 수정필요
    }

}