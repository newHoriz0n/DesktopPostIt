package gfx;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class DesktopPostIt extends JFrame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7945728226601305289L;
	private JTextArea text;

	public DesktopPostIt() {

		laden("postIts.ser");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event1) {
				speichern();
			}
		});

	}

	public DesktopPostIt(DesktopPostIt so) {
		this.text = so.getText();
		loadGUI(so);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event1) {
				speichern();
			}
		});
	}

	public DesktopPostIt(int neu) {
		this.text = new JTextArea();
		loadGUI(null);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event1) {
				speichern();
			}
		});
	}

	private void laden(String saveURL) {
		DesktopPostIt me = null;

		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(saveURL);
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			if (obj instanceof DesktopPostIt) {
				DesktopPostIt so = (DesktopPostIt) obj;
				me = new DesktopPostIt(so);
			}
		} catch (IOException e) {
			me = new DesktopPostIt(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
		}

		me.requestFocus();
	}

	protected void speichern() {

		// Standard
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream("postIts.ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
		}

		// Backup
		try {

			fos = new FileOutputStream("backup/" + System.currentTimeMillis() + ".ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
				}
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
		}

	}

	private void loadGUI(DesktopPostIt dp) {
		setVisible(true);

		if (dp != null) {
			setSize(dp.getSize());
			setLocation(dp.getLocation());
		} else {
			setSize(200, 500);
		}

		Timer t = new Timer();
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				speichern();
			}
		};
		t.schedule(tt, 0, 60000); // Auto-Speichert jede Minute

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		add(text);

		revalidate();

	}

	public JTextArea getText() {
		return text;
	}

	public static void main(String[] args) {
		DesktopPostIt pse = new DesktopPostIt();
		pse.requestFocus();
	}

}
