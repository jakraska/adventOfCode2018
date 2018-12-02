import org.apache.commons.cli.*;
import puzzles.PuzzleDay;

import java.lang.reflect.InvocationTargetException;

public class application {


  public static void main(String[] args) {
    Options options = new Options();
    options.addRequiredOption("d", "day", true, "day of puzzle to run");
    options.addRequiredOption("p", "part", true, "part of puzzle to run");

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;
    try {
      cmd = parser.parse( options, args);
    } catch (ParseException e) {
      System.err.println( "Parsing options: " + e.getMessage() );
      System.exit(-1);
      return;
    }

    try {
      runPuzzle(Integer.parseInt(cmd.getOptionValue("d")), Integer.parseInt(cmd.getOptionValue("p")));
    } catch (Exception e) {
      System.err.println( "Error loading puzzle: " + e.getMessage() );
      System.exit(-1);
    }

  }

  private static void runPuzzle(Integer day, Integer part) throws ClassNotFoundException,
          NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    Class<?> puzzleClass = Class.forName("puzzles.Day" + String.format("%02d", day));


    PuzzleDay puzzleDay = (PuzzleDay) puzzleClass.getConstructor().newInstance();
    try {
      switch (part) {
        case 1:
          puzzleDay.solvePart1();
          break;
        case 2:
          puzzleDay.solvePart2();
          break;
        default:
          System.err.println("Invalid part option: " + part);
          System.exit(-1);
      }
    } catch (Exception e){
      System.err.println("Exception thrown in puzzle");
      e.printStackTrace();
    }
  }
}
