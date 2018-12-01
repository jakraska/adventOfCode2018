package utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class InputParser {


  public static List<String> getInputStrings(String resourceFilename) throws IOException {

    File f = new File(InputParser.class.getClassLoader().getResource(resourceFilename).getFile());

    return FileUtils.readLines(f, "UTF-8");
  }
}
