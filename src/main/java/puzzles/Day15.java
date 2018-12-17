package puzzles;

import java.util.*;
import java.util.stream.Collectors;

public class Day15 extends PuzzleDay {

  /**
   * --- Day 15: Beverage Bandits ---
   * Having perfected their hot chocolate, the Elves have a new problem: the Goblins that live in
   * these caves will do anything to steal it. Looks like they're here for a fight.
   *
   * You scan the area, generating a map of the walls (#), open cavern (.), and starting position
   * of every Goblin (G) and Elf (E) (your puzzle input).
   *
   * Combat proceeds in rounds; in each round, each unit that is still alive takes a turn, 
   * resolving all of its actions before the next unit's turn begins. On each unit's turn, it 
   * tries to move into range of an enemy (if it isn't already) and then attack (if it is in range).
   *
   * All units are very disciplined and always follow very strict combat rules. Units never move 
   * or attack diagonally, as doing so would be dishonorable. When multiple choices are equally 
   * valid, ties are broken in reading order: top-to-bottom, then left-to-right. For instance, 
   * the order in which units take their turns within a round is the reading order of their 
   * starting positions in that round, regardless of the type of unit or whether other units have
   * moved after the round started. For example:
   *
   *                  would take their
   * These units:   turns in this order:
   *   #######           #######
   *   #.G.E.#           #.1.2.#
   *   #E.G.E#           #3.4.5#
   *   #.G.E.#           #.6.7.#
   *   #######           #######
   * Each unit begins its turn by identifying all possible targets (enemy units). If no targets 
   * remain, combat ends.
   *
   * Then, the unit identifies all of the open squares (.) that are in range of each target; 
   * these are the squares which are adjacent (immediately up, down, left, or right) to any 
   * target and which aren't already occupied by a wall or another unit. Alternatively, the unit 
   * might already be in range of a target. If the unit is not already in range of a target, and 
   * there are no open squares which are in range of a target, the unit ends its turn.
   *
   * If the unit is already in range of a target, it does not move, but continues its turn with 
   * an attack. Otherwise, since it is not in range of a target, it moves.
   *
   * To move, the unit first considers the squares that are in range and determines which of 
   * those squares it could reach in the fewest steps. A step is a single movement to any 
   * adjacent (immediately up, down, left, or right) open (.) square. Units cannot move into 
   * walls or other units. The unit does this while considering the current positions of units 
   * and does not do any prediction about where units will be later. If the unit cannot reach 
   * (find an open path to) any of the squares that are in range, it ends its turn. If multiple 
   * squares are in range and tied for being reachable in the fewest steps, the step which is 
   * first in reading order is chosen. For example:
   *
   * Targets:      In range:     Reachable:    Nearest:      Chosen:
   * #######       #######       #######       #######       #######
   * #E..G.#       #E.?G?#       #E.@G.#       #E.!G.#       #E.+G.#
   * #...#.#  -->  #.?.#?#  -->  #.@.#.#  -->  #.!.#.#  -->  #...#.#
   * #.G.#G#       #?G?#G#       #@G@#G#       #!G.#G#       #.G.#G#
   * #######       #######       #######       #######       #######
   * In the above scenario, the Elf has three targets (the three Goblins):
   *
   * Each of the Goblins has open, adjacent squares which are in range (marked with a ? on the map).
   * Of those squares, four are reachable (marked @); the other two (on the right) would require 
   * moving through a wall or unit to reach.
   * Three of these reachable squares are nearest, requiring the fewest steps (only 2) to reach 
   * (marked !).
   * Of those, the square which is first in reading order is chosen (+).
   * The unit then takes a single step toward the chosen square along the shortest path to that 
   * square. If multiple steps would put the unit equally closer to its destination, the unit 
   * chooses the step which is first in reading order. (This requires knowing when there is more 
   * than one shortest path so that you can consider the first step of each such path.) For example:
   *
   * In range:     Nearest:      Chosen:       Distance:     Step:
   * #######       #######       #######       #######       #######
   * #.E...#       #.E...#       #.E...#       #4E212#       #..E..#
   * #...?.#  -->  #...!.#  -->  #...+.#  -->  #32101#  -->  #.....#
   * #..?G?#       #..!G.#       #...G.#       #432G2#       #...G.#
   * #######       #######       #######       #######       #######
   * The Elf sees three squares in range of a target (?), two of which are nearest (!), and so 
   * the first in reading order is chosen (+). Under "Distance", each open square is marked with 
   * its distance from the destination square; the two squares to which the Elf could move on 
   * this turn (down and to the right) are both equally good moves and would leave the Elf 2 
   * steps from being in range of the Goblin. Because the step which is first in reading order is
   * chosen, the Elf moves right one square.
   *
   * Here's a larger example of movement:
   *
   * Initially:
   * #########
   * #G..G..G#
   * #.......#
   * #.......#
   * #G..E..G#
   * #.......#
   * #.......#
   * #G..G..G#
   * #########
   *
   * After 1 round:
   * #########
   * #.G...G.#
   * #...G...#
   * #...E..G#
   * #.G.....#
   * #.......#
   * #G..G..G#
   * #.......#
   * #########
   *
   * After 2 rounds:
   * #########
   * #..G.G..#
   * #...G...#
   * #.G.E.G.#
   * #.......#
   * #G..G..G#
   * #.......#
   * #.......#
   * #########
   *
   * After 3 rounds:
   * #########
   * #.......#
   * #..GGG..#
   * #..GEG..#
   * #G..G...#
   * #......G#
   * #.......#
   * #.......#
   * #########
   * Once the Goblins and Elf reach the positions above, they all are either in range of a target
   * or cannot find any square in range of a target, and so none of the units can move until a 
   * unit dies.
   *
   * After moving (or if the unit began its turn in range of a target), the unit attacks.
   *
   * To attack, the unit first determines all of the targets that are in range of it by being 
   * immediately adjacent to it. If there are no such targets, the unit ends its turn. Otherwise,
   * the adjacent target with the fewest hit points is selected; in a tie, the adjacent target 
   * with the fewest hit points which is first in reading order is selected.
   *
   * The unit deals damage equal to its attack power to the selected target, reducing its hit 
   * points by that amount. If this reduces its hit points to 0 or fewer, the selected target 
   * dies: its square becomes . and it takes no further turns.
   *
   * Each unit, either Goblin or Elf, has 3 attack power and starts with 200 hit points.
   *
   * For example, suppose the only Elf is about to attack:
   *
   *        HP:            HP:
   * G....  9       G....  9  
   * ..G..  4       ..G..  4  
   * ..EG.  2  -->  ..E..     
   * ..G..  2       ..G..  2  
   * ...G.  1       ...G.  1  
   * The "HP" column shows the hit points of the Goblin to the left in the corresponding row. The
   * Elf is in range of three targets: the Goblin above it (with 4 hit points), the Goblin to its
   * right (with 2 hit points), and the Goblin below it (also with 2 hit points). Because three 
   * targets are in range, the ones with the lowest hit points are selected: the two Goblins with
   * 2 hit points each (one to the right of the Elf and one below the Elf). Of those, the Goblin 
   * first in reading order (the one to the right of the Elf) is selected. The selected Goblin's 
   * hit points (2) are reduced by the Elf's attack power (3), reducing its hit points to -1, 
   * killing it.
   *
   * After attacking, the unit's turn ends. Regardless of how the unit's turn ends, the next unit
   * in the round takes its turn. If all units have taken turns in this round, the round ends, 
   * and a new round begins.
   *
   * The Elves look quite outnumbered. You need to determine the outcome of the battle: the 
   * number of full rounds that were completed (not counting the round in which combat ends) 
   * multiplied by the sum of the hit points of all remaining units at the moment combat ends. 
   * (Combat only ends when a unit finds no targets during its turn.)
   *
   * Below is an entire sample combat. Next to each map, each row's units' hit points are listed 
   * from left to right.
   *
   * Initially:
   * #######   
   * #.G...#   G(200)
   * #...EG#   E(200), G(200)
   * #.#.#G#   G(200)
   * #..G#E#   G(200), E(200)
   * #.....#   
   * #######   
   *
   * After 1 round:
   * #######   
   * #..G..#   G(200)
   * #...EG#   E(197), G(197)
   * #.#G#G#   G(200), G(197)
   * #...#E#   E(197)
   * #.....#   
   * #######   
   *
   * After 2 rounds:
   * #######   
   * #...G.#   G(200)
   * #..GEG#   G(200), E(188), G(194)
   * #.#.#G#   G(194)
   * #...#E#   E(194)
   * #.....#   
   * #######   
   *
   * Combat ensues; eventually, the top Elf dies:
   *
   * After 23 rounds:
   * #######   
   * #...G.#   G(200)
   * #..G.G#   G(200), G(131)
   * #.#.#G#   G(131)
   * #...#E#   E(131)
   * #.....#   
   * #######   
   *
   * After 24 rounds:
   * #######   
   * #..G..#   G(200)
   * #...G.#   G(131)
   * #.#G#G#   G(200), G(128)
   * #...#E#   E(128)
   * #.....#   
   * #######   
   *
   * After 25 rounds:
   * #######   
   * #.G...#   G(200)
   * #..G..#   G(131)
   * #.#.#G#   G(125)
   * #..G#E#   G(200), E(125)
   * #.....#   
   * #######   
   *
   * After 26 rounds:
   * #######   
   * #G....#   G(200)
   * #.G...#   G(131)
   * #.#.#G#   G(122)
   * #...#E#   E(122)
   * #..G..#   G(200)
   * #######   
   *
   * After 27 rounds:
   * #######   
   * #G....#   G(200)
   * #.G...#   G(131)
   * #.#.#G#   G(119)
   * #...#E#   E(119)
   * #...G.#   G(200)
   * #######   
   *
   * After 28 rounds:
   * #######   
   * #G....#   G(200)
   * #.G...#   G(131)
   * #.#.#G#   G(116)
   * #...#E#   E(113)
   * #....G#   G(200)
   * #######   
   *
   * More combat ensues; eventually, the bottom Elf dies:
   *
   * After 47 rounds:
   * #######   
   * #G....#   G(200)
   * #.G...#   G(131)
   * #.#.#G#   G(59)
   * #...#.#   
   * #....G#   G(200)
   * #######   
   * Before the 48th round can finish, the top-left Goblin finds that there are no targets 
   * remaining, and so combat ends. So, the number of full rounds that were completed is 47, and 
   * the sum of the hit points of all remaining units is 200+131+59+200 = 590. From these, the 
   * outcome of the battle is 47 * 590 = 27730.
   *
   * Here are a few example summarized combats:
   *
   * #######       #######
   * #G..#E#       #...#E#   E(200)
   * #E#E.E#       #E#...#   E(197)
   * #G.##.#  -->  #.E##.#   E(185)
   * #...#E#       #E..#E#   E(200), E(200)
   * #...E.#       #.....#
   * #######       #######
   *
   * Combat ends after 37 full rounds
   * Elves win with 982 total hit points left
   * Outcome: 37 * 982 = 36334
   * #######       #######   
   * #E..EG#       #.E.E.#   E(164), E(197)
   * #.#G.E#       #.#E..#   E(200)
   * #E.##E#  -->  #E.##.#   E(98)
   * #G..#.#       #.E.#.#   E(200)
   * #..E#.#       #...#.#   
   * #######       #######   
   *
   * Combat ends after 46 full rounds
   * Elves win with 859 total hit points left
   * Outcome: 46 * 859 = 39514
   * #######       #######   
   * #E.G#.#       #G.G#.#   G(200), G(98)
   * #.#G..#       #.#G..#   G(200)
   * #G.#.G#  -->  #..#..#   
   * #G..#.#       #...#G#   G(95)
   * #...E.#       #...G.#   G(200)
   * #######       #######   
   *
   * Combat ends after 35 full rounds
   * Goblins win with 793 total hit points left
   * Outcome: 35 * 793 = 27755
   * #######       #######   
   * #.E...#       #.....#   
   * #.#..G#       #.#G..#   G(200)
   * #.###.#  -->  #.###.#   
   * #E#G#G#       #.#.#.#   
   * #...#G#       #G.G#G#   G(98), G(38), G(200)
   * #######       #######   
   *
   * Combat ends after 54 full rounds
   * Goblins win with 536 total hit points left
   * Outcome: 54 * 536 = 28944
   * #########       #########   
   * #G......#       #.G.....#   G(137)
   * #.E.#...#       #G.G#...#   G(200), G(200)
   * #..##..G#       #.G##...#   G(200)
   * #...##..#  -->  #...##..#   
   * #...#...#       #.G.#...#   G(200)
   * #.G...G.#       #.......#   
   * #.....G.#       #.......#   
   * #########       #########   
   *
   * Combat ends after 20 full rounds
   * Goblins win with 937 total hit points left
   * Outcome: 20 * 937 = 18740
   * What is the outcome of the combat described in your puzzle input?
   */

