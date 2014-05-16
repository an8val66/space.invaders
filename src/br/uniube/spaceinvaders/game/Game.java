package br.uniube.spaceinvaders.game;

import br.uniube.spaceinvaders.objects.Bomb;
import br.uniube.spaceinvaders.objects.Bullet;
import br.uniube.spaceinvaders.objects.Invader;
import br.uniube.spaceinvaders.objects.Shooter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JPanel;

// class de implementacao do painel do game, controle de elementos e renderizacao
public class Game extends JPanel implements Runnable, KeyListener
{
    // Configs
    private static final int    NUM_INVASORES = 5;
    private static final int    MAX_TIROS = 5;
    private static final int    NUM_BOMBAS_DISPONIVEIS = 10;
    
    private static final int    SCORE_BOMBA = 1;
    private static final double SCORE_TIRO_BOMBA = 0.5;
    private static final int    SCORE_TIRO = 2;
    
    // dimensoes do game
    private static final int largura = 800;
    private static final int altura = 600;
    
    // thread para controle de animacao
    private Thread animator;
    
    // Controle do jogo
    private GameStatus gameStatus;
    private boolean persistentDirection = true;
    
    // movimentacao invaders
    private ArrayList<Invader> invasores;
   
    // shooter e direcao de movimento
    private Shooter shooter;
    private Direcao dir;
    
    // conjunto de tiros
    private ArrayList<Bullet> tiros;
    
    // uma unica bomba
    private Bomb bomba;
    private int numBombs;
   
    // level do usuário
    private int nivel;
    
    // score do usuário
    private double score;
    
    // Construtor, inicializa estrutura cria interface, etc
    public Game()
    {
        setBackground( Color.WHITE );
        setPreferredSize( new Dimension( largura, altura ) );
        setFocusable( true );
        requestFocus();
        addKeyListener( this );
        zerarGame();
        gameStatus = GameStatus.AGUARDANDO_INICIO;
    }
    
    private void zerarGame(){
        if (gameStatus == GameStatus.AGUARDANDO_PROX_NIVEL){
            nivel++;
        } else {
            nivel = 1;
        }
        
        shooter = new Shooter( this.getPreferredSize() );
        tiros = new ArrayList<Bullet>( MAX_TIROS );
        
        double taxaNivel = (nivel / 10 + 1 );
        
        invasores = new ArrayList<Invader>( NUM_INVASORES );
        for ( int i = 0; i < (int) NUM_INVASORES * taxaNivel; i++ )
            invasores.add( new Invader( this.getPreferredSize(), taxaNivel) );
        
        numBombs = NUM_BOMBAS_DISPONIVEIS;
        bomba = null;
        
        score = 0;
        
       
    }
    
    // avisa que agora temos interface em um container parente
    public void addNotify()
    {
        super.addNotify();
        startGame();
    }
    
    // iniciando o jogo ( e a thread de controle )
    private void startGame()
    {
        if ( animator == null ) {
            animator = new Thread( this );
            animator.start();
        }
    }
    
    // laço principal do game
    public void run()
    {
        while( true ) {
            
            gameUpdate();
            repaint();
            try { Thread.sleep( 10 ); }
            catch ( InterruptedException e ) { e.printStackTrace(); }
        }
    }
    
    // atualizar os elementos do game
    private synchronized void gameUpdate()
    {
        if ( gameStatus == GameStatus.RODANDO ) {
            
            // movendo os sprites
            for( Invader i:invasores ) i.move();
            shooter.move( dir );
            for( Bullet b:tiros ) b.move();
            if ( bomba != null ) bomba.move();
 
            if (invasores.isEmpty()){
                gameStatus = GameStatus.AGUARDANDO_PROX_NIVEL;
            }
            
            // colisoes?
            for ( Iterator<Invader> iteInvasor = invasores.iterator(); iteInvasor.hasNext() ;  ) {
                Invader invasor = iteInvasor.next();
                
                if (shooter.hitIn(invasor)) {
                    gameStatus = GameStatus.AGUARDANTO_RESTART;
                    break;
                }
                
                for ( Iterator<Bullet> iteTiro = tiros.iterator(); iteTiro.hasNext() ; ){
                    Bullet tiro = iteTiro.next();
                    
                    if ( tiro.getY() < 0){
                        iteTiro.remove();
                    }else if ( tiro.hitIn( invasor ) ){
                        score += ( tiro.isBombBullet() ? SCORE_TIRO_BOMBA : SCORE_TIRO );
                        iteInvasor.remove();
                        iteTiro.remove();
                    } 
                }
                
                if ( bomba != null) { 
                    if ( bomba.getY() < 0 ) {
                        bomba.deactive();
                        bomba = null;
                    } else  if ( bomba.hitIn( invasor ) ) {
                        score += SCORE_BOMBA;
                        iteInvasor.remove();
                        tiros.addAll(bomba.explode());
                        bomba.deactive();
                        bomba = null;
                    }
                }
            }

        }
    }
    
    // desenha o componente ( e os seus elementos )
    protected synchronized void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        
        for( Invader i : invasores ) i.draw( g );
        shooter.draw( g );
        for( Bullet b : tiros ) b.draw( g );
        if ( bomba != null ) bomba.draw( g );
        
