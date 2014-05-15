import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JPanel;

// class de implementacao do painel do game, controle de elementos e renderizacao
public class InvadersPanel extends JPanel implements Runnable, KeyListener
{
    // Configs
    private static final int NUM_INVASORES = 30;
    private static final int MAX_TIROS = 5;
    private static final int NUM_BOMBAS_DISPONIVEIS = 10;
    private static final int SCORE_BOMBA = 1;
    private static final int SCORE_TIRO = 2;
    
    // dimensoes are game
    private static final int largura = 800;
    private static final int altura = 600;
    
    // thread para controle de animacao
    private Thread animator;
    private boolean isPaused = false;
    private boolean isWaitingForRestart = false;
    
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
   
    // score do usuário
    private double score;
    
    // Construtor, inicializa estrutura cria interface, etc
    public InvadersPanel()
    {
        setBackground( Color.WHITE );
        setPreferredSize( new Dimension( largura, altura ) );
        setFocusable( true );
        requestFocus();
        addKeyListener( this );
        zerarGame();
    }
    
        
    private void zerarGame(){
        invasores = new ArrayList<Invader>( NUM_INVASORES );
        for ( int i = 0; i < NUM_INVASORES; i++ )
            invasores.add( new Invader( this.getPreferredSize() ) );
        shooter = new Shooter( this.getPreferredSize() );
        tiros = new ArrayList<Bullet>( MAX_TIROS );
        bomba = null;
        numBombs = NUM_BOMBAS_DISPONIVEIS;
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
        if ( !isPaused && !isWaitingForRestart ) {
            
            // movendo os sprites
            for( Invader i:invasores ) i.move();
            shooter.move( dir );
            for( Bullet b:tiros ) b.move();
            if ( bomba != null ) bomba.move();
 
            // colisoes?
            for ( Iterator<Invader> iteInvasor = invasores.iterator(); iteInvasor.hasNext() ;  ) {
                Invader invasor = iteInvasor.next();
                
                if (shooter.hitIn(invasor)) {
                    isWaitingForRestart = true;
                    break;
                }
                
                for ( Iterator<Bullet> iteTiro = tiros.iterator(); iteTiro.hasNext() ; ){
                    Bullet tiro = iteTiro.next();
                    
                    if ( tiro.getY() < 0){
                        iteTiro.remove();
                    }else if ( tiro.hitIn( invasor ) ){
                        score += SCORE_TIRO;
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
        
        for( Invader i:invasores ) i.draw( g );
        shooter.draw( g );
        for( Bullet b:tiros ) b.draw( g );
        if ( bomba != null ) bomba.draw( g );
        
        // print statisticas
        if (isPaused || isWaitingForRestart){
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect( 0 , 0 , getWidth(), getHeight());
            
            g.setColor(new Color(255, 255, 255));
            String s = "Score: " + score;
            g.drawString( s, getWidth() / 2 - 25 , getHeight() / 2 );
            
            s = "PRESSIONE ENTER PARA CONTINUAR";
            g.drawString( s, getWidth() / 2 - 110 , getHeight() / 2 + 20 );
            
            
        }else{
            String s = " Bombas: " + numBombs + " Invasores: " + invasores.size() + " Score: " + score;
            g.drawString( s, 5, getHeight() - 10 );
        }
        
    }
    
    // processo de teclas pressionadas durante o game
    public void keyPressed( KeyEvent e )
    {
        int keyCode = e.getKeyCode();
        
        if (isPaused && keyCode != KeyEvent.VK_P){
            return;
        } else if (isWaitingForRestart && keyCode != KeyEvent.VK_ENTER){
            return;
        }
        
        switch (keyCode){
            case KeyEvent.VK_ENTER:
                if (isWaitingForRestart) {
                    isWaitingForRestart = false;
                    zerarGame();
                }
            break;
            
            case KeyEvent.VK_P:
                if (!isWaitingForRestart) isPaused = !isPaused;
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
            
        }      
    }
    
    // Satisfaz a interface KeyListener
    public void keyReleased( KeyEvent e ){}
    public void keyTyped( KeyEvent e ){}
}