  @Override
  public void solvePart1() {
    Game game = new Game(getInput());
    int i = 0;
    print(game);

    while (game.processRound()){
      print(game);
      
    }
    print(game.round * game.remainingHealth());
    
//    print(game.navMesh);

//    List<NavPath> paths = game.navMesh.getShortestPaths(new Vector2(7, 11), new Vector2(9, 
//            9), game.units);
//    game.navMesh.fillUnits(game.units);
//    game.navMesh.fillCosts(new Vector2(8, 11), new Vector2(16, 11) );

//    print(game.navMesh);
    
//    for(NavPath path : paths){
//      print("---------");
//      print(path);
//    }

    print("Rounds: " + game.round);
    print("Health: " + game.remainingHealth());
    print("Outcome: " + (game.round * game.remainingHealth()));
    return;
  }

  public class Game {
    public List<List<GameObject>> board;
    public List<Unit> units;
    public NavMesh navMesh;
    public int width = 0;
    public int height = 0;
    public int round = 0;


    public Game(List<String> input) {
      //init our board
      height = input.size();
      width = input.get(0).length();
      navMesh = new NavMesh(width, height);
      board = new ArrayList<>();
      for (int x = 0; x < width; x++) {
        List<GameObject> col = new ArrayList<>();
        board.add(col);
        for (int y = 0; y < height; y++) {
          col.add(null);
        }
      }
      units = new ArrayList<>();

      for (int y = 0; y < input.size(); y++) {
        char[] row = input.get(y).toCharArray();
        for (int x = 0; x < row.length; x++) {
          switch (row[x]) {
            case '#':
              board.get(x).set(y, new Obstacle(new Vector2(x, y)));
              break;
            case '.':
              navMesh.addNode(x, y);
              break;
            case 'G':
              Unit unit = new Unit(new Vector2(x, y), Team.goblin);
              units.add(unit);
              board.get(x).set(y, unit);
              navMesh.addNode(x, y);
              break;
            case 'E':
              Unit unitE = new Unit(new Vector2(x, y), Team.elf);
              units.add(unitE);
              board.get(x).set(y, unitE);

              navMesh.addNode(x, y);
              break;

          }
        }
      }
    }

