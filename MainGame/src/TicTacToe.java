import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Timer;


public class TicTacToe implements ActionListener, Runnable {
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel backPanel;
	private JButton buttons[] ;
	private JMenuBar mainMenu;
	private JMenu file;
	private JMenu players;
	private JMenu help;
	private JMenuItem load;
	private JMenuItem saveAs;
	private JMenuItem newGame;
	private JRadioButtonMenuItem singlePlayer;
	private JRadioButtonMenuItem twoPlayer;
	private ButtonGroup radioGroup;
	private JMenuItem about;
	private JMenuItem exit;
	private JLabel lblClock;
	private int count;
	private String letter;
	private String cpuLetter;
	private boolean computer;
	private boolean win ;
	private boolean ltdAccess;
	private Thread updateClock;
	private Timer timeSeconds;
	private objTimeCounter timeObject;
	private PlayMuzic BGmusic;
	private JFileChooser fileChooser;
	
	private static int winCombos[][] = {
		{0,1,2},{3,4,5},{6,7,8},
		{0,3,6},{1,4,7},{2,5,8},
		{6,4,2},{8,4,0}
	};
	private static String BACKGROUND = "SureShot.wav"; 
	private static String CLICK = "cool2.wav"; 
	private static String WIN = "wahoo.wav"; 
	private static String LOSE = "loser.wav"; 
	
	

	
	public TicTacToe()
	{
		computer = false;
		ltdAccess = false;
		win = false;
		int i = 0;
		count = 0;
		letter = new String();
		mainFrame = new JFrame();
		backPanel = new JPanel( new BorderLayout());
		mainPanel = new JPanel(new GridLayout(3, 3, 5, 5));
		buttons = new JButton[9];
		mainMenu = new JMenuBar();
		file = new JMenu("File");
		players = new JMenu("Players");
		help = new JMenu("Help");
		newGame = new JMenuItem("New Game");
		saveAs = new JMenuItem("Save As");
		load = new JMenuItem("Load Game");
		singlePlayer = new JRadioButtonMenuItem("Single Player");
		twoPlayer = new JRadioButtonMenuItem("Two Player");
		exit = new JMenuItem("Exit");
		about = new JMenuItem("About");
		radioGroup = new ButtonGroup();
		letter = "X";
		BGmusic=new PlayMuzic();
		lblClock = new JLabel("Time: 00:00");
		timeObject = new objTimeCounter();
		timeSeconds = new Timer();

		
		lblClock.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClock.setSize(200,100);
		lblClock.setBorder(BorderFactory.createEmptyBorder(1,15,1,1));
		lblClock.setVisible(true);
			
		timeSeconds.schedule(timeObject, 0, 990);
			
		updateClock = new Thread(this, "clock");
		updateClock.start();
		
		mainMenu.add(file);
		file.add(newGame);
		file.add(load);
		file.add(saveAs);
		file.add(exit);
		mainMenu.add(players);
		players.add(singlePlayer);
		players.add(twoPlayer);
		mainMenu.add(help);
		help.add(about);
		
		newGame.addActionListener(this);
		saveAs.addActionListener(this);
		load.addActionListener(this);
		singlePlayer.addActionListener(this);
		twoPlayer.addActionListener(this);
		about.addActionListener(this);
		exit.addActionListener(this);
		
		radioGroup.add(singlePlayer);
		radioGroup.add(twoPlayer);
		twoPlayer.setSelected(true);
		
		
		
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("TicTacToe Saved Games", "ttts"));
		fileChooser.setSelectedFile( new File("*.ttts"));
		
		
		mainPanel.setBackground(new Color(255,192,203));
		for( i = 0; i < 9 ; i++)
		{
			buttons[i] = new JButton();
			buttons[i].setBackground(Color.white);
			buttons[i].addActionListener(this);
			buttons[i].setForeground(Color.red);
			buttons[i].setFont(new Font("SansSerif",Font.BOLD,50));
			mainPanel.add(buttons[i]);
		}
		
