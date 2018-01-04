// @Author ash975

public class WordNet{

    public WordNet(String synsets, String hypernyms){
      if(synsets == null || hypernyms == null) 
        throw new java.lang.IllegalArgumentException();
      
    }

    public Iterable<String> nouns(){}

    public boolean isNoun(String word){}

    public int distance(String nounA, String nounB){}

    public String sap(String nounA, String nounB){}

    public static void main(String[] args){}

}
