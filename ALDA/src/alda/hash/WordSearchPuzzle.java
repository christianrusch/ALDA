package alda.hash;

/**
 * @author Christian Rusch
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class WordSearchPuzzle {
	
	HashMap<String, Object> dictionary = new HashMap<String, Object>();
	char[][] grid;
	
	private static final int MINIMUM_WORDLENGTH = 4;
	
	/**
	 * @param startCoordinate Array containing the word
	 * coordinates; 0 is the row, 1 is the column.
	 * @param endCoordinate Array containing the word
	 * coordinates; 0 is the row, 1 is the column. 
	 */
	private static class Word {
		
		String word;
		int[] startCoordinate;
		int[] endCoordinate;
		
		public Word(String s, int[] sC, int[] eC) {
			word = s;
			startCoordinate = sC;
			endCoordinate = eC;
		}
		
		public String getWord() {
			return word;
		}
		
		public String toString() {
			return word +
					" (" + String.valueOf(startCoordinate[0]) + ":" + String.valueOf(startCoordinate[1]) +
					") - (" + String.valueOf(endCoordinate[0]) + ":" + String.valueOf(endCoordinate[1]) + ")";
		}
		
	}

	/**
	 * Loads a dictionary file to a HashMap for quick access.
	 */
	private HashMap<String, Object> loadDictionary(String filename) {
		try {
			FileReader dictionaryFile = new FileReader(filename);
			BufferedReader inputBuffer = new BufferedReader(dictionaryFile);
			String entry = inputBuffer.readLine();
			while( entry != null ) {
				dictionary.put(entry, null);
				entry = inputBuffer.readLine();
			}
			inputBuffer.close();
		} catch( java.io.IOException e ) {
			 System.out.println("Error in " + filename + " : " + e.getMessage() + " : " + e.getStackTrace());
		}
		return dictionary;
	}
	
	/**
	 * Loads a text file containing a the strings for the grid,
	 * with each row in the grid delimited by a row break.
	 * 
	 * @param filename The path to the text file containing the grid data.
	 * @param gridLines An ArrayList<String> containing each grid row.
	 */
	private void loadGrid(String filename) {
		
		List<String> gridLines = new ArrayList<>();
		
		int rows = 0;
		int cols = 0;
		
		try {
			FileReader gridFile = new FileReader(filename);
			BufferedReader inputBuffer = new BufferedReader(gridFile);
			String entry = inputBuffer.readLine();
			while( entry != null ) {
				gridLines.add(entry);
				entry = inputBuffer.readLine();
			}
			inputBuffer.close();
		} catch( java.io.IOException e ) {
			 System.out.println("Error in " + filename + " : " + e.getMessage() + " : " + e.getStackTrace());
		}
		
		rows = gridLines.size();
		if(rows > 0) {
			cols = gridLines.get(0).length();
		}
		
		grid = new char[rows][cols];
		
		for(int i = 0; i < rows; i++) {
			grid[i] = gridLines.get(i).toCharArray();
		}
	}
	
	private List<Word> findWords(int wordLength) {
		
		List<Word> wordList = new ArrayList<Word>();
		int numberOfRows = grid.length;
		int numberOfColumns = grid[0].length;
		
		for(int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
			for(int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
				if(rowIndex - (wordLength-1) >= 0) {
					wordList.addAll( searchTopToBottom(rowIndex, columnIndex) );
				}
				if(columnIndex - (wordLength-1) >= 0) {
					wordList.addAll( searchLeftToRight(rowIndex, columnIndex) );
				}
				if( columnIndex - (wordLength-1) >= 0 && rowIndex - (wordLength-1) > 0) {
					wordList.addAll( searchTopLeftToBottomRight(rowIndex, columnIndex) );
				}
				if( rowIndex - (wordLength-1) >= 0 && columnIndex + (wordLength-1) < numberOfColumns) {
					wordList.addAll( searchTopRightToBottomLeft(rowIndex, columnIndex) );
				}
			}
		}
		return wordList;
	}
	
	/**
	 * Searches 
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private List<Word> searchTopToBottom(int rowIndex, int columnIndex) {
		
        List<Word> foundWords = new ArrayList<Word>();
        StringBuilder word = new StringBuilder();
        for(int row = rowIndex; row >= 0; row--) {
            word.append(grid[row][columnIndex]);
            if(word.length() >= MINIMUM_WORDLENGTH) {
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {rowIndex, columnIndex}, new int[] {row, columnIndex}));
                }
                word.reverse();
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {row, columnIndex}, new int[] {rowIndex, columnIndex}));
                }
                word.reverse();
            }
        }
		return foundWords;
	}
	
	/**
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private List<Word> searchLeftToRight(int rowIndex, int columnIndex) {
        List<Word> foundWords = new ArrayList<Word>();
        StringBuilder word = new StringBuilder();
        for(int column = columnIndex; column >= 0; column--) {
            word.append(grid[rowIndex][column]);
            if(word.length() >= MINIMUM_WORDLENGTH) {
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {rowIndex, columnIndex}, new int[] {rowIndex, column}));
                }
                word.reverse();
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {rowIndex, column}, new int[] {rowIndex, columnIndex}));
                }
                word.reverse();
            }
        }
		return foundWords;
	}
	
	/**
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private List<Word> searchTopLeftToBottomRight(int rowIndex, int columnIndex) {
        List<Word> foundWords = new ArrayList<Word>();
        StringBuilder word = new StringBuilder();
        for(int row = rowIndex, column = columnIndex; row >= 0 && column >= 0; row--, column--) {
            word.append(grid[row][column]);
            if(word.length() >= MINIMUM_WORDLENGTH) {
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {rowIndex, columnIndex}, new int[] {row, column}));
                }
                word.reverse();
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {row, column}, new int[] {rowIndex, columnIndex}));
                }
                word.reverse();
            }
        }
		return foundWords;
	}
	
	/**
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	private List<Word> searchTopRightToBottomLeft(int rowIndex, int columnIndex) {
        List<Word> foundWords = new ArrayList<Word>();
        StringBuilder word = new StringBuilder();
        for(int row = rowIndex, column = columnIndex; row >= 0 && column < grid[0].length; row--, column++) {
            word.append(grid[row][column]);
            if(word.length() >= MINIMUM_WORDLENGTH) {
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {rowIndex, columnIndex}, new int[] {row, column}));
                }
                word.reverse();
                if(dictionary.containsKey(word.toString().toLowerCase())) {
                    foundWords.add(new Word(word.toString(), new int[] {row, column}, new int[] {rowIndex, columnIndex}));
                }
                word.reverse();
            }
        }
		return foundWords;
	}
	/**
	 * 
	 */
	private void initShit() {
		loadDictionary("./src/alda/hash/mywordlistfile.txt");
		loadGrid("./src/alda/hash/mygridfile.txt");
//		printDictionary();
		printGrid();
		List<Word> foundWordsList = findWords(MINIMUM_WORDLENGTH);
		printSolutions(foundWordsList);
	}
	
	/**
	 * 
	 */
	private void printDictionary() {
		for(String s : dictionary.keySet()) {
			System.out.println(s);
		}
		System.out.println();
	}
	
	/**
	 * 
	 */
	private void printGrid() {
		for(int i = 0; i < grid.length; i++) {
			for(char c : grid[i]) {
				System.out.print(c + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * 
	 * @param foundWords
	 */
    private void printSolutions(List<Word> foundWords) {

    	Collections.sort(foundWords, new Comparator<Word>() {
    		@Override
    		public int compare(Word word1, Word word2) {
    			return word1.getWord().compareTo(word2.getWord());
    		}
    	});
    	System.out.println("Found " + foundWords.size() + " words:");
    	System.out.println("============================");
    	for(Word w: foundWords) {
    		System.out.println(w.toString());
    	}
    }
	
	public static void main(String args[]) {
		WordSearchPuzzle wsp = new WordSearchPuzzle();
		wsp.initShit();
	}

}
