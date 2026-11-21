import java.util.*;

/**
 * inspiration from:
 * "https://www.geeksforgeeks.org/java/trie-data-structure-in-java/"
 * "https://luc.devroye.org/TiffanyYong+AlexandraBell-COMP252-Stringology-2022.pdf"
 */
public class PrefixTree {
    private final PrefixTreeNode root;

    public PrefixTree(){
        this.root = new PrefixTreeNode();
    }//end of PrefixTree constructor



    public void insertName(String nameToAdd){
        //only keep letters from the english alphabet
        nameToAdd = transformIntoValidName(nameToAdd);

        PrefixTreeNode currentNode = root;
        char currentChar;

        for (int i = 0; i < nameToAdd.length(); i++) {
            currentChar =  nameToAdd.charAt(i);
            currentNode.children.putIfAbsent(currentChar, new PrefixTreeNode());
            currentNode = currentNode.children.get(currentChar);
        }
        currentNode.endOFWord = true;
    }//end of insertString


    public ArrayList<String> getFormattedNamesThatStartsWith(String firstLetters){
        String validString = transformIntoValidName(firstLetters);
        List<String> namesThatStartWithPrefix = getStringsStartingWith(validString);
        String[] alphabeticallySortedNames = sortNamesIntoArray(namesThatStartWithPrefix);
        ArrayList<String> formattedSortedNames = formatNames(alphabeticallySortedNames);
        return formattedSortedNames;
    }//end of getFormattedNamesThatStartsWith
    
    
    private String transformIntoValidName(String nameToTransform){
        nameToTransform = nameToTransform.replaceAll("[^a-zA-Z]","");
        nameToTransform = nameToTransform.toLowerCase();
        return nameToTransform;
    }//end of transformIntoValidName


    private List<String> getStringsStartingWith(String firstLetters){
        char currentChar;
        PrefixTreeNode currentNode = root;

        for (int i = 0; i < firstLetters.length(); i++) {
            currentChar = firstLetters.charAt(i);

            if (currentNode.children.get(currentChar) == null){return new ArrayList<>();}
            currentNode = currentNode.children.get(currentChar);
        }

        List<String> StringsWithFirstLetters = DFS(currentNode, firstLetters);
        return StringsWithFirstLetters;
    }//end of getStringsStartingWith


    private String[] sortNamesIntoArray(List<String> namesToSort){
        String[] sortedNames = namesToSort.toArray(new String[0]);
        java.util.Arrays.sort(sortedNames);
        return  sortedNames;
    }//end of sortNamesIntoArray


    /**
     * uses a custom DFS implementation to find string associated with nodes starting from a
     * source node.
     * @param startNode root node of DFS traversal (may not be the actual tree root)
     * @return
     */
    private List<String> DFS(PrefixTreeNode startNode, String prefix){
        char nextLetter;
        List<String> AllStringWithPrefix = new ArrayList<>();

        if (startNode.endOFWord){
            AllStringWithPrefix.add(prefix);
        }

        for (int i = 0; i < 26; i++){
            nextLetter = (char) (i + 'a');
            PrefixTreeNode child = startNode.children.get(nextLetter);
            if (child != null) {
                DFSVisit(child, prefix + nextLetter, AllStringWithPrefix);
            }
        }

        return AllStringWithPrefix;
    }//end of DFS
    
    
    private void DFSVisit(PrefixTreeNode currentNode, String currentString, List<String> allStringsFound){
        if (currentNode.endOFWord) {
            allStringsFound.add(currentString);
        }

        char nextLetter;
        for (int i = 0; i < 26; i++) {
            nextLetter = (char) (i + 'a');
            PrefixTreeNode child = currentNode.children.get(nextLetter);
            if (child != null) {
                DFSVisit(child, currentString + nextLetter, allStringsFound);
            }
        }
    } //end of DFSVisit


    private ArrayList<String> formatNames(String[] namesToFormat){
        ArrayList<String> formattedNames = new ArrayList<>();
        for (String name : namesToFormat){
            if (name.isEmpty()){continue;}
            String formatted = name.substring(0, 1).toUpperCase() + name.substring(1);
            formattedNames.add(formatted);
        }

        return formattedNames;
    }//end of formatNames




    private static class PrefixTreeNode{
        private boolean endOFWord;
        private final Map<Character, PrefixTreeNode> children;


        PrefixTreeNode(){
            this.endOFWord = false;
            int INITIAL_CAPACITY_NEEDED_IN_ORDER_TO_RESIZE_ONLY_ONCE = 32;
            this.children = new HashMap<>(INITIAL_CAPACITY_NEEDED_IN_ORDER_TO_RESIZE_ONLY_ONCE);
        }//end of PrefixTreeNode constructor

    }//end of PrefixTreeNode nested class

}//end of PrefixTree class
