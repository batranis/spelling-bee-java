# Spelling Bee Java Project

A Java implementation of a Spelling Bee-style word game with a graphical interface.

## Features
- Validates 7-letter puzzle input
- Checks dictionary words from a file
- Detects valid words and pangrams
- Calculates scoring system
- "Solve" button finds all possible words
- GUI-based interaction using SpellingBeeGraphics library

## How it works
Users input a 7-letter puzzle, then try to form words using those letters. Words must:
- Be at least 4 letters long
- Contain the center letter
- Only use allowed letters
- Exist in the dictionary

## Scoring
- 4-letter words = 1 point
- 5+ letters = word length
- Pangrams (use all 7 letters) = +7 bonus points

## Tech Used
- Java
- File I/O (Scanner)
- Arrays
- GUI library: SpellingBeeGraphics

## Notes
Requires the SpellingBeeGraphics library from Willamette CS1 course.
