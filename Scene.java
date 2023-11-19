import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scene {
  private Map<Integer, Person> people;
  private int numberOfMics;
  private int sceneNumber;
  private List<Person> pooledPeople = new ArrayList<>();
  private Map<Person, Integer> previousNameDistances = new HashMap<>();

  public Scene(int numberOfMics, int sceneNumber){
    this.sceneNumber = sceneNumber;
    this.numberOfMics = numberOfMics;
    this.people = new HashMap<>();
  }

  public void setPerson(Person p, int micNumber) throws Exception {
    if (micNumber <= 0 || micNumber > numberOfMics) {
      throw new Exception("Tried to add a mic that does not exist");
    }
    this.people.put(micNumber, p);
  }

  public Person getPerson(int i) {
    return this.people.get(i);
  }

  //Returns the index of a person if they are in the scene, otherwise zero
  public int getIndex(Person p){
    return people.entrySet().stream()
        .filter(entry -> p.equals(entry.getValue()))
        .findFirst().map(Map.Entry::getKey)
        .orElse(0);

  }

  public void setPooledPeople(List<Person> pooledPeople) {
    this.pooledPeople = pooledPeople;
  }

  public void removePooledPerson(Person p) {
    this.pooledPeople.remove(p);
  }

  public List<Person> getPooledPeople() {
    return pooledPeople;
  }

  public int getNumberOfPooledPeople() {
    return pooledPeople.size();
  }

  public List<Integer> getGapLocations() {
    List<Integer> gapLocations = new ArrayList<>();
    for (int i = 1; i < numberOfMics + 1; i++) {
      if (!people.containsKey(i)){
        gapLocations.add(i);
      }
    }
    return gapLocations;
  }

  public int getNumberOfFreeSpaces() {
    return numberOfMics - people.size() - getNumberOfPooledPeople();
  }

  public Map<Integer, Person> previousNamesInMic(List<Scene> plot, int sceneNumber) {
    Map<Integer, Person> previousNameInMic = new HashMap<>();
    for (int i = sceneNumber - 1; i >= 0; i--) {
      for (int j = 1; j < numberOfMics + 1; j++) {
        if (!previousNameInMic.containsKey(j)){
          if (plot.get(i).getPerson(j) != null) {
            previousNameInMic.put(j, plot.get(i).getPerson(j));
          }
        }
      }
    }
    return previousNameInMic;
  }

  public Map<Person, Integer> setPreviousNameDistances(List<List<Person>> peopleInScenes, int sceneNumber) {
    previousNameDistances = new HashMap<>();
    for (int i = sceneNumber - 1; i >= 0; i--) {
      for (Person person: peopleInScenes.get(i)) {
        if (!previousNameDistances.containsKey(person)) {
          previousNameDistances.put(person, sceneNumber - i);
        }
      }
    }
    return previousNameDistances;
  }

  public Map<Person, Integer> getPreviousNameDistances() {
    return previousNameDistances;
  }

  public int getNumberOfChanges(Scene scene) {
    int changes = 0;
    for (int i = 1; i < numberOfMics + 1; i++) {
      if (scene.getPerson(i) != this.getPerson(i)) {
        if (scene.getPerson(i) != null){
          if (this.getPerson(i) != null) {
            changes += 1;
          }
        }
      }
    }
    return changes;
  }

  @Override
  public String toString() {
    String str = "Scene " + sceneNumber + ": ";
    List<String> names = new ArrayList<>();
    for (int i = 1; i < numberOfMics + 1; i++) {
      if (people.containsKey(i)) {
        names.add(people.get(i).getName());
      } else {
        names.add("_");
      }
    }
    return str + String.join(", ", names);
  }
}
