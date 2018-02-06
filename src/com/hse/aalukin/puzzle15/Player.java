package com.hse.aalukin.puzzle15;

/**
 * Game's player
 */
class Player implements Comparable<Player>{

    /**
     * Player's name
     */
    private String name;

    /**
     * Player's moves
     */
    private int moves;

    /**
     * Constructor of player
     * @param name name of player
     * @param moves player's moves number
     */
    Player(String name, int moves){
        this.name = name;
        this.moves = moves;
    }

    /**
     * Getter of player's name
     * @return name of player
     */
    String getName(){
        return name;
    }

    /**
     * Getter of player's moves number
     * @return number of moves
     */
    int getMoves(){
        return moves;
    }

    /**
     * Comparing (player with less moves more)
     * @param o other player
     * @return result of comparing
     */
    @Override
    public int compareTo(Player o) {
        return getMoves() - this.moves;
    }

    @Override
    public String toString(){
        return name + " " + moves;
    }
}
