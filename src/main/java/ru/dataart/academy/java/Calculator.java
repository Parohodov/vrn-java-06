package ru.dataart.academy.java;

import java.io.*;
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

        String checkedPath = checkPath(zipFilePath);
        if (checkedPath == null) {
            return -1;
        }

        int counter = 0;

        try (ZipInputStream in = new ZipInputStream(new FileInputStream(checkedPath))) {
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

        String checkedPath = checkPath(zipFilePath);
        if (checkedPath == null) {
            return -1;
        }

        try (ZipInputStream in = new ZipInputStream(new FileInputStream(checkedPath))) {
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
     * Checks if a String represents a correct path
     * @param path - path to zip archive with text files
     * @return - a String that contains a correct path
     */
    public String checkPath(String path) {
        String checkedPath = path;

        if (checkedPath.charAt(0) == '/') {
            checkedPath = checkedPath.substring(1);
        }

        if (!Files.exists(Paths.get(checkedPath))) {
            return null;
        }
        return checkedPath;
    }
}
