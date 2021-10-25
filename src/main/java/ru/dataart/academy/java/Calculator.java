package ru.dataart.academy.java;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Calculator {
    /**
     * @param zipFilePath -  path to zip archive with text files
     * @param character   - character to find
     * @return - how many times character is in files
     */
    public Integer getNumberOfChar(String zipFilePath, char character) {
        //Task implementation

        String checkedPath = getValidPath(zipFilePath);
        if (checkedPath == null) {
            return -1;
        }

        int counter = 0;

        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(checkedPath)))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    counter += countCharsInFile(zin, character);
                }
                zin.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counter;
    }

    /**
     * @param zipFilePath - path to zip archive with text files
     * @return - max length
     */
    public Integer getMaxWordLength(String zipFilePath) {
        //Task implementation
        int longestWordLen = 0;

        String checkedPath = getValidPath(zipFilePath);
        if (checkedPath == null) {
            return -1;
        }

        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(checkedPath)))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) { // obtaining next file
                if (!entry.isDirectory()) {
//                    longestWordLen = Math.max(longestWordLen, getLongestWordLength(zin));
                    longestWordLen = Math.max(longestWordLen, getLongestWordLength(zin, new Character[] {' ', '\n'}));
                }
                zin.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return longestWordLen;
    }

    /**
     * @param inStream - path to zip archive with text files
     * @return - a length of a longest word
     */
    private int getLongestWordLength(ZipInputStream inStream) throws IOException {
        int space = ' ';
        int eol = '\n';
        int maxLen = 0;
        int currLen = 0;

        int readChar;
        while ((readChar = inStream.read()) != -1) { // reading next file
            if (readChar == space || readChar == eol) {
                maxLen = Math.max(maxLen, currLen);
                currLen = 0;
            } else {
                currLen++;
            }
        }
        return maxLen;
    }

    // Returns a longest word length in a file passed within a zip stream
    private int getLongestWordLength(ZipInputStream inStream, Character[] separators) throws IOException {
        int maxLen = 0;
        int currLen = 0;

        int readChar;
        while ((readChar = inStream.read()) != -1) { // reading next file
            if (containsChar(separators, (char) readChar)) {
                maxLen = Math.max(maxLen, currLen);
                currLen = 0;
            } else {
                currLen++;
            }

        }
        return maxLen;
    }

    // Checks if a character is a given separator or not
    private boolean containsChar(Character[] separators, char character) {
        return Arrays.stream(separators).anyMatch(ch -> ch == character);
    }

    //
    private int countCharsInFile(ZipInputStream inStream, char wantedChar) throws IOException {
        int readChar;
        int counter = 0;
        while ((readChar = inStream.read()) != -1) {
            if (readChar == (int) wantedChar) {
                counter++;
            }
        }
        return counter;
    }

    // Checks if a path is a URI or a file path and if a file with this path exists
    private String getValidPath(String path) {
        String uriString = "file://" + path;
        String checkedPath;

        try { // In case the path is a URI
            URI uri = new URI(uriString);
            checkedPath = Paths.get(uri).toString();
        } catch (URISyntaxException urlException) { // If it's not a URI it could be a file path
            try {
                Paths.get(path);
                checkedPath = path;
            } catch (InvalidPathException pathException) { // The path is invalid
                System.out.println("Path is invalid");
                return null;
            }
        }

        if (!Files.exists(Paths.get(checkedPath))) {
            System.out.println("File doesn't exist");
            return null;
        }
        return checkedPath;
    }
}
