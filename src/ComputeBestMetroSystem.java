import java.util.*;

/**
 * kruskal's algorithm for finding MAXIMAL spanning trees. In fact, here we want to maximise
 * the "goodness" of the overall tree given that the "goodness" of each track is computed by the following:
 * floor( trackCapacity / trackCost), where trackCapacity is defined to be:
 * min (startBuildingCapacity, endBuildingCapacity, trackCapacity)
 *
 */
public class ComputeBestMetroSystem {
    private GraphMetro graphForMST;
    private Map<TrackID, Double> goodnessOfTracks;


    public TrackID[] getBestMetroSystemTracks(GraphMetro metroSystem){
        this.goodnessOfTracks = new HashMap<>();
        this.graphForMST = new GraphMetro(metroSystem.tracksWithMinCapacity(), metroSystem.getAllBuildingAsMap());

        for (Track currentTrack : graphForMST.getTracks()){
            double division = ((double) currentTrack.capacity())/ ((double) currentTrack.cost());
            double goodness = Math.floor(division);
            goodnessOfTracks.put(currentTrack.id(), goodness);
        }

        //we want to maximise goodness of tracks, so we want maximum spanning tree
        java.util.Arrays.sort(graphForMST.getTracks(), new TrackGoodnessComparator(goodnessOfTracks).reversed());//sort in decreasing order

        return Kruskal();
    }//end of getBestMetroSystemTracks


    /**
     * @precondition sorted array of edges
     * @return maximum spanning tree of a graph
     */
    private TrackID[] Kruskal(){
        List<TrackID> mst = new LinkedList<>();
        NaiveDisjointSet<BuildingID> disjointSet = new NaiveDisjointSet<>();

        for (Track currentTrack : graphForMST.getTracks()){
            disjointSet.add(currentTrack.startBuildingId());
            disjointSet.add(currentTrack.endBuildingId());
        }

        for (Track currentTrack : graphForMST.getTracks()){
            if (!disjointSet.find(currentTrack.startBuildingId()).equals(disjointSet.find(currentTrack.endBuildingId()))){
                mst.add(currentTrack.id());
                disjointSet.union(currentTrack.startBuildingId(), currentTrack.endBuildingId());
            }
        }

        return mst.toArray(new TrackID[0]);
    }//end of Kruskal


    private static class TrackGoodnessComparator implements Comparator<Track>{
        Map<TrackID, Double> goodnessOfTracks;

        TrackGoodnessComparator(Map<TrackID, Double> goodnessOfTracks){
            this.goodnessOfTracks = goodnessOfTracks;
        }//end of TrackComparator constructor

        @Override
        public int compare(Track track, Track otherTrack){
            Double goodnessFirstTrack = goodnessOfTracks.get(track.id());
            Double goodnessSecondTrack = goodnessOfTracks.get(otherTrack.id());
            return goodnessFirstTrack.compareTo(goodnessSecondTrack);
        }//end of compare

    }//end of TrackComparator class

}//end of computeBestMetroSystem class
