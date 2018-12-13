package puzzles;

import java.util.*;
import java.util.stream.Collectors;

public class Day13 extends PuzzleDay {
  /**
   * --- Day 13: Mine Cart Madness ---
   * A crop of this size requires significant logistics to transport produce, soil, fertilizer,
   * and so on. The Elves are very busy pushing things around in carts on some kind of
   * rudimentary system of tracks they've come up with.
   *
   * Seeing as how cart-and-track systems don't appear in recorded history for another 1000
   * years, the Elves seem to be making this up as they go along. They haven't even figured out
   * how to avoid collisions yet.
   *
   * You map out the tracks (your puzzle input) and see where you can help.
   *
   * Tracks consist of straight paths (| and -), curves (/ and \), and intersections (+). Curves
   * connect exactly two perpendicular pieces of track; for example, this is a closed loop:
   *
   * /----\
   * |    |
   * |    |
   * \----/
   * Intersections occur when two perpendicular paths cross. At an intersection, a cart is
   * capable of turning left, turning right, or continuing straight. Here are two loops connected
   * by two intersections:
   *
   * /-----\
   * |     |
   * |  /--+--\
   * |  |  |  |
   * \--+--/  |
   *    |     |
   *    \-----/
   * Several carts are also on the tracks. Carts always face either up (^), down (v), left (<),
   * or right (>). (On your initial map, the track under each cart is a straight path matching
   * the direction the cart is facing.)
   *
   * Each time a cart has the option to turn (by arriving at any intersection), it turns left the
   * first time, goes straight the second time, turns right the third time, and then repeats
   * those directions starting again with left the fourth time, straight the fifth time, and so
   * on. This process is independent of the particular intersection at which the cart has arrived
   * - that is, the cart has no per-intersection memory.
   *
   * Carts all move at the same speed; they take turns moving a single step at a time. They do
   * this based on their current location: carts on the top row move first (acting from left to
   * right), then carts on the second row move (again from left to right), then carts on the
   * third row, and so on. Once each cart has moved one step, the process repeats; each of these
   * loops is called a tick.
   *
   * For example, suppose there are two carts on a straight track:
   *
   * |  |  |  |  |
   * v  |  |  |  |
   * |  v  v  |  |
   * |  |  |  v  X
   * |  |  ^  ^  |
   * ^  ^  |  |  |
   * |  |  |  |  |
   * First, the top cart moves. It is facing down (v), so it moves down one square. Second, the
   * bottom cart moves. It is facing up (^), so it moves up one square. Because all carts have
   * moved, the first tick ends. Then, the process repeats, starting with the first cart. The
   * first cart moves down, then the second cart moves up - right into the first cart, colliding
   * with it! (The location of the crash is marked with an X.) This ends the second and last tick.
   *
   * Here is a longer example:
   *
   * /->-\
   * |   |  /----\
   * | /-+--+-\  |
   * | | |  | v  |
   * \-+-/  \-+--/
   *   \------/
   *
   * /-->\
   * |   |  /----\
   * | /-+--+-\  |
   * | | |  | |  |
   * \-+-/  \->--/
   *   \------/
   *
   * /---v
   * |   |  /----\
   * | /-+--+-\  |
   * | | |  | |  |
   * \-+-/  \-+>-/
   *   \------/
   *
   * /---\
   * |   v  /----\
   * | /-+--+-\  |
   * | | |  | |  |
   * \-+-/  \-+->/
   *   \------/
   *
   * /---\
   * |   |  /----\
   * | /->--+-\  |
   * | | |  | |  |
   * \-+-/  \-+--^
   *   \------/
   *
   * /---\
   * |   |  /----\
   * | /-+>-+-\  |
   * | | |  | |  ^
   * \-+-/  \-+--/
   *   \------/
   *
   * /---\
   * |   |  /----\
   * | /-+->+-\  ^
   * | | |  | |  |
   * \-+-/  \-+--/
   *   \------/
   *
   * /---\
   * |   |  /----<
   * | /-+-->-\  |
   * | | |  | |  |
   * \-+-/  \-+--/
   *   \------/
   *
   * /---\
   * |   |  /---<\
   * | /-+--+>\  |
   * | | |  | |  |
   * \-+-/  \-+--/
   *   \------/
   *
   * /---\
   * |   |  /--<-\
   * | /-+--+-v  |
   * | | |  | |  |
   * \-+-/  \-+--/
   *   \------/
   *
   * /---\
   * |   |  /-<--\
   * | /-+--+-\  |
   * | | |  | v  |
   * \-+-/  \-+--/
   *   \------/
   *
   * /---\
   * |   |  /<---\
   * | /-+--+-\  |
   * | | |  | |  |
   * \-+-/  \-<--/
   *   \------/
   *
   * /---\
   * |   |  v----\
   * | /-+--+-\  |
   * | | |  | |  |
   * \-+-/  \<+--/
   *   \------/
   *
   * /---\
   * |   |  /----\
   * | /-+--v-\  |
   * | | |  | |  |
   * \-+-/  ^-+--/
   *   \------/
   *
   * /---\
   * |   |  /----\
   * | /-+--+-\  |
   * | | |  X |  |
   * \-+-/  \-+--/
   *   \------/
   * After following their respective paths for a while, the carts eventually crash. To help
   * prevent crashes, you'd like to know the location of the first crash. Locations are given in
   * X,Y coordinates, where the furthest left column is X=0 and the furthest top row is Y=0:
   *
   *            111
   *  0123456789012
   * 0/---\
   * 1|   |  /----\
   * 2| /-+--+-\  |
   * 3| | |  X |  |
   * 4\-+-/  \-+--/
   * 5  \------/
   * In this example, the location of the first crash is 7,3.
   */
  @Override
  public void solvePart1() {
    Mine mine = new Mine(getInput());
     print(mine.print());
  

    while(!mine.tick()){
      print(mine.print());
    }

    print(mine.print());
    Point collisionPoint = mine.getCollision();
    print("Collision: (" + collisionPoint.x + "," + collisionPoint.y + ")");
    return;
  }

