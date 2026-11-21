# Graph and Data Structure Algorithms (Java)

A Java project implementing several classic algorithms around a campus metro planning scenario. Buildings are nodes, tracks between them are edges, and the program answers questions about capacity, cost, passengers, and staffing.

This was the final project for an algorithms course. Each part of the scenario maps onto a standard algorithm or data structure.

## What it does

`Metro` is the main class. It is built from an array of tracks and an array of buildings, and exposes five operations:

| Operation | Algorithm / data structure |
|---|---|
| `maxPassengers(start, end)` | Edmonds-Karp maximum flow |
| `bestMetroSystem()` | Kruskal's algorithm for a maximum spanning tree, using a disjoint set |
| `addPassenger(name)` / `searchForPassengers(prefix)` | Prefix tree (trie) |
| `hireTicketCheckers(schedules)` | Greedy interval scheduling |

## Implementation notes

**Maximum passengers (Edmonds-Karp)**
The graph is stored as adjacency lists of residual edges, with a forward and a backward edge for each track. Before running the algorithm, each track's capacity is reduced to the minimum of the track's own capacity and the occupancy of the two buildings it connects. Shortest augmenting paths are found with BFS. Time complexity is O(V * E^2).

**Best metro system (Kruskal)**
Each track gets a score of floor(capacity / cost), using the same reduced capacity as above. Tracks are sorted by score in decreasing order, and Kruskal's algorithm picks the highest-scoring tracks that do not form a cycle, giving a maximum spanning tree. Cycle detection uses `NaiveDisjointSet`, which I extended with path compression and union by rank. Time complexity is O(E log E), dominated by the sort.

**Passenger search (trie)**
Names are lowercased and stripped of non-letters, then inserted into a trie. A search walks down the trie along the prefix and does a DFS from that node, visiting children in alphabetical order. Results are returned capitalized and without duplicates. Insertion is O(L) for a name of length L.

**Ticket checkers (interval scheduling)**
Applicants with a valid schedule are sorted by finishing time, then selected greedily: take the first, then each next applicant whose start time is not before the last selected applicant's end time. This gives the maximum number of non-overlapping schedules in O(n log n).

## Project structure

```
Metro.java                 main class tying everything together
Track.java                 record: id, start, end, cost, capacity
TrackID.java               record
Building.java              record: id, occupants
BuildingID.java            record
GraphMetro.java            graph wrapper (tracks and buildings)
EdmondsKarp.java           max flow
ComputeBestMetroSystem.java  Kruskal's algorithm
NaiveDisjointSet.java      disjoint set with path compression and union by rank
PrefixTree.java            trie
HireTicketCheckers.java    interval scheduling
```

## Usage

```java
Metro metro = new Metro(tracks, buildings);

int flow = metro.maxPassengers(new BuildingID(1), new BuildingID(5));
TrackID[] bestTracks = metro.bestMetroSystem();

metro.addPassenger("Alex");
ArrayList<String> matches = metro.searchForPassengers("al");

int hired = Metro.hireTicketCheckers(new int[][] {{1, 2}, {2, 3}, {5, 6}});
```

## Requirements

- Java 16 or newer (the project uses records)
- No external dependencies

```
javac *.java
```

There is no `main` method; the classes are meant to be used through `Metro` or called from your own test code.

## Credits

The Edmonds-Karp implementation was adapted from William Fiset's adjacency list version, and the trie was based on public tutorials and lecture notes. Specific sources are listed in the comments at the top of `EdmondsKarp.java` and `PrefixTree.java`.
