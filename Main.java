import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) throws Exception {
//    Csv csv = new Csv();
//    csv.csvWriter();
    List<List<String>> peopleInScenesCsv = Csv.csv("csv/sor.csv");
    List<List<Person>> peopleInScenes = peopleInScenesCsv
            .stream().map(x -> x.stream().map(Person::new).collect(Collectors.toList())).toList();
    int NUMBEROFMICS = 18;
//    List<List<Person>> peopleInScenes2;
//    Person a = new Person("A");
//    Person b = new Person("B");
//    Person c = new Person("C");
//    Person d = new Person("D");
//    Person e = new Person("E");
//    Person f = new Person("F");
//    Person g = new Person("G");
//    Person h = new Person("H");
//
//
//    Person[][] people =
//        {
//            {a, b, c},
//            {a, b, d, e},
//            {c, f},
//            {a, d, g, h},
//            {a, b, c}
//        };
//    System.out.println(peopleInScenes);
//    peopleInScenes2 = convertArrayToList(people);
//    System.out.println(peopleInScenes2);
//    System.out.println(peopleInScenes == peopleInScenes2);
//    System.out.println(Objects.equals(peopleInScenes.get(1).get(0).getName(), "A"));
//    System.out.println(Objects.equals(peopleInScenes.get(0).get(0).getName(), "A"));
//    String FIRSTELEMENT = "a";
//    peopleInScenes.get(0).set(0, new Person(FIRSTELEMENT));


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


}