  public class Mine{
    char[][] tracks;
    char[][] activeTracks;
    List<Cart> carts;
    int xCount;
    int yCount;

    public Mine(List<String> input) {
      xCount = input.get(0).length();
      yCount = input.size();
      tracks = new char[xCount][];
      activeTracks = new char[xCount][];
      for(int x = 0; x < xCount; x++){
        tracks[x] = new char[yCount];
        activeTracks[x] = new char[yCount];
      }
      carts = new ArrayList<>();

      for(int y = 0; y < input.size(); y++){
         char[] row = input.get(y).toCharArray();
         for(int x = 0; x < row.length; x++){
           char current = row[x];
           if(current == '>'){
             carts.add(new Cart(new Point(x, y), new Point(1,0)));
             current = '-';
           }
           if(current == '<'){
             carts.add(new Cart(new Point(x, y), new Point(-1,0)));
             current = '-';
           }
           if(current == 'v'){
             carts.add(new Cart(new Point(x, y), new Point(0,1)));
             current = '|';
           }
           if(current == '^'){
             carts.add(new Cart(new Point(x, y), new Point(0,-1)));
             current = '|';
           }
           tracks[x][y] = current;
           activeTracks[x][y] = row[x];
         }
      }
    }
    
    public boolean tick(){
      for(Cart cart : getCartQueue()){
        cart.tick(tracks, activeTracks);
        if(cart.collided){
          return true;
        }
      }
      return false;
      
    }

    public void tickInstantRemoveCollisions(){
      for(Cart cart : getCartQueue()){
        if(cart.collided){
          continue;
        }
        cart.tick(tracks, activeTracks);
        if(cart.collided){
          activeTracks[cart.pos.x][cart.pos.y] = tracks[cart.pos.x][cart.pos.y];
          carts.stream()
                  .filter(c -> c.pos.x == cart.pos.x && c.pos.y == cart.pos.y)
                  .forEach(c -> {c.collided = true;});
        }
      }
    }
    
    
    
    public Point getCollision(){
      return carts.stream().filter(cart -> cart.collided).findFirst().get().pos;
    }
    public String print(){
      StringBuilder stringBuilder = new StringBuilder();
      for(int y = 0; y < yCount; y++){
        for (int x = 0; x < xCount; x++){
          stringBuilder.append(activeTracks[x][y]);
        }
        stringBuilder.append("\n");
      }
      stringBuilder.append("\n");

      return stringBuilder.toString();

    }
    
