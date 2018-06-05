import java.awt.*;

public class Tetromino {
    private static final Point[][][] PIECES = {
        // I-Piece
        {
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
        },

        // J-Piece
        {
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
        },

        // L-Piece
        {
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
            { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
        },

        // O-Piece
        {
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
        },

        // S-Piece
        {
            { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
            { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
            { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
            { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
        },

        // T-Piece
        {
            { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
            { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
        },

        // Z-Piece
        {
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
            { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
            { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
        }
    };

    private int type;
    private int rotation;
    private Point position;

    Tetromino() {
        type = (int)(Math.random() * 7);
        rotation = (int)(Math.random() * 4);
        position = new Point(4, 0);
    }

    Point[] getPiece() {
        return PIECES[type][rotation];
    }

    public Point getPosition() {
        return position;
    }

    public void rotate() {
        int left = 0;
        int right = 0;
        rotation = (rotation + 1) % 4;
        for (Point p : this.getPiece()) {
            int nleft = position.x + p.x;
            left = left > nleft ? nleft : left;
            int nright = position.x + p.x - 9;
            right = right < nright ? nright : right;
        }

        position.x -= left;
        position.x -= right;
    }

    /**
     * Move piece on board
     *
     * @param direction -1 left, 1 right
     * @param board
     */
    public void move(int direction, boolean[][] board) {
        boolean collision = false;
        for (Point p : getPiece()) {
            if (p.x + position.x >= 9 && direction > 0 || p.x + position.x < 1 && direction < 0) {
                collision = true;
                break;
            }
            for (int x = 0; x < 10; ++x) {
                for (int y = 0; y < 20; ++y) {
                    if (board[x][y] && p.x + position.x + direction == x && p.y + position.y == y) {
                        collision = true;
                        break;
                    }
                }
                if (collision) {
                    break;
                }
            }
        }
        if (!collision) {
            position.x += direction;
        }
    }

    public boolean fall() {
        for (int i = 0; i < 4; ++i) {
            if (getPiece()[i].y + getPosition().y > 18) {
                return false;
            }
        }

        position.y += 1;
        return true;
    }
}
