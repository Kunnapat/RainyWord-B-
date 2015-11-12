import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private JLabel  playerName, opponentName, playerScoreLabel, opponentScoreLabel, timerLabel;
	// to hold the Username and later on the messages
	private JTextField tf;
	// to hold the server address an the port number
	private JTextField tfServer, tfPort;
	// to Logout and get the list of the users
	private JButton login, logout, whoIsIn, startButton;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected, gamePlaying = true;
	// the Client object
	private Client client;
	// the default port number
	private int defaultPort;
	
	private String defaultHost;
	
	private String pName, oName;
	
	private int playerScore = 0, opponentScore = 0;
	
	JPanel serverAndPort, northPanel, southPanel, centerPanel, scorePanel;
	
	LinkedList wordList, welcomeList;
	
	final static int WINDOWWH = 600, gameDuration = 10000;
	
	ActionListener al;
	
	javax.swing.Timer timer;
	
	Thread t1;
	

	// Constructor connection receiving a socket number
	ClientGUI(String host, int port) {

		super("===Rainy Word===");
		defaultPort = port;
		defaultHost = host;
		createNorthPanel();
		createServerAndPortPanel(host, port);
		createScorePanel();
		createCenterPanel();
		createSouthPanel();
		northPanel.add(serverAndPort);
		northPanel.add(scorePanel);
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WINDOWWH, WINDOWWH);
		setVisible(true);
		tf.requestFocus();

	}

	private void createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(1,1));
		ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				while(gamePlaying){

					repaint();
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		});
		centerPanel = new GamePanel();
	}



	private void createSouthPanel() {
		southPanel = new JPanel();
		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);		// you have to login before being able to logout
//		whoIsIn = new JButton("Who is in");
//		whoIsIn.addActionListener(this);
//		whoIsIn.setEnabled(false);		// you have to login before being able to Who is in
		createTimerLabel();
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(startButton);
		southPanel.add(timerLabel);
