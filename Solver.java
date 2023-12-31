import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Solver {
  private final int numberOfMics;
  private List<Scene> plot;
  private final List<List<Person>> peopleInScenes;
  int numberOfScenes;
  public Solver(int numberOfMics, List<List<Person>> peopleInScenes) {
    this.numberOfMics = numberOfMics;
    this.peopleInScenes = peopleInScenes;
    this.numberOfScenes = peopleInScenes.size();
  }
  public void solve(boolean minCalc) throws Exception {

    plot = new ArrayList<>();
    for (int i = 0; i < numberOfScenes; i++) {
      Scene scene = new Scene(numberOfMics, i);
      plot.add(scene);
    }


    for (int sceneCounter = 0; sceneCounter < numberOfScenes; sceneCounter++) {


      //First pass
      for (int i = sceneCounter; i < numberOfScenes; i++) {
        Scene currentScene = plot.get(i);
        //TODO: Sort by priority
        List<Person> peopleToInsert = new ArrayList<>(peopleInScenes.get(i));

        if (i > 0) {
          Scene previous = plot.get(i - 1);

          //Places every person who is in the previous scene into the current scene
          for (Person person : peopleInScenes.get(i)) {
            if (previous.getIndex(person) != 0) {
              currentScene.setPerson(person, previous.getIndex(person));
              peopleToInsert.remove(person);
            }
          }

          currentScene.setPreviousNameDistances(peopleInScenes, i);

          for (int j = 0; j < currentScene.getNumberOfFreeSpaces(); j++) {
            //TODO: find maximum in get previous distances list, that is also in previous Names in mic,
            //TODO: and is not in that scene already.
            Map<Integer, Person> previousNamesInMic = currentScene.previousNamesInMic(plot, i);
            currentScene.setPreviousNameDistances(peopleInScenes, i);
            List<Person> peopleToInsertInPreviousMic = previousNamesInMic.values().stream().filter(peopleToInsert::contains).toList();


            if (minCalc) {
              Person minDistancePerson = null;
              int MAXVALUE = 1000000;
              int minDistance = MAXVALUE;
              for (Person p : peopleToInsertInPreviousMic) {
                int distance = currentScene.getPreviousNameDistances().get(p);
                boolean space = true;
                for (int k = 1; k < distance; k++) {
                  if (plot.get(i - k).getNumberOfFreeSpaces() <= 0) {
                    space = false;
                  }
                }
                if (space) {
                  if (distance > 1 && distance < minDistance) {
                    minDistancePerson = p;
                    minDistance = distance;
                  }
                }
              }
              if (minDistancePerson != null && minDistance != MAXVALUE) {
                Person finalMaxDistancePerson = minDistancePerson;
                int index = previousNamesInMic.entrySet().stream()
                        .filter(entry -> finalMaxDistancePerson.equals(entry.getValue()))
                        .findFirst().map(Map.Entry::getKey)
                        .orElse(null);
                for (int k = 0; k < minDistance; k++) {
                  Scene scene = plot.get(i - k);
                  scene.setPerson(minDistancePerson, index);
                  scene.removePooledPerson(minDistancePerson);
                }
                peopleToInsert.remove(minDistancePerson);
              }
            } else {
              Person maxDistancePerson = null;
              int maxDistance = 0;
              for (Person p : peopleToInsertInPreviousMic) {
                int distance = currentScene.getPreviousNameDistances().get(p);
                boolean space = true;
                for (int k = 1; k < distance; k++) {
                  if (plot.get(i - k).getNumberOfFreeSpaces() <= 0) {
                    space = false;
                  }
                }
                if (space) {
                  if (distance > maxDistance) {
                    maxDistancePerson = p;
                    maxDistance = distance;
                  }
                }
              }
              if (maxDistancePerson != null && maxDistance > 1) {
                Person finalMaxDistancePerson = maxDistancePerson;
                int index = previousNamesInMic.entrySet().stream()
                        .filter(entry -> finalMaxDistancePerson.equals(entry.getValue()))
                        .findFirst().map(Map.Entry::getKey)
                        .orElse(null);
                for (int k = 0; k < maxDistance; k++) {
                  Scene scene = plot.get(i - k);
                  scene.setPerson(maxDistancePerson, index);
                  scene.removePooledPerson(maxDistancePerson);
                }
                peopleToInsert.remove(maxDistancePerson);
              }
            }
          }


          //Places people into a mic if it has always been empty
          for (int j = 1; j < numberOfMics + 1; j++) {
            boolean previouslyNull = false;
            for (int k = 0; k < i; k++) {
              if (plot.get(k).getPerson((j)) != null) {
                previouslyNull = true;
              }
            }
            if (!previouslyNull && peopleToInsert.size() > 0) {
              Person p = peopleToInsert.get(0);
              currentScene.setPerson(p, j);
              peopleToInsert.remove(p);
            }
          }

          currentScene.setPooledPeople(peopleToInsert);

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
      }

      //Second pass
      //Left with just pooled mics and spaces, so just needs to be placed in at the
      //first available space
      Scene currentScene = plot.get(sceneCounter);
      List<Person> pooledPeople = currentScene.getPooledPeople();
      for (Person p: pooledPeople) {
        List<Integer> gaps = currentScene.getGapLocations();
        currentScene.setPerson(p, gaps.get(0));
      }

    }



  }

  public String getMicPlotString() {
    return plot.stream().map(Object::toString).collect(Collectors.joining(" \n"));
  }

  public List<String[]> getMicPlotListOfArrays() {
    return plot.stream().map(x -> x.getNames().toArray(new String[0])).toList();
  }

  public List<String[]> getTransposedMicPLot() {
    List<String[]> transposedPlot = new ArrayList<>();
    for (int i = 0; i < numberOfMics; i++){
      transposedPlot.add(new String[plot.size()]);
    }
    for (int i = 0; i < plot.size(); i++) {
      for (int j = 0; j < numberOfMics; j++) {
        transposedPlot.get(j)[i] = getMicPlotListOfArrays().get(i)[j];
      }
    }
    return transposedPlot;
  }

  public int getNumberOfChanges() {
//    int changes = 0;
//    for (int i = 0; i < numberOfScenes - 1; i++) {
//      changes += plot.get(i).getNumberOfChanges(plot.get(i+1));
//    }
//    return changes;
    int changes = 0;
    for (int i = 0; i < this.numberOfMics; i++) {
      Person previous = null;
      for (Scene scene: plot) {
        Person p = scene.getPerson(i);
        if (!Objects.isNull(previous) && !Objects.isNull(p)) {
          changes += Objects.equals(previous.getName(), p.getName()) ? 0 : 1;
        }
        if (!Objects.isNull(p)) {
          previous = p;
        }
      }
    }
    return changes;
  }
}
