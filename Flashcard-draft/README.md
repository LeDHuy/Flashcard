# Flashcards App

## Overview
This is a Java Swing-based Flashcards application designed for easy study and review. It supports creating, editing, deleting, navigating, and sorting flashcards with an intuitive graphical interface.

---

## Features
- Display flashcards showing **key** or **description** (toggle by clicking card).
- Navigate flashcards sequentially with **previous** and **next** buttons.
- Search flashcards by keywords.
- Add, delete, and edit flashcards via sidebar buttons.
- Sort flashcards by **key** or **description** (planned).
- Random access flashcards by entering their index (planned).
- Uses a **doubly linked list** (`FlashcardLinkedList`) as the main data structure for dynamic card management.
- Optionally, an **array-based structure** (`FlashcardArray`) can be used for fast random access and sorting.

---

## Architecture

### Data Structures
- **FlashcardLinkedList:** Doubly linked list storing flashcards as nodes, optimized for dynamic insertion and deletion.
- **FlashcardArray:** Dynamic array storing flashcards, optimized for fast index-based access and sorting.

### UI Components
- Built with Java Swing.
- Main window shows the flashcard content area.
- Sidebar with buttons for add, delete, edit, arrange, and quiz (quiz under development).
- Search bar at the top.
- Navigation buttons and index display below the card.

---

## How to Use
1. **Add Cards:** Use the "Add card" button to create new flashcards.
2. **Delete/Edit Cards:** Select a card and use the respective sidebar buttons.
3. **Navigate:** Use arrows to move to previous or next cards.
4. **Flip Card:** Click on the card area to toggle between key and description.
5. **Search:** Enter keywords in the search bar and press "Search" (functionality to be implemented).
6. **Sort and Random Access:** Future features planned using `FlashcardArray`.

---

## Design Notes
- The app uses a linked list for efficient insertions and deletions.
- For random access and sorting, an array-based class was designed but not fully integrated.
- Sorting is done using merge sort.
- The UI is built with absolute positioning (`null` layout) for precise control.

---

## Future Improvements
- Implement sorting UI and link with `FlashcardArray`.
- Add quiz functionality.
- Improve search with filtering.
- Switch to layout managers for responsive UI.
- Replace bubble sort with more efficient sorting algorithms.
- Synchronize linked list and array data structures for best performance.

---

## Requirements
- Java 8 or later
- No external libraries required.

---

## How to Run

Compile and run `FlashcardUI` as the main class.

```bash
javac FlashcardUI.java
java FlashcardUI
