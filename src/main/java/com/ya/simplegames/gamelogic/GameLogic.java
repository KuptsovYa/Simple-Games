package com.ya.simplegames.gamelogic;

import com.ya.simplegames.entity.Sep;

public class GameLogic {

    /**
     * The method that makes the secret number
     * @return this number in model for each player
     */
    public static String guessNumber() {
        int N = 10;
        int [] arr = new int[N];
        for (int i = 0; i < N ; i++){
            arr[i] = i;
        }
        for (int i = 0; i < N ; i++){
            int j = (int) (Math.random() * N);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        String number = "";
        for (int i = 0; i < 4; i++){
            int digit = arr[i];
            number += String.valueOf(digit) ;
        }
        return number;
    }

    /**
     * Get a "Secret number"
     * @param gameLog - log of a game
     * @param playerNum - number of player
     * @return the number guessed by the computer
     */
    public static String getGuessedNumber(String gameLog, int playerNum){
        int index = gameLog.indexOf(Sep.HASH);
        String s1 = gameLog.substring(0, index);
        String[] array = s1.split((Sep.COMMA + ""));
        return array[playerNum - 1];
    }

    /**
     * Method compares computer's number and user's number
     * @param number - number guessed by the user
     * @param NUMBER - number guessed by the computer
     * @return number of cows and bulls
     */
    public static String getAnswer(String number, String NUMBER){
        int cows = 0;
        int bulls = 0;
        for(int i = 0; i < number.length(); i++){
            char d = number.charAt(i);
            for(int j = 0; j < NUMBER.length(); j++){
                if(d == NUMBER.charAt(j)) {
                    if(i == j) bulls++;
                    else cows++;
                }
            }
        }
        return "" + Sep.COL + bulls + Sep.COL + cows;
    }

    /**
     * Last number what written by user
     * @param gameLog - log of game
     * @return step
     */
    public static String prevStep(String gameLog){
        int comma = gameLog.lastIndexOf(Sep.COMMA);
        int hash = gameLog.lastIndexOf(Sep.HASH);
        String step;
        if(hash < comma) step = gameLog.substring(hash+1, comma);
        else step = gameLog.substring(comma+1, hash);
        if (step.indexOf(Sep.COL)==-1) {
            return "";
        }
        else return step;
    }

    /**
     * Last number what written by this user
     * @param gameLog - log of game
     * @return step of this user
     */
    public static String myPrevStep(String gameLog){
        //log("Enter myPrevStep gameLog=" + gameLog);
        char lastElem = gameLog.charAt(gameLog.length() - 1);
        String gameLog1 = gameLog.substring(0, gameLog.length() - 1);
        int index = gameLog1.lastIndexOf(lastElem);
        //log("myPrevStep index=" + index);
        if(index == -1){
            //log("Exit myPrevStep with empty str");
            return "";
        }else {
            String twowords = gameLog1.substring(index + 1);
            String[] result = twowords.split("" + Sep.COMMA + "|" + Sep.HASH);
            //log("Exit myPrevStep with " + result[0]);
            if (result[0].indexOf(Sep.COL)==-1) return "";
            else return result[0];
        }
    }

    /**
     * Get word guessed by computer
     * @param gameLog - state of the game
     * @param playerNum - number of player (1?2)
     * @return guessed word
     */
    public static String getGuesedWord(String gameLog, int playerNum){
        int index = gameLog.indexOf(Sep.HASH);
        String s1 = gameLog.substring(0, index);
        String[] array = s1.split((Sep.COMMA + ""));
        return array[playerNum - 1];
    }

    /**
     * Method is checking how many letters in word guessed by computer
     * @param letter - letter guessed by user
     * @param WORD - word guessed by another player
     * @param myPrevStep - previous step by this user
     * @return updated result of guessing
     */
    public static String getAnswer(char letter, String WORD, String myPrevStep) {
        //log("Enter getAnswer: letter=" + letter + " WORD=" + WORD + " myPrevStep="+myPrevStep);
        if (myPrevStep.equals("")) {
            for (int i = 0; i < WORD.length(); i++) {
                myPrevStep += Sep.UNDER;
            }
            myPrevStep += Sep.COL + "0";
        }
        //log("In getAnswer 1: myPrevStep =" + myPrevStep);
        boolean flag = false;
        for (int i = 0; i < WORD.length(); i++) {
            char l = WORD.charAt(i);
            if (l == letter) {
                flag = true;
                myPrevStep = myPrevStep.substring(0, i) + l + myPrevStep.substring(i + 1);
            }
        }
        //log("In getAnswer 2: myPrevStep =" + myPrevStep + " flag=" + flag);
        if (!flag) {
            int index = myPrevStep.lastIndexOf(Sep.COL);
            int num = Integer.parseInt(myPrevStep.substring(index + 1)) + 1;
            myPrevStep = myPrevStep.substring(0, index + 1) + Integer.toString(num);
        }
        //log("Exit getAnswer myPrevStep=" + myPrevStep);
        return myPrevStep;
    }

}
