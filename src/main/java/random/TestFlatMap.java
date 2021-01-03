package random;

import java.util.Arrays;
import java.util.List;

public class TestFlatMap {



    public static void main(String[] args) {
        List<List<Integer>> dList = Arrays.asList(Arrays.asList(1,2,3,4), Arrays.asList(5,6,7,8));
        dList.stream().flatMap(arrays -> arrays.stream()).forEach(arr -> System.out.println(arr));
    }

}