    public List<Cart> getCartQueue(){
      return carts.stream()
              .filter(cart -> !cart.collided)
              .sorted(Comparator.comparing(cart ->
              (cart.pos.y * xCount) + cart.pos.x)).collect(Collectors.toList());
      
    }
  }

  public class Cart{
    public Point pos;
    public Point velocity;
    public boolean collided;
    public int intersectionsDecided;
    public Cart(Point pos, Point velocity) {
      this.pos = pos;
      this.velocity = velocity;
      this.collided = false;
      intersectionsDecided = 0;
    }
    
    public void tick(char[][] tracks, char[][] activeTracks){
      activeTracks[pos.x][pos.y] = tracks[pos.x][pos.y];
      this.pos.x += velocity.x;
      this.pos.y += velocity.y;
      char next = activeTracks[pos.x][pos.y];
      if(isCollision(next)){
        collided = true;
      }else{
        velocity = nextVelocity(next, velocity);
      }
      activeTracks[pos.x][pos.y] = getChar();
    }
    
    public char getChar(){
      if(collided){
        return 'x';
      }
      if(velocity.x == 0 && velocity.y == 1){
        return 'v';
      }
      if(velocity.x == 0 && velocity.y == -1){
        return '^';
      }
      if(velocity.x == 1 && velocity.y == 0){
        return '>';
      }
      if(velocity.x == -1 && velocity.y == 0){
        return '<';
      }
      return '?';
    }
    
    public boolean isCollision(char next){
      return next == '>' || next == '<' || next == 'v' || next == '^';
    }
    
    public Point nextVelocity(char next, Point currentVelocity){
      if(next == '|' || next == '-'){
        return currentVelocity;
      }
      if(next == '+'){
        return decideIntersection(currentVelocity);
      }
      if(next == '\\'){
        if(currentVelocity.y == 0){
          return currentVelocity.clockwise();
        } else {
          return currentVelocity.counterClockwise();
        }
      }

      if(next == '/'){
        if(currentVelocity.x == 0){
          return currentVelocity.clockwise();
        } else {
          return currentVelocity.counterClockwise();
        }
      }
      return currentVelocity;
    }
    
    public Point decideIntersection(Point currentVelocity){
      Point newV = new Point(currentVelocity.x, currentVelocity.y);
      if(intersectionsDecided % 3 == 0){
        newV = currentVelocity.counterClockwise();
      }

      if(intersectionsDecided % 3 == 2){
        newV = currentVelocity.clockwise();
      }
      intersectionsDecided++;
      return newV;
    }
  }
  public class Point{
    public int x;
    public int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
    public Point clockwise(){
      return new Point(y * -1, x);
    }
    public Point counterClockwise(){
      return new Point(y, x * -1);
    }
  }

  /**
   * --- Part Two ---
   * There isn't much you can do to prevent crashes in this ridiculous system. However, by 
   * predicting the crashes, the Elves know where to be in advance and instantly remove the two 
   * crashing carts the moment any crash occurs.
   *
   * They can proceed like this for a while, but eventually, they're going to run out of carts. 
   * It could be useful to figure out where the last cart that hasn't crashed will end up.
   *
   * For example:
   *
   * />-<\  
   * |   |  
   * | /<+-\
   * | | | v
   * \>+</ |
   *   |   ^
   *   \<->/
   *
   * /---\  
   * |   |  
   * | v-+-\
   * | | | |
   * \-+-/ |
   *   |   |
   *   ^---^
   *
   * /---\  
   * |   |  
   * | /-+-\
   * | v | |
   * \-+-/ |
   *   ^   ^
   *   \---/
   *
   * /---\  
   * |   |  
   * | /-+-\
   * | | | |
   * \-+-/ ^
   *   |   |
   *   \---/
   * After four very expensive crashes, a tick ends with only one cart remaining; its final 
   * location is 6,4.
   *
   * What is the location of the last cart at the end of the first tick where it is the only cart
   * left?
   */
  @Override
  public void solvePart2() {
    Mine mine = new Mine(getInput());
    print(mine.print());

    List<Cart> activeCarts;
    while((activeCarts = mine.getCartQueue()).size() > 1){
      mine.tickInstantRemoveCollisions();
//      print(mine.print());
    }
    Cart last = activeCarts.get(0);
    print("Last Cart: (" + last.pos.x + "," + last.pos.y + ")");

    return;
  }
}
