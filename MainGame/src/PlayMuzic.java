import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;


public class PlayMuzic {
	
	private AudioData audiodata;
	private AudioStream audioStream,as;
	private ContinuousAudioDataStream continuousaudiostream;
	private Clip clip;
	private AudioInputStream audioIn;
	
public void PlayMuzicLoop(String filepath) throws Exception
{
	 
	   FileInputStream in = new FileInputStream(filepath);

	    // create an audiostream from the inputstream
	    audioStream = new AudioStream(in);
	    audiodata=audioStream.getData();			
	    
	    audioStream = null;						//initialize default value as null
	    continuousaudiostream = null;
	    
	    // play the audio clip with the audioplayer class
	    continuousaudiostream = new ContinuousAudioDataStream(audiodata);

	    AudioPlayer.player.start(continuousaudiostream);
	
}

public void PlayUrlLoop(URL url)
{
	try {
		audioIn = AudioSystem.getAudioInputStream(url);
		clip = AudioSystem.getClip();
		clip.open(audioIn);
	} catch (UnsupportedAudioFileException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (LineUnavailableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	clip.start();
	clip.loop(Clip.LOOP_CONTINUOUSLY); 
	
	
	
 }
public void PlayUrlOnce(URL url) 
{
	try {
		as=new AudioStream(url.openStream());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	AudioPlayer.player.start(as);
}


public void PlayOnce(String filename) throws Exception
{
	FileInputStream in = new FileInputStream(filename);
	AudioStream as=new AudioStream(in);
	AudioPlayer.player.start(as);

}
public void StopMuzic()
{
	 if (audioStream != null)					//if audioStream is still playing a music, close it
	      AudioPlayer.player.stop(audioStream);
	    if (continuousaudiostream != null)		//if continuousaudiostream is still playing a music file close it 
	      AudioPlayer.player.stop(continuousaudiostream);
if(as != null)
{
	AudioPlayer.player.stop(as);
}
if(audioIn != null)
{
	clip.stop();
	AudioPlayer.player.stop(audioIn);
}
}
}
