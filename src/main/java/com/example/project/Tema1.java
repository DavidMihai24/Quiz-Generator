package com.example.project;
import static com.example.project.FileOperations.cleanFile;
import static com.example.project.FileOperations.readFromFile;
import static com.example.project.FileOperations.writeToFile;
import static com.example.project.FileOperations.writeWithoutIndentationToFile;
import static com.example.project.Question.createQuestion;
import static com.example.project.Question.getAllQuestions;
import static com.example.project.Question.getQuestionId;
import static com.example.project.Quizz.createQuizz;
import static com.example.project.Quizz.deleteQuiz;
import static com.example.project.Quizz.getAllQuizzes;
import static com.example.project.Quizz.getQuizDetailsById;
import static com.example.project.Quizz.getQuizzId;
import static com.example.project.Quizz.submitQuiz;
import static com.example.project.User.createUser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Tema1 {

	static Path currentRelativePath = Paths.get("");
	static String s = currentRelativePath.toAbsolutePath().toString();

	static final String USERS_FILE = s + "/users.csv";
	static final String QUESTIONS_FILE = s + "/questions.csv";
	static final String ANSWERS_FILE = s + "answers.csv";
	static final String QUIZZES_FILE = s + "/quizzes.csv";
	static final String QUIZQUESTIONS_FILE = s + "/quizQuestions.csv";
	static final String FULLQUESTIONS_FILE = s + "/fullQuestions.csv";
	static final String REALANSWERS_FILE = s + "/realAnswers.csv";
	static final String COMPLETEDQUIZZES_FILE = s + "/completedQuizzes.csv";
	static final String QUIZZESWITHUSERNAME_FILE = s + "/quizzesWithUsername.csv";

	public static void main(String[] args) {

		if (args == null) {
			System.out.print("Hello world!");
			return;
		}

		if (args[0].equals("-cleanup-all")) {
			cleanFile(USERS_FILE);
			cleanFile(QUESTIONS_FILE);
			cleanFile(ANSWERS_FILE);
			cleanFile(QUIZZES_FILE);
			cleanFile(QUIZQUESTIONS_FILE);
			cleanFile(FULLQUESTIONS_FILE);
			cleanFile(REALANSWERS_FILE);
			cleanFile(COMPLETEDQUIZZES_FILE);
			cleanFile(QUIZZESWITHUSERNAME_FILE);
			System.out.println("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");
			Question.totalQuestions = 0;
			Answer.totalAnswers = 0;
			Quizz.totalQuizzes = 0;
			User.totalUsers = 0;
			return;
		}

		if (args[0].equals("-create-user")) {
			if (args.length < 2) {
				System.out.print("{'status':'error','message':'Please provide username'}");
				return;
			}

			if (args.length < 3) {
				System.out.print("{ 'status' : 'error', 'message' : 'Please provide password'}");
				return;
			}

			if (readFromFile(USERS_FILE, false).contains(createUser(args[1], args[2], false))) {
				System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
				return;
			}

			createUser(args[1], args[2], true);
			System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
			return;
		}

		if (args.length < 3) {
			System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			return;
		}

		if (!readFromFile(USERS_FILE, false).contains(createUser(args[1], args[2], false))) {
			System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
			return;
		}

		if (args.length > 3) {
			if (args[0].equals("-create-question")) {
				if (args.length < 4 || !args[3].startsWith("-text")) {
					System.out.println("{ 'status' : 'error', 'message' : 'No question text provided'}");
					return;
				}

				List<String> results = readFromFile(QUESTIONS_FILE, false);
				for (String result : results) {
					if (result.contains(args[3].substring(7, args[3].length() - 1))) {
						System.out.println("{ 'status' : 'error', 'message' : 'Question already exists'}");
						return;
					}
				}

				if (args.length < 6) {
					System.out.println("{ 'status' : 'error', 'message' : 'No answer provided'}");
					return;
				}

				int j = 5;
				int answerIndex = 1;
				while (j < args.length) {
					if (args.length < 8) {
						System.out.println("{ 'status' : 'error', 'message' : 'Only one answer provided'}");
						return;
					}
					if (args.length > 14) {
						System.out.println("status : error, message : More than 5 answers were submitted");
						return;
					}

					if (!args[j].startsWith("-answer-" + answerIndex + " '")) {
						System.out.println("{ 'status' : 'error', 'message' : 'Answer " + answerIndex
								+ " has no answer description'}");
						return;
					}

					if (!args[j + 1].endsWith("'1'") && !args[j + 1].endsWith("'0'")) {
						System.out.println("{ 'status' : 'error', 'message' : 'Answer " + answerIndex
								+ " has no answer correct flag'}");
						return;
					}
					answerIndex++;

					for (int k = j + 2; k < args.length; k += 2) {
						if ((args[k].substring(10)).equals(args[j].substring(10))) {
							System.out.println(
									"{ 'status' : 'error', 'message' : 'Same answer provided more than once'}");
							return;
						}
						if (args[4].equals("-type 'single'")) {
							if (args[j + 1].endsWith("'1'") && args[k + 1].endsWith("'1'")) {
								System.out.println(
										"{ 'status' : 'error', 'message' : 'Single correct answer question has more than one correct answer'}");
								return;
							}
						}
					}
					List<String> answers = new ArrayList<>();
					createQuestion(args[3], args[j], args[j + 1], j < 7, answers, j < 7, args[4].substring(7, args[4].length()-1));
					j += 2;
				}
				writeToFile("\n", ANSWERS_FILE);
				System.out.println("{ 'status' : 'ok', 'message' : 'Question added successfully'}");
				return;
			}

			if (args[0].equals("-get-question-id-by-text")) {
				List<String> results = readFromFile(QUESTIONS_FILE, false);
				for (String result : results) {
					if (result.contains(args[3].substring(7, args[3].length() - 1))) {
						System.out.println("{ 'status' : 'ok', 'message' : '" + getQuestionId(args[3]) + "'}");
						return;
					}
				}
				System.out.println("{ 'status' : 'error', 'message' : 'Question does not exist'}");
				return;
			}

			if (args[0].equals("-create-quizz")) {
				if (args.length > 13) {
					System.out.println(
							"{ 'status' : 'error', 'message' : 'Quizz has more than 10 questions'}");
					return;
				}

				List<String> resultsQuiz = readFromFile(QUIZZES_FILE, false);
				for (String value : resultsQuiz) {
					if (value.contains(args[3].substring(7, args[3].length() - 1))) {
						System.out.println("{ 'status' : 'error', 'message' : 'Quizz name already exists'}");
						return;
					}
				}

				List<String> results = readFromFile(QUESTIONS_FILE, false);
				int i = 0;
				while (i < results.size()) {
					for (int k = 4; k < args.length; k++) {
						if (args.length - 4 > results.size() && !results.get(i)
								.contains(args[k].substring(13, args[k].length() - 1))) {
							System.out.println("{ 'status' : 'error', 'message' : 'Question ID for question "
									+ args[k].substring(13, args[k].length() - 1) + " does not exist'}");
							return;
						}
					}
					i++;
				}

				int j = 4;
				while (j < args.length) {
					List<String> questions = new ArrayList<>();
					createQuizz(args[3], args[j], j < 5, questions, j < 5, args[1]);
					j++;
				}
				writeWithoutIndentationToFile("\n", QUIZQUESTIONS_FILE);
				System.out.println("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");
				return;
			}

			if (args[0].equals("-get-quizz-by-name")) {
				List<String> results = readFromFile(QUIZZES_FILE, false);
				for (String result : results) {
					if (result.contains(args[3].substring(7, args[3].length() - 1))) {
						System.out.println("{ 'status' : 'ok', 'message' : '" + getQuizzId(args[3]) + "'}");
						return;
					}
				}
				System.out.println("{ 'status' : 'error', 'message' : 'Quizz does not exist'}");
				return;
			}

			if (args[0].equals("-get-quizz-details-by-id")) {
				getQuizDetailsById(args[3].substring(5, args[3].length() - 1));
				return;
			}
		}

		if (args[0].equals("-submit-quizz")) {
			if (args.length < 4) {
				System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
				return;
			}


			if (readFromFile(USERS_FILE, false).contains(args[1].substring(4, args[1].length() - 1))) {
				System.out.println(
						"{ 'status' : 'error', 'message' : 'You already submitted this quizz'}");
				return;
			}

			List<String> quizzess = readFromFile(QUIZZES_FILE, false);
			for (String value : quizzess) {
				if (!value.contains(args[3].substring(10, args[3].length() - 1))) {
					System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
					return;
				}
			}
			int j = 4;
			while (j < args.length) {
				submitQuiz(args[3], args[j], j < 5);
				j++;
			}
			return;
		}

		if (args[0].equals("-get-all-questions")) {
			getAllQuestions();
			return;
		}

		if (args[0].equals("-get-all-quizzes")) {
			getAllQuizzes();
			return;
		}

		if(args[0].equals("-delete-quizz-by-id")) {
			if (args.length < 4) {
				System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
				return;
			}

			int i = 0;
			while (i < readFromFile(QUIZZES_FILE, false).size()) {
				if (!readFromFile(QUIZZES_FILE, false).get(i).contains(args[3].substring(5, args[3].length() - 1))) {
					System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
					return;
				}
				i++;
			}

			if(!readFromFile(QUIZZESWITHUSERNAME_FILE, false).get(
					Integer.parseInt(args[3].substring(5, args[3].length() - 1))-1).contains(args[1].substring(4, args[1].length() - 1))) {
				System.out.println("{ 'status' : 'error', 'message' : 'You can only delete your own quiz'}");
				return;
			}

			deleteQuiz(args[3]);
			return;
		}

		if(args[0].equals("-get-my-solutions")) {
			return;
		}
	}
}