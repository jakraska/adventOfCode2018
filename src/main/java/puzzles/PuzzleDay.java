package puzzles;

import utils.InputParser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class PuzzleDay {

  protected String inputFileName;


  public PuzzleDay(String inputFileName) {
    this.inputFileName = inputFileName;
  }

  public PuzzleDay() {
    this.inputFileName = this.getClass().getSimpleName().toLowerCase() + ".txt";
  }

  public abstract void solvePart1();
  public abstract void solvePart2();

  protected List<String> getInput(){
    try {
      return InputParser.getInputStrings(inputFileName);
    } catch (IOException e) {
      return new LinkedList<>(); // not really useful.
    }
  }

  protected List<Matcher> getRegexInput(String patternStr){
    Pattern pattern = Pattern.compile(patternStr);
    return getInput().stream().map(pattern::matcher).collect(Collectors.toList());
  }

  protected void print(Object obj){
    System.out.println(obj);
  }
}
