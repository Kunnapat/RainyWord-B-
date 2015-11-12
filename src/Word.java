import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class Word extends JPanel {

    int wordXLocation;
    int wordYLocation;
    int fallSpeed;
    Random rand = new Random();
    String word;
    boolean visible;
    Color c = Color.GREEN;


    public int getYLocation(){
    	return wordYLocation;
    }

    public int generateRandomXLocation(){
        return wordXLocation = rand.nextInt(ClientGUI.WINDOWWH - 150);
    }

    

    public void paint(Graphics g){
    	Graphics2D g2 = (Graphics2D) g;
		g2.setFont(new Font("Menlo",Font.PLAIN,20)); 
        g2.setColor(c);
        g2.drawString(word, wordXLocation, wordYLocation);
      
        
    }


    public Word(int y,String s, int f){
        generateRandomXLocation();
        wordYLocation = y;
        word = s;
        fallSpeed = f;
        visible = true;
    }
    public Word(int x,int y,String s, int f){
        wordXLocation = x;
        wordYLocation = y;
        word = s;
        fallSpeed = f;
        visible = true;
    }
    public Word(int x,int y,String s, int f, Color cc){
        wordXLocation = x;
        wordYLocation = y;
        word = s;
        fallSpeed = f;
        visible = true;
        c = cc;
    }
    public void changeColor(Color c){
    	this.c = c;
    }
    public void update(){


        if(wordYLocation <= ClientGUI.WINDOWWH){
            wordYLocation += fallSpeed;
        }
    }
}