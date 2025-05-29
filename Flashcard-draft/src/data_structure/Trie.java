package data_structure;
import java.util.*;
public class Trie {
    private final TrieNode root = new TrieNode();

    public void insert(String key, String description) {
        TrieNode node = root;
        for (char ch : key.toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.key = key;
        node.description = description;
    }



    public List<String> suggest(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) {
                // Debug log
                System.out.println("No match found for prefix: " + prefix);
                return new ArrayList<>();
            }
        }

        List<String> results = new ArrayList<>();
        dfs(node, results);

        System.out.println("Suggestions for prefix '" + prefix + "': " + results);

        return results;
    }

    private void dfs(TrieNode node, List<String> results) {
        if (node.isEndOfWord) {
            if (node.key == null || node.description == null) {
                System.out.println("Missing key or description for node!");
            }
            results.add(node.key + ": " + node.description); // Ensure no null point exception occurs
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            dfs(entry.getValue(), results);
        }
    }
}

