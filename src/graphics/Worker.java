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
		// Змінити зображення на стоячого працівника
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
		
		// Зупинка перед першими контейнерами, для того, щоб генератор встиг створити контейнер
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		while(true) {
			
			// Коли працівник перебуває біля площадки з контейнерами
			if(i == 0) {
				// Змінюємо напрямок шляху на протилежний
				flagOfMove = true;
				
				// Потрібно вставити зображення стоячого працівника
					
				// Процес перевірки та очікування прибуття літака біля площадки
				synchronized(airplane) {
					// Доки літак не прилетів та його статус = 0 чекаємо
					while(airplane.getStatus() == 0) {
						try {
							airplane.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
	
				}
			
				// Процес очікування контейнеру для переміщення до літака
				synchronized(boxGenerator.getBox()) {
					// Доки статус контейнера не дорівнює 0, отже він не знаходить на площадці
					while(boxGenerator.getBox().getBoxStatus() != 0) {
						// Перевірка чи створюються ще контейнери, якщо ні то виходимо з потоку 
						if(boxGenerator.getBox().getBoxStatus() != -1) {
							return;
						}
						// Очікування зміни статусу контейнеру
						try {
							boxGenerator.getBox().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// Запам'ятовуємо цей конейнер
					currentBox = boxGenerator.getBox();
					// Задаємо контейнеру статус 1 - переміщення до літака
					boxGenerator.getBox().setBoxStatus(1);
				}
				
			}
			
			// Коли працівник перебуває біля місця завантаження літака
			if(x + i == 300) {
				// Змінюємо напрямок шляху на протилежний 
				flagOfMove = false;
				
				// Потрібно вставити зображення стоячого працівника
				
				// Процес очікування літака та завантаження контейнерів 
				synchronized(airplane) {
					// Доки літак переправляє контейнери
					while(airplane.getStatus() == 0) {
						textFieldCountOfWaits.setText(Integer.toString(Integer.parseInt(textFieldCountOfWaits.getText()) + 1));
						try {
							airplane.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					// Додати контейнер на літак
					airplane.boxAdd();
					
				}
				
				frame.getContentPane().remove(currentBox.getLabel());
				
				frame.repaint();
			}
			
			// Пересування працівника
			
			// Вибір намрямку руху працівника
			if(flagOfMove) {
				i++;
			}else {
				i--;
			}
			
			// Зміна анімації (малюнків переміщення)
			if(i % 20 == 0 && flagImage) {
				setImage(image_1);
				flagImage = false;
			}else if(i % 20 == 0) {
				setImage(image_2);
				flagImage = true;
			}
			
			// Переміщення від та до літака 
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
