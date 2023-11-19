import java.util.Objects;

public class Person {
  private String name;
  private int weight;

  public Person(String name, int weight) {
    this.name = name;
    this.weight = weight;
  }

  public Person(String name) {
    this.name = name;
    this.weight = 0;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (!Objects.isNull(obj)) {
      if (obj.getClass() == Person.class) {
        Person person = (Person) obj;
        return Objects.equals(person.getName(), this.getName());
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
