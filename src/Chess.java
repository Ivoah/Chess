import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

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
	private JProgressBar progress;
	
	private Board board = null;
	
	private Board.Color currentPlayer;
	private AI ai;
	
	final JFileChooser fc = new JFileChooser();

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

		progress = new JProgressBar();
		progress.setIndeterminate(true);
		
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
		
		panel = new JPanel(new GridLayout(8, 8));
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton btn = new JButton();
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
		while (true) {
			try {
				int difficulty = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter AI difficulty"));
				if (difficulty < 1 || difficulty > 5) {
					JOptionPane.showMessageDialog(frame, "Enter a number between 1 and 5");
					continue;
				}

				ai = new AI(Board.Color.Black, difficulty);
				break;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(frame, "Please enter an integer!");
			}
		}
		
		currentPlayer = Board.Color.White;
		selected = null;
		board = new Board();
		
		updateBoard();
	}

	/**
	 * Handler for button presses
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "New Game") {
			newGame();
		} else if (e.getActionCommand() == "Open...") {
			if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				try {
					Scanner moves = new Scanner(f);
					board = new Board();
					while (moves.hasNextLine()) {
						board.move(moves.nextLine());
					}
					updateBoard();
					moves.close();
				} catch (FileNotFoundException e1) {
					// Won't happen
				}
			}
		} else if (e.getActionCommand() == "Save...") {
			if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				try {
					PrintWriter pw = new PrintWriter(f);
					for (String move : board.history) {
						pw.println(move);
					}
					pw.close();
				} catch (FileNotFoundException e1) {
					// Won't happen
				}
			}
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
					//currentPlayer = Board.otherColor(currentPlayer);
					board.move(selected, p);
					updateBoard();
					if (board.checkDraw(Board.otherColor(currentPlayer))) {
						if (JOptionPane.showConfirmDialog(frame, "Draw!\nPlay again?", "Draw!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							newGame();
						} else {
							System.exit(0);
						}
					} else if (board.checkCheckmate(Board.otherColor(currentPlayer))) {
						if (JOptionPane.showConfirmDialog(frame, currentPlayer + " won!\nPlay again?", currentPlayer + " won!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							newGame();
						} else {
							System.exit(0);
						}
					} else {
						board = ai.makeMove(board);
						updateBoard();
						if (board.checkDraw(currentPlayer)) {
							if (JOptionPane.showConfirmDialog(frame, "Draw!\nPlay again?", "Draw!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								newGame();
							} else {
								System.exit(0);
							}
						} else if (board.checkCheckmate(currentPlayer)) {
							if (JOptionPane.showConfirmDialog(frame, ai.getColor() + " won!\nPlay again?", ai.getColor() + " won!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								newGame();
							} else {
								System.exit(0);
							}
						}
					}
				} else {
					updateBoard();
				}
				selected = null;
			}
		}
		frame.repaint();
	}
	
	/**
	 * Synchronize internal board state and GUI
	 */
	private void updateBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton button = getButton(j, i);
				
				button.setText(board.getPiece(j, i).toString());
				if (board.getColor(j, i) == currentPlayer && board.getMoves(j, i).size() > 0) button.setEnabled(true);
				else button.setEnabled(false);
			}
		}
	}
	
	/**
	 * Get the button at Vec2(x, y)
	 * @param pos Vec2(x, y)
	 * @return the JButton at x, y
	 */
	private JButton getButton(Vec2 pos) {
		return getButton(pos.getX(), pos.getY());
	}
	
	private JButton getButton(int x, int y) {
		return (JButton)panel.getComponentAt(x*T_WIDTH + + T_WIDTH/2, y*T_HEIGHT + T_HEIGHT/2);
	}

	/**
	 * Kickstart the program
	 */
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		new Chess();
	}
}
