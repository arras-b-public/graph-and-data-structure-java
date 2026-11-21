import java.util.*;

/**
 * THIS ALGORITHM IMPLEMENTATION WAS HEAVILY INSPIRED BY William Fiset's one, but was adapated to the context of the project
 * "https://www.geeksforgeeks.org/dsa/ford-fulkerson-algorithm-for-maximum-flow-problem/",
 * "https://www.youtube.com/watch?v=Xu8jjJnwvxE"
 * "https://www.youtube.com/watch?v=OViaWp9Q-Oc",
 * "https://github.com/williamfiset/Algorithms/blob/master/src/main/java/com/williamfiset/algorithms/graphtheory/networkflow/EdmondsKarpAdjacencyList.java",and
 * "https://www.cs.cornell.edu/courses/cs4820/2012sp/handouts/edmondskarp.pdf
 *
 */
public class EdmondsKarp {
    private final GraphMetro validGraph;
    private final BuildingID source;
    private final BuildingID target;
    private final Map<BuildingID, List<ResidualEdge>> residualGraph;


    public EdmondsKarp(GraphMetro graph, BuildingID source,  BuildingID sink){
        this.validGraph = transformIntoValidFlowNetwork(graph);
        this.source =source;
        this.target =sink;
        this.residualGraph = new HashMap<>();
        for (BuildingID currentBuildingID : validGraph.getAllBuildingIDs()){
            residualGraph.put(currentBuildingID, new ArrayList<>());
        }
    }//end of EdmondsKarp constructor


    public int EdmondsKarpAlgorithm(){
        /*
        1: f←0; Gf ←G
        2: while Gf contains a s−t path P do
        3:   Let P be an s−t path in Gf with the minimum number of edges.
        4:   Augment f using P.
        5:   Update Gf
        6:   end while
        7: return f
        */

        makeResidualGraph();

        int maxFlow = 0;
        List<ResidualEdge> path = new ArrayList<>();

        while (BFS(path)) {
            int bottleneck = findBottleneck(path);
            augmentFlow(path, bottleneck);
            maxFlow += bottleneck;
        }

        return maxFlow;

    }//end of EdmondsKarpAlgorithm


    /**
     * returns new Graph object with edge capacity such that each capacity is the minimum between
     * StartBuildingCapacity, EndBuildingCapacity, TrackCapacity
     * The final graph can be used on max flow algorithms
     * @return new Graph with correct edges capacities
     */
    private GraphMetro transformIntoValidFlowNetwork(GraphMetro originalGraph){
        return new GraphMetro(originalGraph.tracksWithMinCapacity(), originalGraph.getAllBuildingAsMap());//TODO MAKE DEEP COPY
    }//end of convertVerticesCapacityIntoEdgeCapacity


    private void makeResidualGraph(){
        for (Track currentTrack : validGraph.getTracks()) {
            ResidualEdge forward = new ForwardResidualEdge(currentTrack);
            ResidualEdge backward = new BackwardResidualEdge(currentTrack);
            forward.setOtherDirection(backward);
            backward.setOtherDirection(forward);

            this.residualGraph.get(currentTrack.startBuildingId()).add(forward);
            this.residualGraph.get(currentTrack.endBuildingId()).add(backward);
        }
       // return new GraphMetro(residualEdges.toArray(new Track[0]), validGraph.getAllBuildingIDs());
    }//end of makeResidualGraph


