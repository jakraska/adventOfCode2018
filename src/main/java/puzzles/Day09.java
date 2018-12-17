package puzzles;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;

public class Day09 extends PuzzleDay {

  /**
   * --- Day 9: Marble Mania ---
   * You talk to the Elves while you wait for your navigation system to initialize. To pass the
   * time, they introduce you to their favorite marble game.
   *
   * The Elves play this game by taking turns arranging the marbles in a circle according to very
   * particular rules. The marbles are numbered starting with 0 and increasing by 1 until every
   * marble has a number.
   *
   * First, the marble numbered 0 is placed in the circle. At this point, while it contains only
   * a single marble, it is still a circle: the marble is both clockwise from itself and
   * counter-clockwise from itself. This marble is designated the current marble.
   *
   * Then, each Elf takes a turn placing the lowest-numbered remaining marble into the circle
   * between the marbles that are 1 and 2 marbles clockwise of the current marble. (When the
   * circle is large enough, this means that there is one marble between the marble that was just
   * placed and the current marble.) The marble that was just placed then becomes the current
   * marble.
   *
   * However, if the marble that is about to be placed has a number which is a multiple of 23,
   * something entirely different happens. First, the current player keeps the marble they would
   * have placed, adding it to their score. In addition, the marble 7 marbles counter-clockwise
   * from the current marble is removed from the circle and also added to the current player's
   * score. The marble located immediately clockwise of the marble that was removed becomes the
   * new current marble.
   *
   * For example, suppose there are 9 players. After the marble with value 0 is placed in the
   * middle, each player (shown in square brackets) takes a turn. The result of each of those
   * turns would produce circles of marbles like this, where clockwise is to the right and the
   * resulting current marble is in parentheses:
   *
   * [-] (0)
   * [1]  0 (1)
   * [2]  0 (2) 1
   * [3]  0  2  1 (3)
   * [4]  0 (4) 2  1  3
   * [5]  0  4  2 (5) 1  3
   * [6]  0  4  2  5  1 (6) 3
   * [7]  0  4  2  5  1  6  3 (7)
   * [8]  0 (8) 4  2  5  1  6  3  7
   * [9]  0  8  4 (9) 2  5  1  6  3  7
   * [1]  0  8  4  9  2(10) 5  1  6  3  7
   * [2]  0  8  4  9  2 10  5(11) 1  6  3  7
   * [3]  0  8  4  9  2 10  5 11  1(12) 6  3  7
   * [4]  0  8  4  9  2 10  5 11  1 12  6(13) 3  7
   * [5]  0  8  4  9  2 10  5 11  1 12  6 13  3(14) 7
   * [6]  0  8  4  9  2 10  5 11  1 12  6 13  3 14  7(15)
   * [7]  0(16) 8  4  9  2 10  5 11  1 12  6 13  3 14  7 15
   * [8]  0 16  8(17) 4  9  2 10  5 11  1 12  6 13  3 14  7 15
   * [9]  0 16  8 17  4(18) 9  2 10  5 11  1 12  6 13  3 14  7 15
   * [1]  0 16  8 17  4 18  9(19) 2 10  5 11  1 12  6 13  3 14  7 15
   * [2]  0 16  8 17  4 18  9 19  2(20)10  5 11  1 12  6 13  3 14  7 15
   * [3]  0 16  8 17  4 18  9 19  2 20 10(21) 5 11  1 12  6 13  3 14  7 15
   * [4]  0 16  8 17  4 18  9 19  2 20 10 21  5(22)11  1 12  6 13  3 14  7 15
   * [5]  0 16  8 17  4 18(19) 2 20 10 21  5 22 11  1 12  6 13  3 14  7 15
   * [6]  0 16  8 17  4 18 19  2(24)20 10 21  5 22 11  1 12  6 13  3 14  7 15
   * [7]  0 16  8 17  4 18 19  2 24 20(25)10 21  5 22 11  1 12  6 13  3 14  7 15
   * The goal is to be the player with the highest score after the last marble is used up.
   * Assuming the example above ends after the marble numbered 25, the winning score is 23+9=32
   * (because player 5 kept marble 23 and removed marble 9, while no other player got any points
   * in this very short example game).
   *
   * Here are a few more examples:
   *
   * 10 players; last marble is worth 1618 points: high score is 8317
   * 13 players; last marble is worth 7999 points: high score is 146373
   * 17 players; last marble is worth 1104 points: high score is 2764
   * 21 players; last marble is worth 6111 points: high score is 54718
   * 30 players; last marble is worth 5807 points: high score is 37305
   * What is the winning Elf's score?
   */
  @Override
  public void solvePart1() {
    
    Game game = new Game();
    List<Integer> players = getIntegerList(getInputPlayers(), 0);
    int maxMarbleValue = getInputLastMarble();
    int currPlayer = 0;
    
    for(int marbleIndex = 1; marbleIndex <= maxMarbleValue; marbleIndex++){

      int score = game.addMarble(marbleIndex);
      players.set(currPlayer, players.get(currPlayer) + score);
      currPlayer = (currPlayer +1) % players.size();
    }
    
    print(Collections.max(players));
    return;
  }
  
