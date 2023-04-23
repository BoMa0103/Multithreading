package graphics;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;

import generators.BoxGenerator;

public class Worker extends AbstractSubject{
	
	private JTextField textFieldCountOfWaits;
	private JSlider sliderOfSpeed;
	private Box currentBox;
	
	private BoxGenerator boxGenerator;
	private Airplane airplane;
	
	{
		h = 40;
		w = 40;
	}
	
    Image image_1 = getImage("worker_1.png");
    Image image_2 = getImage("worker_2.png");
    
	
	public Worker(JFrame frame, int x, int y, Airplane airplane, JTextField textFieldCountOfWaits, JSlider sliderOfSpeed) {
		super();
		this.frame = frame;
		this.x = x;
		this.y = y;
		this.airplane = airplane;
		this.textFieldCountOfWaits = textFieldCountOfWaits;
		// ������ ���������� �� �������� ����������
		setImage(image_1);
		label.setBounds(x, y, w, h); 
		frame.getContentPane().add(label);
		this.sliderOfSpeed = sliderOfSpeed;
	}
	
	public void setBoxGenerator(BoxGenerator boxGenerator) {
		this.boxGenerator = boxGenerator;
	}

	@Override
	public void run() {
		
		int i = 0;
		boolean flagOfMove = true, flagImage = true;
		
		// ������� ����� ������� ������������, ��� ����, ��� ��������� ����� �������� ���������
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		while(true) {
			
			// ���� ��������� �������� ��� �������� � ������������
			if(i == 0) {
				// ������� �������� ����� �� �����������
				flagOfMove = true;
				
				// ������� �������� ���������� �������� ����������
					
				// ������ �������� �� ���������� �������� ����� ��� ��������
				synchronized(airplane) {
					// ���� ���� �� ������� �� ���� ������ = 0 ������
					while(airplane.getStatus() == 0) {
						try {
							airplane.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
	
				}
			
				// ������ ���������� ���������� ��� ���������� �� �����
				synchronized(boxGenerator.getBox()) {
					// ���� ������ ���������� �� ������� 0, ���� �� �� ��������� �� ��������
					while(boxGenerator.getBox().getBoxStatus() != 0) {
						// �������� �� ����������� �� ����������, ���� � �� �������� � ������ 
						if(boxGenerator.getBox().getBoxStatus() != -1) {
							return;
						}
						// ���������� ���� ������� ����������
						try {
							boxGenerator.getBox().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// �����'������� ��� ��������
					currentBox = boxGenerator.getBox();
					// ������ ���������� ������ 1 - ���������� �� �����
					boxGenerator.getBox().setBoxStatus(1);
				}
				
			}
			
			// ���� ��������� �������� ��� ���� ������������ �����
			if(x + i == 300) {
				// ������� �������� ����� �� ����������� 
				flagOfMove = false;
				
				// ������� �������� ���������� �������� ����������
				
				// ������ ���������� ����� �� ������������ ���������� 
				synchronized(airplane) {
					// ���� ���� ����������� ����������
					while(airplane.getStatus() == 0) {
						textFieldCountOfWaits.setText(Integer.toString(Integer.parseInt(textFieldCountOfWaits.getText()) + 1));
						try {
							airplane.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					// ������ ��������� �� ����
					airplane.boxAdd();
					
				}
				
				frame.getContentPane().remove(currentBox.getLabel());
				
				frame.repaint();
			}
			
			// ����������� ����������
			
			// ���� �������� ���� ����������
			if(flagOfMove) {
				i++;
			}else {
				i--;
			}
			
			// ���� ������� (������� ����������)
			if(i % 20 == 0 && flagImage) {
				setImage(image_1);
				flagImage = false;
			}else if(i % 20 == 0) {
				setImage(image_2);
				flagImage = true;
			}
			
			// ���������� �� �� �� ����� 
			label.setBounds(x + i, y, w, h);
			try {
				if(sliderOfSpeed.getValue() == 1) {
					Thread.sleep(10);
				}else if(sliderOfSpeed.getValue() == 2) {
					Thread.sleep(6);
				}else{
					Thread.sleep(3);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
