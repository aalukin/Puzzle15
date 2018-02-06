package com.hse.aalukin.puzzle15;

import java.io.*;
import java.util.*;

/**
 * Top of players
 */
class LeadersTable {

    /**
     * Directory with top players result file
     */
    private static final String RESOURCES_DIRECTORY = "players";

    /**
     * File with top players results
     */
    private static final String PLAYERS_FILE_NAME = "players.txt";

    /**
     * File which contains players' results
     */
    private File leadersFile = new File(RESOURCES_DIRECTORY + File.separator + PLAYERS_FILE_NAME);

    /**
     * List of players
     */
    private List<Player> playersList = null;

    /**
     * Constructor of players' table
     */
    LeadersTable(){
        checkingForDirectory();
        playersList = readPlayersFile();
        playersList.sort(Player::compareTo);
    }

    /**
     * Getter of players' list
     * @return list of players
     */
    List<Player> getPlayersList(){
        return playersList;
    }

    /**
     * Add new player
     * @param player player
     */
    void addPlayer(Player player){
        playersList.add(player);
        playersList.sort(Player::compareTo);
        writeFile();
    }

    /**
     * Write leaders file
     */
    void writeFile(){
        checkingForDirectory();
        if (leadersFile.exists()){
            leadersFile.delete();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(leadersFile))){
            for (Player player : playersList){
                bw.write(player.getName() + " " + player.getMoves());
                bw.newLine();
            }
        } catch (IOException ioe){
            System.out.println("Error, impossible to write the file");
        }
    }

    /**
     * Checking for table directory
     */
    private void checkingForDirectory(){
        File tableDirectory = new File(RESOURCES_DIRECTORY);
        if (!tableDirectory.exists() || !tableDirectory.isDirectory()){
            tableDirectory.delete();
        }

        tableDirectory.mkdir();
    }

    /**
     * Reading of leaders file
     * @return leaders
     */
    private List<Player> readPlayersFile(){
        List<Player> players = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(leadersFile))){
            for (String line; (line = br.readLine()) != null ; ){
                String[] lines = line.split("\\s+");
                if (line.length() != 2){
                    remakeFile();
                }
                int num = Integer.parseInt(lines[1]);
                if (num <= 0){
                    remakeFile();
                }
                players.add(new Player(lines[0], num));
            }
            br.close();
        } catch (IOException| NumberFormatException ex){
            remakeFile();
        }

        return players;
    }

    /**
     * Remake leaders file
     */
    private void remakeFile(){
        if (leadersFile.exists()){
            leadersFile.delete();
        }
    }
}
