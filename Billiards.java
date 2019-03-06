package billar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("serial")

/**
 * Clase Billiards para la Pr�ctica 02 de Programaci�n Concurrente y de Tiempo Real.
 * 
 * @author Lisa Can� y  Noelia Ubierna
 */
public class Billiards extends JFrame {

	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;

	//TODO update with number of group label. See practice statement.
	private final int N_BALL = 6;
	private Ball[] balls;

	protected Thread[] threads;
	protected ExecutorService executor;

	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Inicializaci�n de las bolas.
	 */
	private void initBalls() {
		//TODO init balls
		
		// Inicializar el vector de las bolas.
		balls = new Ball[N_BALL];
		
		// Crear de las bolas.
		for(int i=0; i < N_BALL; i++) {
			balls[i] = new Ball();
		}
		
		// Establecer las bolas en el tablero.
		board.setBalls(balls);
	}
	
	/**
	 * M�todo utilidad.
	 */
	private Thread createThread(final Ball ball) {

		Runnable loop = new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						ball.move();
						board.repaint();
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					return;
				}

			}

		};
		return new Thread(loop);
	}

	/**
	 * Clase StartListener para ejecutar el c�digo cuando se pulsa el bot�n de
	 * start.
	 * 
	 * @author Lisa Can� y Noelia Ubierna
	 *
	 */
	private class StartListener implements ActionListener {

		/**
		 * M�todo que se ejecuta cuando se pulsa el bot�n start.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//TODO Code is executed when start button is pushed
			
			executor = Executors.newFixedThreadPool(N_BALL);
			
			if(threads == null) {
				
				Ball[] balls = new Ball[N_BALL];
				
				for(int i=0; i < N_BALL; i++) {
					balls[i] = new Ball();
				}
				board.setBalls(balls);
				threads = new Thread[N_BALL];
				for(int i=0; i < N_BALL; i++) {
					threads[i] = createThread(balls[i]);
					executor.execute(threads[i]);
				}
			}
		}
	}

	/**
	 * Clase StopListener para ejecutar el c�digo cuando se pulsa el bot�n de stop.
	 * 
	 * @author Lisa Can� y Noelia Ubierna
	 *
	 */
	private class StopListener implements ActionListener {

		/**
		 * M�todo que se ejecuta cuando se pulsa el bot�n stop.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//TODO Code is executed when stop button is pushed
			if (threads != null) {
				executor.shutdownNow();
				threads = null;
			}

		}
	}

	public static void main(String[] args) {
		new Billiards();
	}
}