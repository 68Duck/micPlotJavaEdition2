import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Main {
  public static void main(String[] args) throws Exception {
    List<List<String>> peopleInScenesCsv = Csv.csv("csv/test2.csv");

    List<List<Person>> peopleInScenes = peopleInScenesCsv
            .stream().map(x -> x.stream().map(Person::new).collect(Collectors.toList())).toList();
    int NUMBEROFMICS = 20;
    int minMicsRequired = peopleInScenes.stream().map(List::size).reduce(Math::max).orElse(0);
    if (minMicsRequired > NUMBEROFMICS) {
      throw new Exception("There must be enough mics for the people in the biggest scene");
    };


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

    Map<Integer, Integer> mins = new HashMap<>();
    Map<Integer, Integer> maxs = new HashMap<>();
    int CALCULATEDMICS = 20;

    for (int i = minMicsRequired; i < minMicsRequired +CALCULATEDMICS; i++) {
      Solver solver = new Solver(i, peopleInScenes);
      solver.solve(true);
      mins.put(i, solver.getNumberOfChanges());
//      System.out.println("min" + i + "=" + solver.getNumberOfChanges());
//      Csv csv = new Csv();
//      csv.csvWriter(solver.getTransposedMicPLot(), "csv/" + i + "mics.csv");
    }
    for (int i = minMicsRequired; i < minMicsRequired +CALCULATEDMICS; i++) {
      Solver solver = new Solver(i, peopleInScenes);
      solver.solve(false);
      maxs.put(i, solver.getNumberOfChanges());
//      System.out.println("max" + i + "=" + solver.getNumberOfChanges());
//      Csv csv = new Csv();
//      csv.csvWriter(solver.getTransposedMicPLot(), "csv/" + i + "mics.csv");
    }

    for (int i = minMicsRequired; i < minMicsRequired + CALCULATEDMICS; i++) {
      System.out.println("mic " + i + " min:" + mins.get(i) + " max:" + maxs.get(i));
    }


    Solver solver = new Solver(NUMBEROFMICS, peopleInScenes);
    solver.solve(true);
    Csv csv = new Csv();
    csv.csvWriter(solver.getTransposedMicPLot(), "csv/test.csv");
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
