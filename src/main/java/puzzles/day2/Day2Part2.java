package puzzles.day2;

import utils.InputParser;

import java.io.IOException;
import java.util.*;

/**
 * --- Part Two ---
 * Confident that your list of box IDs is complete, you're ready to find the boxes full of
 * prototype fabric.
 * <p>
 * The boxes will have IDs which differ by exactly one character at the same position in both
 * strings. For example, given the following box IDs:
 * <p>
 * abcde
 * fghij
 * klmno
 * pqrst
 * fguij
 * axcye
 * wvxyz
 * The IDs abcde and axcye are close, but they differ by two characters (the second and fourth).
 * However, the IDs fghij and fguij differ by exactly one character, the third (h and u). Those
 * must be the correct boxes.
 * <p>
 * What letters are common between the two correct box IDs? (In the example above, this is found
 * by removing the differing character from either ID, producing fgij.)
 */
public class Day2Part2 {

  public static void main(String[] args) throws IOException {


    List<String> inputLines = InputParser.getInputStrings("day-2-part-1.txt");
    List<Map<Character, List<Integer>>> data = new ArrayList<>();

    //set up our data - create groups of row indexes that share the same letter per column
    //IE: data[columnIndex][character] = [row Index, row index, ...]
    for (int i = 0; i < inputLines.size(); i++) {

      char[] chars = inputLines.get(i).toCharArray();
      for (int j = 0; j < chars.length; j++) {

        Map<Character, List<Integer>> characterMap;
        if (data.size() - 1 < j) {
          characterMap = new HashMap<>();
          data.add(characterMap);
        } else {
          characterMap = data.get(j);
        }

        char c = chars[j];
        List<Integer> codes;
        if (characterMap.containsKey(c)) {
          codes = characterMap.get(c);
        } else {
          codes = new ArrayList<>();
          characterMap.put(c, codes);
        }
        codes.add(i);
      }
    }


    //loop through each code and use our data to see if there are any other rows that have the same
    // values for all columns except for one.
    for (int i = 0; i < inputLines.size(); i++) {
      List<Integer> intersects = new ArrayList<>(); //store a list of rows with dupes
      char[] chars = inputLines.get(i).toCharArray();
      for (int j = 0; j < chars.length; j++) {
        intersects.addAll(data.get(j).get(chars[j]));
      }
      Set<Integer> uniqueSet = new HashSet<>(intersects);
      for (Integer rowId : uniqueSet) {
        if (Collections.frequency(intersects, rowId) == (chars.length - 1)) {
          System.out.println(inputLines.get(i));
          System.out.println(inputLines.get(rowId));
          return;
        }
      }
    }
  }


}
