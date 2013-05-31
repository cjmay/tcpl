Tri-Cities Programming League
=============================

Overview
--------

The tcpl package demonstrates usage of JUNG, the Java Universal Network/Graph Framework, to visualize a network.  It was written with social networks in mind but it is equally applicable to any other kind of network.

Getting Started
---------------

First you'll want to load this project in your favorite Java IDE, perhaps Eclipse.  Once that's done, open `src/SocialNetwork.java` .  At the top of the `SocialNetwork` class you'll see a `main` method that creates a new `SocialNetwork` object, calls its `load` method on a certain file, and then calls its `visualize` method.  This `main` method will be invoked when you compile and run the program.

If you try to run the program now you will see an empty window.  This is because the `load` method still needs to be filled in!  Your task is to read the graph in from the file and store it in the `SocialNetwork` object so that it can be visualized.

You may find that the provided graph file cannot easily be visualized as-is.  The real challenge is to modify your new `load` method in order to find the key patterns in this large, noisy data!  If you succeed, try evaluating your code on different datasets.

Small hints are provided in a comment inside the `load` function.

Graph File Description
----------------------

The graph file read by the `main` method should be a GZIP-compressed tab-delimited file with one edge per line.  Each line contains three fields, the names of the two nodes the edge connects and a number indicating how often that edge appeared in the original network.  Example:

    Bob	Alice	42
    Alice	Eve	3
    Bob	Eve	4

In this relatively simple example there are three nodes: Bob, Alice, and Eve.  There is an edge between Bob and Alice with weight (count) 42.  There is an edge between Alice and Eve with weight 3, and an edge between Bob and Eve with weight 4.  If these weights indicated how many times one person talked with the other, we might conclude that Bob and Alice are close friends.

Resources
---------

* JUNG (Java Universal Network/Graph Framework): http://jung.sourceforge.net/
* Gephi (Popular graph visualization tool): https://gephi.org/
