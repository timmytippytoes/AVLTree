import java.util.*;
import java.lang.*;

public class main{
   public static void main(String[] args){
      //creates AVLNode roots
      AVLNode tree1 = new AVLNode();
      AVLNode tree2 = new AVLNode();
      
      Scanner in = new Scanner(System.in);//Scanner for "Enter" requirement
      thing thingy;//generic variable to hold the value that is to be removed, 
      //and to make it easier to convert to integer for the output below
      
      int[] numbers = new int[5000];//establishes an array of ints with a size of 5000
      Random rand = new Random();
      for(int i = 0; i < numbers.length; i++){//intitalizes the "numbers" array then inserts the newly created number into the AVL tree
         numbers[i] = rand.nextInt(4098);
         tree1 = tree1.insert(tree1,new thing (numbers[i]));
      }
      while(true){//forever loop, per the instructions  
         while(tree1 != null){//continues until tree1 is empty
            thingy = new thing(tree1.getVal(tree1.findMin(tree1)));//thingy is to be used for integer conversion
            System.out.printf("The process with a priority of %d is now scheduled to run!\n", thingy.getIntVal());//convert generic to integer, per the guidelines
            tree1 = tree1.remove(tree1, tree1.getVal(tree1.findMin(tree1)));//reduces the size of tree1
            tree2 = tree2.insert(tree2, new  thing(rand.nextInt(4098)));//this inserts a new random value into tree2
            System.out.printf("The process with a priority of %d has run out of its timeslice!\n", thingy.getIntVal());//output per the guidelines
         } 
         System.out.println("Every process has got a chance to run; Please press \"Enter\" to start the next round! ");//output per the guidelines
         in.nextLine();//pauses the program until user input is placed in
         tree1 = tree2;//tree2 is copied into tree1
         tree2 = new AVLNode();//tree2 is reset to an empty tree
      }
   }
}

class thing<E extends Comparable<E>> implements Comparable<thing<E>>{//generic to implement comparable for all types
      private E e;//stores our value
      
      public thing(E x){//constructor
         e = x;
      }
      public E getE(){//returns e, no other purpose but to cover all my bases
         return e;
      }
      
      public thing getThing(){//returns the current 'thing' being accessed
         return this;
      }
      
      public int getIntVal(){// used to fit the requested format
         return Integer.parseInt(toString());//parses a the toString method and converts it to an integer
      }
      
      public int compareTo(thing<E> i){//implementation
         return getE().compareTo(i.getE());//used to compare the two generics
      }
      
      public String toString(){//used in the case of a toString() call
         return  "" + e;
      }
}

class AVLNode<E extends Comparable<E>>{
   private thing val;//holds data
   private AVLNode left;//left child
   private AVLNode right;//right child
   
   //constructors to cover all posibilities
   
   public AVLNode(){//creates an empty node
      left = null;
      right = null;
      val = null;
   }
   
   public AVLNode(thing<E> x){//creates node with only a value no children
      left = null;
      right = null;      
      val = x;
   }
   
   public AVLNode(AVLNode l, AVLNode r, thing<E> x){//creates a node with two children
      left = l;
      right = r;
      val = x;
   }
   
   public AVLNode(AVLNode t, thing<E> x){//if only one child node is sent in
      if (t.val.compareTo(x) < 0)
         left = t;
      else if (t.val.compareTo(x) < 0)
         right = t;
   }
   
   //end constructors
   
   //public methods
   
