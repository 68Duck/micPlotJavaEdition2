import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) throws Exception {
    int NUMBEROFMICS = 3;
    Person person1 = new Person("Person 1");
    Person person2 = new Person("Person 2");
    List<List<Person>> peopleInScenes;

    Person[][] people = {{person1}, {person2, person1}};
    peopleInScenes = convertArrayToList(people);

    Solver solver = new Solver(NUMBEROFMICS, peopleInScenes);
    solver.solve();
    System.out.println(solver.getMicPlot());
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
