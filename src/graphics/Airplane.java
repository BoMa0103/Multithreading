package graphics;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class Airplane extends AbstractSubject{
	
	private JTextField textFieldCountOfBoxes;
	private JSlider sliderCountOfBoxes;
	
	private int maxCountOfBoxes = 10;		// Максимальна кількість контейнерів на літаку
	private int currentCountOfBoxes = 0;	// Поточна кількість контейнерів на літаку
	
	private boolean flagOfEnd = false;
	
	private int status = 1;					// Статут літака: 1 - готовий до заповнення, 0 - переправляє контейнери
	
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
		// Повідомляємо літак про новий контейнер
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
				// Доки на кількість контейнерів на борту не більше максимальної кількості
				while(currentCountOfBoxes < maxCountOfBoxes) {
					// Якщо роботу потоків закінчено, то не чекаємо наповнення літака
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
			
			// Імітація перельоту. Відліт
			
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
			
			// Імітація перельоту. Приліт
			
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
			
			// Повідомляємо всім працівникам, що літак готовий до завантаження
			synchronized (this){
				this.notifyAll();
			}
		}
		
	}
}
