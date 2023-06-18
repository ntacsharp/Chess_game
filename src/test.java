import java.util.ArrayList;
import java.util.List;

import com.chess.engine.minigame.cards.Deck;
import com.chess.engine.minigame.pieces.player.Babarian;

public class test {

   public static class B {
      private int m, n;

      B(int m, int n) {
         this.m = m;
         this.n = n;
      }

      public int getM() {
         return m;
      }

      public void setM(int m) {
         this.m = m;
      }

      public int getN() {
         return n;
      }

      public void setN(int n) {
         this.n = n;
      }

   }

   public static class A {
      private int x, y;
      private B myB;

      A(int x, int y, B myB) {
         this.x = x;
         this.y = y;
         this.myB = myB;
      }

      public int getX() {
         return x;
      }

      public void setX(int x) {
         this.x = x;
      }

      public int getY() {
         return y;
      }

      public void setY(int y) {
         this.y = y;
      }

      public B getMyB() {
         return myB;
      }

      public void setMyB(B myB) {
         this.myB = myB;
      }
   }

   public static void main(String[] args) {
      // test demo = new test();
      // demo.showCardLayoutDemo();
      // Deck deck = new Deck(new Babarian(4, 2));
      // deck.fillHand(3);
      // deck.handToString();
      List<A> aList = new ArrayList<A>();
      aList.add(new A(1, 2, new B(3, 4)));
      aList.add(new A(5, 6, new B(7, 8)));
      aList.add(new A(9, 10, new B(11, 12)));
      List<B> bList = new ArrayList<B>();
      for (A a : aList) {
         bList.add(a.getMyB());
      }
      B testB = bList.get(1);
      testB.setM(100);
      testB.setN(101);
      for (A a : aList) {
         System.out.println(a.getX() + " " + a.getY() + " " + a.getMyB().getM() + " " + a.getMyB().getN() + " "+ a.getClass());
      }
   }
}