   public AVLNode insert(AVLNode t, thing<E> x){//intserts nodes into the AVL tree
      if (t == null || (t.left == null && t.right == null && t.val == null)){// if the node is empty or if the node is empty, then create a new node
         return new AVLNode(null,null, x);
      } 
      if (x.compareTo(t.val) > 0){//tests if x > the node's value
         t.right = insert(t.right, x);//recursive call to insert to find an empty child in the proper location
         if (height(t.right) - height(t.left) == 2)//tests for an AVL tree violation
            if(x.compareTo(t.right.val) > 0)//if right right nodes then rotate left to correct
               t = rotateLeft(t);
            else//else we know that there is a left right violation therefore the violating node is rotated right then left
               t = doubleRotateRight(t);
      }
      else if (x.compareTo(t.val) < 0){
         t.left = insert(t.left, x);//recursive call to insert to find an empty child in the proper location
         
         if (height(t.left) - height(t.right) == 2)//if right right nodes then rotate right to correct
            if(x.compareTo(t.left.val) < 0)
               t = rotateRight(t);
            else//else we know that there is a right left violation therefore the violating node is rotated left then right
               t = doubleRotateLeft(t);
      }
      else{//if the value already exists in the tree then return the tree back to the caller
         return t;
      }
      return t;//java forces me to have this here
   }
   
   public AVLNode remove (AVLNode t, thing<E> x){//removes nodes from an AVL tree
    //  AVLNode t2;//
      if(t == null)//returns null if not found
         return null;
      else if (x.compareTo(t.val) < 0){//if x < the value passed in 
         t.left = remove(t.left, x);// then set the left child = to the recursive call of remove
      }
      else if (x.compareTo(t.val) > 0){//if x < the value passed in
         t.right = remove(t.right, x);// then set the right child = to the recursive call of remove
      }
      else if(t.left != null && t.right != null){//if the value is found then check to see if there are children trees
         t.val = findMin(t.right).val;//copies the minimum value on the right side child tree
         t.right = remove(t.right, t.val);//this will find the copied value and remove it 
      }
      else//if the value is found and the one child is null
         if(t.left != null)//if the left child contains data then return it's left child to the caller, this removes the node
            return t.left;
         else//if the right child contains data then return it's right child to the caller, this removes the node
            return t.right;
      if (height(t.left) - height(t.right) == 2)//checks for violations and does the same as insert's checks
            if(t.val.compareTo(t.left.val) > 0)
               t = rotateRight(t);  
            else
               t = doubleRotateLeft(t);
      else if (height(t.right) - height(t.left) == 2)//checks for violations and does the same as insert's checks
            if(t.val.compareTo(t.right.val) < 0)
               t = rotateLeft(t);
            else
               t = doubleRotateRight(t);    
      return t;//java forces this 
   }
   
   public AVLNode findMin(AVLNode t){//finds the minimum value in a tree
      if (t == null)//if the tree is empty return null
         return null;
      if (t.val.compareTo(new thing (0)) == 0)//if found return the smallest value node I don't need this but since 0 is the lowest this tree will go it made sense to do
         return t;
      else if (t.left == null)//if the left child empty then return the root
         return t;
      else{
         return findMin(t.left);//recursive call to findMin() that continues down until the smallest value is found
      }   
   }
   
   public int max(int a, int b){//finds the largest of two values
      if (a > b)
         return a;
      else
         return b;
   }
   
   public int height(AVLNode t){//finds the height of each child
      int h1;
      int h2;
      if (t == null)//if the passed in node is null then it has a value of -1
         return -1;
      else{
         h1 = height(t.left);//recursive call of height, this will find the left child's height
         h2 = height(t.right);//recursive call of height, this will find the right child's height
         return max(h1,h2)+1;//accumulates as the recursive call acsends the tree after reaching a bottom
      }
   } 
   
   public AVLNode rotateRight(AVLNode t1){//rotates a tree to the Right to correct right violations
      AVLNode t2 = t1.left;//new node to hold the left of the the node sent into the method
      t1.left = t2.right;//this overwrites the passed in node with the right child of t2
      t2.right = t1;//this set the node passed in, to the right side of t2
      return t2;//the new node, t2, is returned
   }
   
   public AVLNode rotateLeft(AVLNode t1){//this is the exact mirror of rotateRight
      AVLNode t2 = t1.right;
      t1.right = t2.left;
      t2.left = t1;
      return t2;
   }
   
