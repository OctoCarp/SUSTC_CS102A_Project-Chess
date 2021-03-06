package chess;

import chessboard.Chessboard;
import controller.ClickController;
import chessboard.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class QueenChessComponent extends ChessComponent {
    private ChessComponent[][]chessComponents=Chessboard.chessComponents;

    private static Image QUEEN_WHITE;
    private static Image QUEEN_BLACK;
    private Image queenImage;

    public void loadResource() throws IOException {
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("./resources/images/queen-white.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("./resources/images/queen-black.png"));
        }
    }

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size) {
        super(chessboardPoint, location, color, listener, size);
        setName(color);
        initiateQueenImage(color);
    }
    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, int size) {
        super(chessboardPoint, location, color, size);
        initiateQueenImage(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if (source.getX() - destination.getX() == source.getY() - destination.getY()) {
            int initRow = Math.min(source.getX(), destination.getX());
            int initCol = Math.min(source.getY(), destination.getY());
            int destRow = Math.max(source.getX(), destination.getX());
            int destCol = Math.max(source.getY(), destination.getY());
            for (int i = initRow + 1, j = initCol + 1; i < destRow; i++, j++) {
                if (!(chessComponents[i][j] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getX() - destination.getX() == destination.getY() - source.getY()) {
            int initRow = Math.min(source.getX(), destination.getX());
            int initCol = Math.min(source.getY(), destination.getY());
            int destRow = Math.max(source.getX(), destination.getX());
            int destCol = Math.max(source.getY(), destination.getY());
            for (int i = initRow + 1, j = destCol - 1; i < destRow; i++, j--) {
                if (!(chessComponents[i][j] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getX() == destination.getX()) {
            int row = source.getX();
            for (int col = Math.min(source.getY(), destination.getY()) + 1;
                 col < Math.max(source.getY(), destination.getY()); col++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else if (source.getY() == destination.getY()) {
            int col = source.getY();
            for (int row = Math.min(source.getX(), destination.getX()) + 1;
                 row < Math.max(source.getX(), destination.getX()); row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(queenImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }
    @Override
    public void setName(ChessColor color) {
        if (color == ChessColor.BLACK) {
            this.name = 'Q';
        } else {
            this.name = 'q';
        }
    }
    @Override
    public  void removeSelected(){
        getClickController().removeFirst(this);
    }
}
