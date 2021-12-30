import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    GamePanel() {
        newPaddles();
        newBall();
        score = new Score(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(Constants.SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((Constants.GAME_WIDTH / 2) - (Constants.BALL_DIAMETER / 2), random.nextInt(Constants.GAME_HEIGHT -
                Constants.BALL_DIAMETER), Constants.BALL_DIAMETER,
                Constants.BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (Constants.GAME_HEIGHT / 2) - (Constants.PADDLE_HEIGHT / 2), Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(Constants.GAME_WIDTH - Constants.PADDLE_WIDTH, (Constants.GAME_HEIGHT / 2) - (Constants.PADDLE_HEIGHT / 2), Constants.PADDLE_WIDTH,
                Constants.PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision() {
        // bounce ball off top and bottom of window edges
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= Constants.GAME_HEIGHT - Constants.BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }

        // bounces ball off paddles
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // increases speed each time ball is hit
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // increases speed each time ball is hit
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        // Stop paddles at window edges
        if (paddle1.y <= 0)
            paddle1.y = 0;
        if (paddle1.y >= (Constants.GAME_HEIGHT - Constants.PADDLE_HEIGHT))
            paddle1.y = Constants.GAME_HEIGHT - Constants.PADDLE_HEIGHT;
        if (paddle2.y <= 0)
            paddle2.y = 0;
        if (paddle2.y >= (Constants.GAME_HEIGHT - Constants.PADDLE_HEIGHT))
            paddle2.y = Constants.GAME_HEIGHT - Constants.PADDLE_HEIGHT;

        // Give player 1 point and creates new paddles & ball
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: " + score.player2);
        }
        if (ball.x >= Constants.GAME_WIDTH - Constants.BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: " + score.player1);
        }
    }

    public void run() {
        do {
            move();
            checkCollision();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }


    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