        // print statisticas
        if (!isInStatus(GameStatus.RODANDO)){
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect( 0 , 0 , getWidth(), getHeight());
            
            String s = null;
            g.setColor(new Color(255, 255, 255));
            
            int xOffset = -25;
            int yOffset = 0;
            
            if (gameStatus != GameStatus.AGUARDANDO_INICIO){
                s = "Score: " + score;
                g.drawString( s, getWidth() / 2 + xOffset , getHeight() / 2 + yOffset );
                s = null;
            }
            
            xOffset = -110;
            yOffset = +20;
            
            if (isInStatus(GameStatus.AGUARDANDO_INICIO)){
                s = "PRESSIONE ENTER PARA INICIAR";
            } else if (isInStatus(GameStatus.AGUARDANTO_RESTART)) {
                s = "PRESSIONE ENTER PARA REINICIAR";
            } else if (isInStatus(GameStatus.AGUARDANTO_RESTART)) {
                s = "PRESSIONE ENTER PARA CONTINUAR";
            } else if (isInStatus(GameStatus.PAUSADO)) {
                s = "PRESSIONE 'P' PARA CONTINUAR";
                xOffset = -80;
            } else if (isInStatus(GameStatus.AGUARDANDO_PROX_NIVEL)) {
                s = "VOCÊ PASSOU DE NIVEL!! PRESSIONE ENTER PARA CONTINUAR";
                xOffset = -200;
            }

             if (s != null){
                 g.drawString( s, getWidth() / 2 + xOffset , getHeight() / 2 + yOffset );
             }
             
        }else{
            g.setColor(new Color(0, 0, 0, 230));
            g.fillRect( 0 , getHeight() - 28 , getWidth(), getHeight());
            
            g.setColor(new Color(255, 255, 255));
            String s = "Nível: " + nivel + "   Bombas: " + numBombs + "   Invasores: " + invasores.size() + "   Score: " + score;
            g.drawString( s, 5, getHeight() - 10 );
        }
    }
    
    
    private boolean isInStatus(GameStatus... statusList){
        for (GameStatus status : statusList){
            if (gameStatus == status){
                return true;
            }
        }
        
        return false;
    }
    
    // processo de teclas pressionadas durante o game
    public void keyPressed( KeyEvent e )
    {
        int keyCode = e.getKeyCode();
        
        if (gameStatus == GameStatus.PAUSADO && keyCode != KeyEvent.VK_P){
            return;
        } else if ( isInStatus(GameStatus.AGUARDANTO_RESTART, GameStatus.AGUARDANDO_INICIO, GameStatus.AGUARDANDO_PROX_NIVEL) && keyCode != KeyEvent.VK_ENTER){
            return;
        }
        
        switch (keyCode){
            case KeyEvent.VK_ENTER:
                if (isInStatus(GameStatus.AGUARDANTO_RESTART, GameStatus.AGUARDANDO_INICIO, GameStatus.AGUARDANDO_PROX_NIVEL)) {
                    if (isInStatus(GameStatus.AGUARDANTO_RESTART, GameStatus.AGUARDANDO_PROX_NIVEL)){
                        zerarGame();   
                    }
                    
                    gameStatus = GameStatus.RODANDO;
                }
            break;
            
            case KeyEvent.VK_P:
                if (gameStatus == GameStatus.RODANDO){
                    gameStatus = GameStatus.PAUSADO;
                } else if (gameStatus == GameStatus.PAUSADO){
                    gameStatus = GameStatus.RODANDO;
                }
            break;
            
            case KeyEvent.VK_LEFT:
                dir = Direcao.LEFT;
            break;
            
            case KeyEvent.VK_RIGHT:
                dir = Direcao.RIGHT;
            break;
                
            case KeyEvent.VK_UP:
                dir = Direcao.UP;
            break;
        
            case KeyEvent.VK_DOWN:
                dir = Direcao.DOWN;
            break;
            
            case KeyEvent.VK_D:
                Configs.isMovimentoPersistente = !Configs.isMovimentoPersistente;
                dir = Direcao.NONE;
            break;
                
            case KeyEvent.VK_SPACE:
                 // pode adicionar mais tiros
                if ( tiros.size() < MAX_TIROS ) {
                    tiros.add( new Bullet( getPreferredSize(), shooter.getX(), shooter.getY() - shooter.getHeight() / 2, Direcao.UP ) );
                }
            break;
        
            case KeyEvent.VK_B:
                if ( numBombs > 0 && bomba == null ) {
                    bomba = new Bomb( getPreferredSize(), shooter.getX(), shooter.getY() - shooter.getHeight() / 2, Direcao.UP );
                    numBombs--;
                }
            break;
            
            case KeyEvent.VK_R:
                if (gameStatus == GameStatus.RODANDO){
                    zerarGame();
                    gameStatus = GameStatus.AGUARDANDO_INICIO;
                } 
            break;
            
        }      
    }
    
    public void pausarGame(){
        gameStatus = GameStatus.PAUSADO;
    }
    
    // Satisfaz a interface KeyListener
    public void keyReleased( KeyEvent e ){
        if ( !Configs.isMovimentoPersistente &&
            (e.getKeyCode() == KeyEvent.VK_LEFT ||
             e.getKeyCode() == KeyEvent.VK_RIGHT ||
             e.getKeyCode() == KeyEvent.VK_UP ||
             e.getKeyCode() == KeyEvent.VK_DOWN) ){
            dir = Direcao.NONE;
        }
    }
    public void keyTyped( KeyEvent e ){}
}
