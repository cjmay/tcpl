Tri-Cities Programming League
=============================

Overview
--------

The tcpl package demonstrates usage of JUNG, the Java Universal Network/Graph Framework, to visualize a network.  It was written with social networks in mind but it is equally applicable to any other kind of network.

Getting Started
---------------

First you'll want to load this project in your favorite Java IDE, perhaps Eclipse.  Once that's done, open `src/SocialNetwork.java` .  At the top of the `SocialNetwork` class you'll see a `main` method that creates a new `SocialNetwork` object, calls its `load` method on a certain file, and then calls its `visualize` method.  This `main` method will be invoked when you compile and run the program.

If you try to run the program now you will see an empty window.  This is because the `load` method still needs to be filled in!  Your task is to read the graph in from the file and store it in the `SocialNetwork` object so that it can be visualized.  A small hint is provided in a comment inside the `load` function.

Graph File Description
----------------------

The graph file read by the `main` method should be a GZIP-compressed tab-delimited file with one edge per line.  Each line contains three fields separated by tabs, the names of the two nodes the edge connects and a number indicating how often that edge appeared in the original network.  Example:

    bob	alice	42
    alice	eve	3
    bob	eve	4

In this relatively simple example there are three nodes: bob, alice, and eve.  There is an edge between bob and alice with weight (count) 42.  There is an edge between alice and eve with weight 3, and an edge between bob and eve with weight 4.  If these weights indicated how many times one person talked with the other, we might conclude that bob and alice are close friends.

Resources
---------

JUNG: http://jung.sourceforge.net/
