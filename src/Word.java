import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class Word extends JPanel {

    private int wordXLocation;
    private int wordYLocation;
    private int fallSpeed = 1;
    Random rand = new Random();
    String word;
    boolean visible;


    public int getYLocation(){
    	return wordYLocation;
    }

    public int generateRandomXLocation(){
        return wordXLocation = rand.nextInt(GameServer.windowWidth - 150);
    }

    

    public void paint(Graphics g){
    	Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Menlo",Font.PLAIN,20)); 
        g2.setColor(Color.GREEN);
        g2.drawString(word, wordXLocation, wordYLocation);
      
        
    }


    public Word(int y,String s){
        generateRandomXLocation();
        wordYLocation = y;
        word = s;
        visible = true;
    }
    public Word(int x,int y,String s){
        wordXLocation = x;
        wordYLocation = y;
        word = s;
        visible = true;
    }

    public void update(){


        if(wordYLocation <= GameServer.windowWidth){
            wordYLocation += fallSpeed;
        }
    }
}