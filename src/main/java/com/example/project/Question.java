package com.example.project;

import static com.example.project.Answer.createAnswer;
import static com.example.project.FileOperations.readFromFile;
import static com.example.project.FileOperations.writeToFile;
import static com.example.project.FileOperations.writeWithoutIndentationToFile;
import static com.example.project.Tema1.ANSWERS_FILE;
import static com.example.project.Tema1.FULLQUESTIONS_FILE;
import static com.example.project.Tema1.QUESTIONS_FILE;

import java.util.List;

public class Question {
  String text;
  private int id = 0;
  static Integer totalQuestions = 0;
  List<String> answers;
  String type;

  public Question(String text, List<String> answers, boolean toIncrementId, String type) {
    this.text = text;
    if(toIncrementId) {
      totalQuestions++;
      this.id = totalQuestions;
    }
    this.answers = answers;
    this.type = type;
  }

  public String printQuestionText() {
    return "[" + id + "]" + text;
  }

  public String printFullQuestionDetails() {
    return "\"question-name\":\"" + text + "\", \"question_index\":\""  + id + "\", \"question_type\":\"" + type + "\"";
  }

  public static String createQuestion(String text, String answerText, String answerTruth, boolean toPrint, List<String> answers, boolean toIncrementId, String type) {

    text = text.substring(7, text.length() - 1);

    Question question = new Question(text, answers, toIncrementId, type);

    answers.add(createAnswer(answerText, answerTruth));

    for (String answer : answers) {
      writeWithoutIndentationToFile(answer, ANSWERS_FILE);
    }

    if(toPrint) {
      writeToFile(question.printQuestionText(), QUESTIONS_FILE);
      writeToFile(question.printFullQuestionDetails(), FULLQUESTIONS_FILE);
    }

    return question.printQuestionText();
  }

  public static String getQuestionId(String text) {
    text = text.substring(7, text.length() - 1);
    List<String> results = readFromFile(QUESTIONS_FILE, false);
    for (String currentQuestion : results) {
      if (isTwoDigitId(currentQuestion)) {
        if (currentQuestion.substring(4).equals(text)) {
          return currentQuestion.substring(1, 3);
        }
      } else {
        if (currentQuestion.substring(3).contains(text)) {
          return currentQuestion.substring(1, 2);
        }
      }
    }
    return "";
  }

  public static void getAllQuestions () {
    System.out.print("{ 'status' : 'ok', 'message' : '[");
    List<String> list = readFromFile(QUESTIONS_FILE, false);
    for (int i = 0; i < list.size(); i++) {
      String name = list.get(i).substring(3);
      String id;
      if (isTwoDigitId(list.get(i))) {
        id = list.get(i).substring(1, 3);
      } else {
        id = list.get(i).substring(1, 2);
      }
      System.out.print("{\"question_id\" : \"" + id + "\", \"question_name\" : \"" + name + "\"}");
      if (i != list.size() - 1) {
        System.out.print(", ");
      }
    }
    System.out.print("]'}");
  }

  public static boolean isTwoDigitId(String text) {
    return Character.isDigit(text.charAt(1)) && Character.isDigit(text.charAt(2));
  }
}