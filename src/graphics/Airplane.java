package graphics;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class Airplane extends AbstractSubject{
	
	private JTextField textFieldCountOfBoxes;
	private JSlider sliderCountOfBoxes;
	
	private int maxCountOfBoxes = 10;		// ����������� ������� ���������� �� �����
	private int currentCountOfBoxes = 0;	// ������� ������� ���������� �� �����
	
	private boolean flagOfEnd = false;
	
	private int status = 1;					// ������ �����: 1 - ������� �� ����������, 0 - ����������� ����������
	
	{
		h = 300;
		w = 700;
	}

    Image image = getImage("airplane.png");
	
	public Airplane(JFrame frame, int x, int y, JTextField textFieldCountOfBoxes, JSlider sliderCountOfBoxes) {
		super();
		this.frame = frame;
		this.x = x;
		this.y = y;
		setImage(image);
		label.setBounds(x, y, w, h); 
		frame.getContentPane().add(label);
		frame.getContentPane().setComponentZOrder(label, 0);
		this.textFieldCountOfBoxes = textFieldCountOfBoxes;
		this.sliderCountOfBoxes = sliderCountOfBoxes;
	}
	
	public void boxAdd() {
		currentCountOfBoxes++;
		// ����������� ���� ��� ����� ���������
		synchronized (this) {
			notifyAll();
		}
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setFlagOfEnd(boolean flagOfEnd) {
		this.flagOfEnd = flagOfEnd;
	}
	
	public boolean getFlagOfEnd() {
		return flagOfEnd;
	}

	@Override
	public void run() {
		
		while(!getFlagOfEnd()) {
			synchronized (this) {
				// ���� �� ������� ���������� �� ����� �� ����� ����������� �������
				while(currentCountOfBoxes < maxCountOfBoxes) {
					// ���� ������ ������ ��������, �� �� ������ ���������� �����
					if(getFlagOfEnd()) {
						break;
					}
					try {
						this.wait();
						sliderCountOfBoxes.setValue(currentCountOfBoxes);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				status = 0;
			}
			
			// ������� ���������. ³���
			
			int xStep = 0;
			
			while(xStep + x < 700) {
				label.setBounds(x + xStep, y, w, h);
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				xStep++;
				
			}
			
			textFieldCountOfBoxes.setText(Integer.toString(Integer.parseInt(textFieldCountOfBoxes.getText()) + currentCountOfBoxes));
			
			currentCountOfBoxes = 0;
			
			sliderCountOfBoxes.setValue(currentCountOfBoxes);
			
			// ������� ���������. �����
			
			while(xStep + x > 200) {
				label.setBounds(x + xStep, y, w, h);
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				xStep--;
			}
			
			status = 1;
			
			// ����������� ��� �����������, �� ���� ������� �� ������������
			synchronized (this){
				this.notifyAll();
			}
		}
		
	}
}
