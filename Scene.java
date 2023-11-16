import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scene {
  private Map<Integer, Person> people;
  private int numberOfMics;
  private int sceneNumber;

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

  @Override
  public String toString() {
    return "Scene " + sceneNumber + ": " + people.values().stream().map(Person::getName).collect(Collectors.joining(", "));
  }
}
