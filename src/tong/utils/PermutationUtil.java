package tong.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

public class PermutationUtil {

    private PermutationUtil () {}

    public static ArrayList<ArrayList<Integer>> generateAllPermutations(int[] array) {
        ArrayList<ArrayList<Integer>> retv = new ArrayList<>();
        permute(array, 0, retv);
        return retv;
    }
    /*
    This is the meat of the brute force method. It recursively sorts an array into its various permutations.
     */
    static void permute(int[] num, int start, ArrayList<ArrayList<Integer>> result) {

        if (start >= num.length) {

            ArrayList<Integer> item = convertArrayToList(num);
            //This checks if the array already exists in the list in reverse form.
            ArrayUtils.reverse(num);
            ArrayList<Integer> reversedItem = convertArrayToList(num);

            if(!result.contains(reversedItem)) {
                result.add(item);
            }
        }

        for (int j = start; j <= num.length - 1; j++) {
            swap(num, start, j);
            permute(num, start + 1, result);
            swap(num, start, j);
        }
    }

    static private ArrayList<Integer> convertArrayToList(int[] num) {
        ArrayList<Integer> item = new ArrayList<>();
        for (int h = 0; h < num.length; h++) {
            item.add(num[h]);
        }
        return item;
    }

    static private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
