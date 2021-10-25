package ru.dataart.academy.java;

import sun.net.util.URLUtil;

import java.io.*;
import java.net.*;
import java.nio.file.*;
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

        try (ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(checkedPath)))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    int readChar;
                    while ((readChar = in.read()) != -1) {
                        if (readChar == (int) character) {
                            counter++;
                        }
                    }
                }
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
        int currWordLen = 0;
        int maxWordLen = 0;

        String checkedPath = getValidPath(zipFilePath);
        if (checkedPath == null) {
            return -1;
        }

        try (ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(checkedPath)))) {
            int space = ' ';
            int eol = '\n';

            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    int readChar;
                    while ((readChar = in.read()) != -1) {
                        if (readChar == space || readChar == eol) {
                            maxWordLen = Math.max(maxWordLen, currWordLen);
                            currWordLen = 0;
                        } else {
                            currWordLen++;
                        }
                    }
                    currWordLen = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxWordLen;
    }


    /**
     * Checks if a String represents a correct path and returns valid path if it doesn't
     * @param path - path to zip archive with text files
     * @return - a String that contains a correct path or null if the path is invalid or file doesn't exist
     */
    public String getValidPath(String path) {
        String uriString = "file://" + path;
        String checkedPath = null;

        try { // In case the path is URI
            URI uri = new URI(uriString);
            checkedPath = Paths.get(uri).toString();
        } catch (URISyntaxException urlException) { // If it's not a URL it could be a file path
            try {
                Paths.get(path);
                checkedPath = path;
            } catch (InvalidPathException pathException) { // The path is invalid
                System.out.println("Path is invalid");
                return null;
            }
        }

        if (!Files.exists(Paths.get(checkedPath))) {
            return null;
        }
        return checkedPath;
    }
}
