# Mini-Watson

This is my attempt to create a replica of this, since the answers to many of the Jeopardy questions are actually titles of Wikipedia pages,  the task reduces to the classification of Wikipedia pages, that is, finding which page is the most likely answer to the given clue. 
It is an information retrieval project, I researched some of the technologies I could use to index the 300,000 Wikipedia pages that were provided to us and I thought of using Lucene. I made sure that each Wikipedia page appears as a separate document in the index. 
I found that many documents consisted of the tpl tags. Also, there were a lot of URLs(ref tags) inside the documents, which I wasn’t sure how to deal with so for the time being I have just discarded it.
I then used the precision at one for measuring the performance because in such competitions the first answer is your final answer, to improve the ranking because Lucene uses a default vector tf-idf scoring function, 
I used various other scoring function to get a better performance. Another thing after talking to the professor, I noticed was that my index was currently a bag of words so to improve that I implemented a positional index instead of a bag of words; you could use a parser to extract syntactic dependencies and index these dependencies.
Doing so increased the accuracy of my IR system.

# How to run
To run this project, clone the repo and just run the bestTest.java in the test folder and it should run the entire program, it should print all the questions alon with their results and their accuracies.

