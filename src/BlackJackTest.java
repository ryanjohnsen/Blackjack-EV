public class BlackJackTest {
    public static void main(String[] args) {
        int sum;
        int dealerWin = 0;
        int[] deck;
        int[][] outcomes = new int[3000][];
        int bustOutcomes = 0; // dealer bust outcomes


        for (int i = 0; i < 10000; i++) {
//            System.out.println("\nnew game");
            int[] outcome = new int[3];
            outcome[0] = 10;
            int count = 1;
            sum = 10;
            deck = new int[]{ 1,1,1,1,
                              2,2,2,2,
                              3,3,3,3,
                              4,4,4,4,
                              5,5,5,5,
                              6,6,6,0,
                              7,7,7,7,
                              8,8,8,0,
                              9,9,9,9,
                              10,10,10,
                              11,11,11,11,
                              12,12,12,12,
                              13,13,13,13, };
            while(sum < 17) {
                int randomNum = (int) (Math.random() * deck.length); // player gets 6,8; dealer gets 10

                if (deck[randomNum] == 0) {
                    continue;
                }
                int value = Math.min(10, deck[randomNum]);
                if (value == 1 && sum == 10) {
//                    System.out.print(value + " ");
//                    System.out.print("blackjack!");
                    dealerWin++;
                    break;
                } else if ((sum + value) > 16 && (sum + value) < 22) {
//                    System.out.print(value + " " + "DW");
                    dealerWin++;
                    break;
                } else {
                    sum += value;

                    if (count == outcome.length) {
                        // Resize array if necessary
                        int[] newArray = new int[outcome.length + 1];
                        for (int j = 0; j < outcome.length; j++) {
                            newArray[j] = outcome[j];
                        }
                        outcome = newArray;
                    }

                    outcome[count++] = deck[randomNum];

                    if (sum > 21) {
                        outcomes[bustOutcomes++] = outcome;
                    }
//                    System.out.print(value + " ");
                }
                deck[randomNum] = 0;
                //int mapped = Math.floorDiv(randomNum, 4) + 1;
            }
            /*
                bust rate ~21.33754% at 100 million samples (dealer 10; player 6,8)
                ~21.3253% at 100 million samples (dealer 10; player 4,9)
            */
        }
//        for (int i = 0; i < bustOutcomes; i++) {
//            for (int j = 0; j < outcomes[i].length; j++) {
//                System.out.print(outcomes[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//        System.out.println();


        // Creating unique outcome array
        int[][] diffOutcomes = new int[bustOutcomes][];
        int diffCount = 0;
        diffOutcomes[diffCount++] = new int[outcomes[0].length + 1];
        for (int i = 0; i < outcomes[0].length; i++) {
            diffOutcomes[0][i] = outcomes[0][i];
        }
        diffOutcomes[0][outcomes[0].length] = 1;


        // Combining congruent outcomes
        boolean congruent;
        int j;
        for (int i = 1; i < bustOutcomes; i++) {
            for (j = 0; j < diffCount; j++) {
                congruent = true;
                if (outcomes[i].length == (diffOutcomes[j].length - 1)) {
                    for (int k = 0; k < diffOutcomes[j].length - 1; k++) {
                        if (outcomes[i][k] != diffOutcomes[j][k]) {
                            congruent = false;
                            break;
                        }
                    }
                } else {
                    congruent = false;
                }
                if (congruent) {
                    diffOutcomes[j][diffOutcomes[j].length - 1]++;
                    break;
                }
            } // Different outcomes
            if (j == diffCount) {
                // Unique bust outcome
                diffOutcomes[diffCount++] = new int[outcomes[i].length + 1];
                for (int k = 0; k < outcomes[i].length; k++) {
                    diffOutcomes[j][k] = outcomes[i][k];
                }
                diffOutcomes[j][outcomes[i].length] = 1;
            }
        } // Outcomes

        //int[][] sortedOutcomes = new int[diffCount][];
        //int sortCount = 0;
        int smallestIndex;
        int[] smallestSequence;
        for (int i = 0; i < diffCount - 1; i++) {
            smallestIndex = i;
            smallestSequence = diffOutcomes[i];
            for (j = i + 1; j < diffCount; j++) {
                for (int k = 1; k < diffOutcomes[j].length - 1 && k < smallestSequence.length - 1; k++) {
                    if (diffOutcomes[j][k] < smallestSequence[k]) {
                        smallestSequence = diffOutcomes[j];
                        smallestIndex = j;
                        break;
                    } else if (diffOutcomes[j][k] == smallestSequence[k]) {
                        continue;
                    } else {
                        break;
                    }
                }

            }
            //sortedOutcomes[sortCount] = smallestSequence;
            int[] temp = diffOutcomes[i];
            diffOutcomes[i] = smallestSequence;
            diffOutcomes[smallestIndex] = temp;
            //sortCount++;
        }

        for (int i = 0; i < diffCount; i++) {
            for (j = 0; j < diffOutcomes[i].length; j++) {
                if (j == diffOutcomes[i].length - 1) {
                    for (int k = 0; k < diffOutcomes[i][j]; k++) {
                        System.out.print("*");
                    }
//                    System.out.print("Count: " + diffOutcomes[i][j]);
                } else {
                    System.out.print(diffOutcomes[i][j] + " ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Unique Outcomes: " + diffCount);
        System.out.println("Bust Outcomes: " + bustOutcomes);
        // System.out.println("\n\n" + dealerWin);
    }
}