   public AVLNode doubleRotateLeft(AVLNode t3){//this method corrects left right violations
      AVLNode t = t3.left;//a new node that is set to the left side of the node passed in
      t3.left = rotateLeft(t);//the new node is rotated left and then assigned to the left child of the node passed in
      return rotateRight(t3);//the node passed into the function is then rotated right
   }
   
   public AVLNode doubleRotateRight(AVLNode t3){//this is an exact mirror of doubleRotateLeft
      AVLNode t = t3.right;
      t3.right = rotateRight(t);
      return rotateLeft(t3);
   }      
   
   public thing getVal(AVLNode t){//accessor moethod to access the value held within the node
      return t.val.getThing();
   }
   
   
   
   /*
         NOTE: The following helper methods are for practice and fun.
               Most of them are to print out a small tree in a nice neat format.
   */
   
   
   
   public String toString(){//for calls toString()
      return toString(this)+"";//this will print out the entire tree in a pyramid format
   }
   
   
   
   public String toString(AVLNode t){//prints the tree in a pyramid format, too complicated for large trees
      Queue<AVLNode> q = new LinkedList<>();
      Queue<AVLNode> q2 = new LinkedList<>();
      q.add(t);
      AVLNode cur;
      String s = "";
      for (int i = 0; i < height(t)+1; i++){
         cur = q.peek();
         q2 = queuer(q);
         s += treePrinter(q, cur);
         cur = q2.peek();
         q = queuer(q2);
         s += treePrinter(q2, cur);
      }
      
      return s;
   }
   
   public String treePrinter(Queue<AVLNode> q, AVLNode cur){//this method prints out a tree line by line in the desired format.  Good for small trees, terrible for large ones
      int totalHeight = height(this)+1;
      int level = height(this)-height(cur);
      int largestTree = largestTreeFinder(q);
      int size = q.size();
      int temp = 0;
      boolean[][] links = new boolean[size][2];
      int counter = 0;
      String s = "";
      for (int i = 0; i < Math.pow(2,largestTree) - 1 ; i++){
         s += " ";
      }

      while(!q.isEmpty()){
         s += q.remove().val;
         for (int i = 0; i < Math.pow(2,largestTree)-1 ; i++){
            s += " ";
         }
         s += " ";
         for (int i = 0; i < Math.pow(2,largestTree) -1; i++){
               s += " ";
         }
         
      }

      if(cur !=null)
         s += "\n";
      return s;
   }
   
   public int largestTreeFinder(Queue<AVLNode> q){//finds the largest tree within a queue of AVLNodes and returns an int height value
      int temp1 = 0;
      int largest = height(q.peek());
      if (q.size() > 1){
         for(int i = 0; i < q.size();i++){
            temp1 = height(q.peek());
            q.add(q.remove());
            if(temp1 > largest)
               largest = temp1;
         }
      }
      if (largest == -1)
         return 0;
      else
         return largest;
   }
  
   public Queue<AVLNode> queuer(Queue<AVLNode> q){//takes a queue of AVLNodes and places each nodes children into a new node
      Queue<AVLNode> q2 = new LinkedList<>();
      thing e = new thing(" ");
      thing e2 = new thing(" ");
      for(int i = 0; i< q.size();i++){
         if(q.peek().left != null)
            q2.add(q.peek().left);
         else if (q.peek().left == null)
            q2.add(new AVLNode(null, null, e));
         if(q.peek().right != null)
            q2.add(q.peek().right);
         else if (q.peek().right == null)
            q2.add(new AVLNode(null, null, e2));
         q.add(q.remove());       
      }
      return q2;
   }

 
   public int heightDifference(AVLNode t){//finds the difference in childern heights
      if(t == null)
         return 0;
      return height(t.left) - height(t.right);
   }



   public AVLNode find(AVLNode t, thing<E> x){//finds a node with a given value
      if (t == null)
         return null;
      if(t.val.compareTo(x) == 0)
         return t;
      else if(t.val.compareTo(x) > 0)
         return find(t.left, x);
      else 
         return find(t.right, x);
   }
   
   /*end of superfluous practice code*/
}
