import java.util.Scanner;

public class Main {
    // Dealer probabilities
    private static double dealerPrBust;
    private static double dealerPrStand;
    private static double dealerPrBlackjack;

    // Probabilities given that the player stands
    private static double prDealerBeatPlayerStand;
    private static double prPlayerBeatDealerStand;
    private static double prPushStand;

    // Probabilities given that the player hits
    private static double prPlayerBeatDealerHit;
    private static double prDealerBeatPlayerHit;
    private static double prPushHit;
    private static double prDealerBustHit;
    private static double prPlayerBustHit;

    private static int playerHandTotal;

    public static void main(String[] args) {

        int[] playerUpcards = new int[]{8,10};
        for (int i = 0; i < playerUpcards.length; i++) {
            playerHandTotal += playerUpcards[i];
        }
        CardNode dc = generateStandOutcomes(10, playerUpcards);
        generateHitOutcomes(10, playerUpcards);

        // Todo: Calculate EV/Probabilities of Standing

        int dealerHandTotal;
        int dealerWinCount;
        int pushCount;
        int playerWinCount;


        /*

        // P(Win | Stand)
            if player total < 17:
                calculate dealer outcomes:
                    P(dealerBust)
                    vs.
                    P(dealerBlackjack) + P(dealerStand)
            else if 17 <= player total <= 21:
                // Dealer win
                P(dealerTotal > playerTotal)
                // Push
                P(dealerTotal == playerTotal)
                // Player win
                P(playerTotal > dealerTotal)
                P(dealerBust)

        // P(Win | Hit)
            // Dealer win
            P(playerBust)
            P(dealerTotal > newPlayerTotal)
            // Push
            P(dealerTotal == newPlayerTotal)
            // Player win
            P(dealerBust)
            P(dealerTotal > newPlayerTotal

        */

    }

