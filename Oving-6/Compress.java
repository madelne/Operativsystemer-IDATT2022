import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Objects.requireNonNull;

public class Compress{
    static String message;
    static HashMap<Character, Integer> frequencyMap;
    static PriorityQueue<HuffmanNode> priorityQueue;
    static HuffmanNode rootS;

    static class HuffmanNode implements Comparable<HuffmanNode> {
        char data;
        int frequency;
        HuffmanNode left, right;

        HuffmanNode(char data, int frequency) {
            this.data = data;
            this.frequency = frequency;
            left = null;
            right = null;
        }

        HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.frequency = left.frequency + right.frequency;
            this.data = '\0';
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(HuffmanNode o) {
            return 0;
        }
    }

    private static HuffmanNode buildTree(HuffmanNode node){
        HashMap<Character, Integer> map = new HashMap<>();
        for(char c : message.toCharArray()){
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        frequencyMap = map;

        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        for(char c : map.keySet()){
            queue.add(new HuffmanNode(c, map.get(c)));
        }
        priorityQueue = queue;

        while(queue.size() > 1){
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            HuffmanNode parent = new HuffmanNode(left, right);

        }

        return queue.poll();
    }

    private static void writeCode(HuffmanNode root, StringBuilder prefix, String[] st){
        rootS = root;
        if(!checkLeaf(root)){
            writeCode(root.left, prefix.append('0'), st);
            prefix.deleteCharAt(prefix.length()-1);

            writeCode(root.right, prefix.append('1'), st);
            prefix.deleteCharAt(prefix.length()-1);
        } else {
            st[root.data] = prefix.toString();
        }
    }

    private static boolean checkLeaf(HuffmanNode x){
        return(x.left == null && x.right == null);
    }

    public void compress(String inputText, String outputText) throws IOException {
        Queue<HuffmanNode> queue = new PriorityQueue<>();
        frequencyMap.forEach((character, frequency) ->
                queue.add(new HuffmanNode(character, frequency))
        );
        while (queue.size() > 1) {
            queue.add(new HuffmanNode(queue.poll(), requireNonNull(queue.poll())));
        }
        buildTree(rootS = queue.poll());
        return getEncodedText();
    }

    private String getEncodedText() {
        StringBuilder sb = new StringBuilder();
        for (char character : message.toCharArray()) {
            sb.append(priorityQueue.get(character));
        }
        return sb.toString();
    }




    public class Main {
        public static void main(String[] args) {

        }
    }
}




