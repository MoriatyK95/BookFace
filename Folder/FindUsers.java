//Author: MoriatyK95
//Description: FindUsers, graph using linkedlist representation implement Dijkstra
//Date: 10/18/2019
/*references: Dijkstra's Shortest Path Algorithm | Graph Theory youtube video by youtube william fisset,
            Java - Command Line Arguments  youtube video by  youtuber McProgamming
            previous assignments lab2 and lab3


*/


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class FindUsers {
    /*

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
     */

//create a class Graph representing a social network bookface
    static class Graph {
        //fields
        HashMap<String, Integer> idMap;
        LinkedList<Vertex> adjList;

        // constructor
        public Graph() {
            adjList = new LinkedList<>();
            idMap = new HashMap<>();
        }

        //new User
        //create a new user in bookface and give him/her an ID
        public void newUser(String name) {
            //check if User is already an existing user of BookFace
            if (idMap.containsKey(name)) {
                //user is already an existing user of BookFace
                //therefore, do nothing
                //in the case user is not an existing user of BookFace
            } else {
                Vertex newUser = new Vertex(name);
                //new user added to adjList (BookFace's list of users)
                adjList.add(newUser);
                //map user name to an ID
                idMap.put(name, adjList.indexOf(newUser));

            }

        }

        //get vertex with least shortestDist to be implemented in dijksta
        public Vertex getMinVertex(LinkedList<Vertex> adjList) {
            Vertex current = adjList.getFirst();
            Collections.sort(adjList, new Comparator<Vertex>() {
                @Override
                public int compare(Vertex o1, Vertex o2) {
                    return o1.shortestDist - o2.shortestDist;
                }
            });
            return adjList.getFirst();
        }

        //get User by entering userID
        public Vertex getUser(int ID) {
            if (idMap.containsValue(ID)) {
                return adjList.get(ID);
            } else {
                return null;
            }
        }

        public int getUserID(Vertex name) {

            if (idMap.containsKey(name)) {
                return idMap.get(name);
            } else {
                return -1;

            }

        }


        //get user by returning a vertex
        public Vertex getUser(String name) {
            if (idMap.containsKey(name)) {
                return adjList.get(idMap.get(name));
            } else {
                System.out.println("user not found in bookface's network");
                return null;
            }
        }

        //see if user exsit in bookface's network
        public boolean userExist(String name){
            if(idMap.containsKey(name)){
                return true;
            }else{
                return false;
            }
        }


        public int getUserID(String name) {
            if (idMap.containsKey(name)) {
                return idMap.get(name);
            } else {
                System.out.println(name + " is not in bookface's network");
                return -1;
            }
        }

        //return a user's connection (edges) in a linkedlist
        public LinkedList<Edge> getUserFriends(String name) {
            Vertex user = getUser(name);
            LinkedList<Edge> userFriends = user.neighbors;
            return userFriends;
        }


        //shortest dist between friend1 to friend2
        public int DistBetween(Vertex friend1, Vertex friend2) {
            int i = 0;

            Edge cur_Friend = friend1.neighbors.get(i);
            while (cur_Friend.to != friend2) {
                cur_Friend = friend1.neighbors.get(i + 1);
            }

            return cur_Friend.cost;
        }

        //get closest friend of a user (shortest cost/distance)
        public Vertex closestFriend(String name) {
            LinkedList<Edge> friends = getUserFriends(name);
            Collections.sort(friends, new Comparator<Edge>() {
                @Override
                public int compare(Edge o1, Edge o2) {
                    return o1.cost - o2.cost;
                }
            });
            return friends.getFirst().to;
        }

        public int closestFriendDist(String name) {
            LinkedList<Edge> friends = getUserFriends(name);
            Collections.sort(friends, new Comparator<Edge>() {
                @Override
                public int compare(Edge o1, Edge o2) {
                    return o1.cost - o2.cost;
                }
            });
            return friends.getFirst().cost;

        }


        //add a connection between 2 BookFace Users
        public void addConnection(String fromUser, String toUser, int distance) {
            //check to see if both User exist in the BookList's IDmap

            //case 1: both user exists in
            if (idMap.containsKey(fromUser) && idMap.containsKey(toUser)) {
                //retrieve user1 and user 2from adjList
                Vertex user1 = adjList.get(idMap.get(fromUser));
                Vertex user2 = adjList.get(idMap.get(toUser));

                //create an Friendship (edge) between user1 and user2 with a distance value
                Edge friendship = new Edge(user1, user2, distance);

                //add this friendship to user1's profile; LinkedList<Friendship> connections
                user1.neighbors.addFirst(friendship);


                //case 2: fromUser is an existing user but no toUser
                //create a toUser profile
            } else if (idMap.containsKey(fromUser) && !idMap.containsKey(toUser)) {
                Vertex user1 = adjList.get(idMap.get(fromUser));
                //create a newUser profile for user2
                Vertex user2 = new Vertex(toUser);

                //create a friendship between them
                Edge friendship = new Edge(user1, user2, distance);
                user1.neighbors.addFirst(friendship);

                //case 3: if toUser exist but not from User
            } else if (!idMap.containsKey(fromUser) && idMap.containsKey(toUser)) {
                Vertex user2 = adjList.get(idMap.get(toUser));
                //create a newUser profile for user2
                Vertex user1 = new Vertex(toUser);

                //create a friendship between them
                Edge friendship = new Edge(user1, user2, distance);
                user1.neighbors.addFirst(friendship);
                //both fromUser and toUser aren't exisin users
            } else {
                Vertex user1 = new Vertex(fromUser);
                Vertex user2 = new Vertex(fromUser);

                Edge friendship = new Edge(user1, user2, distance);
                user1.neighbors.addFirst(friendship);

            }

        }

        //find edge between 2 vertex
        public Edge getEdge(Vertex from, Vertex to) {
            LinkedList<Edge> connections = from.neighbors;
            Edge current = connections.get(0);
            for (int i = 0; i < connections.size(); i++) {
                if (current.to != to) {
                    current.to = connections.get(i).to;
                }

            }
            return current;
        }

        //print out a graph of bookFace
        public void printBookFace() {
            for (int i = 0; i < adjList.size(); i++) {
                for (int j = 0; j < adjList.get(i).neighbors.size(); j++) {
                    System.out.println(adjList.get(i).name + " is connected to " + adjList.get(i).neighbors.get(j).to.name + " at a distance of " + adjList.get(i).neighbors.get(j).cost);
                }
            }

        }
        //calculate total number of edges
        public int calcTotalConnections() {
            int count = 0;
            for (int i = 0; i < adjList.size(); i++) {
                count = count + adjList.get(i).neighbors.size();
            }
            return count;
        }

    }

    //Vertex representing a user of bookface,
    //each user has a name and a list of friendships/connections (edges) call neighbors
    static class Vertex {
        //fields
        String name;
        //each Vertex has a LinkedList of edges representing connections
        LinkedList<Edge> neighbors;
        Boolean visited;


        //shortestDist (to be used in dijkstra algo) -1 for initial value
        int shortestDist;

        //constructor
        public Vertex(String name) {
            this.name = name;
            neighbors = new LinkedList<>();
            visited = false;
            shortestDist = Integer.MAX_VALUE;
        }


        //visit the Vertex when running Dijkstra
        public void visit() {
            visited = true;
        }


        //see if vertex is visited return true or false
        public boolean isVisited() {
            if (visited == false) {
                return false;

            } else {
                return true;
            }
        }


    }

    //an Edge represents a connection or friendship between from and to
    static class Edge {
        //fields
        Vertex from;
        Vertex to;
        int cost;


        public Edge(Vertex from, Vertex to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;

        }

    }

    //helper class pair (KEY, VALUE) to be stored in priority queue
    static class Pair {
        //key representing userID
        int key;
        int value;

        //constructor
        public Pair(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }

    //create a main method where i'll implement read file
    public static void main(String[] args) throws FileNotFoundException {
        //args = new String[3];
        String sourcePerson = "";
        String targetPerson = "";
        String file = "";
        try {

            file = args[0];
            sourcePerson = args[1];
            targetPerson = args[2];
        } catch (NumberFormatException nfe) {
            System.out.println("The first argument must be a file.");
            System.exit(1);
        }

        //test for commandline argument
//        System.out.println("Filename is " + file);
//        System.out.println("sourcePerson is  " + sourcePerson);
//        System.out.println("targetPerson is " + targetPerson);


        //initialize bookface
        Graph bookFace = new Graph();
        String n = targetPerson.substring(0, 2);
        //System.out.println(targetPerson);

        try (Scanner scanner = new Scanner(new FileReader(file))) {
            scanner.useDelimiter(" ");
            //scanner.useDelimiter(System.getProperty("line.separator"));

            //read file checked
            while (scanner.hasNext()) {
                String name1 = scanner.next();
                String name2 = scanner.next();
                String dist = scanner.nextLine();

                //int dist = Integer.parseInt(scanner.next());
                //add name1 and name2 to bookFace
                bookFace.newUser(name1);
                bookFace.newUser(name2);
                //int Distance = Integer.parseInt(dist);
                //make a connection between name1 and name2
                int dis = Integer.parseInt(dist.substring(1,dist.length()));
                bookFace.addConnection(name1, name2, dis);
            }
            //if user enters a valid name, find shorted dist between source and target
            //DijkstraDIST(bookFace,bookFace.getUser(sourcePerson),bookFace.getUser(targetPerson));


            //DijkstraLT(bookFace,bookFace.getUser(sourcePerson), 8);
            String determinant = targetPerson.substring(0, 2);
            //terminal length of targetPerson
            int tlength = targetPerson.length();
            //String i = targetPerson.substring(2,3);
            //System.out.println("determiant is "+ determinant + " dist = " + i);

            if (determinant.equals("lt")) {
                int distance = Integer.parseInt(targetPerson.substring(2, tlength));
                DijkstraLT(bookFace, bookFace.getUser(sourcePerson), distance);
            } else if (determinant.equals("gt")) {
                int distance = Integer.parseInt(targetPerson.substring(2, tlength));
                DijkstraGT(bookFace, bookFace.getUser(sourcePerson), distance);
            } else if (determinant.equals("eq")) {
                int distance = Integer.parseInt(targetPerson.substring(2, tlength));
                DijkstraEQ(bookFace, bookFace.getUser(sourcePerson), distance);
                //if user enters a valid name, find shorted dist between source and target
            }else if ( bookFace.userExist(targetPerson)){
                DijkstraDIST(bookFace, bookFace.getUser(sourcePerson), bookFace.getUser(targetPerson));
            }
        }
    }

    //out vertex.names with the exact distance from source
    public static void DijkstraEQ(Graph graph, Vertex sourcePerson, int distance) {
        sourcePerson.shortestDist = 0;
        LinkedList<Vertex> path = new LinkedList<>();
        Queue<Vertex> Q = new ArrayDeque<>();
        Q.add(sourcePerson);
        while (!Q.isEmpty()) {
            Vertex vertex = Q.poll();
            path.add(vertex);
            vertex.visit();
            for (Edge e : vertex.neighbors) {
                //update all distance
                //e.to.shortestDist = e.cost;
                if (!e.to.isVisited()) {
                    e.to.visit();
                    //e.to.shortestDist = e.cost;
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                        Q.add(e.to);
                    }
                } else {
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                    }
                }
            }
        }
        int count = 0;
        for (Vertex v : path) {
            if (v.shortestDist == distance) {
                System.out.print(v.name + " ");
                count++;
            }
        }

        //System.out.println();
        if (count == 0) {
            System.out.println("None");
        }

    }


    //dijkstra that output a list of names less than distance
    public static void DijkstraLT(Graph graph, Vertex sourcePerson, int distance) {
        sourcePerson.shortestDist = 0;
        LinkedList<Vertex> path = new LinkedList<>();
        Queue<Vertex> Q = new ArrayDeque<>();
        Q.add(sourcePerson);
        while (!Q.isEmpty()) {
            Vertex vertex = Q.poll();
            path.add(vertex);
            vertex.visit();
            for (Edge e : vertex.neighbors) {
                //update all distance
                //e.to.shortestDist = e.cost;
                if (!e.to.isVisited()) {
                    e.to.visit();
                    //e.to.shortestDist = e.cost;
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                        Q.add(e.to);
                    }
                } else {
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                    }
                }
            }
        }
        int count = 0;
        for (Vertex v : path) {
            if (v.shortestDist < distance && v.name != sourcePerson.name) {
                System.out.print(v.name + " ");
                count++;
            }
        }
        //System.out.println();
        if (count == 0) {
            System.out.println("None");
        }

    }

    public static void DijkstraGT(Graph graph, Vertex sourcePerson, int distance) {
        sourcePerson.shortestDist = 0;
        LinkedList<Vertex> path = new LinkedList<>();
        Queue<Vertex> Q = new ArrayDeque<>();
        Q.add(sourcePerson);
        while (!Q.isEmpty()) {
            Vertex vertex = Q.poll();
            path.add(vertex);
            vertex.visit();
            for (Edge e : vertex.neighbors) {
                //update all distance
                //e.to.shortestDist = e.cost;
                if (!e.to.isVisited()) {
                    e.to.visit();
                    //e.to.shortestDist = e.cost;
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                        Q.add(e.to);
                    }
                } else {
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                    }
                }
            }
        }
        int count = 0;
        for (Vertex v : path) {
            if (v.shortestDist > distance && v.name != sourcePerson.name) {
                System.out.print(" " + v.name + " ");
                count++;
            }
        }
        //System.out.println();
        if (count == 0) {
            System.out.println("None");
        }

    }

    public static void DijkstraDIST(Graph graph, Vertex sourcePerson, Vertex targetPerson) {
        sourcePerson.shortestDist = 0;
        LinkedList<Vertex> path = new LinkedList<>();
        Queue<Vertex> Q = new ArrayDeque<>();
        Q.add(sourcePerson);
        while (!Q.isEmpty()) {
            Vertex vertex = Q.poll();
            path.add(vertex);
            vertex.visit();
            for (Edge e : vertex.neighbors) {
                //update all distance
                //e.to.shortestDist = e.cost;
                if (!e.to.isVisited()) {
                    e.to.visit();
                    //e.to.shortestDist = e.cost;
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                        Q.add(e.to);
                    }
                } else {
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                    }
                }
            }
        }
        int count = 0;
        for (Vertex v : path) {
            if (v.name.equals(targetPerson.name)) {
                System.out.println(v.shortestDist);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("None");
        }
    }


    public static LinkedList<Vertex> Dijkstra(Graph graph, Vertex sourcePerson) {
        sourcePerson.shortestDist = 0;
        LinkedList<Vertex> path = new LinkedList<>();
        Queue<Vertex> Q = new ArrayDeque<>();

        Q.add(sourcePerson);

        while (!Q.isEmpty()) {
            Vertex vertex = Q.poll();
            path.add(vertex);
            vertex.visit();

            for (Edge e : vertex.neighbors) {
                //update all distance
                //e.to.shortestDist = e.cost;
                if (!e.to.isVisited()) {
                    e.to.visit();
                    //e.to.shortestDist = e.cost;
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                        Q.add(e.to);
                    }
                } else {
                    int newDist = vertex.shortestDist + e.cost;
                    if (newDist < e.to.shortestDist) {
                        e.to.shortestDist = newDist;
                    }
                }
            }
        }
        for (Vertex v : path) {
            System.out.println("shortest path from Fred to " + v.name + " is " + v.shortestDist);

        }
        return path;
    }
}





