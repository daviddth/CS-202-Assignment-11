/*David Hoang - CSC 202 - November 20, 2017 - Programming Ex. 28.5 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BFS_Abstractgraph {


    static class UnweightedGraph<V> extends AbstractGraph<V> {
  
        public UnweightedGraph() {
        }

       
        public UnweightedGraph(int[][] tips, V[] names) {
            super(tips, names);
        }

    
        public UnweightedGraph(List<Edge> tips, List<V> names) {
            super(tips, names);
        }

        
        public UnweightedGraph(List<Edge> tips, int numberOfNames) {
            super(tips, numberOfNames);
        }

       
        public UnweightedGraph(int[][] tips, int numberOfNames) {
            super(tips, numberOfNames);
        }
    }

    static abstract class AbstractGraph<V> implements Graph<V> {
        protected List<V> names = new ArrayList<V>();  
        protected List<List<Integer>> neighbors = new ArrayList<List<Integer>>();  
     

  
        protected AbstractGraph() {
        }

       
        protected AbstractGraph(int[][] tips, V[] names) {
            for (int i = 0; i < names.length; i++)
                this.names.add(names[i]);

            createAdjacencyLists(tips, names.length);
        }

  
        protected AbstractGraph(List<Edge> tips, List<V> names) {
            for (int i = 0; i < names.size(); i++)
                this.names.add(names.get(i));

            createAdjacencyLists(tips, names.size());
        }

  
        @SuppressWarnings("unchecked")
        protected AbstractGraph(List<Edge> tips, int numberOfNames) {
            for (int i = 0; i < numberOfNames; i++)
                names.add((V) (new Integer(i)));  

            createAdjacencyLists(tips, numberOfNames);
        }

       
        @SuppressWarnings("unchecked")
        protected AbstractGraph(int[][] tips, int numberOfNames) {
            for (int i = 0; i < numberOfNames; i++)
                names.add((V) (new Integer(i)));  

            createAdjacencyLists(tips, numberOfNames);
        }

 
        private void createAdjacencyLists(int[][] tips, int numberOfNames) {
 
            for (int i = 0; i < numberOfNames; i++) {
                neighbors.add(new ArrayList<Integer>());
            }

            for (int i = 0; i < tips.length; i++) {
                int u = tips[i][0];
                int v = tips[i][1];
                neighbors.get(u).add(v);
            }
        }

 
        private void createAdjacencyLists(List<Edge> tips, int numberOfNames) {
 
            for (int i = 0; i < numberOfNames; i++) {
                neighbors.add(new ArrayList<Integer>());
            }

            for (Edge edge : tips) {
                neighbors.get(edge.u).add(edge.v);
            }
        }

        @Override
       
        public int getSize() {
            return names.size();
        }

        @Override
 
        public List<V> getVertices() {
            return names;
        }

        @Override
       
        public V getVertex(int index) {
            return names.get(index);
        }

        @Override
       
        public int getIndex(V v) {
            return names.indexOf(v);
        }

        @Override
      
        public List<Integer> getNeighbors(int index) {
            return neighbors.get(index);
        }

        @Override
       
        public int getDegree(int v) {
            return neighbors.get(v).size();
        }

        @Override
   
        public void printTips() {
            for (int u = 0; u < neighbors.size(); u++) {
                System.out.print(getVertex(u) + " (" + u + "): ");
                for (int j = 0; j < neighbors.get(u).size(); j++) {
                    System.out.print("(" + u + ", " + neighbors.get(u).get(j)
                            + ") ");
                }
                System.out.println();
            }
        }

        @Override
        /** Clear graph */
        public void clear() {
            names.clear();
            neighbors.clear();
        }

        @Override

        public void addVertex(V vertex) {
            names.add(vertex);
            neighbors.add(new ArrayList<Integer>());
        }

        @Override

        public void addEdge(int u, int v) {
            neighbors.get(u).add(v);
            neighbors.get(v).add(u);
        }


        public static class Edge {
            public int u;
            public int v;


            public Edge(int u, int v) {
                this.u = u;
                this.v = v;
            }
        }


        @Override

        public Tree dfs(int v) {
            List<Integer> searchOrder = new ArrayList<Integer>();
            int[] parent = new int[names.size()];
            for (int i = 0; i < parent.length; i++)
                parent[i] = -1;


            boolean[] isVisited = new boolean[names.size()];


            dfs(v, parent, searchOrder, isVisited);


            return new Tree(v, parent, searchOrder);
        }


        private void dfs(int v, int[] parent, List<Integer> searchOrder,
                         boolean[] isVisited) {

            searchOrder.add(v);
            isVisited[v] = true;

            for (int i : neighbors.get(v)) {
                if (!isVisited[i]) {
                    parent[i] = v;
                    dfs(i, parent, searchOrder, isVisited);
                }
            }
        }


        public List<Integer> getPath(int u, int v) {
            Tree tree = bfs(u);
            ArrayList<Integer> path = new ArrayList<>();

            do {
                path.add(v);
                v = tree.parent[v];
            } while (v != -1);

            Collections.reverse(path);
            return path;
        }


        @Override

        public Tree bfs(int v) {
            List<Integer> searchOrder = new ArrayList<Integer>();
            int[] parent = new int[names.size()];
            for (int i = 0; i < parent.length; i++)
                parent[i] = -1;

            java.util.LinkedList<Integer> queue = new java.util.LinkedList<Integer>();
            boolean[] isVisited = new boolean[names.size()];
            queue.offer(v);
            isVisited[v] = true;

            while (!queue.isEmpty()) {
                int u = queue.poll();
                searchOrder.add(u);
                for (int w : neighbors.get(u)) {
                    if (!isVisited[w]) {
                        queue.offer(w);
                        parent[w] = u;
                        isVisited[w] = true;
                    }
                }
            }
            return new Tree(v, parent, searchOrder);
        }


        public class Tree {
            private int root;
            private int[] parent;
            private List<Integer> searchOrder;

            public Tree(int root, int[] parent, List<Integer> searchOrder) {
                this.root = root;
                this.parent = parent;
                this.searchOrder = searchOrder;
            }

            public int getRoot() {
                return root;
            }

            public int getParent(int v) {
                return parent[v];
            }


            public List<Integer> getSearchOrder() {
                return searchOrder;
            }

            public int getNumberOfVerticesFound() {
                return searchOrder.size();
            }


            public List<V> getPath(int index) {
                ArrayList<V> path = new ArrayList<V>();

                do {
                    path.add(names.get(index));
                    index = parent[index];
                } while (index != -1);

                return path;
            }


            public void printPath(int index) {
                List<V> path = getPath(index);
                System.out.print("A path from " + names.get(root) + " to "
                        + names.get(index) + ": ");
                for (int i = path.size() - 1; i >= 0; i--)
                    System.out.print(path.get(i) + " ");
            }


            public void printTree() {
                System.out.println("Root is: " + names.get(root));
                System.out.print("Edges: ");
                for (int i = 0; i < parent.length; i++) {
                    if (parent[i] != -1) {

                        System.out.print("(" + names.get(parent[i]) + ", "
                                + names.get(i) + ") ");
                    }
                }
                System.out.println();
            }
        }
    }

    interface Graph<V> {

        public int getSize();

        public java.util.List<V> getVertices();

        public V getVertex(int index);


        public int getIndex(V v);


        public java.util.List<Integer> getNeighbors(int index);


        public int getDegree(int v);


        public void printTips();


        public void clear();


        public void addVertex(V vertex);


        public void addEdge(int u, int v);

        public AbstractGraph<V>.Tree dfs(int v);


        public AbstractGraph<V>.Tree bfs(int v);
    }

    public static void main(String[] args) {
        String[] names = { "David", "Billy", "Ricardo", " Billard", "Tanes", "Jonathan", "Ethan", "John", "Pauline", "Nancy", "Emma", "Richard" };

        int[][] tips = {{0, 1}, {0, 3}, {0, 5}, {1, 0}, {1, 2}, {1, 3}, {2, 1}, {2, 3}, {2, 4}, {2, 10}, {3, 0}, {3, 1}, {3, 2}, {3, 4}, {3, 5}, {4, 2}, {4, 3}, {4, 5}, {4, 7}, {4, 8}, {4, 10}, {5, 0}, {5, 3}, {5, 4}, {5, 6}, {5, 7}, {6, 5}, {6, 7}, {7, 4}, {7, 5}, {7, 6}, {7, 8}, {8, 4}, {8, 7}, {8, 9}, {8, 10}, {8, 11}, {9, 8}, {9, 11}, {10, 2}, {10, 4}, {10, 8}, {10, 11}, {11, 8}, {11, 9}, {11, 10}};

        UnweightedGraph<String> graph1 = new UnweightedGraph<String>(tips, names);

        System.out.println(graph1.getPath(0, 11));
        System.out.println(graph1.getPath(11, 0));
    }

}