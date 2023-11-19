import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
//    csv();
    int NUMBEROFMICS = 5;
    Person a = new Person("A");
    Person b = new Person("B");
    Person c = new Person("C");
    Person d = new Person("D");
    Person e = new Person("E");
    Person f = new Person("F");
    Person g = new Person("G");
    Person h = new Person("H");
    List<List<Person>> peopleInScenes;

    Person[][] people =
        {
            {a, b, c},
            {a, b, d, e},
            {c, f},
            {a, d, g, h},
            {a, b, c}
        };
    peopleInScenes = convertArrayToList(people);

    Solver solver = new Solver(NUMBEROFMICS, peopleInScenes);
    solver.solve();
    System.out.println(solver.getNumberOfChanges());
    System.out.println(solver.getMicPlotString());
  }

  private static List<List<Person>> convertArrayToList(Person[][] people) {
    List<List<Person>> list = new ArrayList<>();
    for (int i = 0; i < people.length; i++) {
      List<Person> currentList = new ArrayList<>(Arrays.asList(people[i]));
      list.add(currentList);
    }
    return list;
  }

  private static void csv() throws FileNotFoundException {
    File getCSVFiles = new File("/csv/example.csv");
    Scanner sc = new Scanner(getCSVFiles);
    sc.useDelimiter(",");
    while (sc.hasNext())
    {
      System.out.print(sc.next() + " | ");
    }
    sc.close();
  }


}
