Generator simplu de chestionare

Fisiere utilizate si datele pe care le-am stocat in fiecare:

users.csv: numele si parola utilizatorilor

questions.csv: id-ul si textul intrebarilor

fullQuestions.csv: numele intrebarilor, id-ul si tipul (la fel ca pentru get-quiz-details)

answers.csv: textul raspunsurilor, id-ul si valoarea lor de adevar

realAnswers.csv: un fisier auxiliar cu ajutorul caruia elimin liniile goale din answers.csv

quizzes.csv: id-ul si numele chestionarelor

completedQuizzes.csv: daca chestionarele au fost completate sau nu

quizQuestions.csv: id-ul intrebarilor adaugate la un quiz

\*Functia readFromFile intoarce o lista de String-uri ce contine fiecare rand din diversele fisiere.

Creare utilizator: apelez functia createUser, care creeaza un nou utilizator cu numele si parola corespunzatoare (fara parametrii '-u' si '-p').

Creare intrebare: apelez functia createQuestion, care creeaza o noua intrebare si adauga raspunsurile (primite ca argumente din linia de comanda)
in lista de raspunsuri, pe linia corespunzatoare id-ului intrebarii.

Intoarce id-ul intrebarii dupa text: iterez prin lista de intrebari si daca textul intrebarii este prezent, intorc index-ul intrebarii.

Toate intrebarile din sistem: intorc fiecare element al listei de intrebari.

Creare chestionar: retin id-urile intrebarilor intr-o lista pe care o scriu in fisierul quizQuestions (la fel ca pentru raspunsurile intrebarilor)

Intoarce id-ul quiz-ului: La fel ca la get-question-id

Intoarce toate quiz-urile: la fel ca la get-all-questions

Intoarce detaliile quiz-ului in functie de id: iterez prin lista de intrebari (din fullQuestions.csv) si, pentru fiecare element din ea, iterez
prin lista de raspunsuri corespunzatoare.

Incarca raspunsuri chestionar: mai intai parcurg lista de raspunsuri pentru a vedea cate raspunsuri corecte si gresite sunt la fiecare intrebare
si le retin. Apoi calculez punctajul pentru fiecare intrebare si il adaug la scorul total.

Stergere quiz: iterez prin lista de quiz-uri si sterg linia cu id-ul quiz-ului meu.

Cazurile de erori (Login credentials are wrong, username doesn't exist etc.) le-am tratat in main, in functie de argumentele primite in linia de
comanda.
