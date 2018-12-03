package puzzles;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends PuzzleDay {

  /**
   * --- Day 3: No Matter How You Slice It ---
   * The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit (thanks to
   * someone who helpfully wrote its box IDs on the wall of the warehouse in the middle of the
   * night). Unfortunately, anomalies are still affecting them - nobody can even agree on how to
   * cut the fabric.
   *
   * The whole piece of fabric they're working on is a very large square - at least 1000 inches
   * on each side.
   *
   * Each Elf has made a claim about which area of fabric would be ideal for Santa's suit. All
   * claims have an ID and consist of a single rectangle with edges parallel to the edges of the
   * fabric. Each claim's rectangle is defined as follows:
   *
   * The number of inches between the left edge of the fabric and the left edge of the rectangle.
   * The number of inches between the top edge of the fabric and the top edge of the rectangle.
   * The width of the rectangle in inches.
   * The height of the rectangle in inches.
   * A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from the
   * left edge, 2 inches from the top edge, 5 inches wide, and 4 inches tall. Visually, it claims
   * the square inches of fabric represented by # (and ignores the square inches of fabric
   * represented by .) in the diagram below:
   *
   * ...........
   * ...........
   * ...#####...
   * ...#####...
   * ...#####...
   * ...#####...
   * ...........
   * ...........
   * ...........
   * The problem is that many of the claims overlap, causing two or more claims to cover part of
   * the same areas. For example, consider the following claims:
   *
   * #1 @ 1,3: 4x4
   * #2 @ 3,1: 4x4
   * #3 @ 5,5: 2x2
   * Visually, these claim the following areas:
   *
   * ........
   * ...2222.
   * ...2222.
   * .11XX22.
   * .11XX22.
   * .111133.
   * .111133.
   * ........
   * The four square inches marked with X are claimed by both 1 and 2. (Claim 3, while adjacent
   * to the others, does not overlap either of them.)
   *
   * If the Elves all proceed with their own plans, none of them will have enough fabric. How
   * many square inches of fabric are within two or more claims?
   */
  @Override
  public void solvePart1() {
    //init our fabric
    Fabric fabric = new Fabric(1000, 1000);
    for(Square square :parseInput()){
      fabric.fillClaims(square);
    }

    //check to see how many are over-claimed;
    Integer overclaims = 0;
    for(List<List<Integer>> row : fabric.claims){
      for(List<Integer> cell: row){
        if(cell.size() > 1){
          overclaims++;
        }
      }
    }

    print(overclaims);
  }

  private class Square {
    public Integer id;
    public Integer x;
    public Integer y;
    public Integer width;
    public Integer height;

    public Square(Integer id, Integer x, Integer y, Integer width, Integer height) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }
  }

  private class Fabric {
    public List<List<List<Integer>>> claims;

    public Fabric(Integer width, Integer height) {
      this.claims = new ArrayList<>();
      for(int x = 0; x < width; x++){
        List<List<Integer>> row = new ArrayList<>();
        claims.add(row);
        for(int y = 0; y < height; y++){
          row.add(new ArrayList<>());
        }
      }
    }

    public void fillClaims(Square square){
      for(int x = square.x; x < square.x + square.width; x++){
        for(int y = square.y; y < square.y + square.height; y++){
           claims.get(x).get(y).add(square.id);
        }
      }
    }

    public Set<Integer> getClaimIds(Square square){
      Set<Integer> ids = new HashSet<>();
      for(int x = square.x; x < square.x + square.width; x++){
        for(int y = square.y; y < square.y + square.height; y++){
          ids.addAll(claims.get(x).get(y));
        }
      }
      return ids;
    }
  }

  private List<Square> parseInput(){
    List<Square> squares = new ArrayList<>();
    Pattern pattern = Pattern.compile("#(\\d*) @ (\\d*),(\\d*): (\\d*)x(\\d*)");
    for(String line : getInput()){
      Matcher matcher = pattern.matcher(line);
      if(matcher.find()){
        squares.add(new Square(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4)),
                Integer.parseInt(matcher.group(5))));


      }
    }
    return squares;
  }

  private List<List<List<Integer>>> initFabric(){
    List<List<List<Integer>>> fabric = new ArrayList<>();
    for(int x = 0; x < 1000; x++){
      List<List<Integer>> row = new ArrayList<>();
      fabric.add(row);
      for(int y = 0; y < 1000; y++){
        row.add(new ArrayList<>());
      }
    }
    return fabric;
  }


  /**
   * --- Part Two ---
   * Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single square
   * inch of fabric with any other claim. If you can somehow draw attention to it, maybe the
   * Elves will be able to make Santa's suit after all!
   *
   * For example, in the claims above, only claim 3 is intact after all claims are made.
   *
   * What is the ID of the only claim that doesn't overlap?
   */
  @Override
  public void solvePart2() {
    Fabric fabric = new Fabric(1000, 1000);
    for(Square square :parseInput()){
      fabric.fillClaims(square);
    }

    for(Square square :parseInput()){
      if(fabric.getClaimIds(square).size() == 1){
        print(square.id);
        return;
      };
    }
  }
}
