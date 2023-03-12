package com.example.project;

import static com.example.project.FileOperations.deleteEmptyLinesFromFile;
import static com.example.project.FileOperations.deleteLineFromFile;
import static com.example.project.FileOperations.readFromFile;
import static com.example.project.FileOperations.writeToFile;
import static com.example.project.FileOperations.writeWithoutIndentationToFile;
import static com.example.project.Question.isTwoDigitId;
import static com.example.project.Tema1.ANSWERS_FILE;
import static com.example.project.Tema1.COMPLETEDQUIZZES_FILE;
import static com.example.project.Tema1.FULLQUESTIONS_FILE;
import static com.example.project.Tema1.QUESTIONS_FILE;
import static com.example.project.Tema1.QUIZQUESTIONS_FILE;
import static com.example.project.Tema1.QUIZZESWITHUSERNAME_FILE;
import static com.example.project.Tema1.QUIZZES_FILE;
import static com.example.project.Tema1.REALANSWERS_FILE;

import java.util.ArrayList;
import java.util.List;

public class Quizz {
  String name;
  private int id;
  static Integer totalQuizzes = 0;
  List<String> questions = new ArrayList<>();

  public Quizz () {
  }

  public Quizz (String name, List<String> questions, boolean toIncrementId) {
    this.name = name;
    if (toIncrementId) {
      totalQuizzes++;
      this.id = totalQuizzes;
    }
    this.questions = questions;
  }

  public String printQuizz() {
    return "[" + id + "]" + name;
  }

  public static String createQuizz(String name, String questionId, boolean toPrint, List<String> questions, boolean toIncrementId, String user) {
    name = name.substring(7, name.length() - 1);
    user = user.substring(4, user.length() - 1);
    questionId = questionId.substring(13, questionId.length() - 1);

    Quizz quizz = new Quizz(name, questions, toIncrementId);

    questions.add(questionId);


    for (String question : questions) {
      writeWithoutIndentationToFile(question, QUIZQUESTIONS_FILE);
    }

    for (int i = 0; i < readFromFile(QUESTIONS_FILE, false).size(); i++) {
      if(readFromFile(QUESTIONS_FILE, false).get(i).contains(questionId)) {
        questions.add(readFromFile(QUESTIONS_FILE, false).get(i));
      }
    }

    if (toPrint) {
      writeToFile(quizz.printQuizz(), QUIZZES_FILE);
      writeToFile(quizz.printQuizz() + " " + user, QUIZZESWITHUSERNAME_FILE);
    }
    return quizz.printQuizz();
  }

  public static String getQuizzId(String name) {
    name = name.substring(7, name.length() - 1);
    List<String> results = readFromFile(QUIZZES_FILE, false);
    for (String currentQuizz : results) {
      if (isTwoDigitId(currentQuizz)) {
        if (currentQuizz.substring(4).equals(name)) {
          return currentQuizz.substring(1, 3);
        }
      } else {
        if (currentQuizz.substring(3).equals(name)) {
          return currentQuizz.substring(1, 2);
        }
      }
    }
    return "";
  }

  public static void getAllQuizzes () {
    System.out.print("{ 'status' : 'ok', 'message' : '[");
    List<String> quizList = readFromFile(QUIZZES_FILE, false);

    for (String s : quizList) {
      writeToFile(s, COMPLETEDQUIZZES_FILE);
    }

    List<String> list = readFromFile(COMPLETEDQUIZZES_FILE, false);

    for (int i = 0; i < list.size(); i++) {
      String name = list.get(i).substring(3);
      String id;
      if(isTwoDigitId(list.get(i))) {
        id = list.get(i).substring(1, 3);
      }
      else {
        id = list.get(i).substring(1, 2);
      }
      if(list.get(i).contains("is_completed")) {
        System.out.print("{\"quizz_id\" : \"" + id + "\", \"quizz_name\" : \"" + name + "\", \"is_completed\" : \"True\"}");
        if (i != list.size() - 1) {
          System.out.print(", ");
        }
      }
      else {
        System.out.print("{\"quizz_id\" : \"" + id + "\", \"quizz_name\" : \"" + name + "\", \"is_completed\" : \"False\"}");
        if (i != list.size() - 1) {
          System.out.print(", ");
        }
      }
    }
    System.out.print("]'}");
  }

  public static void getQuizDetailsById(String quizId) {
    System.out.print("{'status':'ok','message':'[");
    deleteEmptyLinesFromFile(ANSWERS_FILE);

    int questionId;
    List<String> quizQuestionsFile = readFromFile(QUIZQUESTIONS_FILE, false);
    List<String> quizFile = readFromFile(QUIZZES_FILE, false);
    List<String> fullQuestions = readFromFile(FULLQUESTIONS_FILE, false);
    List<String> realAnswersFile = readFromFile(REALANSWERS_FILE, false);
    int i = 0;
    int index = 0;
    String[] str;

    while(i < quizFile.size() && quizFile.get(i).contains(quizId)) {
      for (int j = 0; j < fullQuestions.size(); j++) {
        System.out.print("{");
        questionId = Integer.parseInt(quizQuestionsFile.get(Integer.parseInt(quizId)-1).substring(index, index+1));
        System.out.print(fullQuestions.get(questionId-1) + ", \"answers\":\"[");

        str = realAnswersFile.get(questionId - 1).split("\\s+Truth: 0|\\s+Truth: 1", 0);
        for(int k = 0; k < str.length; k++) {
          if (str[k].equals(" ")) {
            continue;
          }
          System.out.print(str[k]);
          if (k < str.length - 1 && !str[k+1].equals(" ")) {
            System.out.print(",");
          }
        }

        System.out.print("]\"}");
        if (j < fullQuestions.size() - 1) {
          System.out.print(", ");
        }
        index+=2;
      }
      System.out.print("]'}");
      i++;
      quizId += (quizId.charAt(0));
    }
  }

