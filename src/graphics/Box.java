package graphics;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JSlider;

public class Box extends AbstractSubject{
	private JSlider slider1;
	private JSlider slider2;
	private int boxStatus = -1;			// -1 - контейнер рухається на площадку від генератора 
										//  0 - контейнер перебуває на площадці, та чекає поки його забере працівник
										//  1 - працівник переносить контейнер до літака 
	
	{
		h = 40;
		w = 40;
	}
	
    Image image = getImage("box.jpg");
	
	public Box(JFrame frame, int x, int y, JSlider slider1, JSlider slider2) {
		super();
		this.frame = frame;
		this.x = x;
		this.y = y;
		this.slider1 = slider1;
		this.slider2 = slider2;
		setImage(image);
	}
	
	public void setBoxStatus(int boxStatus) {
		this.boxStatus = boxStatus;	
		// Повідомляємо про зміну статусу контейнера
		synchronized (this) {
			notifyAll();
		}
	}
	
	public int getBoxStatus() {
		return boxStatus;
	}

	@Override
	public void run() {
		// Встановлюємо початкову позицію контейнеру
		label.setBounds(x, y, w, h); 
		frame.getContentPane().add(label);
		frame.getContentPane().setComponentZOrder(label, 1);
		
		// Переміщення контейнеру до площадки 
		
		int xStep = 1;
		
		while(xStep <= 70) {
			
			label.setBounds(x + xStep, y, w, h);
			
			try {
				if(slider2.getValue() == 1) {
					Thread.sleep(30);
				}else if(slider2.getValue() == 2) {
					Thread.sleep(10);
				}else{
					Thread.sleep(2);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			xStep++;
		}
		
		// Змінюємо статус контейнера та повідомляємо про це
		synchronized (this) {
			setBoxStatus(0);
		}
		
		// Процесс очікування, коли контейнер забере працівник
		synchronized (this) {
			while(this.getBoxStatus() != 1) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Переміщення контейнеру до літака
		
		label.setBounds(x + 140, y, w, h); 
		
		xStep = 1;
		
		while(x + xStep + 140 <= 340) {
			
			label.setBounds(x + 140 + xStep, y, w, h);
			
			try {
				if(slider1.getValue() == 1) {
					Thread.sleep(10);
				}else if(slider1.getValue() == 2) {
					Thread.sleep(6);
				}else{
					Thread.sleep(3);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			xStep++;
		}
		
		
	}

}
