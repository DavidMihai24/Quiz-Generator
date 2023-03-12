package com.example.project;

import static com.example.project.Tema1.REALANSWERS_FILE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOperations {
  static final String ERROR = "An error occurred.";
  private FileOperations() {

  }

  public static void writeToFile(String x, String file) {
    try {
      BufferedWriter myWriter = new BufferedWriter(new FileWriter(file, true));
      myWriter.write(x + "\n"); // + "\n"
      myWriter.close();
    } catch (IOException e) {
      System.out.println(ERROR);
      e.printStackTrace();
    }
  }

  public static void writeWithoutIndentationToFile(String x, String file) {
    try {
      BufferedWriter myWriter = new BufferedWriter(new FileWriter(file, true));
      myWriter.write(x + " ");
      myWriter.close();
    } catch (IOException e) {
      System.out.println(ERROR);
      e.printStackTrace();
    }
  }

  public static List<String> readFromFile(String file, boolean toPrint) {
    List<String> list = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new FileReader((file)));
      String line = br.readLine();
      while ((line != null)) {
        if(toPrint) {
          System.out.println(line);
        }
        list.add(line);
        line = br.readLine();
        if(line == null)
          break;
      }
      br.close();
    }
    catch (IOException e) {
      System.out.println(ERROR);
      e.printStackTrace();
    }
    return list;
  }

  public static void deleteEmptyLinesFromFile (String file) {
    PrintWriter writer;

    try {
      Scanner fileName = new Scanner(new File(file));
      writer = new PrintWriter(REALANSWERS_FILE);

      while (fileName.hasNext()) {
        String line = fileName.nextLine();
        if (!line.isEmpty()) {
          writer.write(line);
          writer.write("\n");
        }
      }

      fileName.close();
      writer.close();

    } catch (IOException e) {
      System.out.println(ERROR);
      e.printStackTrace();
    }
  }

  public static void deleteLineFromFile(String file, String lineToRemove) {
    try {
      File inFile = new File(file);
      if (!inFile.isFile()) {
        System.out.println("Parameter is not an existing file");
        return;
      }

      File tempFile = new File(inFile + ".tmp");

      BufferedReader br = new BufferedReader(new FileReader(file));
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

      String line = null;

      while ((line = br.readLine()) != null) {
        if (!line.trim().equals(lineToRemove)) {
          pw.println(line);
          pw.flush();
        }
      }
      pw.close();
      br.close();

      if (!inFile.delete()) {
        System.out.println("Could not delete file");
        return;
      }

      if (!tempFile.renameTo(inFile))
        System.out.println("Could not rename file");

    }
    catch (FileNotFoundException ex) {
      System.out.println(ERROR);
      ex.printStackTrace();
    }
    catch (IOException ex) {
      System.out.println(ERROR);
      ex.printStackTrace();
    }
  }

  public static void cleanFile(String file) {
    try {
      new FileWriter(file, false).close();
    }
    catch (IOException e) {
      System.out.println(ERROR);
      e.printStackTrace();
    }
  }
}