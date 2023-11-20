import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) throws Exception {
    List<List<String>> peopleInScenesCsv = Csv.csv("csv/shrek.csv");
    List<List<Person>> peopleInScenes = peopleInScenesCsv
            .stream().map(x -> x.stream().map(Person::new).collect(Collectors.toList())).toList();
    int NUMBEROFMICS = 20;
    //TODO: Add a check to see if number of mics is big enough

    List<Person> listOfALlPeople = new ArrayList<>();

    for (List<Person> ps : peopleInScenes) {
      for (Person p : ps) {
        if (!listOfALlPeople.contains(p)) {
          listOfALlPeople.add(p);
        }
      }
    }
    System.out.println(listOfALlPeople);
    System.out.println(listOfALlPeople.size());

    Solver solver = new Solver(NUMBEROFMICS, peopleInScenes);
    solver.solve();
    System.out.println(solver.getNumberOfChanges());
    System.out.println(solver.getMicPlotString());

    Csv csv = new Csv();
    csv.csvWriter(solver.getMicPlotListOfArrays());
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