    public static void generateDealerOutcomes(CardNode cn, int handTotal) {
        int newHandTotal;
        boolean nextPrevAceEleven = cn.getHomeDeck().getPrevAceElevenPresent();

        if (cn.getCardsLeft() == 0) {
            return;
        }

        // Determining new hand total
        if (cn.getCardName() == 1) { // Special case of Ace
            if (handTotal > 10) {
                newHandTotal = handTotal + 1;
            } else {
                newHandTotal = handTotal + 11;
                // Change prevAceEleven to true for next deck
                nextPrevAceEleven = true;
            }
        } else {
            newHandTotal = handTotal + cn.getCardValue();
        }

        // Changing previous Ace value from 11 to 1 if current card causes bust
        if (cn.getHomeDeck().getPrevAceElevenPresent() && newHandTotal > 21) {
            newHandTotal -= 10;
            // Change prevAceEleven to false for next deck
            nextPrevAceEleven = false;
        }

        cn.setHandTotal(newHandTotal);

        // Updating card and creating new deck if needed
        if (newHandTotal < 17) {
            DeckNode nextDeck = new DeckNode(cn.getHomeDeck(), cn.getCardName());
            cn.setNextDeck(nextDeck);
            nextDeck.setPrevAceElevenPresent(nextPrevAceEleven);
            for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
                generateDealerOutcomes(nextDeck.getDeck()[i], newHandTotal);
            }
        } else if (newHandTotal < 21) {
            cn.setDealerStand(true);
            cn.getHomeDeck().incrementStandCount(cn.getCardsLeft());
        } else if (newHandTotal == 21) {
            cn.setBlackJack(true);
            cn.getHomeDeck().incrementBlackjackCount(cn.getCardsLeft());
        } else { // Bust
            cn.setBust(true);
            cn.getHomeDeck().incrementBustCount(cn.getCardsLeft());
        }
    }

    public static CardNode generateStandOutcomes(int dealerUpcardName, int[] playerCardNames) {
        DeckNode mainDeck = new DeckNode(playerCardNames, dealerUpcardName);
        CardNode dealerCard = new CardNode(dealerUpcardName, 4, null);
        dealerCard.setNextDeck(mainDeck);

        // Edge case of dealer Ace upcard
        if (dealerUpcardName == 1) {
            dealerCard.setCardValue(11);
        }

        // Generating dealer outcomes
        for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
            generateDealerOutcomes(mainDeck.getDeck()[i], dealerCard.getCardValue());
        }

        calculateDealerProbabilities(mainDeck);

        // Generating probabilities if player stands
        for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
            calculateStandProbabilities(mainDeck.getDeck()[i]);
        }

        System.out.println("Total Decknodes: " + DeckNode.deckNodesCreated);
        System.out.println("Dealer Bust: " + dealerPrBust);
        System.out.println("Dealer Stand: " + dealerPrStand);
        System.out.println("Dealer Blackjack: " + dealerPrBlackjack);
        System.out.println("Total probability: " + (dealerPrBust + dealerPrStand + dealerPrBlackjack));

        System.out.println("Dealer beat player (Stand): " + prDealerBeatPlayerStand);
        System.out.println("Player beat dealer (Stand): " + prPlayerBeatDealerStand);
        System.out.println("Push (Stand): " + prPushStand);
        System.out.println("Expected Value of Stand: " + (dealerPrBust - prDealerBeatPlayerStand + prPlayerBeatDealerStand));
        return dealerCard;
    }

    public static void calculateDealerProbabilities(DeckNode dn) {
        dealerPrBust += (dn.getEventSpace() * dn.getBustCount()) / dn.getSampleSpace();
        dealerPrStand += (dn.getEventSpace() * dn.getStandCount()) / dn.getSampleSpace();
        dealerPrBlackjack += (dn.getEventSpace() * dn.getBlackjackCount()) / dn.getSampleSpace();

        for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
            if (dn.getDeck()[i].getNextDeck() != null) {
                calculateDealerProbabilities(dn.getDeck()[i].getNextDeck());
            }
        }
    }

    public static void calculateStandProbabilities(CardNode cn) {
        if (cn.getDealerStand() || cn.getBlackjack()) {
            // Compare player total to dealer total
            if (playerHandTotal > cn.getHandTotal()) {
                prPlayerBeatDealerStand += (cn.getCardsLeft() * cn.getHomeDeck().getEventSpace()) / cn.getHomeDeck().getSampleSpace();
            }
            else if (playerHandTotal == cn.getHandTotal()) {
                prPushStand += (cn.getCardsLeft() * cn.getHomeDeck().getEventSpace()) / cn.getHomeDeck().getSampleSpace();
            }
            else {
                prDealerBeatPlayerStand += (cn.getCardsLeft() * cn.getHomeDeck().getEventSpace()) / cn.getHomeDeck().getSampleSpace();
            }
        }
        // Go to next deck if possible
        else if (!cn.getBust() && cn.getNextDeck() != null) {
            for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
                calculateStandProbabilities(cn.getNextDeck().getDeck()[i]);
            }
        }
    }

    public static void generateHitOutcomes(int dealerUpcard, int[] playerCardNames) {
        double mainSampleSpace = 52 - (1 + playerCardNames.length);
        int bustCount = 0;

        for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
            DeckNode mainDeck = new DeckNode(playerCardNames, dealerUpcard);
            int cardValue = Math.min(i + 1, 10); // Todo: Currently assume ace will be 1

            if (playerHandTotal + cardValue > 21) {
                bustCount += mainDeck.getDeck()[i].getCardsLeft();
            }
            else if (playerHandTotal + cardValue < 17) {
                // Modify main deck
                mainDeck.setEventSpace(mainDeck.getDeck()[i].getCardsLeft());
                mainDeck.getDeck()[i].removeCard();
                mainDeck.decrementDeckSize();
                mainDeck.setSampleSpace((mainDeck.getDeckSize() + 1) * mainDeck.getDeckSize());

                // Generate dealer outcomes
                for (int j = 0; j < DeckNode.DECK_LENGTH; j++) {
                    generateDealerOutcomes(mainDeck.getDeck()[j], Math.min(10, dealerUpcard));
                }

                // Use simpler method to calculate probabilities of dealer outcomes
                calculateSimpleDealerProbabilities(mainDeck);
            }
            else {
                // Modify main deck
                mainDeck.setEventSpace(mainDeck.getDeck()[i].getCardsLeft());
                mainDeck.getDeck()[i].removeCard();
                mainDeck.decrementDeckSize();
                mainDeck.setSampleSpace((mainDeck.getDeckSize() + 1) * mainDeck.getDeckSize());

                // Generate dealer outcomes
                for (int j = 0; j < DeckNode.DECK_LENGTH; j++) {
                    generateDealerOutcomes(mainDeck.getDeck()[j], Math.min(10, dealerUpcard));
                }

                // Generate dealer outcomes, then compare the player total to them
                for (int j = 0; j < DeckNode.DECK_LENGTH; j++) {
                    calculateHitProbabilities(mainDeck.getDeck()[j], playerHandTotal + cardValue);
                }
            }
        }
        prPlayerBustHit = bustCount / mainSampleSpace;

        System.out.println();
        System.out.println("Player beat dealer (Hit): " + prPlayerBeatDealerHit);
        System.out.println("Dealer beat player (Hit): " + prDealerBeatPlayerHit);
        System.out.println("Push (Hit): " + prPushHit);
        System.out.println("Player Bust (Hit): " + prPlayerBustHit);
        System.out.println("Dealer bust (Hit): " + prDealerBustHit);
        System.out.println("Total probability: " + (prPlayerBeatDealerHit + prDealerBeatPlayerHit + prPushHit + prPlayerBustHit + prDealerBustHit));
        System.out.println("Expected Value: " + (prPlayerBeatDealerHit + prDealerBustHit - prDealerBeatPlayerHit - prPlayerBustHit));
    }

    public static void calculateSimpleDealerProbabilities(DeckNode dn) {
        prDealerBustHit += (dn.getEventSpace() * dn.getBustCount()) / dn.getSampleSpace();
        prDealerBeatPlayerHit += (dn.getEventSpace() * (dn.getStandCount() + dn.getBlackjackCount())) / dn.getSampleSpace();

        for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
            if (dn.getDeck()[i].getNextDeck() != null) {
                calculateSimpleDealerProbabilities(dn.getDeck()[i].getNextDeck());
            }
        }
    }

    public static void calculateHitProbabilities(CardNode cn, int playerTotal) {
        if (cn.getDealerStand() || cn.getBlackjack()) {
            // Compare player total to dealer total
            if (playerTotal > cn.getHandTotal()) {
                prPlayerBeatDealerHit += (cn.getCardsLeft() * cn.getHomeDeck().getEventSpace()) / cn.getHomeDeck().getSampleSpace();
            }
            else if (playerTotal == cn.getHandTotal()) {
                prPushHit += (cn.getCardsLeft() * cn.getHomeDeck().getEventSpace()) / cn.getHomeDeck().getSampleSpace();
            }
            else {
                prDealerBeatPlayerHit += (cn.getCardsLeft() * cn.getHomeDeck().getEventSpace()) / cn.getHomeDeck().getSampleSpace();
            }
        }
        else if (cn.getBust()) {
            // Dealer busted
            prDealerBustHit += (cn.getCardsLeft() * cn.getHomeDeck().getEventSpace()) / cn.getHomeDeck().getSampleSpace();
        }
        // Go to next deck if possible
        else if (cn.getNextDeck() != null) {
            for (int i = 0; i < DeckNode.DECK_LENGTH; i++) {
                calculateHitProbabilities(cn.getNextDeck().getDeck()[i], playerTotal);
            }
        }
    }
}
