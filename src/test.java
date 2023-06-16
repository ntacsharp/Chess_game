import com.chess.engine.minigame.cards.Deck;
import com.chess.engine.minigame.pieces.player.Babarian;

public class test {

   public static void main(String[] args) {
      // test demo = new test();
      // demo.showCardLayoutDemo();
      Deck deck = new Deck(new Babarian(4, 2));
      deck.fillHand(3);
      deck.handToString();
   }
}