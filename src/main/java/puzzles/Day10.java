package puzzles;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 extends PuzzleDay {

  /**
   * --- Day 10: The Stars Align ---
   * It's no use; your navigation system simply isn't capable of providing walking directions in
   * the arctic circle, and certainly not in 1018.
   *
   * The Elves suggest an alternative. In times like these, North Pole rescue operations will
   * arrange points of light in the sky to guide missing Elves back to base. Unfortunately, the
   * message is easy to miss: the points move slowly enough that it takes hours to align them,
   * but have so much momentum that they only stay aligned for a second. If you blink at the
   * wrong time, it might be hours before another message appears.
   *
   * You can see these points of light floating in the distance, and record their position in the
   * sky and their velocity, the relative change in position per second (your puzzle input). The
   * coordinates are all given from your perspective; given enough time, those positions and
   * velocities will move the points into a cohesive message!
   *
   * Rather than wait, you decide to fast-forward the process and calculate what the points will
   * eventually spell.
   *
   * For example, suppose you note the following points:
   *
   * position=< 9,  1> velocity=< 0,  2>
   * position=< 7,  0> velocity=<-1,  0>
   * position=< 3, -2> velocity=<-1,  1>
   * position=< 6, 10> velocity=<-2, -1>
   * position=< 2, -4> velocity=< 2,  2>
   * position=<-6, 10> velocity=< 2, -2>
   * position=< 1,  8> velocity=< 1, -1>
   * position=< 1,  7> velocity=< 1,  0>
   * position=<-3, 11> velocity=< 1, -2>
   * position=< 7,  6> velocity=<-1, -1>
   * position=<-2,  3> velocity=< 1,  0>
   * position=<-4,  3> velocity=< 2,  0>
   * position=<10, -3> velocity=<-1,  1>
   * position=< 5, 11> velocity=< 1, -2>
   * position=< 4,  7> velocity=< 0, -1>
   * position=< 8, -2> velocity=< 0,  1>
   * position=<15,  0> velocity=<-2,  0>
   * position=< 1,  6> velocity=< 1,  0>
   * position=< 8,  9> velocity=< 0, -1>
   * position=< 3,  3> velocity=<-1,  1>
   * position=< 0,  5> velocity=< 0, -1>
   * position=<-2,  2> velocity=< 2,  0>
   * position=< 5, -2> velocity=< 1,  2>
   * position=< 1,  4> velocity=< 2,  1>
   * position=<-2,  7> velocity=< 2, -2>
   * position=< 3,  6> velocity=<-1, -1>
   * position=< 5,  0> velocity=< 1,  0>
   * position=<-6,  0> velocity=< 2,  0>
   * position=< 5,  9> velocity=< 1, -2>
   * position=<14,  7> velocity=<-2,  0>
   * position=<-3,  6> velocity=< 2, -1>
   * Each line represents one point. Positions are given as <X, Y> pairs: X represents how far
   * left (negative) or right (positive) the point appears, while Y represents how far up
   * (negative) or down (positive) the point appears.
   *
   * At 0 seconds, each point has the position given. Each second, each point's velocity is added
   * to its position. So, a point with velocity <1, -2> is moving to the right, but is moving
   * upward twice as quickly. If this point's initial position were <3, 9>, after 3 seconds, its
   * position would become <6, 3>.
   *
   * Over time, the points listed above would move like this:
   *
   * Initially:
   * ........#.............
   * ................#.....
   * .........#.#..#.......
   * ......................
   * #..........#.#.......#
   * ...............#......
   * ....#.................
   * ..#.#....#............
   * .......#..............
   * ......#...............
   * ...#...#.#...#........
   * ....#..#..#.........#.
   * .......#..............
   * ...........#..#.......
   * #...........#.........
   * ...#.......#..........
   *
   * After 1 second:
   * ......................
   * ......................
   * ..........#....#......
   * ........#.....#.......
   * ..#.........#......#..
   * ......................
   * ......#...............
   * ....##.........#......
   * ......#.#.............
   * .....##.##..#.........
   * ........#.#...........
   * ........#...#.....#...
   * ..#...........#.......
   * ....#.....#.#.........
   * ......................
   * ......................
   *
   * After 2 seconds:
   * ......................
   * ......................
   * ......................
   * ..............#.......
   * ....#..#...####..#....
   * ......................
   * ........#....#........
   * ......#.#.............
   * .......#...#..........
   * .......#..#..#.#......
   * ....#....#.#..........
   * .....#...#...##.#.....
   * ........#.............
   * ......................
   * ......................
   * ......................
   *
   * After 3 seconds:
   * ......................
   * ......................
   * ......................
   * ......................
   * ......#...#..###......
   * ......#...#...#.......
   * ......#...#...#.......
   * ......#####...#.......
   * ......#...#...#.......
   * ......#...#...#.......
   * ......#...#...#.......
   * ......#...#..###......
   * ......................
   * ......................
   * ......................
   * ......................
   *
   * After 4 seconds:
   * ......................
   * ......................
   * ......................
   * ............#.........
   * ........##...#.#......
   * ......#.....#..#......
   * .....#..##.##.#.......
   * .......##.#....#......
   * ...........#....#.....
   * ..............#.......
   * ....#......#...#......
   * .....#.....##.........
   * ...............#......
   * ...............#......
   * ......................
   * ......................
   * After 3 seconds, the message appeared briefly: HI. Of course, your message will be much
   * longer and will take many more seconds to appear.
   *
   * What message will eventually appear in the sky?
   */
  @Override
  public void solvePart1() {
    Sky sky = getSky();

    long lastMsgArea = sky.msgArea();

    sky.tick();
    while(sky.msgArea() < lastMsgArea){
      lastMsgArea = sky.msgArea();
      sky.tick();
    }
    sky.unTick();
    sky.print();
    return;
  }

  public Sky getSky(){


    List<Star> stars = getRegexInput("position=< ?(-?\\d*),  ?(-?\\d*)> velocity=< ?(-?\\d*),  ?(-?\\d*)>")
            .stream()
            .map(matcher -> {
              matcher.find();
              return new Star(
                    new Point(
                            Integer.parseInt(matcher.group(1)),
                            Integer.parseInt(matcher.group(2))
                    ),
                    new Point(
                            Integer.parseInt(matcher.group(3)),
                            Integer.parseInt(matcher.group(4))

            ));
            }
            ).collect(Collectors.toList());

    return new Sky(stars);
  }

  public class Sky{
    List<Star> stars;
    long time = 0;

    public Sky(List<Star> stars) {
      this.stars = stars;
    }

    public void tick(){
      time++;
      stars.forEach(Star::tick);
    }

    public void unTick(){
      time--;
      stars.forEach(Star::unTick);
    }

    public void print(){
      int absMinX = (int) minX();
      int absMaxX = (int) maxX();
      int absMinY = (int) minY();
      int absMaxY = (int) maxY();

      int xSize = (absMaxX - absMinX) + 1;
      int ySize = (absMaxY - absMinY) + 1;

      String[][] msg = new String[xSize][];
      for(int x = 0; x < xSize; x++){
        msg[x] = new String[ySize];
      }

      stars.forEach(star -> {
        msg[star.position.x - absMinX][star.position.y - absMinY] = "#";
      });

      System.out.print("\n\n");

      for(int y = 0; y < ySize; y++){
        System.out.print("\n");
        for(int x = 0; x < xSize; x++){
          System.out.print(msg[x][y] == null? '.' : "#");
        }

      }

    }

    public long msgArea(){
      return (maxX() - minX()) * (maxY() - minY());
    }

    public long minX(){
      return (long) stars.stream().min(Comparator.comparing(star -> star.position.x)).get()
              .position.x;
    }
    public long maxX(){
      return (long) stars.stream().max(Comparator.comparing(star -> star.position.x)).get()
              .position.x;
    }
    public long minY(){
      return (long) stars.stream().min(Comparator.comparing(star -> star.position.y)).get()
              .position.y;
    }
    public long maxY(){
      return (long) stars.stream().max(Comparator.comparing(star -> star.position.y)).get()
              .position.y;
    }
  }
  public class Star{
    public Point position;
    public Point velocity;

    public Star(Point position, Point velocity) {
      this.position = position;
      this.velocity = velocity;
    }
    public void tick(){
      this.position.x += this.velocity.x;
      this.position.y += this .velocity.y;
    }
    public void unTick(){
      this.position.x -= this.velocity.x;
      this.position.y -= this .velocity.y;
    }

  }
  public class Point{
    public int x;
    public int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  /**
   * --- Part Two ---
   * Good thing you didn't have to wait, because that would have taken a long time - much longer
   * than the 3 seconds in the example above.
   *
   * Impressed by your sub-hour communication capabilities, the Elves are curious: exactly how
   * many seconds would they have needed to wait for that message to appear?
   */
  @Override
  public void solvePart2() {

    Sky sky = getSky();

    long lastMsgArea = sky.msgArea();

    sky.tick();
    while(sky.msgArea() < lastMsgArea){
      lastMsgArea = sky.msgArea();
      sky.tick();
    }
    sky.unTick();
    print(sky.time);
  }
}
