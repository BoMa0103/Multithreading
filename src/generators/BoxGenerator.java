package generators;

import javax.swing.JFrame;

import javax.swing.JSlider;

import graphics.Box;

public class BoxGenerator implements Runnable{

	private JFrame frame;
	private JSlider sliderOfWorkerSpeed;
	private JSlider generatorOfBoxesSlider;
	private int x, y;
	private Box box;
	private boolean flag = true;
	
	public BoxGenerator(JFrame frame, int x, int y, JSlider sliderOfWorkerSpeed, JSlider generatorOfBoxesSlider) {
		super();
		this.frame = frame;
		this.x = x;
		this.y = y;
		this.sliderOfWorkerSpeed = sliderOfWorkerSpeed;
		this.generatorOfBoxesSlider = generatorOfBoxesSlider;
	}
	
	public Box getBox() {
		return box;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public boolean getFlag() {
		return this.flag;
	}

	@Override
	public void run() {
		
		while(getFlag()) {
			
			// Створення нового контейнеру
			box = new Box(frame, x, y, sliderOfWorkerSpeed, generatorOfBoxesSlider);
			
			// Іммітація доствки/вигрузки контейнера
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Thread thread = new Thread(box);
			
			thread.start();
			
			 // Постійна перевірка статусу коробки (0 - стоїть на майданчику, 1 - переміщується працівником, -1 - переміщується до платформи)
			synchronized (box) {
				// Доки контейнер переміщується до площадки або стоїть на ній, чекаємо
				while(box.getBoxStatus() == 0 || box.getBoxStatus() == -1) {
					try {
						box.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
			}
			// Контейнер забрали, можна знову генерувати новий
		}
		
	}

}
