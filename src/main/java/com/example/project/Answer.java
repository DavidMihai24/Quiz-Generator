package com.example.project;

public class Answer {
  private int id = 0;
  static Integer totalAnswers = 0;
  String text;
  String truth;

  public Answer() {

  }

  public Answer(String text, String truth) {
    this.text = text;
    this.truth = truth;
    totalAnswers++;
    this.id = totalAnswers;
  }

  public String printAnswer() {
    return "{\"answer_name\":\"" + text + "\", \"answer_id\":\"" + id + "\"}";
  }

  public String printAnswerWithTruth() {
    return " Truth: " + truth;
  }

  public static String createAnswer(String text, String truth) {
    text = text.substring(text.length() - (text.length() - 11), text.length() - 1);
    truth = truth.substring(truth.length() - (truth.length() - 22), truth.length() - 1);
    Answer answer = new Answer(text, truth);

    return answer.printAnswer() + answer.printAnswerWithTruth();
  }
}

