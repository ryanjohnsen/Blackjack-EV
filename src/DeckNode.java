public class DeckNode {
    public static int deckNodesCreated;
    public static final int DECK_LENGTH = 13;
    private CardNode[] deck;
    private int deckSize; // Number of total cards left in deck
    private int bustCount;
    private int standCount;
    private int blackjackCount;
    private double sampleSpace;
    private int eventSpace;
    private double prBust;
    private double prStandCount;
    private double prBlackjackCount;
    private boolean prevAceElevenPresent;

    public DeckNode(int[] playerCards, int dealerCard) { // Main deck
        deck = new CardNode[DECK_LENGTH];
        deckSize = 52;

        // Initializing card nodes
        for (int i = 0; i < DECK_LENGTH; i++) {
            deck[i] = new CardNode(i+1, 4, this);
        }
        // Remove cards that player has drawn
        for (int i = 0; i < playerCards.length; i++) {
            deck[playerCards[i] - 1].removeCard();
            deckSize--;
        }
        // Remove dealer card
        deck[dealerCard - 1].removeCard();
        deckSize--;

        // Handling edge case of dealer upcard being an Ace
        if (dealerCard == 1) { // Todo: if playerCards has an ace 11
            this.setPrevAceElevenPresent(true);
        }

        // Initializing probability terms
        sampleSpace = deckSize;
        eventSpace = 1;
        //System.out.println("Decksize: " + deckSize);
    }

    public DeckNode(DeckNode prevDeckNode, int prevCardName) { // Subsequent decks
        deck = new CardNode[DECK_LENGTH];
        for (int i = 0; i < DECK_LENGTH; i++) {
            deck[i] = new CardNode(i+1, prevDeckNode.getDeck()[i].getCardsLeft(), this); // this
        }
        deck[prevCardName - 1].removeCard();
        deckSize = prevDeckNode.getDeckSize() - 1;
        eventSpace = prevDeckNode.getDeck()[prevCardName - 1].getCardsLeft() * prevDeckNode.getEventSpace();
        sampleSpace = deckSize * prevDeckNode.getSampleSpace();

        if (prevDeckNode.getPrevAceElevenPresent()) {
            prevAceElevenPresent = true;
        }
//        for (int i = 0; i < DECK_LENGTH; i++) {
//            System.out.print("<" + (i+1) + ">" + ": " + deck[i].getCardsLeft() + "  ");
//        }
//        System.out.println();
        deckNodesCreated++;
    }

    public int getDeckSize() {
        return deckSize;
    }
    public void decrementDeckSize() {
        deckSize--;
    }
    public CardNode[] getDeck() {
        return deck;
    }
    public void incrementStandCount(int cardCount) {
        standCount += cardCount;
    }
    public void incrementBlackjackCount(int cardCount) {
        blackjackCount += cardCount;
    }
    public void incrementBustCount(int cardCount) {
        bustCount += cardCount;
    }
    public int getEventSpace() {
        return eventSpace;
    }
    public double getSampleSpace() {
        return sampleSpace;
    }
    public int getBustCount() {
        return bustCount;
    }
    public int getStandCount() {
        return standCount;
    }
    public int getBlackjackCount() {
        return blackjackCount;
    }
    public boolean getPrevAceElevenPresent() {
        return prevAceElevenPresent;
    }
    public void setPrevAceElevenPresent(boolean bool) {
        prevAceElevenPresent = bool;
    }
    public void setSampleSpace(double newSampleSpace) {
        sampleSpace = newSampleSpace;
    }
    public void setEventSpace(int newEventSpace) {
        eventSpace = newEventSpace;
    }
}
