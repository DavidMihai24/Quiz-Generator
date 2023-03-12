package com.example.project;

import static com.example.project.FileOperations.writeToFile;
import static com.example.project.Tema1.USERS_FILE;

public class User {
  String name;
  String password;
  private int id;
  static Integer totalUsers = 0;

  public User() {

  }

  public User(String name, String password) {
    this.password = password;
    this.name = name;
    totalUsers++;
    this.id = totalUsers;
  }

  public String printUser(String name, String password) {
    return name + "," + password;
  }

  public static String createUser(String name, String password, boolean toPrint) {

    name = name.substring(4, name.length() - 1);
    password = password.substring(4, password.length() - 1);
    User user = new User(name, password);

    if (toPrint) {
      writeToFile(user.printUser(name, password), USERS_FILE);
    }
    return name + "," + password;
  }
}