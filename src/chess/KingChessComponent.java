package chess;

import chessboard.Chessboard;
import controller.ClickController;
import chessboard.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class KingChessComponent extends ChessComponent{
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    private Image kingImage;

    public void loadResource() throws IOException {
        if (KING_WHITE == null) {
            KING_WHITE = ImageIO.read(new File("./resources/images/king-white.png"));
        }

        if (KING_BLACK == null) {
            KING_BLACK = ImageIO.read(new File("./resources/images/king-black.png"));
        }
    }

    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, Chessboard chessboard) {
        super(chessboardPoint, location, color, listener, size);
        setName(color);
        initiateKingImage(color);
        this.chessboard=chessboard;
    }
    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, int size) {
        super(chessboardPoint, location, color, size);
        initiateKingImage(color);
    }

    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        if ((Math.abs(source.getX() - destination.getX()) == 1 && Math.abs(source.getY() - destination.getY()) == 0) ||
                (Math.abs(source.getX() - destination.getX()) == 0 &&Math.abs(source.getY() - destination.getY()) == 1)||
                (Math.abs(source.getX() - destination.getX()) == 1 &&Math.abs(source.getY() - destination.getY()) == 1)) {
            return true;
        }
        else if (moved==0){
            if (chessComponents[destination.getX()][destination.getY()]instanceof RookChessComponent
                    &&chessComponents[destination.getX()][destination.getY()].moved==0
                    &&chessComponents[destination.getX()][destination.getY()].getChessColor()==chessColor
                    &&(chessboard.castle1(this,chessComponents[destination.getX()][destination.getY()])
                    ||chessboard.castle2(this,chessComponents[destination.getX()][destination.getY()])))return true;
            else return false;
        }else return false;
    }

    private boolean CheckKingB(ChessboardPoint destination){
        for (int i=0;i<chessboard.getCanMoveToW().size();i++){
            if (chessboard.getCanMoveToW().get(i).getX()==destination.getX()&&chessboard.getCanMoveToW().get(i).getY()==destination.getY())
                return true;
        }
        return false;
    }

    private boolean CheckKingW(ChessboardPoint destination){
        for (int i=0;i<chessboard.getCanMoveToB().size();i++){
            if (chessboard.getCanMoveToB().get(i).getX()==destination.getX()&&chessboard.getCanMoveToB().get(i).getY()==destination.getY())
                return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(kingImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) {
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }
    @Override
    public void setName(ChessColor color) {
        if (color == ChessColor.BLACK) {
            this.name = 'K';
        } else {
            this.name = 'k';
        }
    }
    @Override
    public  void removeSelected(){
        getClickController().removeFirst(this);
    }


}
