import java.awt.*;
import java.awt.event.*;

import javax.swing.*;




public class MainGame extends KeyAdapter  {
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JLabel imgLbl;
	private JButton playTTT;
	private JButton playHM;
	private ImageIcon imgIcon;
	private PlayMuzic mainGameMusic;
	private int imgNo;
	
	static int totalNoImages = 15;
	private static String BACKGROUND1 = "night_in_the_ocean.wav";
	private static String BACKGROUND2 = "blue_sphere.wav";
	private static String BACKGROUND3 = "the_best_day_ever.wav";
	private static String BACKGROUND4 = "the__fight.wav";

	public MainGame()
	{
		
		mainFrame = new JFrame("Are You Game");
		mainPanel = new JPanel(new BorderLayout());
		imgIcon =  new ImageIcon(this.getClass().getResource("story000.png"));
		imgLbl = new JLabel(imgIcon);
		imgLbl.setLayout(null);
		mainGameMusic = new PlayMuzic();
		playTTT = new JButton("TicTacToe");
		playHM = new JButton("HangMan");
		
		imgNo = 0;

		playTTT.setBackground(Color.white);
		playTTT.setSize(100, 50);
		playTTT.setLocation( 1200 , 700);
		playTTT.setVisible(false);
		playTTT.addActionListener( new GameStarter());
		playHM.setBackground(Color.white);
		playHM.setSize(100, 50);
		playHM.setLocation(80, 700);
		playHM.setVisible(false);
		playHM.addActionListener( new GameStarter());
		
		imgLbl.add(playTTT);
		imgLbl.add(playHM);
		mainPanel.add(imgLbl,BorderLayout.CENTER);
		mainFrame.setContentPane(mainPanel);
		mainFrame.setVisible(true);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.removeNotify();
		mainFrame.setUndecorated(true);
		mainFrame.addNotify();
		mainFrame.setBounds(0, 0, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.requestFocus();
		mainFrame.addKeyListener(this);
		playHM.addKeyListener(this);
		playTTT.addKeyListener(this);
		imgLbl.addKeyListener(this);
	
		mainFrame.addFocusListener(new MyFocusListener());
		playHM.addFocusListener( new MyFocusListener());
		playTTT.addFocusListener( new MyFocusListener());
		imgLbl.addFocusListener( new MyFocusListener());
		mainFrame.requestFocus();
		
	}
	public static void main(String arg[])
	{
		new MainGame();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == 39)//right arrow key
		{
			//sets the backgroung image
			//also determines when the player has to play tictactoe or hangman using imgNo as a 
			//count
			
			if(imgNo < totalNoImages)
			{
				imgNo ++;
				if(imgNo == 6)
				{
					JOptionPane
							.showMessageDialog(
									null,
									"Help the warrior defeat the enemy in a game of tictactoe\n",
									"Help the warrior",
									JOptionPane.INFORMATION_MESSAGE);
					
					TicTacToe ttt = new TicTacToe();
					ttt.setLimitedAccess();
				}
				else if(imgNo == 14)
				{
					JOptionPane
							.showMessageDialog(
									null,
									"Help the save the princess befor she is hanged\n" +
									"Guess the correct word to save her\n ",
									"Help the warrior",
									JOptionPane.INFORMATION_MESSAGE);
					
					HangMan hm = new HangMan();
					hm.setLtdAccess();
				}
				imgLbl.setIcon(new ImageIcon("src\\story" + String.format("%03d", imgNo) + ".png"));
				if(imgNo ==9)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND3));

				}
				else if(imgNo == 10)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND1));

				}
				else if(imgNo == 13)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND4));

				}
				else if(imgNo == 15)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND3));
					playHM.setVisible(true);
					playTTT.setVisible(true);
				}
			}
			
		}
		else if(e.getKeyCode() == 37)
		{
			if(imgNo>0)
			{
				imgNo--;
				imgLbl.setIcon(new ImageIcon(this.getClass().getResource("story" + String.format("%03d", imgNo) + ".png")));
				if(imgNo ==8)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND2));

				}
				else if(imgNo ==9)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND3));

				}
				else if(imgNo ==12)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND1));

				}
				else if(imgNo ==14)
				{
					mainGameMusic.StopMuzic();
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND4));

				}
			}
			
		}
		else if(e.getKeyCode() == 27)
		{
			System.exit(0);
		}
		
	}
	
	//actionlistener created for buttons to play tictactoe and hangman
	private class GameStarter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Object src = e.getSource();
			if(src == playTTT)
			{
				new TicTacToe();
			}
			else
			{
				new HangMan();
			}
		}
		
	}
	
	private class MyFocusListener extends FocusAdapter{

			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				mainGameMusic.StopMuzic();
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(imgNo <6)
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND1));
				else if(imgNo >= 6 && imgNo<14)
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND2));
				else if(imgNo == 14)
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND4));
				else
					mainGameMusic.PlayUrlLoop(this.getClass().getClassLoader().getResource(BACKGROUND3));
	
			}
		}
	

}

