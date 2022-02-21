public class BlackjackHitTest {
    public static void main(String[] args) {
        int pbd = 0;
        int dbp = 0;
        int pb = 0;
        int db = 0;
        int[] deck;
        int playerSum;
        int dealerSum;
        int sampleSize = 10000000;

        // player hand { 8,6,4 }
        // dealer card { 10 }


        for (int i = 0; i < sampleSize; i++) {
            playerSum = 18;
            dealerSum = 10;
            deck = new int[]{ 1,1,1,1,
                              2,2,2,2,
                              3,3,3,3,
                              4,4,4,0,
                              5,5,5,5,
                              6,6,6,0,
                              7,7,7,7,
                              8,8,8,0,
                              9,9,9,9,
                              10,10,10,0,
                              11,11,11,11,
                              12,12,12,12,
                              13,13,13,13, };

            int randomNum = (int) (Math.random() * deck.length);
            while (deck[randomNum] == 0) {
                randomNum = (int) (Math.random() * deck.length);
            }

            int cardValue = Math.min(10, deck[randomNum]);
            playerSum += cardValue;

            if (playerSum > 21) {
                pb += 1;
            } else {
                while (dealerSum < 17) {
                    randomNum = (int) (Math.random() * deck.length);
                    int randomCard = Math.min(10, deck[randomNum]);

                    if (dealerSum == 10 && randomCard == 1) {
                        dealerSum = 21;
                        break;
                    }
                    dealerSum += randomCard;
                    deck[randomNum] = 0;
                }

                if (dealerSum > 21) {
                    db += 1;
                } else if (dealerSum > playerSum) {
                    dbp += 1;
                } else if (dealerSum < playerSum) {
                    pbd += 1;
                }
            }
        } // for loop

        // Estimating proportions to construct a 99% confidence interval
        // phat: proportion where player wins
        // qhat: proportion where dealer wins

        double phat = (pbd + db) / (double) sampleSize;
        double qhat = (dbp + pb) / (double) sampleSize;

        // 99% CI Equation:
        // phat +- z_0.005 * sqrt[(phat * (1 - phat)) / sampleSize]

        double phatUncertainty = 2.576 * Math.sqrt((phat * (1 - phat)) / sampleSize);
        double qhatUncertainty = 2.576 * Math.sqrt((qhat * (1 - qhat)) / sampleSize);

        // Since we are subtracting qhat from phat to find expected value
        double totalUncertainty = Math.sqrt(Math.pow(phatUncertainty, 2) + Math.pow(qhatUncertainty, 2));

        // EV = phat - qhat  or  EV = (pbd + db - dbp - pb) / sampleSize
        double expectedValue = phat - qhat;
        double lowerBound = expectedValue - totalUncertainty;
        double upperBound = expectedValue + totalUncertainty;

        System.out.println("Player beat dealer: " + pbd);
        System.out.println("Dealer bust: " + db);
        System.out.println("Dealer beat player: " + dbp);
        System.out.println("Player bust: " + pb);
        System.out.println("99% Confidence Interval for EV: " + "[" + lowerBound + ", " + upperBound + "]");
    }
}
