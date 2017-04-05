import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chess implements ActionListener {
	private static final String[][] start = new String[][] {
		{"♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"},
		{"♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟"},
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"",  "",  "",  "",  "",  "",  "",  "" },
		{"♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙"},
		{"♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"}
	};
	private static enum Color {WHITE, BLACK, NEUTRAL};

	private JButton[][] board = new JButton[8][8];
	private JButton selected;
	private JFrame frame;

	public Chess() {
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
	
	private ArrayList<int[]> getMoves(JButton piece) {
		ArrayList<int[]> moves = new ArrayList<>();
		switch (piece.getText().charAt(0)) {
		case '♜':
		case '♖':
			for (int i = -7; i < 8; i++) {
				if (i == 0) continue;
				moves.add(new int[]{0, i});
				moves.add(new int[]{i, 0});
			}
		    break;
		case '♞':
		case '♘':
			moves.add(new int[]{ 1, -2});
			moves.add(new int[]{ 2, -1});
			moves.add(new int[]{ 2,  1});
			moves.add(new int[]{ 1,  2});
			moves.add(new int[]{-1,  2});
			moves.add(new int[]{-2,  1});
			moves.add(new int[]{-2, -1});
			moves.add(new int[]{-1, -2});
		    break;
		case '♝':
		case '♗':
			for (int i = -7; i < 8; i++) {
				if (i == 0) continue;
				moves.add(new int[]{i,  i});
				moves.add(new int[]{i, -i});
			}
		    break;
		case '♛':
		case '♕':
			for (int i = -7; i < 8; i++) {
				if (i == 0) continue;
				moves.add(new int[]{0,  i});
				moves.add(new int[]{i,  0});
				moves.add(new int[]{i,  i});
				moves.add(new int[]{i, -i});
			}
		    break;
		case '♚':
		case '♔':
			for (int i = -1; i < 2; i++) {
				if (i == 0) continue;
				moves.add(new int[]{0,  i});
				moves.add(new int[]{i,  0});
				moves.add(new int[]{i,  i});
				moves.add(new int[]{i, -i});
			}
		    break;
		case '♟':
			moves.add(new int[]{0, 1});
			break;
		case '♙':
			moves.add(new int[]{0, -1});
		    break;
		}
		return moves;
	}
	
	private Color getColor(JButton piece) {
		if (piece.getText().equals("")) {
			return Color.NEUTRAL;
		}
		switch (piece.getText().charAt(0)) {
			case '♖':
			case '♘':
			case '♗':
			case '♕':
			case '♔':
			case '♙':
			    return Color.WHITE;
			case '♜':
			case '♞':
			case '♝':
			case '♛':
			case '♚':
			case '♟':
			    return Color.BLACK;
			default:
				return Color.NEUTRAL;
		}
	}
	
	private void newGame() {
		frame.getContentPane().removeAll();
		selected = null;
		JPanel panel = new JPanel(new GridLayout(8, 8));
		panel.setPreferredSize(new Dimension(500, 500));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JButton btn = new JButton(start[i][j]);
				btn.setActionCommand(j + "" + i);
				btn.setFont(new Font("", Font.PLAIN, 42));
				btn.addActionListener(this);
				btn.setFocusable(false);
				board[i][j] = btn;
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
			JButton btn = (JButton)e.getSource();
			int x = btn.getActionCommand().charAt(0) - '0';
			int y = btn.getActionCommand().charAt(1) - '0';
			
			if (selected == null) {
				if (btn.getText() != "") {
					selected = btn;
					for (JButton[] row : board) {
						for (JButton b : row) {
							b.setEnabled(false);
						}
					}
					for (int[] move : getMoves(btn)) {
						int c = x + move[0];
						int r = y + move[1];
						if (c < 8 && c >= 0 && r < 8 && r >= 0 && getColor(selected) != getColor(board[r][c])) {
							board[r][c].setEnabled(true);
							JOptionPane.showOptionDialog(
								frame,
								"What do you want to promote your pawn to?",
								"Promotion",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,
								new String[]{"Queen", "Bishop", "Knight", "Rook"},
								"Queen"
							);
						}
					}
					selected.setEnabled(true);
				}
			} else {
				if (btn != selected) {
					btn.setText(selected.getText());
					selected.setText("");
				}
				selected = null;
				for (JButton[] row : board) {
					for (JButton b : row) {
						b.setEnabled(true);
					}
				}
			}
		}
		frame.repaint();
	}

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		new Chess();
	}
}
