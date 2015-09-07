import java.util.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class ObjHangWord
{
	
	private String strWordToGuess, strDisplay, strError;
	private int amountOfGuesses;
	private boolean isFinished = true;
	private BufferedReader readSourceFile;
	private Vector vecRandomWords;
	private objTimeCounter timeObject;
	private Timer timeSeconds;
	private boolean win;
	private boolean ltd;
	
	public void ObjHangWord()
	{
	}
	
	public void setupTime()
	{
		
		timeSeconds = new Timer();
		timeObject = new objTimeCounter();		
		timeSeconds.schedule(timeObject, 0, 1*1000);
		
	}
	
	public boolean setupWords()
	{
		
		vecRandomWords = new Vector();
		boolean isError = false; //Used to keep track if an error occurs
		
		try
		{
			readSourceFile = new BufferedReader(new FileReader("src\\wordList.txt")); //Puts the file in the fileReader inside the steam
		}
		catch (IOException errorMsg)
		{			
			
			isError = true;
			strError = "Error: Unable to find \"wordList.txt\"";
			
		}
		
		if (!isError)
		{			
			isError = fillVectorWithWords(isError);			
		}
		
		return isError;		
		
	}		
	
	private boolean fillVectorWithWords(boolean isError)
	{
		
		String currentWord = "";
		
		try
		{

			while ((currentWord = readSourceFile.readLine()) != null) //Keep looping until end of file
			{
				
				if (currentWord.length() >= 5) //Only accept words 5 or more chars long 
				{
					vecRandomWords.add(currentWord);
				}				
							
			}
			
			readSourceFile.close(); //Close the stream
		
		}
		catch (IOException errorMsg)
		{
				
			isError = true;		
			strError = "Error reading the words, check file \"wordList.txt\"";			
			
		}
		
		return isError;
		
	}
	
	public void newGame(String strBuffer)
	{
		
		resetVariables();
		
		if (strBuffer.equals("")) //If left blank, get a random word
		{
			if(ltd)
				strBuffer = new String("kungfu");
			else
				strBuffer = getRandomWord();
		}
		
		strBuffer = strBuffer.toUpperCase();
		
		while (strBuffer.charAt(0) == ' ') //Removes any space padding before the word
		{
			strBuffer = strBuffer.substring(1, strBuffer.length());
		}
		
		if (strBuffer.indexOf(' ') != -1) //Removes any extra chars after the first space
		{
			strBuffer = strBuffer.substring(0, strBuffer.indexOf(' '));
		}
		
		strWordToGuess = strBuffer;
		
		for (int i = 0; i < strWordToGuess.length(); i++) //Puts astriks for each char
		{
			strDisplay += "*";
		}
		
	}
	
	public void guessWord(String strBuffer)
	{
		
		if (strBuffer.equalsIgnoreCase(strWordToGuess)) //If correct word
		{			
			strDisplay = getWinningMessage();
			win = true;
		}
		else
		{
			strDisplay = "Bad luck, the correct word was \"" + strWordToGuess.toLowerCase() + "\"";
			win = false;
		}
		
		isFinished = true;
		
	}
	
	public void guessLetter(char currentChar)
	{
		
		String strBefore = "", strAfter = "";
		
		if (strWordToGuess.indexOf(currentChar) == -1) //If letters is not in guess word
		{
			
			amountOfGuesses++;			
					
			if (amountOfGuesses == 10)
			{
				
				isFinished = true;
				strDisplay = "Bad luck, the correct word was \"" + strWordToGuess.toLowerCase() + "\"";
				win = false;
				
			}
			
		}
		else
		{
			
			for (int i = 0; i < strDisplay.length(); i++) //Loop as many chars are in strDisplay
			{
				
				if (currentChar == strWordToGuess.charAt(i)) //If the letter is correct at the current index
				{
					
					if (i != 0) //If not first letter
					{
						strBefore = strDisplay.substring(0, i);
					}
					else
					{
						strBefore = "";
					}
					
					if (i != strWordToGuess.length()) //If not last letter
					{
						strAfter = strDisplay.substring((i + 1), strWordToGuess.length()); //From letter to replace + 1, to end of string
					}
					else
					{
						strAfter = "";
					}					
				
					strDisplay = strBefore + currentChar + strAfter;
										
				}
				
			}
			
			if (strDisplay.equalsIgnoreCase(strWordToGuess)) //If got all the letters
			{
				
				strDisplay = getWinningMessage();
				win = true;
				isFinished = true;
				
			}
			
		}			
		
	}
	
	private String getRandomWord()
	{
		
		int randomNumber = 1 + (int) (Math.random() * vecRandomWords.size()); //Gets a random number between 1 and the size of the vector
		return vecRandomWords.elementAt(randomNumber - 1).toString(); //Gets the corrosponding name from the vector
		
	}
	
	public String getDisplayString() //Get display string to show
	{
		return strDisplay;
	}
	
	public boolean isFinished() //Get flag to reset the game
	{
		return isFinished;
	}
	
	public int getAmountGuesses()
	{
		return (amountOfGuesses - 1);
	}
	
	public String getErrorMsg()
	{
		return strError;
	}
	
	private void resetVariables() //Resets everything
	{
		
		isFinished = false;
		strDisplay = "";
		strWordToGuess = "";
		amountOfGuesses = 1;
		
		timeObject.resetTime();
		
	}
	
	private String getWinningMessage()
	{
		return "Congrats, you got \"" + strWordToGuess.toLowerCase() + "\" in " + amountOfGuesses + " amount of guesses";
	}
	
	public String getTime()
	{
		return timeObject.getTimeFormat(1);
	}
	public boolean isWin()
	{
		return win;
	}
	public void setLtdAccess()
	{
		ltd = true;
	}
	
}