  public Integer getInputPlayers(){
    Matcher matcher = getRegexInput("(\\d*) players; last marble is worth (\\d*) points")
            .get(0);
    matcher.find();
    return Integer.parseInt(matcher.group(1));
  }
  public Integer getInputLastMarble(){
    Matcher matcher = getRegexInput("(\\d*) players; last marble is worth (\\d*) points")
            .get(0);
    matcher.find();
    return Integer.parseInt(matcher.group(2));
  }

  public class Game{
    LinkedList<Integer> circle = new LinkedList<>();
    ListIterator<Integer> listIterator;
    

    public Game() {
      circle.add(0);
      listIterator = circle.listIterator(0);
    }
    
    public int currentIndex(){
      return listIterator.nextIndex() - 1;
    }

    public Integer addMarble(int marbleId){

      if(marbleId % 23 == 0){
        Integer score = marbleId;
        safePrevious();
        safePrevious();
        safePrevious();
        safePrevious();
        safePrevious();
        safePrevious();
        safePrevious();
        
        //take another one away due to the the linked list nodes work
        int removedVal = safePrevious();
        score += removedVal;
        listIterator.remove();
        safeNext();
        
        return score;
      }else {
          safeNext();
          listIterator.add(marbleId);
        return 0;
      }
    }
    
    public Integer safeNext(){
      if(!listIterator.hasNext()){
        listIterator = circle.listIterator(0);
      }
      return listIterator.next();
    }
    public Integer safePrevious(){
      if(!listIterator.hasPrevious()){
        listIterator = circle.listIterator(circle.size() -1);
        listIterator.next();
      }
      return listIterator.previous();
    }



    public String print(){
      StringBuilder sb = new StringBuilder();
      int currIndex = currentIndex();
      for(int i = 0; i < circle.size(); i++){
        if(i == currIndex){

        }
        sb.append((i == currIndex)? "(" : " ");
        sb.append(circle.get(i));
        sb.append((i == currIndex)? ")" : " ");

      }
      return sb.toString();
    }


  }

  /**
   * --- Part Two ---
   * Amused by the speed of your answer, the Elves are curious:
   *
   * What would the new winning Elf's score be if the number of the last marble were 100 times 
   * larger?
   */
  @Override
  public void solvePart2() {
    Game game = new Game();
    List<BigInteger> players = new ArrayList<>();
    for(int i = 0; i < getInputPlayers(); i++){
      players.add(BigInteger.valueOf(0));
    }
    int maxMarbleValue = getInputLastMarble() * 100;
    int currPlayer = 0;

    for(int marbleIndex = 1; marbleIndex <= maxMarbleValue; marbleIndex++){
      int score = game.addMarble(marbleIndex);
      if(score != 0) {
        players.set(currPlayer, players.get(currPlayer).add(BigInteger.valueOf(score)));
        
      }
      currPlayer = (currPlayer + 1) % players.size();

    }
    
    BigInteger max = players.get(0);
    for(BigInteger score : players){
      max = score.max(max);
    }
    print(max);

    return;
  }
}
