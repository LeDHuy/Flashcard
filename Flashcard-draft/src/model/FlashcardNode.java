package model;

public class FlashcardNode {
    public Flashcard data;
    public FlashcardNode prev;
    public FlashcardNode next;

    public FlashcardNode(Flashcard data) {
        this.data = data;
    }
}