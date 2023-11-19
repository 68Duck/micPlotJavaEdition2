import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Csv {

  public void csvWriter() throws IOException {
    givenDataArray_whenConvertToCSV_thenOutputCreated();
  }

  public void givenDataArray_whenConvertToCSV_thenOutputCreated() throws IOException {
    List<String[]> dataLines = new ArrayList<>();
    dataLines.add(new String[]
            {"John", "Doe", "38", "Comment Data\nAnother line of comment data"});
    dataLines.add(new String[]
            {"Jane", "Doe, Jr.", "19", "She said \"I'm being quoted\""});
    File csvOutputFile = new File("csv/example.csv");
    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
      dataLines.stream()
              .map(this::convertToCSV)
              .forEach(pw::println);
    }
  }

  public String convertToCSV(String[] data) {
    return Stream.of(data)
            .map(this::escapeSpecialCharacters)
            .collect(Collectors.joining(","));
  }

  public String escapeSpecialCharacters(String data) {
    String escapedData = data.replaceAll("\\R", " ");
    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
      data = data.replace("\"", "\"\"");
      escapedData = "\"" + data + "\"";
    }
    return escapedData;
  }

  public static List<List<String>> csv(String fileName) {
    List<List<String>> records = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        records.add(Arrays.asList(values));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


    List<List<String>> transposed = new ArrayList<>();
    for (int i = 0; i < records.get(0).size(); i++) {
      transposed.add(new ArrayList<>());
    }
    for (int i = 0; i < records.get(0).size(); i++) {
      for (int j = 1; j < records.size(); j++) {
        if (i >= records.get(j).size()) {
        } else {
          if (Objects.equals(records.get(j).get(i), "")) {
          } else {
            transposed.get(i).add(records.get(j).get(i));
          }
        }
      }
    }
    System.out.println(transposed);
    return transposed;
  }
}