package wiev;

import java.awt.Color;


import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import generators.BoxGenerator;
import graphics.Airplane;
import graphics.Worker;
import music.MusicPlayer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainGui {

	private JFrame frmAirport;
	
	private JButton startButton = new JButton("Start");
	private JButton stopButton = new JButton("Stop");
	
	private JTextField textFieldCountOfBoxes;
	private JTextField textFieldCountOfWaits;
	
	private JSlider generatorOfBoxesSlider = new JSlider(1, 3, 2);
	private JSlider worker1Slider = new JSlider(1, 3, 1);
	private JSlider worker2Slider = new JSlider(1, 3, 1);
	private JSlider worker3Slider = new JSlider(1, 3, 1);
	
	private MusicPlayer player;
	
	private Airplane airplane;
	
	private BoxGenerator boxGenerator1;
	private BoxGenerator boxGenerator2;
	private BoxGenerator boxGenerator3;
	
	private Worker worker1;
	private Worker worker2;
	private Worker worker3;
	
	private Thread threadOfWorker1;
	private Thread threadOfWorker2;
	private Thread threadOfWorker3;
	
	private Thread threadOfAirplane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frmAirport.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGui() {
		initialize();
	}

	// ����� ��� ����������� ������� �� �����
	private void graphic() {
		Container c = frmAirport.getContentPane();
		//Graphics2D g = (Graphics2D) c.getGraphics();
		
		c.setBackground(Color.gray);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAirport = new JFrame();
		frmAirport.setTitle("AviaBridg");
		frmAirport.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				graphic();
			}
		});
		frmAirport.setBounds(100, 100, 700, 500);
		frmAirport.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAirport.setResizable(false);
		frmAirport.getContentPane().setLayout(null);
		
		textFieldCountOfBoxes = new JTextField();
		textFieldCountOfBoxes.setEditable(false);
		textFieldCountOfBoxes.setBounds(562, 421, 86, 20);
		frmAirport.getContentPane().add(textFieldCountOfBoxes);
		textFieldCountOfBoxes.setColumns(10);
		textFieldCountOfBoxes.setText("0");
		
		JLabel countOfBoxesLabel = new JLabel("Count of boxes");
		countOfBoxesLabel.setBounds(459, 424, 93, 14);
		frmAirport.getContentPane().add(countOfBoxesLabel);
		
		textFieldCountOfWaits = new JTextField();
		textFieldCountOfWaits.setEditable(false);
		textFieldCountOfWaits.setBounds(339, 421, 86, 20);
		frmAirport.getContentPane().add(textFieldCountOfWaits);
		textFieldCountOfWaits.setColumns(10);
		textFieldCountOfWaits.setText("0");
		
		JLabel countOfWaitsLabel = new JLabel("Count of waits");
		countOfWaitsLabel.setBounds(241, 424, 86, 14);
		frmAirport.getContentPane().add(countOfWaitsLabel);
		
		JSlider airplaneSlider = new JSlider(0, 10, 0);
		airplaneSlider.setBounds(448, 365, 200, 45);
		airplaneSlider.setEnabled(false);
		airplaneSlider.setPaintTicks(true);
		airplaneSlider.setMajorTickSpacing(1);
		airplaneSlider.setPaintLabels(true);
		frmAirport.getContentPane().add(airplaneSlider);
		
		generatorOfBoxesSlider.setBounds(10, 366, 125, 44);
		generatorOfBoxesSlider.setPaintTicks(true);
		generatorOfBoxesSlider.setMajorTickSpacing(1);
		generatorOfBoxesSlider.setPaintLabels(true);
		frmAirport.getContentPane().add(generatorOfBoxesSlider);
		
		worker1Slider.setBounds(145, 366, 89, 44);
		worker1Slider.setPaintTicks(true);
		worker1Slider.setMajorTickSpacing(1);
		worker1Slider.setPaintLabels(true);
		frmAirport.getContentPane().add(worker1Slider);

		worker2Slider.setBounds(241, 366, 89, 44);
		worker2Slider.setPaintTicks(true);
		worker2Slider.setMajorTickSpacing(1);
		worker2Slider.setPaintLabels(true);
		frmAirport.getContentPane().add(worker2Slider);

		worker3Slider.setBounds(336, 366, 89, 44);
		worker3Slider.setPaintTicks(true);
		worker3Slider.setMajorTickSpacing(1);
		worker3Slider.setPaintLabels(true);
		frmAirport.getContentPane().add(worker3Slider);
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startApp();
				stopButton.setEnabled(true);
				startButton.setEnabled(false);
			}
		});
		startButton.setBounds(10, 420, 89, 23);
		frmAirport.getContentPane().add(startButton);
	
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopApp();
				stopButton.setEnabled(false);
			}
		});
		stopButton.setBounds(110, 420, 89, 23);
		stopButton.setEnabled(false);
		frmAirport.getContentPane().add(stopButton);
		
		airplane = new Airplane(frmAirport, 200, 4, textFieldCountOfBoxes, airplaneSlider);
		
		worker1 = new Worker(frmAirport, 70, 80, airplane, textFieldCountOfWaits, worker1Slider);
		worker2 = new Worker(frmAirport, 70, 130, airplane, textFieldCountOfWaits, worker2Slider);
		worker3 = new Worker(frmAirport, 70, 180, airplane, textFieldCountOfWaits, worker3Slider);
		
		JLabel countOfBoxesOnBoardLabel = new JLabel("Count of boxes on board");
		countOfBoxesOnBoardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		countOfBoxesOnBoardLabel.setBounds(448, 340, 200, 14);
		frmAirport.getContentPane().add(countOfBoxesOnBoardLabel);
		
		JLabel boxGenerationSpeedLabel = new JLabel("Box generation speed");
		boxGenerationSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		boxGenerationSpeedLabel.setBounds(10, 341, 125, 14);
		frmAirport.getContentPane().add(boxGenerationSpeedLabel);
		
		JLabel workersSpeedLabel = new JLabel("Workers speed");
		workersSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		workersSpeedLabel.setBounds(145, 341, 280, 14);
		frmAirport.getContentPane().add(workersSpeedLabel);
		
		JSlider musicVolumeSlider = new JSlider(0, 80, 40);
		musicVolumeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				player.setVolume();
			}
		});
		musicVolumeSlider.setPaintTicks(true);
		musicVolumeSlider.setMinorTickSpacing(5);
		musicVolumeSlider.setPaintLabels(true);
		musicVolumeSlider.setMajorTickSpacing(10);
		musicVolumeSlider.setBounds(109, 11, 200, 44);
		frmAirport.getContentPane().add(musicVolumeSlider);
		
		JLabel musicVolumeLabel = new JLabel("Music volume");
		musicVolumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		musicVolumeLabel.setBounds(10, 21, 93, 14);
		frmAirport.getContentPane().add(musicVolumeLabel);
		
		player = new MusicPlayer(musicVolumeSlider);
		
	}

	// ����� ������� ������ ������
	protected void startApp() {
		startGenerateBoxes();
		startWorkers();
		startAirplane();
		startPlayer();
	}
	
	// ����� ��������� �� ������� ������ ��������� �����
	// ϳ��� ��������� ������, ������ ��������� ��� ��, ���� ���������� �� ����������� ����� ���������� ������ ������
	// ������ ���� ��� ����������
	private void startPlayer() {
		Thread playerThread = new Thread(player);
		
		playerThread.start();
		
		new Thread() {
			public void run() {
				try {
					playerThread.join();
					stopApp();
					stopButton.setEnabled(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	// ����� ���������� ������ ������
	protected void stopApp() {
		// ������� ���������� ����������
		boxGenerator1.setFlag(false);
		boxGenerator2.setFlag(false);
		boxGenerator3.setFlag(false);
		
		// ������� ��������� ����������
		player.stopMusic();
		
		// � ������ ������ ������ ��������� ������ ������ ����������, ���� ���� ����������� ����, �� ����� ���������� �� ���� 
		// ������ ���������� ������ ������ ����� �� ������ ������ ������ ����� ��������
		new Thread() {
			public void run() {
				try {
					threadOfWorker1.join();
					threadOfWorker2.join();
					threadOfWorker3.join();
					airplane.setFlagOfEnd(true);
					synchronized(airplane) {
						airplane.notify();
					}
					threadOfAirplane.join();
					startButton.setEnabled(true);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}.start();
		
	}

	// ����� ��������� ������ ������ ����� �� ���� ������
	private void startAirplane() {
		
		airplane.setFlagOfEnd(false);
		
		threadOfAirplane = new Thread(airplane);
		 
		threadOfAirplane.start();
		
	}

	// ����� ��������� ������ ������ ���������� �� �� ������
	private void startWorkers() {
		
		threadOfWorker1 = new Thread(worker1);
		threadOfWorker2 = new Thread(worker2);
		threadOfWorker3 = new Thread(worker3);
		
		worker1.setBoxGenerator(boxGenerator1);
		worker2.setBoxGenerator(boxGenerator2);
		worker3.setBoxGenerator(boxGenerator3);
		 
		threadOfWorker1.start();
		threadOfWorker2.start();
		threadOfWorker3.start();
		
	}

	// ����� ��������� ���������� ����������, ��������� �� ������ �� ������
	private void startGenerateBoxes() {
		
		boxGenerator1 = new BoxGenerator(frmAirport, -40, 80, worker1Slider, generatorOfBoxesSlider);
		boxGenerator2 = new BoxGenerator(frmAirport, -40, 130, worker2Slider, generatorOfBoxesSlider);
		boxGenerator3 = new BoxGenerator(frmAirport, -40, 180, worker3Slider, generatorOfBoxesSlider);
		 
		Thread threadOfBoxGeneretor1 = new Thread(boxGenerator1);
		Thread threadOfBoxGenerator2 = new Thread(boxGenerator2);
		Thread threadOfBoxGenerator3 = new Thread(boxGenerator3);
		 
		threadOfBoxGeneretor1.start();
		threadOfBoxGenerator2.start();
		threadOfBoxGenerator3.start();
		
	}
}
