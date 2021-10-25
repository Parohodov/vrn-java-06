package ru.dataart.academy.java;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.stream.Stream;
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
     * @param inStream - zip input stream
     * @param wantedChar - a character which entries are needed to be counted
     * @return - int - a number of wanted character entries
     */
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
                    longestWordLen = Math.max(longestWordLen, getLongestWordLength(zin));
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


    /**
     * Checks if a String represents a correct path and returns valid path if it doesn't
     * @param path - path to zip archive with text files
     * @return - a String that contains a correct path or null if the path is invalid or file doesn't exist
     */
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
