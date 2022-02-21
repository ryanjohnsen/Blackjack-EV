public class CardNode {
    private final int cardName;
    private int cardValue;
    private int cardsLeft;
    private int handTotal;
    private boolean dealerStand; // whether this card reaches 17
    private boolean blackJack; // whether this card results in blackjack
    private boolean bust; // whether this card causes a bust
    private DeckNode homeDeck; // deck that the card node belongs to
    private DeckNode nextDeck; // next deck in sequence

    public CardNode(int initCardName, int initCardsLeft, DeckNode deckOfResidence){
        cardName = initCardName;
        cardValue = Math.min(10, initCardName);
        cardsLeft = initCardsLeft;
        homeDeck = deckOfResidence;
    }

    public String toString() {
        switch(cardName) {
            case 1:
                return "A";
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            default:
                return "" + cardName;
        }
    }

    public void removeCard() {
        cardsLeft--;
    }
    public int getCardValue() {
        return cardValue;
    }
    public int getCardsLeft() {
        return cardsLeft;
    }
    public DeckNode getHomeDeck() {
        return homeDeck;
    }
    public int getCardName() {
        return cardName;
    }
    public void setNextDeck(DeckNode nd) {
        nextDeck = nd;
    }
    public void setDealerStand(boolean bool) {
        dealerStand = bool;
    }
    public void setBlackJack(boolean bool) {
        blackJack = bool;
    }
    public void setBust(boolean bool) {
        bust = true;
    }
    public DeckNode getNextDeck() {
        return nextDeck;
    }
    public void setCardValue(int newCardValue) {
        cardValue = newCardValue;
    }
    public void setHandTotal(int newHandTotal) {
        handTotal = newHandTotal;
    }
    public boolean getDealerStand() {
        return dealerStand;
    }
    public boolean getBust() {
        return bust;
    }
    public boolean getBlackjack() {
        return blackJack;
    }
    public int getHandTotal() {
        return handTotal;
    }
}
