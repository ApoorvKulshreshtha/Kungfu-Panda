

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class HangMan extends JPanel implements ActionListener, KeyListener, Runnable
{
	
	private final String[] strLetters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private JButton[] cmdLetters;
	private JButton cmdGuess, cmdNewGame;
	private JLabel lblStatus, lblGuess, lblHangImage, lblClock;
	private JTextField txtGuessWord;
	private JPanel panGuessArea2, panMainArea, panHangManArea, panHangManArea2, panGuessArea, panLettersArea, panLettersArea2, panStatusArea;
	private ImageIcon[] imgHang;
	private ObjHangWord hangObject;
	private Color clrBackgrounds, clrForegrounds;
	private Thread updateClock;
	private boolean ltdAccess;
	private PlayMuzic hangMusic;
	JFrame frame;
	
	
	
	/*public static void main(String[] args)
	{
		new HangMan();
    }*/
    
    public HangMan()
    {
	    ltdAccess = false;
	    super.setLayout(new BorderLayout());
	    hangObject = new ObjHangWord();
	    hangObject.setupTime();
			
		cmdGuess = new JButton("Guess Word");
		cmdNewGame = new JButton("New Game");
		hangMusic = new PlayMuzic();
		
		cmdGuess.addActionListener(this);
		cmdNewGame.addActionListener(this);
		
		txtGuessWord = new JTextField("", 10);
		txtGuessWord.addKeyListener(this);
		
		lblStatus = new JLabel("--Enter a word to guess or leave blank for random a word--");
		lblGuess = new JLabel("New Word: ");
		lblClock = new JLabel("Time: 00:00");
		lblGuess.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblClock.setHorizontalAlignment(SwingConstants.RIGHT);
		
		imgHang = new ImageIcon[10];
		
		for (int i = 0; i < imgHang.length; i++)
		{
			imgHang[i] = new ImageIcon("src\\images\\hang" + i + ".gif");
		}
		
		lblHangImage = new JLabel(imgHang[0]);
		
		if (hangObject.setupWords()) //If error occurs filling dictornary
		{
			
			lblStatus.setText(hangObject.getErrorMsg());
			cmdNewGame.setEnabled(false);
			
		}
		
		setupLayout();		
		setupStyle();
		
		cmdLetters = new JButton[strLetters.length];
		
		for (int i = 0; i < strLetters.length; i++)
		{
			
			cmdLetters[i] = new JButton(strLetters[i]);
			cmdLetters[i].addActionListener(this);
			panLettersArea.add(cmdLetters[i]);
			
		}
		
		changeEnableState(false); //Disable all the game buttons
		
		updateClock = new Thread(this, "clock");
		updateClock.start();
		
		frame = new JFrame("Hang Man Game"); //Title
       
        this.setOpaque(true);
		this.setPreferredSize(new Dimension(660, 380));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setContentPane(this);
                
        frame.pack();
        frame.setVisible(true);
        hangMusic.PlayUrlLoop(this.getClass().getResource("HeyThere.wav"));
        frame.addWindowListener( new WindowAdapter() 
		 {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				hangMusic.StopMuzic();
				
			}
			
		});
        
		
    }
    
    public void setupLayout()
    {
	    
	    panLettersArea = new JPanel(new GridLayout(2,13,2,2));
		panLettersArea2 = new JPanel();
		panMainArea = new JPanel(new BorderLayout());
		panGuessArea2 = new JPanel();
		panGuessArea = new JPanel(new GridLayout(2,2,1,1));
		panHangManArea = new JPanel();
		panHangManArea2 = new JPanel();
		panStatusArea = new JPanel(new BorderLayout());
		
		add(panStatusArea, BorderLayout.NORTH);
			panStatusArea.add(lblStatus, BorderLayout.CENTER);
			panStatusArea.add(lblClock, BorderLayout.EAST);
		add(panMainArea, BorderLayout.CENTER);
			panMainArea.add(panGuessArea2, BorderLayout.NORTH);
				panGuessArea2.add(panGuessArea);
					panGuessArea.add(lblGuess);
				panGuessArea.add(txtGuessWord);
				panGuessArea.add(cmdGuess);
				panGuessArea.add(cmdNewGame);
			panMainArea.add(panHangManArea, BorderLayout.CENTER);
				panHangManArea.add(panHangManArea2);
					panHangManArea2.add(lblHangImage);
		add(panLettersArea2, BorderLayout.SOUTH);
			panLettersArea2.add(panLettersArea);
		
		lblHangImage.setPreferredSize(new Dimension(230,200));
		
		panLettersArea.setBorder(BorderFactory.createLineBorder(Color.black, 4));
		lblClock.setBorder(BorderFactory.createEmptyBorder(1,15,1,1));
		
	}	    
	
	public void setupStyle()
	{
		
		clrBackgrounds = new Color(75,141,221); //Light blue
		this.setBackground(clrBackgrounds);
		panStatusArea.setBackground(clrBackgrounds);
		panMainArea.setBackground(clrBackgrounds);
		panGuessArea2.setBackground(clrBackgrounds);
		panGuessArea.setBackground(clrBackgrounds);
		panHangManArea.setBackground(clrBackgrounds);
		panLettersArea2.setBackground(clrBackgrounds);
		
		panHangManArea2.setBackground(Color.white);
		panLettersArea.setBackground(Color.black);
		
		clrForegrounds = new Color(255,255,255); //White
		lblStatus.setForeground(clrForegrounds);
		lblGuess.setForeground(clrForegrounds);	
		lblClock.setForeground(clrForegrounds);	
		
	}
	
	public void changeEnableState(boolean state)
	{
		
		for (int i = 0; i < strLetters.length; i++)
		{
			cmdLetters[i].setEnabled(state);
		}
		
		cmdGuess.setEnabled(state);
		txtGuessWord.setText("");
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		if (e.getSource() == cmdNewGame)
		{
			
			hangObject.newGame(txtGuessWord.getText()); //Send the word to guess
			changeEnableState(true); //Enable everything
			lblGuess.setText("Guess Word (lose if wrong)");
			lblClock.setText("Time: 00:00");
			
		}
		else if (e.getSource() == cmdGuess)
		{
			hangObject.guessWord(txtGuessWord.getText());
		}
		else
		{
			hangObject.guessLetter(e.getActionCommand().charAt(0));
			
			for (int i = 0; i < strLetters.length; i++) //Find which letter was pressed and disable it
			{
				
				if (strLetters[i] == e.getActionCommand())
				{
					cmdLetters[i].setEnabled(false);
				}
				
			}
			
		}
		
		if (hangObject.isFinished())
		{

			changeEnableState(false);
			lblGuess.setText("New Word: ");
			if (ltdAccess) {
				if (!hangObject.isWin()) {
					JOptionPane.showMessageDialog(this,
							"You lost. The princess shall be hanged.\nThe rope broke! She's alive! Hang her again!!");
					cmdNewGame.doClick();
				} else {
					JOptionPane.showMessageDialog(this,
							"You Saved the Princess!!!!");
					hangMusic.StopMuzic();
					
					frame.dispose();
				}
			}
		}
		
		lblStatus.setText(hangObject.getDisplayString());
		lblHangImage.setIcon(imgHang[hangObject.getAmountGuesses()]);
			
	}
	
	public void run()
	{
		
		Thread myThread = updateClock;
		
		while(myThread == updateClock)
		{
			
			if (!hangObject.isFinished())
			{
				lblClock.setText("Time: " + hangObject.getTime());
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
	
	public void keyTyped(KeyEvent e)
	{
		
		char c = e.getKeyChar(); //c = key pressed
		
		//If char is NOT backspace, delete or not a char
		if (!(!(Character.isDigit(c)) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)))
		{
			e.consume(); //Remove entered key				
		}
		
	}
	
	public void keyPressed(KeyEvent e)
	{
	}
	
	public void keyReleased(KeyEvent e)
	{
	}
	public void setLtdAccess()			//To disable certain options while in storyline
	{
		ltdAccess = true;
		cmdGuess.setEnabled(false);
		hangObject.setLtdAccess();
		cmdNewGame.doClick();
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
}