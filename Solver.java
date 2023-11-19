import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.min;

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

        //Places people into a mic if it has always been empty
        for (int j = 1; j < numberOfMics + 1; j++){
          boolean previouslyNull = false;
          for (int k = 0; k < sceneCounter; k++) {
            if (plot.get(k).getPerson((j)) != null){
              previouslyNull = true;
            }
          }
          if (!previouslyNull && peopleToInsert.size() > 0) {
            Person p = peopleToInsert.get(0);
            currentScene.setPerson(p, j);
            peopleToInsert.remove(p);
          }
        }

        currentScene.setPreviousNameDistances(peopleInScenes, sceneCounter);
//        System.out.println(currentScene.getPreviousNameDistances());
//        System.out.println(currentScene.getPreviousNameDistances());

//        System.out.println(currentScene.previousNamesInMic(plot, sceneCounter));

        for (int j = 0; j < currentScene.getNumberOfFreeSpaces(); j++) {
          //TODO: find maximum in get previous distances list, that is also in previous Names in mic,
          //TODO: and is not in that scene already.
          Map<Integer, Person> previousNamesInMic = currentScene.previousNamesInMic(plot, sceneCounter);
          currentScene.setPreviousNameDistances(peopleInScenes, sceneCounter);
          System.out.println(previousNamesInMic);
          System.out.println(currentScene.getPreviousNameDistances());
          int maxDistance = previousNamesInMic.values().stream().map(x -> currentScene.getPreviousNameDistances().get(x)).reduce(Math::max).orElse(0);
          System.out.println(maxDistance);
          if (maxDistance > 1) { //So not already in that scene
              Person p = previousNamesInMic.values().stream().filter(x -> currentScene.getPreviousNameDistances().get(x) == maxDistance).toList().get(0);
            //P is the first person in the list so the person to be input
              System.out.println("Inserting:");
              System.out.println(p.getName());
              currentScene.setPerson(p, previousNamesInMic.entrySet().stream()
                  .filter(entry -> p.equals(entry.getValue()))
                  .findFirst().map(Map.Entry::getKey)
                  .orElse(null));
          }
        }

        currentScene.setPooledPeople(peopleToInsert);



        //TODO: If no mics are pooled, then ???

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

  public String getMicPlotString() {
    return plot.stream().map(Object::toString).collect(Collectors.joining(" \n"));
  }
}