    /**
     * Breath first traversal that returns if there exists a path from source vertex
     * to target vertex. It also returns the shortest path from s to t in the map "path"
     * @param path shortest path from s to t that this method will fill
     * @return true if there exists a path from source to target
     */
    private boolean BFS(List<ResidualEdge> path){
        path.clear();//empty for this method call
        enum vertexState {Finished, Discovered, Undiscovered}
        Map<BuildingID, vertexState> states = new HashMap<>();
        Queue<BuildingID> queue = new LinkedList<>();
        Map<BuildingID, ResidualEdge> edgeUsedToArriveAtVertex = new HashMap<>();

        //initialization
        for (BuildingID currentBuilding : validGraph.getAllBuildingAsMap().keySet()){
            states.put(currentBuilding, vertexState.Undiscovered);
        }

        //add source
        states.replace(source, vertexState.Discovered);
        queue.add(source);
        edgeUsedToArriveAtVertex.put(source, null);//no edge used to reach source

        while(!queue.isEmpty()){
            BuildingID vertexV = queue.poll();
            if (vertexV == null){continue;}//not needed

            if (vertexV.compareTo(target) == 0){
                buildPath(edgeUsedToArriveAtVertex, path);
                return true;
            }

            for (ResidualEdge currentResidualEdge : residualGraph.get(vertexV)){
                BuildingID vertexAdjacentToV = currentResidualEdge.getToVertex();

                if (currentResidualEdge.getResidualCapacity() > 0 &&
                        states.get(vertexAdjacentToV).equals(vertexState.Undiscovered)) {

                    edgeUsedToArriveAtVertex.put(vertexAdjacentToV, currentResidualEdge);
                    states.replace(vertexAdjacentToV, vertexState.Discovered);
                    queue.add(vertexAdjacentToV);
                }
            }
            
            states.replace(vertexV, vertexState.Finished);
        }

        return false;//no path found
    }//end of BFS


    private void buildPath(Map<BuildingID, ResidualEdge> allEdgesTraversed, List<ResidualEdge> path){
        BuildingID currentBuilding = target;

        while(allEdgesTraversed.get(currentBuilding) != null){//i.e. while is not the source
            ResidualEdge edgeUsed = allEdgesTraversed.get(currentBuilding);
            currentBuilding = edgeUsed.from;
            path.add(edgeUsed);
        }
    }//end of buildPath


    private int findBottleneck(List<ResidualEdge> path) {
        int bottleneck = Integer.MAX_VALUE;//set to + inf

        for (ResidualEdge currentResidualEdge : path){
            if (currentResidualEdge.getResidualCapacity() < bottleneck){
                bottleneck = currentResidualEdge.getResidualCapacity();
            }
        }

        return bottleneck;
    }//end of findBottleneck


    private void augmentFlow(List<ResidualEdge> residualEdgesToAugment, int bottleneck) {
        for (ResidualEdge currentResidualEdge : residualEdgesToAugment){
            currentResidualEdge.augment(bottleneck);
        }
    }//end of augmentFlow




    abstract static class ResidualEdge{
        private int originalCapacity;
        int residualCapacity;
        private BuildingID from;
        private BuildingID to;
        private int flow;

        abstract void augment(int bottleneck);
        abstract void setOtherDirection(ResidualEdge otherDirection);
        int getResidualCapacity(){return this.residualCapacity;}
        BuildingID getFromVertex(){return from;}
        BuildingID getToVertex(){return to;}

    }//end of ResidualEdge nested class


     static class ForwardResidualEdge extends ResidualEdge{
        BackwardResidualEdge backward;

        ForwardResidualEdge(Track track){
            super.originalCapacity = track.capacity();
            super.from = track.startBuildingId();
            super.to = track.endBuildingId();
            super.flow = 0;
            super.residualCapacity = super.originalCapacity;
        }//end of ForwardResidualEdge constructor

        void augment(int bottleneck){
            super.flow += bottleneck;

            super.residualCapacity -= bottleneck;
            backward.residualCapacity += bottleneck;
        }//end of augment


         void setOtherDirection(ResidualEdge otherDirection){
            if (otherDirection instanceof BackwardResidualEdge) {
                this.backward = (BackwardResidualEdge) otherDirection;
            }else{
                throw new IllegalArgumentException();
            }
         }//end of setOtherDirection

    }//end of ForwardResidualEdge nested class


    static class BackwardResidualEdge extends ResidualEdge{
        ForwardResidualEdge forward;

        BackwardResidualEdge(Track track){
            super.originalCapacity = track.capacity();
            super.from = track.endBuildingId();
            super.to = track.startBuildingId();
            super.flow = 0;
            super.residualCapacity = 0;
        }//end of BackwardResidualEdge constructor


        void augment(int bottleneck){
            super.flow -= bottleneck;

            super.residualCapacity += bottleneck;
            forward.residualCapacity -= bottleneck;
        }//end of augment


        void setOtherDirection(ResidualEdge otherDirection){
            if( otherDirection instanceof ForwardResidualEdge){
                this.forward = (ForwardResidualEdge) otherDirection;
            }else{
                throw  new IllegalArgumentException();
            }
        }//end of setOtherDirection

    }//end of BackwardResidualEdge nested class

}//end of EdmondsKarp class
