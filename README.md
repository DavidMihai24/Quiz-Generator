GHELESEL DAVID-MIHAI

Simple Quiz Generator

Files used and the data stored in them:

- users.csv: username and password
- questions.csv: id and the text of the questions
- fullQuestions.csv: questions name, id and type (same as for get-quiz-details)
- answers.csv: answers text, id and their truth value (true or false)
- realAnswers.csv: an auxiliary file to remove blank lines from answers.csv
- quizzes.csv: id and name of the quizzes
- completedQuizzes.csv: whether the quizzes have been completed or not
- quizQuestions.csv: id of questions added to a quiz

*The readFromFile function returns a list of strings containing each row from the various files.

Functionalities: 


Create user: I call the createUser function, which creates a new user with the appropriate name and password (without the '-u' and '-p' parameters).

Create question: I call the createQuestion function, which creates a new question and adds the answers (received as arguments from the command line) to the list of answers, on the line corresponding to the question id.

Return question id by text: I scroll through the list of questions and if the question text is present, I return the question index.

All questions in the system: return each element of the question list.

Create quiz: I keep the ids of the questions in a list that I write to the quizQuestions file (same as for the question answers).

Return quiz id: same as get-question-id

Return all quizzes: same as get-all-questions

Return quiz details by id: I iterate through the list of questions (from fullQuestions.csv) and, for each item in it, I iterate through the corresponding answer list.

Load quiz answers: first I go through the answer list to see how many right and wrong answers there are for each question and I retain them. Then I calculate the score for each question and add it to the total score.

Delete quiz: I scroll through the list of quizzes and delete the line with my quiz id.

Error cases (Login credentials are wrong, username doesn't exist etc.): I treated in main, depending on the arguments received in the command line.
