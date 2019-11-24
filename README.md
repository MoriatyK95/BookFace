# BookFace
program is FindUser

Find Users on a social network called BookFace :)
calculate the distance between 2 users connected by an weighted directional edge.

invoke the program using the command line


The program is invoked via the command line, and requires 3 arguments. It can be
    invoked one of two ways:

    a) filename.txt sourcePerson targetPerson
    b) filename.txt sourcePerson separationScore

    where filename.txt is a plain text file that designates a directed graph of BookFace
    users (see below), and sourcePerson is the name of source vertex in the graph. If the
    third argument is a person’s name, targetPerson, the program calculates the shortest
    separation distance between sourcePerson and targetPerson as determined using the
    least-cost path among the edges in the graph. If the third argument is not a person’s
    name, then it can be one of three forms, eq#, lt#, or gt#, specifying equal, less than, and
    greater than, and where the # symbol is an integer. In that case the program identifies
    all names (vertices) in the graph whose shortest distance is equal to, less than, or greater
    than the distance # from the sourcePerson.
