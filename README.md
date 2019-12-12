# CSC-365-

*This program can be ran on any PC, as long as all of the files remain in the same folder*

  This program initilazes with the Wikipedia page https://en.wikipedia.org/wiki/Music and scrapes the words into a custom 
HashTable with a cache for each Wiki page linked to it. The user can then enter a URl into any webpage and a cosine 
similarity will be done to recommend the top 10 wikipedia pages within the database! This program creates a B-Tree which was
persistent in the last assignment, but has been thoroughly tested with Wiki pages. I decided to store the entire
parser persistantly in order to speed up load time as well, and a graph has been implemented for traversal of directly 
linked pages using Dijkstras shortest path algorithm. This algorithm is used for finding the shortest path between two 
Wiki pages typed in by the user(As long as those pages are in the database).

-Andrew Boyce


