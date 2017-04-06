import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chess implements ActionListener {

	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	
	private static final int T_WIDTH = WIDTH/8;
	private static final int T_HEIGHT = HEIGHT/8;
	
	private Vec2 selected;
	private JFrame frame;
	private JPanel panel;

	private Board board = null;

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

		frame.add(panel);
		frame.pack();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "New Game") {
			newGame();
		} else {
			JButton btn = (JButton) e.getSource();
			int x = btn.getActionCommand().charAt(0) - '0';
			int y = btn.getActionCommand().charAt(1) - '0';
			System.out.println("Pressed: " + x + " " + y);

			if (selected == null) {
				if (btn.getText() != " ") {
					selected = new Vec2(x, y);
					for (Component jbutt : panel.getComponents()) {
						jbutt.setEnabled(false);
					}
					System.out.println("Moves:");
					for (Vec2 move : board.getMoves(x, y)) {
						System.out.println("\t" + move);
						getButton(move).setEnabled(true);
						/* JOptionPane.showOptionDialog(frame, "What do you want to promote your pawn to?",
									"Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
									new String[] { "Queen", "Bishop", "Knight", "Rook" }, "Queen");
						*/
					}
				}
			} else {
				if (!selected.equals(new Vec2(x, y))) {
					btn.setText(getButton(selected).getText());
					getButton(selected).setText(" ");
					board.move(selected, new Vec2(x, y));
				}
				selected = null;
				for (Component jbutt : panel.getComponents()) {
					jbutt.setEnabled(true);
				}
			}
		}
		frame.repaint();
	}
	
	private JButton getButton(Vec2 pos) {
		return (JButton)panel.getComponentAt(pos.getX()*T_WIDTH + + T_WIDTH/2, pos.getY()*T_HEIGHT + T_HEIGHT/2);
	}

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		new Chess();
	}
}
