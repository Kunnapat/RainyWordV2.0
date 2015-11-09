import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.omg.CORBA.portable.OutputStream;


public class HomeServer extends JFrame{
	static int windowWidth = 500;
	static int windowHeight = 300;
    JButton startButton, gameSetting;
    JPanel catPanel;
    JComboBox<String> catBox ;
    
    LinkedList wordList = new LinkedList();
    String[] color = {"red", "black", "white","grey","green","yellow","orange","purple","pink"};
    String[] car = {"Honda","Toyota", "Nissan","Mazda","KIA","Hyundai","MercedesBenz","Acura","Lexus","Infiniti","Suzuki","Mitsubishi","Citroen","Chevrolet","Bentley","BMW","Holden","Daewoo","RollsRoyce","Audi","Volswagen","Peugeot","Mini","McLaren","Ford"};
    String[] luxury = {"LouisVuitton","Gucci", "Rolex","Moschino","Hermes","Prada","Givenchy","Chanel","BVLGARI","GiorgioArmani","BOSS","BURBERRY","MARCJACOBS","ChristianDior","Versace","RalphLauren","Patek","Panerai","TomFord","Swarovski","Valentino","MichaelKors","Celine","Vertu","CalvinKlein"};
    String[] country = {"UnitedStates","China", "India","Japan","Germany","Russia","Brazil","UnitedKingdom","France","Mexico","Italy","SouthKorea","Canada","Spain","Indonesia","Turkey","Australia","Iran","SaudiArabia","Poland","Argentina","Netherlands","Thailand","SouthAfrica","Pakistan"};
    String[] pp = {"public","static", "void","main","string","args","int","long","double","char","private","for","if","and","or","while","null","System","println","new","break","thread","runnable","action","parse"};
	public HomeServer(){
		super("Server");
		startButton = new JButton("Start");
		startButton.setFont(new Font("Menlo",Font.PLAIN,12));
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					getWordList();
					Server.oos.writeObject(wordList);
					GameServer game = GameServer.createAndShowGUI(wordList);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}

			private void getWordList() {
				String s = (String) catBox.getSelectedItem();
				LinkedListItr itr1 = wordList.zeroth();
				int temp = 5;
				if(s.equals("Color")){
					for(int i = 0; i < color.length; i++){
						wordList.insert(new Word(temp*-200,color[i]), itr1);
						temp++;
					}
				}else if(s.equals("Car")){
					for(int i = 0; i < car.length; i++){
						wordList.insert(new Word(temp*-200,car[i]), itr1);
						temp++;
					}
				}else if(s.equals("Luxury")){
					for(int i = 0; i < luxury.length; i++){
						wordList.insert(new Word(temp*-200,luxury[i]), itr1);
						temp++;
					}
					
				}else if(s.equals("Country")){
					for(int i = 0; i < country.length; i++){
						wordList.insert(new Word(temp*-200,country[i]), itr1);
						temp++;
					}
				}else if(s.equals("Programming Practice")){
					for(int i = 0; i < pp.length; i++){
						wordList.insert(new Word(temp*-200,pp[i]), itr1);
						temp++;
					}
				}
				
				
			}
			
		});
		this.add(startButton,BorderLayout.NORTH);
		gameSetting = new JButton("Setting");
		gameSetting.setFont(new Font("Menlo",Font.PLAIN,12));
		createCatPanel();
		this.add(catPanel, BorderLayout.CENTER);
		this.add(gameSetting, BorderLayout.SOUTH);
		
	}
	

	private void createCatPanel() {
		catPanel = new JPanel();
		catPanel.setBackground(Color.BLACK);
		JLabel instructionLabel = new JLabel("Please choose word category.");
		instructionLabel.setForeground(Color.WHITE);
		instructionLabel.setFont(new Font("Menlo",Font.PLAIN,12));
		catPanel.add(instructionLabel, BorderLayout.CENTER);
		catBox = new JComboBox<String>();
		catBox.setFont(new Font("Menlo",Font.PLAIN,12));
		catBox.setBackground(Color.GRAY);
		catBox.setForeground(Color.BLACK);
		catBox.addItem("Color");
		catBox.addItem("Car");
		catBox.addItem("Luxury");
		catBox.addItem("Country");
		catBox.addItem("Programming Practice");
		catPanel.add(catBox, BorderLayout.CENTER);
	}


	public static HomeServer createAndShowGUI() throws IOException{
		HomeServer frame = new HomeServer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth,windowHeight); // set the size of GUI
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        return frame;
	}
	public static void main(String[] args) throws IOException{
		GameServer.setServerName("temp");
		GameServer.setClientName("temp");
		createAndShowGUI();
	}


	
}