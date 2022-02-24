import java.util.ArrayList;
import java.util.List;

public class Solution02 {

    static String preType = " ";
    static int pre = 0;
    static String preName = "";
    static List<String> saved = new ArrayList<>();
    static List<String> answer = new ArrayList<>();
    static boolean preWasNotification = false;
    static boolean removeAndAdd = false;

    public String[] solution(String[] records) {

        for (int i = 0; i< records.length; i++) {
            String iType = whatKind(records[i]);
            recording(records, i, iType);
        }

        System.out.println("saved = " + saved);
        System.out.println("answer = " + answer);

        return answer.toArray(new String[answer.size()]);
    }

    private void recording(String[] records, int i, String iType) {

        // 같은 동작이 2 번 이상일 때
        if (preType.equals(iType)) {
            pre++;
            if (preWasNotification) {
                removeAndAdd = true;
            }
            preWasNotification = false;
        }
        else {
            if (preWasNotification) {
                preType = iType;
                pre = 0;
                preName = records[i].split(" ")[0];
                preWasNotification = false;
                return;
            }
            if (preType.equals(" ")) {
                preType = iType;
                preName = records[i].split(" ")[0];
                return;
            }

            String toAdd = "ed on";
            if(preType.equals("share")) {
                toAdd = "d";
            }

            if (removeAndAdd) {
                saved.remove(saved.size()-1);
                removeAndAdd = false;
            }

            if (pre ==0) {
                saved.add(
                        preName + " " + preType + toAdd + " your post"
                );
            }
            else {
                saved.add(
                        preName + " and " + pre + " others " + preType + toAdd + " your post"
                );
            }

            // 저장소로 보낼 때
            if (iType.equals("notification")) {
                notification();
                return;
            }
            preType = iType;
            pre = 0;
            preName = records[i].split(" ")[0];
        }
    }

    private void notification() {
        answer.add(
                saved.get(saved.size()-1)
        );
        saved.remove(
                saved.size()-1
        );
        checkPrevious();
    }

    private void checkPrevious() {
        String s = saved.get(saved.size() - 1);
        System.out.println("s = " + s);
        String[] split = s.split(" ");

        preName = split[0];
        if (split[1].equals("and")) {
            pre = Integer.parseInt(split[2]);
            preType = split[4];
        }
        else {
            pre = 0;
            preType = split[1];
        }
        preWasNotification = true;
    }

    public String whatKind(String s) {
        String[] cut = s.split(" ");
        return cut[1];
    }
}