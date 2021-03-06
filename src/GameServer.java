import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GameServer extends JFrame{
	static int windowWidth = 1000;
	static int windowHeight = 700;
	JPanel gamePanel, optionPanel, infoPanel;
	static JLabel serverNameLabel;
	static JLabel clientNameLabel;
	static JLabel serverScoreLabel;
	static JLabel clientScoreLabel;
	static boolean caseSensitivity = false;
	JFrame popUpFrame;
	static LinkedList wordList = new LinkedList();
	static LinkedList coloredList = new LinkedList();
	LinkedList welcomeList = new LinkedList();
    String[] color = {"red", "black", "white","grey","green","yellow","orange","purple","pink"};
    String inputWord;
	static String serverName;
	static String clientName;
    static JTextField inputField;
    static JButton startButton;
    static boolean gameStarted = false;
    static Thread t1;
    static int serverScore = 0;
    static int clientScore = 0;
    boolean clientIsReady = false;
    static int fallSpeed;
    static Thread t2 = new Thread(new Runnable(){

		@Override
		public void run() {
			while(true){
				String s = inputField.getText().toLowerCase();
				if(!s.equals("")){
					LinkedListItr itr = wordList.first();
					while(!itr.isPastEnd()){
						Word w = itr.current.element;
						String temp = w.word.toLowerCase();
						if(temp.startsWith(s)){
//							LinkedListItr itr1 = coloredList.zeroth();
//							coloredList.insert(new Word(w.wordXLocation,w.wordYLocation,s,w.fallSpeed, Color.RED), itr1);
							itr.current.element.changeColor(Color.RED);
						}
						itr.advance();
					}
				}
				
			}
			
		}
    	
    });
    
	public GameServer(){
		super("Rainy Word V1.1");
		createGamePanel();
		createOptionPanel();
		createInfoPanel();
		this.add(infoPanel, BorderLayout.NORTH);
		this.add(gamePanel,BorderLayout.CENTER);
		this.add(optionPanel,BorderLayout.SOUTH);
		
	}

	private void createInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setSize(new Dimension(1000,300));
		infoPanel.setBackground(Color.GRAY);
		infoPanel.add(serverNameLabel);
		serverScoreLabel = new JLabel("       Score: " + serverScore+"           ");
		infoPanel.add(serverScoreLabel);
		infoPanel.add(clientNameLabel);
		clientScoreLabel = new JLabel("       Score: " + clientScore+"           ");
		infoPanel.add(clientScoreLabel);
		
	}
	private void createGamePanel() {
		gamePanel = new GamePanel();
		gamePanel.setSize(new Dimension(1000,300));
		gamePanel.setBackground(Color.BLACK);
//		LinkedListItr itr1 = wordList.zeroth();
		LinkedListItr itr2 = welcomeList.zeroth();
//		int temp = 5;
//		for(int i = 0; i < color.length; i++){
//			wordList.insert(new Word(temp*-200,color[i]), itr1);
//			temp++;
//		}
		welcomeList.insert(new Word(170,-150,"-------------------"+clientName+" has started the game"+"-------------------",fallSpeed), itr2);
		welcomeList.insert(new Word(170,170,"▒█▀▀█ ░█▀▀█ ▀█▀ ▒█▄░▒█ ▒█░░▒█ 　 ▒█░░▒█ ▒█▀▀▀█ ▒█▀▀█ ▒█▀▀▄",fallSpeed), itr2);
		welcomeList.insert(new Word(170,190,"▒█▄▄▀ ▒█▄▄█ ▒█░ ▒█▒█▒█ ▒█▄▄▄█ 　 ▒█▒█▒█ ▒█░░▒█ ▒█▄▄▀ ▒█░▒█",fallSpeed), itr2);
		welcomeList.insert(new Word(170,210,"▒█░▒█ ▒█░▒█ ▄█▄ ▒█░░▀█ ░░▒█░░ 　 ▒█▄▀▄█ ▒█▄▄▄█ ▒█░▒█ ▒█▄▄▀",fallSpeed), itr2);
		welcomeList.insert(new Word(170,320,"---------------------------------------------------------",fallSpeed), itr2);
		
		repaint();
	}
	
	private void createOptionPanel() {
		optionPanel = new JPanel();
		optionPanel.setSize(new Dimension(1000,200));
		optionPanel.setBackground(Color.GRAY);
		startButton = new JButton("READY");
		
		
		t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				System.out.print("");
				while(gameStarted){
					System.out.print("");
					repaint();
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(wordList.isEmpty()){
						createPopUpFrame();
						gameStarted = false;
						repaint();
						popUpFrame.setVisible(true);
					}
				}
				
			}
			
		});
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				startButton.setEnabled(false);
				inputField.setText("Waiting for " + clientName + " to be ready.");
				try {
					Server.oos.writeObject("server ready");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		});
		optionPanel.add(startButton);
		inputField = new JTextField();
		inputField.setPreferredSize(new Dimension(500,20));
		inputField.setBackground(Color.white);
		inputField.setForeground(Color.black);
		inputField.setFont(new Font("Menlo",Font.PLAIN,12));
		inputField.setText("ARE YOU READY ?");
		inputField.setEnabled(false);
		inputField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String inputWord = "";
				if(caseSensitivity){
					inputWord = inputField.getText();
				}else{
					inputWord = inputField.getText().toLowerCase();
				}
				inputField.setText("");
				System.out.println(inputWord);
				LinkedListItr itr1 = wordList.first();
				while(!itr1.isPastEnd()){
					String temp = "";
					if(caseSensitivity){
						temp = itr1.current.element.word;
					}else{
						temp = itr1.current.element.word.toLowerCase();
					}
					if(inputWord.equals(temp)){
						playSound("src/correct.wav");
						System.out.println("correct");
						wordList.remove(itr1);
						try {
							Server.oos.writeObject(itr1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							addServerScore();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					}
					itr1.advance();
					if(itr1.isPastEnd()){
						playSound("src/wrong.wav");
					}
				}
				
			}
			
		});
		optionPanel.add(inputField);
	
		
	}
	public static void addClientScore(){
		clientScore = clientScore + 1;
		clientScoreLabel.setText("       Score: " + clientScore+"           ");
	}
	private void addServerScore() throws IOException{
		serverScore = serverScore + 1;
		serverScoreLabel.setText("       Score: " + serverScore+"           ");
		Server.oos.writeObject("addServerScore");
	}

	public void playSound(String s) {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(s));
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}

	private void createPopUpFrame() {
		popUpFrame = new JFrame();
		popUpFrame.setSize(400, 150);
		popUpFrame.setResizable(false);
		popUpFrame.setLocationRelativeTo(null);
		popUpFrame.setLayout(new GridLayout(3, 1));
		JLabel winLabel = new JLabel("Your score is " + serverScore + "/" + color.length +".");
		popUpFrame.add(winLabel);
		JButton closeButton = new JButton("OK");
		closeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
		});
		closeButton.setSize(new Dimension(80,40));
		popUpFrame.add(closeButton);
		popUpFrame.setVisible(false);
		
	}
	
	
	
	public static GameServer createAndShowGUI(LinkedList ll) throws IOException{
		GameServer frame = new GameServer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth,windowHeight); // set the size of GUI
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        wordList = ll;
		
        return frame;
	}


	class GamePanel extends JPanel{
		boolean firstTime = true;
		
		public void paint(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			if(firstTime){
				g2.setColor(Color.green);
				g2.setFont(new Font("Menlo",Font.PLAIN,20)); 
				g2.drawString("▒█▀▀█ ░█▀▀█ ▀█▀ ▒█▄░▒█ ▒█░░▒█ 　 ▒█░░▒█ ▒█▀▀▀█ ▒█▀▀█ ▒█▀▀▄ ", 170, 170);
				g2.drawString("▒█▄▄▀ ▒█▄▄█ ▒█░ ▒█▒█▒█ ▒█▄▄▄█ 　 ▒█▒█▒█ ▒█░░▒█ ▒█▄▄▀ ▒█░▒█ ", 170, 190);
				g2.drawString("▒█░▒█ ▒█░▒█ ▄█▄ ▒█░░▀█ ░░▒█░░ 　 ▒█▄▀▄█ ▒█▄▄▄█ ▒█░▒█ ▒█▄▄▀  ", 170, 210);
				g2.drawString("---------------------------------------------------------", 170, 320);
				
				firstTime = false;
			}else{
				g2.setColor(Color.BLACK);
				g2.drawRect(0, 0, windowWidth, windowHeight);
		        LinkedListItr itr1 = wordList.first();
		        LinkedListItr itr2 = welcomeList.first();
//		        LinkedListItr itr3 = coloredList.first();
		        while(!itr2.isPastEnd()){
		        	itr2.current.element.paint(g2);
		        	itr2.advance();
		        }
		        while(!itr1.isPastEnd()){
		        	itr1.current.element.paint(g2);
		        	itr1.current.element.changeColor(Color.GREEN);
		        	itr1.advance();
		        }
//		        while(!itr3.isPastEnd()){
//		        	g2.setColor(Color.RED);
//		        	itr3.current.element.paint(g2);
//		        	coloredList.remove(itr3);
//		        	itr3.advance();
//		        }
		        update();
			}
				
		}
		
		
		


		public void update(){
			LinkedListItr itr1 = wordList.first();
			LinkedListItr itr2 = welcomeList.first();
//			LinkedListItr itr3 = coloredList.first();
	        while(!itr1.isPastEnd()){
	        	itr1.current.element.update();
	        	if(itr1.current.element.getYLocation() > windowHeight){
	        		wordList.remove(itr1);
	        		playSound("src/wrong.wav");
	        	}
	        	itr1.advance();
	        }
	        while(!itr2.isPastEnd()){
	        	itr2.current.element.update();
	        	if(itr2.current.element.getYLocation() > windowHeight){
	        		wordList.remove(itr2);
	        	}
	        	itr2.advance();
	        }
//	        while(!itr3.isPastEnd()){
//	        	itr3.current.element.update();
//	        	if(itr3.current.element.getYLocation() > windowHeight){
//	        		wordList.remove(itr3);
//	        	}
//	        	itr3.advance();
//	        }
		}
	}

	
	public static void setServerName(String s){
		if(s == null){
			s = "";
		}
		serverName = s;
		serverNameLabel = new JLabel("Server Name: " + serverName+"           ");	
		serverNameLabel.setForeground(Color.WHITE);
	}
	
	public static void setClientName(String s){
		if(s == null){
			s = "";
		}
		clientName = s;
		clientNameLabel = new JLabel("Client Name: " + clientName+"           ");
		clientNameLabel.setForeground(Color.WHITE);
	}
	
	

}