		backPanel.add(mainPanel,BorderLayout.CENTER);
		backPanel.add(lblClock,BorderLayout.NORTH);
		backPanel.add(   Box.createRigidArea(new Dimension(300,200)),BorderLayout.EAST);
		backPanel.add(   Box.createRigidArea(new Dimension(200,50)),BorderLayout.SOUTH);
		backPanel.add(   Box.createRigidArea(new Dimension(300,200)),BorderLayout.WEST);
		backPanel.add(lblClock,BorderLayout.NORTH);
		backPanel.setBackground( Color.white);
		
		//start background music
		BGmusic.PlayUrlLoop(this.getClass().getResource(BACKGROUND));
		

		mainFrame.setContentPane(backPanel);
		mainFrame.setJMenuBar(mainMenu);
		mainFrame.setVisible(true);
		mainFrame.setSize(400, 300);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setLocationRelativeTo(null);
		
		mainFrame.addWindowListener( new WindowAdapter() 
		 {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				BGmusic.StopMuzic();
				
			}
			
		});

		
	}
	
	public void run()
	{
		
		Thread myThread = updateClock;
		
		while(myThread == updateClock)
		{
			
			if (!win)
			{
				lblClock.setText("Time: " +timeObject.getTime());
			}
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException errorMsg)
			{
			}
			
		}
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		Object src = event.getSource();
		//if user pressed new game
		if(src == newGame)
		{
			int yes =JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new game? All progress will be lost.","New Game",JOptionPane.YES_NO_OPTION);
			if(yes == JOptionPane.YES_OPTION)
			{
				setClear();
			}
		}
		
		else if(src == load)
		{
			
			if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
				int yes = JOptionPane
						.showConfirmDialog(
								null,
								"Are you sure you want to load a new game?\nAll unsaved progress will be lost.",
								"Load Game", JOptionPane.YES_NO_OPTION);
				if (yes == JOptionPane.YES_OPTION) {

					File file = fileChooser.getSelectedFile();
					// load from file
					count = 0;
					try {
						BufferedReader input = new BufferedReader(
								new FileReader(file));
						Character temp;
						for (int i = 0; i < 9; i++) {
							temp = (char) input.read();
							if (temp == ' ') {
								buttons[i].setText("");
								buttons[i].setEnabled(true);
							} else {
								buttons[i].setText(temp.toString());
								buttons[i].setEnabled(false);
								count++;
							}
							
							if(count%2==0)
							{
								cpuLetter = "O";
							}
							else
							{
								cpuLetter = "X";
							}
							

						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		else if(src == saveAs)
		{
			
			if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
			  File opfile = fileChooser.getSelectedFile();
			  if(!opfile.getPath().toLowerCase().endsWith(".ttts"))		 //save the tictactoe game with extension .ttts
			  {
			      opfile = new File(opfile.getPath() + ".ttts");
			  }
			  // save to file
			  try {
				BufferedWriter output = new BufferedWriter(new FileWriter(opfile));
				for ( int i = 0; i < 9; i ++){
					if ( buttons[i].getText().equals(""))
					{
						output.append(" ");
					}
					else
						output.append(buttons[i].getText());
				}
				output.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	
		else if(src == exit)
		{
			int yes = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?","Confirm Exit",JOptionPane.YES_NO_OPTION);
			if(yes == JOptionPane.YES_OPTION)
			{
				mainFrame.dispose();
				BGmusic.StopMuzic();
			}
		}
		else if(src == singlePlayer)
		{
			if(!computer)
			{
				newGame();
				Object[] options = { "X", "O" };
				int x;
				singlePlayer.setSelected(true);
				computer = true;
				x = JOptionPane
						.showOptionDialog(mainFrame,
								"Do you want to be X or O?", "X or O",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);
				if(x == JOptionPane.YES_OPTION)
				{
					cpuLetter = "O";
				}
				else
				{
					cpuLetter = "X";
					playMove();
				}
				
			}
		}
		else if(src == twoPlayer)
		{
			System.out.println("twoPlayer");
			if(computer)
			{
				newGame();
				twoPlayer.setSelected(true);
				computer = false;
			}
			
		}
		else if(src == about)
		{
			JOptionPane.showMessageDialog(null, "Tic-tac-toe (or Noughts and crosses, Xs and Os) is a pencil-and-paper game\n " +
					"for two players, X and O, who take turns marking the spaces in a 3×3 grid.\n" +
					"The player who succeeds in placing three respective" +
					"marks in a horizontal,\n vertical, or diagonal row wins the game." +
					"\n\nTic-Tac-Toe version 1.45 ","About" ,JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			if(!computer)		//if 2 player
			{
				normalPlay(src);
			}
			else				// if Single Player
			{
				if(!(normalPlay(src)))		//User's Move
				{
					playMove();				//Computer's Move
					checkWin();
				}
			}
		}
			
	}
	
	public void setClear()		//creates a new window of tictactoe
	{
		BGmusic.StopMuzic();
		mainFrame.setVisible(false);
		TicTacToe ttt = new TicTacToe();
		if(computer)
		{
			ttt.singlePlayer.doClick();
		}
		if(ltdAccess)
		{
			ttt.setLimitedAccess();
		}
		
		
	}
	public boolean normalPlay( Object src)		//user moves
 {
		count++;
		if (count % 2 == 0) {
			letter = "O";
		} else {
			letter = "X";
		}

		JButton btnClicked = (JButton) src;
		btnClicked.setText(letter);
		btnClicked.setEnabled(false);
		
		PlayMuzic Click=new PlayMuzic();
		Click.PlayUrlOnce(this.getClass().getResource(CLICK));
		return(checkWin());

	}
	public boolean checkWin()		//check winnning condition
	{
		win = false;
		for( int i = 0 ; i < 8 ; i++)
		{
			if((buttons[winCombos[i][0]].getText().equals(buttons[winCombos[i][1]].getText())) &&
					(buttons[winCombos[i][1]].getText().equals(buttons[winCombos[i][2]].getText())&&
							buttons[winCombos[i][0]].getText()!="")	)
			{
				win  = true;
				letter = buttons[winCombos[i][0]].getText();
				break;
			}
		}
		if( win)
		{
			int yes = 0;
			PlayMuzic Vsong=new PlayMuzic();
			BGmusic.StopMuzic();
			System.out.println("WINNER"+ letter);
			if(!computer)
			{
				Vsong.PlayUrlOnce(this.getClass().getResource(WIN));
				yes = JOptionPane.showConfirmDialog(null, "The winner is " + letter + ". Do you want to play another one?","Victory",JOptionPane.YES_NO_OPTION);
			}
			else
			{
				
				if(letter.equals(cpuLetter))
				{
					Vsong.PlayUrlOnce(this.getClass().getResource(LOSE));
					if(ltdAccess)
					{
						JOptionPane.showMessageDialog(mainFrame, "You lost :P\n Ready to lose another one?");
						setClear();
						return true;
					}
					yes = JOptionPane.showConfirmDialog(null, "You lost :P\n Do you wanna lose another one?","Defeat",JOptionPane.YES_NO_OPTION);
				}
				else
				{
					Vsong.PlayUrlOnce(this.getClass().getResource(WIN));
					if(ltdAccess)
					{
						JOptionPane.showMessageDialog(mainFrame, "You Win!\nI'll tell you where the princess is.");
						mainFrame.dispose();
						return true;
					}
					yes = JOptionPane.showConfirmDialog(null, "Congrats you won.\nI went easy on you this time.\nDo you wanna play another one?","Victory",JOptionPane.YES_NO_OPTION);
				}
			}
			if(yes == JOptionPane.YES_OPTION)
			{
				setClear();
				return true;
			}
			else
			{
				mainFrame.dispose();
			}
		}
		else 
		{
			if(count == 9)
			{
				if(ltdAccess)
				{
					JOptionPane.showMessageDialog(mainFrame, "This game didnt decide anything\nLets play another one.");
					setClear();
					return true;
				}
				int yes = JOptionPane.showConfirmDialog(null, "The game is a tie. Do you want to play another one?","Tie game",JOptionPane.YES_NO_OPTION);
				if(yes == JOptionPane.YES_OPTION)
				{
					setClear();
				}
				else
				{
					mainFrame.dispose();
				}
			}
			
		}
		return false;
	}
	public void playMove()			//Computer plays a move
	{
		
		int i;
		count++;
		for(  i = 0 ; i < 8 ; i++)
		{
			if((buttons[winCombos[i][0]].getText().equals(buttons[winCombos[i][1]].getText()))&&(buttons[winCombos[i][0]].getText().equals(cpuLetter))&&buttons[winCombos[i][2]].isEnabled())
			{
				buttons[winCombos[i][2]].setText(cpuLetter);
				buttons[winCombos[i][2]].setEnabled(false);
				System.out.println("inplaymove1");
				return;
			}
			if((buttons[winCombos[i][0]].getText().equals(buttons[winCombos[i][2]].getText()))&&(buttons[winCombos[i][0]].getText().equals(cpuLetter))&&buttons[winCombos[i][1]].isEnabled())
			{
				buttons[winCombos[i][1]].setText(cpuLetter);
				buttons[winCombos[i][1]].setEnabled(false);
				System.out.println("inplaymove2");
				return;
			}
			if((buttons[winCombos[i][2]].getText().equals(buttons[winCombos[i][1]].getText()))&&(buttons[winCombos[i][1]].getText().equals(cpuLetter))&&buttons[winCombos[i][0]].isEnabled())
			{
				buttons[winCombos[i][0]].setText(cpuLetter);
				buttons[winCombos[i][0]].setEnabled(false);
				System.out.println("inplaymove3");
				return;
			}
		}
		for(i = 0;i < 8 ; i++)
		{
			if((buttons[winCombos[i][0]].getText().equals(buttons[winCombos[i][1]].getText()))&&(buttons[winCombos[i][0]].getText().equals(letter))&&buttons[winCombos[i][2]].isEnabled())
			{
				buttons[winCombos[i][2]].setText(cpuLetter);
				buttons[winCombos[i][2]].setEnabled(false);
				System.out.println("inplaymove4");
				return;
			}
			if((buttons[winCombos[i][0]].getText().equals(buttons[winCombos[i][2]].getText()))&&(buttons[winCombos[i][0]].getText().equals(letter))&&buttons[winCombos[i][1]].isEnabled())
			{
				buttons[winCombos[i][1]].setText(cpuLetter);
				buttons[winCombos[i][1]].setEnabled(false);
				System.out.println("inplaymove5");
				return;
			}
			if((buttons[winCombos[i][2]].getText().equals(buttons[winCombos[i][1]].getText()))&&(buttons[winCombos[i][1]].getText().equals(letter))&&buttons[winCombos[i][0]].isEnabled())
			{
				buttons[winCombos[i][0]].setText(cpuLetter);
				buttons[winCombos[i][0]].setEnabled(false);
				System.out.println("inplaymove6");
				return;
			}
			
		}
		if(buttons[4].isEnabled())
		{
			buttons[4].setText(cpuLetter);
			buttons[4].setEnabled(false);
			return;
		}
		if(count < 4)
		{
			while(true)
			{
				i = (int)(Math.random()*8);
				if(buttons[i].isEnabled())
				{
						buttons[i].setText(cpuLetter);
						buttons[i].setEnabled(false);
						return;
				}
				
				
			}
		}
		
		for(i = 0 ; i < 9 ; i++)
		{
			if(buttons[i].isEnabled())
			{
				buttons[i].setText(cpuLetter);
				buttons[i].setEnabled(false);
				System.out.println("inplaymove");
				return;
			}
				
		}
		System.out.println("Nothing happened");
	}
	
	/*public static void main(String args[])
	{
		new TicTacToe();
	}*/
	
	public void newGame()		//to start a new game
	{
		int i;
		for ( i = 0; i < 9; i++ )
		{
			buttons[i].setText("");
			buttons[i].setEnabled(true);
		}
		count = 0;
		timeObject.resetTime();
	}

	public void setLimitedAccess()	//To disable certain options while in storyline
	{
		singlePlayer.doClick();
		twoPlayer.setEnabled(false);
		file.setEnabled(false);
		ltdAccess =  true;
		
	}
	

}
