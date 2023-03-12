package com.example.imagecompressor;

/* Build a class for creating and performing Tree operations on Haffman
Tree Like creating a tree,checking if node is leaf node etc.
 */

public class HuffmanEncoder {
    public static final int BITS_PER_PIXEL = 24;

    public static class HuffmanNode implements Comparable<HuffmanNode> {
        int value;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;

        public HuffmanNode(int value,int frequency,HuffmanNode left,HuffmanNode right){
            this.value = value;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        //compare the frequency of current node to input node
        public int compareTo(HuffmanNode node){
            return frequency - node.frequency;
        }
    }
}
