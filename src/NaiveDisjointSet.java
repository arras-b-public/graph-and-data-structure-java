import java.util.*;

public class NaiveDisjointSet<T> {
    private final HashMap<T, T> parentMap = new HashMap<>();
    private final HashMap<T, Integer> rank = new HashMap<>();


    void add(T element) {
        parentMap.putIfAbsent(element, element);
        rank.putIfAbsent(element, 0);
    }

    // TODO: Implement path compression
    T find(T a) {
        T node = parentMap.get(a);
        if (node.equals(a)) {
            return node;
        } else {
            //p[i] = find(p[i]);
            parentMap.replace(a, find(parentMap.get(a)));
            return parentMap.get(a);
        }
    }

    // TODO: Implement union by size or union by rank
    //union by rank
    void union(T a, T b) {
        T rep_a = find(a);
        T rep_b = find(b);
        int rank_rep_a = rank.get(rep_a);
        int rank_rep_b = rank.get(rep_b);

        if (rank_rep_a == rank_rep_b){
            parentMap.replace(rep_b, rep_a);//rep_a is now the parent of rep_b
            rank.replace(rep_a, rank_rep_a + 1);

        }else if (rank_rep_a > rank_rep_b){
            parentMap.replace(rep_b, rep_a);//rep_a is now the parent of rep_b

        }else{
            parentMap.replace(rep_a, rep_b);//rep_b is now the parent of rep_a
        }
    }//end of union


    @Override
    public String toString(){
        return parentMap.toString() + rank.toString();
    }//end of toString

}//end of NaiveDisjointSet class