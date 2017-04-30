import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main Chess class
 * Handles players, turns, and winning
 */
public class Chess implements ActionListener {

	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	
	// Tile WIDTH and HEIGHT
	private static final int T_WIDTH = WIDTH/8;
	private static final int T_HEIGHT = HEIGHT/8;
	
	private Vec2 selected;
	private JFrame frame;
	private JPanel panel;

	private Board board = null;

	/**
	 * Chess constructor
	 * 
	 * Sets up the swing GUI and makes a new Board
	 */
	public Chess() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Chess");

		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		int cmd = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		JMenuItem open = new JMenuItem("Open...");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, cmd));
		open.addActionListener(this);
		menu.add(open);

		JMenuItem save = new JMenuItem("Save...");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, cmd));
		save.addActionListener(this);
		menu.add(save);

		menu.add(new JSeparator());
		
		JMenuItem new_game = new JMenuItem("New Game");
		new_game.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, cmd));
		new_game.addActionListener(this);
		menu.add(new_game);

		menubar.add(menu);
		frame.setJMenuBar(menubar);
		
		newGame();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Reset the game
	 * 
	 * reset the swing panel and make a new Board
	 */
	private void newGame() {
		frame.getContentPane().removeAll();
		selected = null;
		board = new Board();
		panel = new JPanel(new GridLayout(8, 8));
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton btn = new JButton(Character.toString(board.getPiece(j, i)));
				btn.setActionCommand(j + "" + i);
				btn.setFont(new Font("", Font.PLAIN, 42));
				btn.addActionListener(this);
				btn.setFocusable(false);
				panel.add(btn);
			}
		}

		frame.add(panel, BorderLayout.CENTER);
		
		JPanel letters = new JPanel(new GridLayout(1, 8));
		JPanel numbers = new JPanel(new GridLayout(8, 1));
		
		for (int i = 0; i < 8; i++) {
			letters.add(new JLabel(Character.toString((char)('a' + i)), SwingConstants.CENTER));
			numbers.add(new JLabel(Integer.toString(8 - i), SwingConstants.CENTER));
		}
		
		frame.add(letters, BorderLayout.SOUTH);
		//frame.add(letters, BorderLayout.NORTH);
		frame.add(numbers, BorderLayout.WEST);
		//frame.add(numbers, BorderLayout.EAST);
		frame.pack();
	}

	/**
	 * Handler for button presses
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "New Game") {
			newGame();
		} else {
			JButton btn = (JButton) e.getSource();
			int x = btn.getActionCommand().charAt(0) - '0';
			int y = btn.getActionCommand().charAt(1) - '0';
			Vec2 p = new Vec2(x, y);

			if (selected == null) {
				if (btn.getText() != " ") {
					selected = p;
					for (Component jbutt : panel.getComponents()) {
						jbutt.setEnabled(false);
					}
					btn.setEnabled(true);
					for (Vec2 move : board.getMoves(p)) {
						getButton(move).setEnabled(true);
						/* JOptionPane.showOptionDialog(frame, "What do you want to promote your pawn to?",
									"Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
									new String[] { "Queen", "Bishop", "Knight", "Rook" }, "Queen");
						*/
					}
				}
			} else {
				if (!selected.equals(p)) {
					btn.setText(getButton(selected).getText());
					getButton(selected).setText(" ");
					board.move(selected, p);
					System.out.println(selected + " to " + p);
				}
				selected = null;
				for (Component jbutt : panel.getComponents()) {
					jbutt.setEnabled(true);
				}
			}
		}
		frame.repaint();
	}
	
	/**
	 * Get the button at Vec2(x, y)
	 * @param pos Vec2(x, y)
	 * @return the JButton at x, y
	 */
	private JButton getButton(Vec2 pos) {
		return (JButton)panel.getComponentAt(pos.getX()*T_WIDTH + + T_WIDTH/2, pos.getY()*T_HEIGHT + T_HEIGHT/2);
	}

	/**
	 * Kickstart the program
	 */
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		new Chess();
	}
}
