package graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class AbstractSubject implements Runnable{
	
	protected JFrame frame;
	
	protected int h, w, x, y;
    
	protected JLabel label = new JLabel();

	protected Image getImage(String way) {
		URL url = Box.class.getResource(way);
		BufferedImage buffImg = null;
		
		try {
			buffImg = ImageIO.read(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	    return buffImg.getScaledInstance(w, h, Image.SCALE_DEFAULT);
	}
	
	protected void setImage(Image image) {
		this.label.setIcon(new ImageIcon(image));
	}
	
	public JLabel getLabel() {
		return this.label;
	}
}