  public static void submitQuiz(String quizId, String answerId, boolean toPrintResult) {
    quizId = quizId.substring(10, quizId.length() - 1);
    answerId = answerId.substring(14, answerId.length() - 1);

    deleteEmptyLinesFromFile(ANSWERS_FILE);

    double scoreCorrectAnswers = 0;
    double scoreWrongAnswers = 0;
    double score = 0;

    List<String> quizQuestionsFile = readFromFile(QUIZQUESTIONS_FILE, false);
    List<String> realAnswersFile = readFromFile(REALANSWERS_FILE, false);
    List<String> quizFile = readFromFile(QUIZZES_FILE, false);

    int index = 0;
    int questionId;
    double m = 0;
    double realScorePerQuestion = 0;

    // parcurg fisierul de raspunsuri pentru a vedea cate raspunsuri corecte
    // si gresite sunt la fiecare intrebare
    while (m < realAnswersFile.size()) {
      for (int i = 0; i < realAnswersFile.size(); i++) {
        String[] s = realAnswersFile.get(i).split(",", 0);
        int totalCorrectAnswersPerQuestion = 0;
        int totalWrongAnswersPerQuestion = 0;

        for(int j = 0; j < s.length; j++) {
          if (s[j].contains("Truth: 1"))
            totalCorrectAnswersPerQuestion++;
          else if (s[j].contains("Truth: 0"))
            totalWrongAnswersPerQuestion++;
        }

        //daca exista in fisierul de QUIZZES quiz-ul cu id-ul meu, merg mai departe
        if (quizFile.get(Integer.parseInt(quizId)-1).contains(quizId)) {
          String[] questions = quizQuestionsFile.get(Integer.parseInt(quizId) - 1).split(" ", 0);
          double totalNumberOfQuestions = questions.length;
          //cat timp index-ul din quizQuestionsFile este mai mic decat numarul intrebarilor, merg mai departe
          while (index <= totalNumberOfQuestions) {
            questionId = Integer.parseInt(quizQuestionsFile.get(Integer.parseInt(quizId) - 1).substring(index, index + 1));
            String[] arr = realAnswersFile.get(questionId - 1).split("\"answer_name\":", 0);
            double scorePerQuestion = 0;
            for (int z = 0; z < arr.length; z++) {
              if (arr[z].contains("\"answer_id\":\"" + answerId + "\"")) {

                if (arr[z].endsWith("Truth: 1 {") || arr[z].endsWith("Truth 1 ")) {
                  scoreCorrectAnswers++;
                } else if (arr[z].endsWith("Truth: 0 {") || arr[z].endsWith("Truth 0 ")) {
                  scoreWrongAnswers++;
                }

                if (totalCorrectAnswersPerQuestion != 0) {
                  scoreCorrectAnswers = scoreCorrectAnswers / totalCorrectAnswersPerQuestion;
                }
                else {
                  scoreCorrectAnswers = 0;
                }

                if (totalWrongAnswersPerQuestion != 0) {
                  scoreWrongAnswers = scoreWrongAnswers / totalWrongAnswersPerQuestion;
                } else {
                  scoreWrongAnswers = 0;
                }

                scorePerQuestion = scoreCorrectAnswers - scoreWrongAnswers;
                realScorePerQuestion = (100 / totalNumberOfQuestions) * scorePerQuestion;
              }
            }
            score += realScorePerQuestion;
            if (score < 0) {
              score = 0;
            }
            index += 2;
          }
        }
        else {
          System.out.println("Quiz does not exist!");
        }
      }
      m++;
    }
    if (toPrintResult) {
      System.out.println("{ 'status' : 'ok', 'message' : '" + (int)score + " points'}");
    }

    for (String s : quizFile) {
      if (s.contains(quizId)) {
        writeToFile(s + "is_completed: True", COMPLETEDQUIZZES_FILE);
      }
    }
  }

  public static void deleteQuiz(String quizId) {
    quizId = quizId.substring(5, quizId.length() - 1);
    List<String> quizFile = readFromFile(QUIZZES_FILE, false);
    for (String s : quizFile) {
      if (s.contains(quizId)) {
        deleteLineFromFile(QUIZZES_FILE, s);
      }
    }
    System.out.println("{ 'status' : 'ok', 'message' : 'Quizz deleted successfully'}");
  }

}
