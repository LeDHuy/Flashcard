package data_structure;
import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    public Map<Character, TrieNode> children = new HashMap<>();
    public boolean isEndOfWord = false;
    public String key;
    public String description;

    public TrieNode() {}
}


