package music;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JSlider;


public class MusicPlayer implements Runnable{
	
	private JSlider volumeSlider;
	private Clip clip;
	private FloatControl volumeControl;
	private boolean playing;
	
	public MusicPlayer(JSlider slider) {
		super();
		try {
			URL url = MusicPlayer.class.getResource("song.wav");
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.addLineListener(new Listener());
			volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.volumeSlider = slider;
	}
	
	// ����� ������� ���������� ������
	public void playMusic() {
		clip.setFramePosition(0);
		setVolume();
		clip.start();
	}
	
	// ����� ������������ ������� �����
	public void setVolume() {
		volumeControl.setValue((volumeSlider.getValue())*(1) + volumeControl.getMinimum());
	}

	// ����� ������� ����������
	public void stopMusic() {
		clip.stop();
	}

	@Override
	public void run() {
		playMusic();
		playing = true;
		synchronized (clip) {
			try {
				while(playing) {
					clip.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// ��������� ����� ������� 
	private class Listener implements LineListener {
		// �����, ���� ��������� ��� ���������� ����������
		public void update(LineEvent ev) {
			if (ev.getType() == LineEvent.Type.STOP) {
				playing = false;
				synchronized(clip) {
					clip.notify();
				}
			}
		}
	}

}
