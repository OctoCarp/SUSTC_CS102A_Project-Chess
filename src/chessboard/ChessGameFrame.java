package chessboard;

import audio.AudioPlay;
import chess.ChessColor;
import controller.Countdown;
import controller.GameController;
import util.BoardLoader;
import util.BoardSaver;
import util.Step;
import util.StepSaver;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static chess.ChessColor.BLACK;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private Chessboard chessboard;
    private GameController gameController;
    static JLabel statusLabel;
    public static JLabel count;
    static JLabel checkLabel;
    static JButton pauseBtn;
    public Winboard winboard;
    static ImagePanel background;
    static ImagePanel background1;
    static ImagePanel background2;
    static JButton changeBack;

    Countdown cd;

    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project Demo"); //设置标题

        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;
        this.winboard = new Winboard(600, 300, this);

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        StepSaver.initiate();
        addCount();
        countTxt();
        addChessboard();
        addLabel();
        addSaveButton();
        addLoadButton();
        addRestart();
        addRegretButton();
        addLabelCheck();
        addPause();
        setBackground();
        addBackground();

        AudioPlay.playBgm();
    }

    public static void setPause() {
        pauseBtn.setText("Pause");
    }

    public static void setResume() {
        pauseBtn.setText("Resume");
    }

    private static void setBackground() {
        try {
            //FIXME: background path
            background1 = new ImagePanel(new ImageIcon("./resources/images/background1.png").getImage());
            background2 = new ImagePanel(new ImageIcon("./resources/images/background2.png").getImage());
            background = background1;
        } catch (NullPointerException e) {
        }
    }

    private void addRestart() {
        JButton button = new JButton("Restart");
        button.addActionListener(e -> restart());
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addPause() {
        pauseBtn = new JButton("Pause");
        pauseBtn.addActionListener(e -> cd.changePause());
        pauseBtn.setLocation(HEIGTH + 120, HEIGTH / 10);
        pauseBtn.setSize(80, 50);
        pauseBtn.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(pauseBtn);
    }

    private void restart() {
        if (chessboard != null) {
            this.remove(chessboard);
        }
        addChessboard();
        add(background);
        checkLabel.setVisible(false);
        setStatusLabel(ChessColor.WHITE);
        repaint();
        Countdown.restart();
    }

    private void addCount() {
        count = new JLabel();
        count.setLocation(HEIGTH + 80, HEIGTH / 10);
        count.setSize(100, 60);
        count.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(count);
    }

    private void countTxt() {
        cd = new Countdown();
        cd.start();
    }

    private void setCountBoard(Countdown cd, Chessboard chessboard) {
        cd.setChessboard(chessboard);
    }

    /**
     * 在游戏面板中添加棋盘
     */
    public void addChessboard() {
        chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        StepSaver.initiate();
        setCountBoard(cd, chessboard);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        chessboard.chessGameFrame = this;
        add(chessboard);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        statusLabel = new JLabel("WHITE");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(100, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    private void addLabelCheck() {
        checkLabel = new JLabel();
        checkLabel.setLocation(HEIGTH, HEIGTH / 10 + 60);
        checkLabel.setSize(200, 60);
        checkLabel.setFont(new Font("Rockwell", Font.BOLD, 10));
        setVisible(false);
        add(checkLabel);
    }

    public static void setStatusLabelCheck(Chessboard chessboard) {
        if (chessboard.CheckKing(chessboard.getKingB())) {
            checkLabel.setText("Check King Black");
            checkLabel.setVisible(true);
        } else if (chessboard.CheckKing(chessboard.getKingW())) {
            checkLabel.setText("Check King White");
            checkLabel.setVisible(true);
        } else checkLabel.setVisible(false);
    }

    public static void setStatusLabel(ChessColor color) {
        if (color == BLACK) {
            statusLabel.setText("BLACK");
        } else {
            statusLabel.setText("WHITE");
        }
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addSaveButton() {
        JButton button = new JButton("Save");
        add(button);
        button.addActionListener(e -> {
            String filePath = JOptionPane.showInputDialog(this, "input the name here");
            Step board = new Step(chessboard.getCurrentColor(), chessboard.getChessComponents());
            board.setCastling(chessboard);
            StepSaver.stepList.add(board);
            BoardSaver.saveGame(filePath + ".txt");
            cd.resumeThread();
        });
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addRegretButton() {
        JButton button = new JButton("Regret");
        button.addActionListener((e) -> chessboard.regretStep());
        button.setLocation(HEIGTH, HEIGTH / 10 + 480);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        JFileChooser chooser = new JFileChooser();
        add(button);
        button.addActionListener(e -> {
            try {
                String path = readPath();
                BoardLoader.readBoard(path);
                if (BoardLoader.legal()) {
                    loadGame();
                    chessboard.loadGame(BoardLoader.boardStrings);
                    BoardLoader.initLoader();
                } else {
                    JOptionPane.showMessageDialog(this, BoardLoader.wrong);
                    BoardLoader.initLoader();
                }
            } catch (NullPointerException w) {
                JOptionPane.showMessageDialog(this, "no file selected");
            }
        });
    }

    String readPath() {
        JFileChooser fc = new JFileChooser();
        //FIXME:load path
        fc.setCurrentDirectory(new File("./resources/saves"));
        fc.showOpenDialog(this);
        try {
            String s = fc.getSelectedFile().getName();
            String[] ss = s.split("\\.");
            if (ss.length != 0) {
                if (!ss[ss.length - 1].equals("txt")) {
                    BoardLoader.one04 = true;
                }
            } else BoardLoader.one04 = true;
            return fc.getSelectedFile().getAbsolutePath();
        } catch (NullPointerException e) {
            return null;
        }
    }

    void loadGame() {
        if (chessboard != null) {
            this.remove(chessboard);
        }
        chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        setCountBoard(cd, chessboard);
        chessboard.chessGameFrame = this;
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        add(chessboard);
        add(background);
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void addBackground() {
        background.setBounds(0, 0, WIDTH, HEIGTH);
        add(background);
    }

    private void addChangeBack() {
        changeBack = new JButton("Theme");
        changeBack.addActionListener(e -> changeBack());
        changeBack.setLocation(HEIGTH + 120, HEIGTH / 10 + 550);
        changeBack.setSize(80, 50);
        changeBack.setFont(new Font("Rockwell", Font.BOLD, 10));
        add(changeBack);
    }

    //FIXME
    private void changeBack() {
        if (background != null) {
            remove(background);
            if (background == background1) {
                background = background2;
            } else {
                background = background1;
            }
        } else {
            background = background1;
        }
        background.repaint();
        addBackground();
    }

}