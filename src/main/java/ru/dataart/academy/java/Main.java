package ru.dataart.academy.java;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        String resourcesPath = System.getProperty("user.dir") + File.separator + // Get current working directory
                "src" + File.separator +
                "test" + File.separator +
                "resources" + File.separator; // To reach resources directory where archives are stored

        Calculator calculator = new Calculator();

        System.out.println("Calculator get number of 1: " +
                calculator.getNumberOfChar(resourcesPath + "test.zip", '1'));

        System.out.println("Calculator get max length: " +
                calculator.getMaxWordLength(resourcesPath + "test1.zip"));
    }
}