    @Override
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Round " + round + ":\n");
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          GameObject gameObject = board.get(x).get(y);
          stringBuilder.append(gameObject == null ? '.' : gameObject.toString());
        }
        stringBuilder.append("\n");
      }
      return stringBuilder.toString();
    }
    
    public int remainingHealth(){
      return units.stream().filter(unit -> unit.alive).mapToInt(unit -> unit.health).sum();
    }

    public boolean processRound() {
      
      for(Unit unit : getUnitQueue()){
        if(!unit.alive){
          continue;
        }
        List<Unit> enemies = getEnemies(unit);
        //check if we are done
        if(enemies.isEmpty()){
          return false; //end game
        }
        
        //see if we have something in range already
        Unit attackTarget = unit.getAttackUnitInRange(this);
        if(attackTarget != null){
          if(!attackTarget.takeDamage(unit.attackPower)){
            removeUnit(attackTarget);
          }
          continue;
        }
        
        //move to target if we can
        Unit nearestEnemy = enemies.stream()
                .min(Comparator.comparing(eu -> navMesh.getDistance(unit.pos, eu.pos, units), 
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .orElse(null);
        

        if(nearestEnemy != null){
          Vector2 moveTo =  navMesh.getNextMove(unit.pos, nearestEnemy.pos, units);
          if(moveTo != null) {
            moveUnit(unit, moveTo);
          }
        }

        //attack target if now in range
         attackTarget = unit.getAttackUnitInRange(this);
        if(attackTarget != null){
          if(!attackTarget.takeDamage(unit.attackPower)){
            removeUnit(attackTarget);
          }
          
        }
        
        
        
      }
      round++;

      return true;
    }

    public List<Unit> getEnemies(Unit unit){
      return units.stream().filter(u -> unit.isEnemy(u)).collect(Collectors.toList());
    }
    
    public void removeUnit(Unit unit){
      units.remove(unit);
      board.get(unit.pos.x).set(unit.pos.y, null);
    }
    public void moveUnit(Unit unit, Vector2 newPosition){
      board.get(unit.pos.x).set(unit.pos.y, null);
      unit.pos = newPosition;
      board.get(newPosition.x).set(newPosition.y, unit);
    }

    public List<Unit> getUnitQueue() {
      return units.stream()
              .filter(unit -> unit.alive)
              .sorted(Comparator.comparing(unit -> (unit.pos.y * height) + unit.pos.x))
              .collect(Collectors.toCollection(LinkedList::new));

    }
    
    public GameObject get(int x, int y){
      if(x < 0 || x >= width || y < 0 || y >= height){
        return null;
      }
      return board.get(x).get(y);
    }
  }
    
    
    
    
  public abstract class GameObject{
    public Vector2 pos;
  }
  
  public class Obstacle extends GameObject{
    public Obstacle(Vector2 pos) {
      this.pos = pos;
    }

    @Override
    public String toString() {
      return "#";
    }
  }
  public class Unit extends GameObject{
    public int health = 200;
    public int attackPower = 3;
    public Team team;
    public boolean alive = true;

    public Unit(Vector2 pos, Team team) {
      this.pos = pos;
      this.team = team;
    }
    
    public int attackWeight(int boardWidth, int boardHeight){
      int weight = health + (boardWidth * boardHeight);
      weight+= pos.y * boardWidth;
      weight+= pos.x;
      return weight;
    }

    @Override
    public String toString() {
      return team == Team.goblin ? "G" : "E";
    }
    public boolean isEnemy(Unit unit){
      return unit.team != team;
    }
    
    public Unit getAttackUnitInRange(Game game){
      List<GameObject> gameObjects = new ArrayList<>(4);
      gameObjects.add(game.get(pos.x, pos.y - 1));
      gameObjects.add(game.get(pos.x, pos.y + 1));
      gameObjects.add(game.get(pos.x + 1, pos.y));
      gameObjects.add(game.get(pos.x - 1, pos.y));
      
      return (Unit)gameObjects.stream()
              .filter(gameObject -> gameObject != null && gameObject.getClass() ==
                      Unit.class && isEnemy((Unit) gameObject) && ((Unit) gameObject).alive)
                      .min(Comparator.comparing(gameObject -> ((Unit) gameObject).attackWeight
                              (game.width, game.height)))
                      .orElse(null);
      
    }
    public boolean takeDamage(int damage){
      health -= damage;
      if(health <= 0){
        alive = false;
      }
      return alive;
    }
  }
  public enum Team{
    elf, goblin
  }
  public class Vector2 {
    public int x;
    public int y;

    public Vector2(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
  public class NavMesh {
    public List<List<NavNode>> grid;
    public List<NavNode> nodes;
    
    public int width;
    public int height;
    
    
    public NavMesh(int width, int height) {
      this.width = width;
      this.height = height;
      this.grid = new ArrayList<>(width);
      this.nodes = new ArrayList<>();
      for(int x = 0; x < width; x++){
        List<NavNode> row = new ArrayList<>(height);
        grid.add(row);
        for(int y = 0; y < height; y++){
          row.add(null);
        }
      }
    }
    
    public void addNode(int x, int y){
      NavNode navNode = new NavNode(new Vector2(x, y));
      if(y > 0) {
        NavNode other = grid.get(x).get(y - 1);
        if(other != null) {
          navNode.neighbors.add(other);
          other.neighbors.add(navNode);
        }
      }
      if(y < grid.get(x).size() - 1) {
        NavNode other = grid.get(x).get(y + 1);
        if(other != null) {
          navNode.neighbors.add(other);
          other.neighbors.add(navNode);
        }
      }
      if(x > 0) {
        NavNode other = grid.get(x - 1).get(y);
        if(other != null) {
          navNode.neighbors.add(other);
          other.neighbors.add(navNode);
        }
      }
      if( x < grid.size() - 1){
        NavNode other = grid.get(x + 1).get(y);
        if(other != null) {
          navNode.neighbors.add(other);
          other.neighbors.add(navNode);
        }
      }
      
      
      
      grid.get(x).set(y, navNode);
      nodes.add(navNode);
      
    }
    
    public Vector2 getNextMove(Vector2 start, Vector2 end, List<Unit> units){
      List<NavPath> paths = getShortestPaths(start, end, units);
      if(paths.size() == 0){
        return null;
      }
      List<Vector2> moveOptions = new ArrayList<>();
      for(NavPath navPath : paths){
        if(navPath.firstMove() != null){
          moveOptions.add( navPath.firstMove());
        }
      }
      
      return moveOptions.stream().min(Comparator.comparing(pos -> (pos.y * height) + pos.x))
              .orElse(null);
      
    }
    
    private Integer getDistance(Vector2 start, Vector2 end, List<Unit> units){
      reset();
      fillUnits(units);
      fillCosts(start, end);
      NavNode endNode = grid.get(end.x).get(end.y);
      return endNode == null ? null : endNode.cost;
    }
    
    private List<NavPath> getShortestPaths(Vector2 start, Vector2 end, List<Unit> units){
      reset();
      fillUnits(units);
      fillCosts(start, end);
      NavNode endNode = grid.get(end.x).get(end.y);
      List<NavPath> paths = getShortestPaths(endNode, new NavPath());

      return paths;
    }
    private List<NavPath> getShortestPaths(NavNode node, NavPath navPath){
      List<NavPath> result = new ArrayList<>();
      if(node.cost == null){
        return  result;
      }

      NavPath newPath = new NavPath(navPath);
      newPath.addNode(node);
      if(node.cost == 0){
        result.add(newPath);
        return result;
      }
      
      node.neighbors.stream()
              .filter(n -> n.cost != null && n.cost == node.cost - 1)
              .forEach(n -> {
                List<NavPath> p = getShortestPaths(n, newPath);
                result.addAll(p);
              });
      return  result;
    }
    
    private void fillUnits(List<Unit> units){
      for(Unit unit : units){
        grid.get(unit.pos.x).get(unit.pos.y).blocked = true;
      }
    }
    
    public void fillCosts(Vector2 start, Vector2 end){
      NavNode node = getNode(start);
      Set<NavNode> pathNodes = new HashSet<>();
      
      if(node == null){
        return;
      }
      fillCost(node, end, pathNodes);
    }
    
    private void fillCost(NavNode node, Vector2 end, Set<NavNode> pathNodes){
      int cost = pathNodes.size();
      boolean isEnd = node.pos.x == end.x && node.pos.y == end.y;
      if(node.cost != null && node.cost <= cost){
        return; //we have already found a shorter path to this node
      }
      if(node.blocked && (pathNodes.size() != 0 &&  !isEnd) ){
        return; // cant move here, ignoring start and end
      }
      if(pathNodes.contains(node)){
        return; //we have already been to this node
      }
      node.cost = cost;
      
      if(isEnd){
        return; //we are at the end
      }

      //update our path to include this node
      Set<NavNode> newPath = new HashSet<>(pathNodes);
      newPath.add(node);
      //recurse to neighbors
      node.neighbors.forEach(n -> {fillCost(n, end, newPath);});
    }
    
    private NavNode getNode(int x, int y){
      return grid.get(x).get(y);
    }
    private NavNode getNode(Vector2 pos){
      return grid.get(pos.x).get(pos.y);
    }
    public void reset(){
      nodes.forEach(navNode -> {navNode.cost = null; navNode.blocked = false;});
    }


    @Override
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      for(int y = 0; y < height; y++){
        for(int x = 0; x < width; x++){
          NavNode navNode = grid.get(x).get(y);
          stringBuilder.append(navNode == null ? ' ' : navNode.toString());
        }
        stringBuilder.append("\n");
      }
      return stringBuilder.toString();
    }
  }
  public class NavNode {
    public Vector2 pos;
    
    public List<NavNode> neighbors;
    public Integer cost = null;
    public boolean blocked = false;

    public NavNode(Vector2 pos) {
      this.pos = pos;
      neighbors = new ArrayList<>();
    }

    @Override
    public String toString() {
      if(cost == null){
        return ".";
      }
      if(cost > 9){
        return "?";
      }
      return cost.toString();
    }
  }
  
  public class NavPath {
    public Vector2 start;
    public Vector2 end;
    public List<NavNode> path;

    public NavPath() {
      this.path = new ArrayList<>();
    }
    
    public NavPath(NavPath other){
      this.path = new ArrayList<>(other.path);
    }
    public void addNode(NavNode node){
      this.path.add(node);
    }
    
    public Vector2 firstMove(){
      if(path.size() < 3){
        return null;
      }
      return path.get(path.size()-2).pos;
    }

    @Override
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      for(NavNode node : path){
        stringBuilder.append("(" +node.pos.x + "," + node.pos.y + ")\n");
      }
      return stringBuilder.toString();
    }
  }
  @Override
  public void solvePart2() {

  }
}