//		southPanel.add(whoIsIn);
	}

	private void createServerAndPortPanel(String host, int port) {
		// the server name and the port number
		serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		playerName = new JLabel("                 ");
		opponentName = new JLabel("                 ");

		serverAndPort.add(new JLabel("   Your Name:  "));
		serverAndPort.add(playerName);
		serverAndPort.add(new JLabel("Opponent Name:  "));
		serverAndPort.add(opponentName);
		serverAndPort.add(new JLabel(""));
		
	}

	private void createScorePanel() {
		// The NorthPanel with:
		scorePanel = new JPanel(new GridLayout(1,2));
		scorePanel.add(new JLabel("Player Score: "));
		playerScoreLabel = new JLabel(playerScore + "");
		scorePanel.add(playerScoreLabel);
		
		scorePanel.add(new JLabel("Opponent Score: "));
		opponentScoreLabel = new JLabel(opponentScore+ "");
		scorePanel.add(opponentScoreLabel);
		
	}

	private void createNorthPanel() {
		// The NorthPanel with:
		northPanel = new JPanel(new GridLayout(4,1));
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.WHITE);
		northPanel.add(tf);
		
	}

	// called by the Client to append text in the TextArea 
	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	
	void setOpponentName(String str) {
		opponentName.setText(str);
		oName = str;
	}
	public void addScore() {
		playerScore+=1;
		playerScoreLabel.setText(playerScore+"");
		client.sendMessage(new Messager(Messager.MESSAGE, pName+" addscore"));
		
	}
	public void addOpponentScore(){
		opponentScore+=1;
		opponentScoreLabel.setText(opponentScore+"");
	}
	void gameStart() {
		t1.start();
		timer = new javax.swing.Timer(1000, al);
		timer.start();
		startButton.setEnabled(false);
	}
	void setWordList(LinkedList wordList) {
		((GamePanel) centerPanel).setWordList(wordList);
	}
	void removeWord(String str) {
		((GamePanel) centerPanel).removeWord(str, false );
	}
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		whoIsIn.setEnabled(false);
		tf.setText("Anonymous");
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
	private void createTimerLabel() {
	    final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("mm : ss");
	    timerLabel = new JLabel(sdf.format(new Date(gameDuration)),JLabel.CENTER);
	    int x = 0;
	    al = new ActionListener(){	
	      int xTime = gameDuration - 1000;
	      public void actionPerformed(ActionEvent ae){
	        timerLabel.setText(sdf.format(new Date(xTime)));
	        if(xTime == 0){
	        	timer.stop();
	        	createAndShowPopUpFrame();
	        	gamePlaying = false;
	        }
	        xTime -= 1000;}
	    };
	    
	}	
	protected void createAndShowPopUpFrame() {
		final JFrame popUpFrame = new JFrame();
		popUpFrame.setSize(300, 200);
		popUpFrame.setResizable(false);
		popUpFrame.setVisible(true);
		popUpFrame.setLocationRelativeTo(null);
		JPanel southP = new JPanel(new GridLayout(1,2));
		String s = "";
		String n = "";
		if(playerScore > opponentScore){
			s = "           You win with a score of ";
			n = "The Winner is " + pName;
		}else if(playerScore < opponentScore){
			s = "           You lose with a score of ";
			n = "The Winner is " + oName;
		}else{
			s = "           It's a tie game with a score of ";
		}
		s = s + playerScore + " : " + opponentScore;
		JPanel centerP = new JPanel(new GridLayout(2,1));
		centerP.add(new JLabel(n));
		centerP.add(new JLabel(s));
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendMessage(new Messager(Messager.LOGOUT, ""));
				System.exit(0); 
			}
			
		});
		southP.add(closeButton);
		JButton playAgainButton = new JButton("Play Again");
		playAgainButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				popUpFrame.dispose();
				client.sendMessage(new Messager(Messager.LOGOUT, ""));
				new ClientGUI("localhost", 1600);
			}
			
		});
		southP.add(playAgainButton);
		popUpFrame.add(centerP, BorderLayout.CENTER);
		popUpFrame.add(southP,BorderLayout.SOUTH);
	}
	

	/*
	* Button or JTextField clicked
	*/
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
			client.sendMessage(new Messager(Messager.LOGOUT, ""));
			return;
		}
		// if it the who is in button
		if(o == whoIsIn) {
			client.sendMessage(new Messager(Messager.WHOISIN, ""));				
			return;
		}
		if(o == startButton){
			System.out.println("startButtonPressed");
			startButton.setEnabled(false);
			client.sendMessage(new Messager(Messager.MESSAGE, "ready"));
		}
		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message
			client.sendMessage(new Messager(Messager.MESSAGE, tf.getText()));		
			((GamePanel) centerPanel).removeWord(tf.getText(), true);
			tf.setText("");
			return;
		}
		

		if(o == login) {
			// ok it is a connection request
			String username = tf.getText().trim();
			pName = username;
			playerName.setText(username);
			// empty username ignore it
			if(username.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = defaultHost;
			if(server.length() == 0)
				return;
			// empty or invalid port number, ignore it
			String portNumber = defaultPort+"";
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;   // nothing I can do if port number is not valid
			}

			// try creating a new Client with GUI
			client = new Client(server, port, username, this);
			// test if we can start the Client
			if(!client.start()) return;
			tf.setText("");
			connected = true;
			
			// disable login button
			login.setEnabled(false);
			// enable the 2 buttons
			logout.setEnabled(true);
			//whoIsIn.setEnabled(true);
			// disable the Server and Port JTextField
			//tfServer.setEditable(false);
			//tfPort.setEditable(false);
			// Action listener for when the user enter a message
			tf.addActionListener(this);
			JOptionPane.showMessageDialog(null,"Welcome " + username);
		}

	}

	// to start the whole thing the server
	public static void main(String[] args) {
		new ClientGUI("localhost", 1600);
	}
	
	class GamePanel extends JPanel{
		boolean firstTime = true;
		LinkedList wordList, welcomeList;
		
//		public GamePanel(LinkedList wordList, LinkedList welcomeList){
//			this.wordList = wordList;
//			this.welcomeList = welcomeList;
//		}
		public void setWordList(LinkedList wordList){
			this.wordList = wordList;
		}
		public void paint(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			if(firstTime){
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, WINDOWWH, WINDOWWH);
				g2.setColor(Color.green);
				g2.setFont(new Font("Menlo",Font.PLAIN,20)); 
				g2.drawString("======Rainy Word======", 170, 170);
				firstTime = false;
			}else{
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, WINDOWWH, WINDOWWH);
		        LinkedListItr itr1 = wordList.first();
//		        LinkedListItr itr2 = welcomeList.first();
//		        while(!itr2.isPastEnd()){
//		        	itr2.current.element.paint(g2);
//		        	itr2.advance();
//		        }
		        while(!itr1.isPastEnd()){
		        	itr1.current.element.paint(g2);
		        	itr1.current.element.changeColor(Color.GREEN);
		        	itr1.advance();
		        }
		        update();
			}
				
		}
		
		public void removeWord(String str, boolean self){
			LinkedListItr itr = wordList.first();
			while(!itr.isPastEnd()){
				String temp = itr.current.element.word.toLowerCase();
				if(str.toLowerCase().equals(temp)){
					wordList.remove(itr);
					if(self){
						addScore();
					}
					break;
				}
				itr.advance();
			}
		}
		


		public void update(){
			LinkedListItr itr1 = wordList.first();
//			LinkedListItr itr2 = welcomeList.first();
	        while(!itr1.isPastEnd()){
	        	itr1.current.element.update();
	        	if(itr1.current.element.getYLocation() > WINDOWWH){
	        		wordList.remove(itr1);
	        		//playSound("src/wrong.wav");
	        	}
	        	itr1.advance();
	        }
//	        while(!itr2.isPastEnd()){
//	        	itr2.current.element.update();
//	        	if(itr2.current.element.getYLocation() > 300){
//	        		wordList.remove(itr2);
//	        	}
//	        	itr2.advance();
//	        }
		}
	}

	

	

}
