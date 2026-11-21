import java.util.*;

import static java.lang.Math.min;


/**
 * Adjacency list representation of a graph G = (V,E)
 */
public class GraphMetro {
    private Track[] edges;
    private Map<BuildingID, Building> vertices;



    public GraphMetro(Track[] edges, Map<BuildingID, Building> vertices){
        this.edges = edges;
        this.vertices = vertices;
    }//end of Graph constructor


    public Track[] getTracks(){return edges;}

    public Map<BuildingID, Building> getAllBuildingAsMap(){return this.vertices;}

    public Set<BuildingID> getAllBuildingIDs(){return this.vertices.keySet();}

    /**
     * track capacity = min (startBuildingCapacity, endBuildingCapacity, trackCapacity)
     * @return track array with new minimum capacities
     */
    public Track[] tracksWithMinCapacity(){
        List<Track> updatedTracks = new LinkedList<>();

        for (Track currentTrack : edges){
            int startBuildingCapacity   = vertices.get(currentTrack.startBuildingId()).occupants();
            int endBuildingCapacity     = vertices.get(currentTrack.endBuildingId()).occupants();
            int updatedCapacity         = min( min(startBuildingCapacity, endBuildingCapacity), currentTrack.capacity() );
            Track newUpdatedTrack       = new Track(
                    currentTrack.id(),
                    currentTrack.startBuildingId(),
                    currentTrack.endBuildingId(),
                    currentTrack.cost(),
                    updatedCapacity);
            updatedTracks.add(newUpdatedTrack);
        }
        return updatedTracks.toArray(new Track[0]);
    }//end of tracksWithMinCapacity

}//end of GraphMetro class
