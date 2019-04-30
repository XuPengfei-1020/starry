package automate;

import java.util.ArrayList;
import java.util.Arrays;

public class TestUtil {
    public static boolean compareRanges(short[][] ranges, short[][] expect) {
        if (ranges == null || expect == null) {
            return false;
        }

        if (ranges.length != expect.length) {
            return false;
        }

        Arrays.sort(ranges, (short[] r1, short[] r2) -> r1[0] == r2[0] ? Short.compare(r1[1], r2[1]) :
            Short.compare(r1[0], r2[0]));
        Arrays.sort(expect, (short[] r1, short[] r2) -> r1[0] == r2[0] ? Short.compare(r1[1], r2[1]) :
            Short.compare(r1[0], r2[0]));

        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i][0] != expect[i][0] || ranges[i][1] != expect[i][1]) {
                return false;
            }
        }

        return true;
    }

    public static boolean compareRanges(ArrayList<short[]> ranges, ArrayList<short[]> expect) {
        if (ranges == null || expect == null) {
            return false;
        }

        if (ranges.size() != expect.size()) {
            return false;
        }

        ranges.sort((short[] r1, short[] r2) -> r1[0] == r2[0] ? Short.compare(r1[1], r2[1]) :
            Short.compare(r1[0], r2[0]));
        expect.sort((short[] r1, short[] r2) -> r1[0] == r2[0] ? Short.compare(r1[1], r2[1]) :
            Short.compare(r1[0], r2[0]));

        for (int i = 0; i < ranges.size(); i++) {
            if (ranges.get(i)[0] != expect.get(i)[0] || ranges.get(i)[1] != expect.get(i)[1]) {
                return false;
            }
        }

        return true;
    }
}
