package com.company;

// IMPORTES
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;


@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable{ // Cria a Classe GameLoopingProfessional que extende a classe Canvas e Implementa a Interface Runnable

    // ATRIBUTOS
    private Thread thread; // Thread do Jogo
    private boolean isRunning = false; // Estado do Loop do Jogo

    private JFrame frame; // Objeto da Janela
    public static final int WEIGH = Image.WIDTH * 2; // Largura
    public static final int HEIGHT = Image.HEIGHT; // Altura
    public static final int SCALE = 3;

    private BufferedImage image; // BufferedImage para o fundo do jogo

    private Image paint;
    private Line newPaint;

    // MÉTODO CONSTRUTOR
    public Game() {
        super(); // ?????
        this.janela(); // Cria uma Janela
        this.setPaint(new Image());
        this.setNewPaint(new Line(this.getPaint().getPoints()));
        this.setImage(new BufferedImage(WEIGH * SCALE, HEIGHT * SCALE, BufferedImage.TYPE_INT_RGB)); // Cria um BufferedImage para o fundo da Janela
    }

    // MÉTODO JANELA
    public void janela() {
        // Cria um Janela(Conteiner) para os componentes do Jogo
        this.setFrame(new JFrame("Janela")); // Cria o objeto janela
        this.setPreferredSize(new Dimension(WEIGH * SCALE, HEIGHT * SCALE)); // Utiliza a classe Dimension para dimensionar a janela
        this.getFrame().add(this); // Adciona a própria classe na Janela para utilizar os métodos da classe Canvas
        this.getFrame().setResizable(false); // Deixa a janela não redimensionável
        this.getFrame().pack();
        this.getFrame().setLocationRelativeTo(null); // Localiza a janela no meio da tela
        this.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Garante que a janela vai ser fechada ao se aperta o X
        this.getFrame().setVisible(true); // Torna a janela visível
    }

    // MÉTODO START THREADS
    public synchronized void start() {
        this.setThread(new Thread(this)); // Cria uma Thread
        this.setRunning(true); // Coloca o estado do Jogo em verdadeiro
        this.getThread().start(); // Inicia a Thread
    }

    // MÉTODO STOP THREADS
    public synchronized void stop() {
        this.setRunning(false); // Coloca o estado do Jogo em falso
        // Exceção responsável por garantir que a Thread vai ser encerrada
        try {
            this.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //MÉTODO PARA A LÓGICA DO JOGO
    public void kick() {
    }

    //MÉTODO PARA A RENDERIZAÇÃO DO JOGO
    public void render() {
        BufferStrategy bs = this.getBufferStrategy(); // Cria um objeto BufferStrategy que recebe o BufferStrategy nativo do Canvas
        // O que é Buffer Strategy ? Daí eu não sei
        if(bs == null) { // Caso o BufferStrategy do Canvas estiver vazio
            this.createBufferStrategy(3); // Cria um Buffer Strategy
            return; // Quebra o método com o Buffer Criado
        }

        Graphics g = this.getImage().getGraphics(); // Cria o objeto g da classe Graphics arribuindo um retorno de método do getGraphics() da classe BufferedImage image
        g.setColor(Color.WHITE); // Muda a cor
        g.fillRect(0, 0, WEIGH * SCALE, HEIGHT * SCALE); // Cria uma figura geométrica do tamanho da tela

        // RENDERIZANDO SPRITES
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(this.getPaint().getSprite(), 0, 0, Image.WIDTH * SCALE, Image.HEIGHT * SCALE, null);
        //g2.drawImage(this.getPaint().getSprite(), Image.WIDTH * SCALE, 0, Image.WIDTH * SCALE, Image.HEIGHT * SCALE, null);

        this.getPaint().render(g2);
        this.getPaint().renderLine(g2, this.getNewPaint(), this);

        g2.drawImage(this.getPaint().getSprite(), Image.WIDTH * SCALE, 0, Image.WIDTH * SCALE, Image.HEIGHT * SCALE, null);
        this.getNewPaint().render(g2);
        this.getNewPaint().renderLine(g2);

        g.dispose(); // Método de otimização
        g = bs.getDrawGraphics(); // ???
        g.drawImage(this.getImage(), 0, 0, WEIGH * SCALE, HEIGHT * SCALE, null); // ???

        bs.show(); // ???
    }

    // MÉTODO RUN SOBRESCRITO DA INTERFACE RUNNABLE
    @Override
    public void run() {

        long lastTime = System.nanoTime(); // Pega o horario do computador em nano segundos
        double amountOfTicks = 60.0; // FPS
        double ns = 1000000000 / amountOfTicks; // Um segundo convertido para nano segundos dividido pelo FPS
        double delta = 0; // Variável auxiliar

        int contFPS = 0; // Contador de FPS
        double timer = System.currentTimeMillis(); // Pego a horário do computador em mili segundos

        while(this.isRunning()) {
            long now = System.nanoTime(); // Pega o horário do computador
            delta += (now - lastTime) / ns; // Subtrai o tempo atual com o tempo do último frame dividindo o resultado com o tempo de um frame
            lastTime = now;

            if(delta >= 1) { // Verifica se passou o tempo de um frame
                this.kick();
                this.render();
                contFPS++;
                delta--;
            }

            if(System.currentTimeMillis() - timer >= 1000) { // Verifica se passou um segundo
                System.out.println("FPS: " + contFPS);
                contFPS = 0;
                timer += 1000;
            }
        }
        this.stop(); // Encerra a Thread
    }

    // METODOS GETTERS E SETTERS
    public boolean isRunning() { return isRunning; }

    public void setRunning(boolean isRunning) {this.isRunning = isRunning; }

    public Thread getThread() { return thread; }

    public void setThread(Thread thread) { this.thread = thread; }

    public JFrame getFrame() {return frame; }

    public void setFrame(JFrame frame) { this.frame = frame; }

    public BufferedImage getImage() { return image; }

    public void setImage(BufferedImage image) { this.image = image; }

    public Image getPaint() { return paint; }

    public void setPaint(Image paint) { this.paint = paint; }

    public Line getNewPaint() { return newPaint; }

    public void setNewPaint(Line newPaint) { this.newPaint = newPaint; }
}
