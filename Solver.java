import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solver {
  private int numberOfMics;
  private List<Scene> plot;
  private List<List<Person>> peopleInScenes;
  int numberOfScenes;
  public Solver(int numberOfMics, List<List<Person>> peopleInScenes) {
    this.numberOfMics = numberOfMics;
    this.peopleInScenes = peopleInScenes;
    this.numberOfScenes = peopleInScenes.size();
  }
  public void solve() throws Exception {
//    plot = new ArrayList<>();
//    Scene scene = new Scene(numberOfMics, 1);
//    scene.addPerson(new Person("Person 1", 1), 1);
//    scene.addPerson(new Person("Person 2", 2), 2);
//    plot.add(scene);
//    Scene scene2 = new Scene(numberOfMics, 2);
//    scene2.addPerson(new Person("Person 3", 1), 1);
//    scene2.addPerson(new Person("Person 4", 2), 2);
//    plot.add(scene2);

    plot = new ArrayList<>();
    for (int i = 0; i < numberOfScenes; i++) {
      Scene scene = new Scene(numberOfMics, i);
      plot.add(scene);
    }

    int sceneCounter = 0;
    for (int i = 0; i < numberOfScenes; i++){
      Scene currentScene = plot.get(sceneCounter);
      //TODO: Sort by priority
      List<Person> peopleToInsert = new ArrayList<>(peopleInScenes.get(sceneCounter));

      if (sceneCounter > 0) {
        Scene previous = plot.get(sceneCounter - 1);

        //Places every person who is in the previous scene into the current scene
        for (Person person: peopleInScenes.get(sceneCounter)) {
          if (previous.getIndex(person) != 0) {
            currentScene.setPerson(person, previous.getIndex(person));
            peopleToInsert.remove(person);
          }
        }

        //Places people into a mic if it is already empty
        for (int j = 1; j < numberOfMics + 1; j++){
          if (previous.getPerson(j) == null && peopleToInsert.size() > 0) {
            Person p = peopleToInsert.get(0);
            currentScene.setPerson(p, j);
            peopleToInsert.remove(p);

          }
        }
      } else {
        int peopleInsertedCounter = 0;
        int initialPeopleToInsertSize = peopleToInsert.size();
        for (int j = 1; j < numberOfMics + 1; j++) {
          if (peopleInsertedCounter < initialPeopleToInsertSize) {

            Person p = peopleToInsert.get(peopleInsertedCounter);
            currentScene.setPerson(p, j);
            peopleInsertedCounter += 1;
          }
        }
        peopleToInsert.subList(0, peopleInsertedCounter).clear();
      }
      sceneCounter += 1;
    }

  }

  public String getMicPlot() {
    return plot.stream().map(Object::toString).collect(Collectors.joining(" \n"));
  }
}
