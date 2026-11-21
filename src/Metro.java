import java.util.*;

public class Metro {
    protected Track[] tracks;//E
    protected HashMap<BuildingID, Building> buildingTable = new HashMap<>();//V
    private final GraphMetro originalGraph;
    private final PrefixTree passengers;

    // You may initialize anything you need in the constructor
    Metro(Track[] tracks, Building[] buildings) {
       this.tracks = tracks;

       // Populate buildings table
       for (Building building : buildings) {
           buildingTable.putIfAbsent(building.id(), building);
       }

       this.originalGraph = new GraphMetro(tracks, buildingTable);
       this.passengers = new PrefixTree();
    }

    // Maximum number of passengers that can be transported from start to end
    int maxPassengers(BuildingID start, BuildingID end) {
        // TODO: your implementation here
        return new EdmondsKarp(originalGraph, start, end).EdmondsKarpAlgorithm();
    }//end of maxPassengers


    // Returns a list of trackIDs that connect to every building maximizing total network capacity taking cost into account
    TrackID[] bestMetroSystem() {
        // TODO: your implementation here
        return new ComputeBestMetroSystem().getBestMetroSystemTracks(originalGraph);
    }//end of bestMetroSystem


    // Adds a passenger to the system
    void addPassenger(String name) {
        // TODO: your implementation here
        passengers.insertName(name);
    }//end of addPassenger


    // Returns all passengers in the system whose names start with firstLetters
    ArrayList<String> searchForPassengers(String firstLetters) {
        // TODO: your implementation here
        return passengers.getFormattedNamesThatStartsWith(firstLetters);
    }//end of searchForPassengers


    // Return how many ticket checkers will be hired
    static int hireTicketCheckers(int[][] schedule) {
        // TODO: your implementation here
        return new HireTicketCheckers().getNumberOfHiredApplicants(schedule);
    }//end of hireTicketCheckers


    // Do not change this
    void addPassengers(String[] names) {
        for (String s : names) {
            addPassenger(s);
        }
    }//end of addPassengers

}//end of Metro class