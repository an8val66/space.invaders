package br.uniube.spaceinvaders.objects;

import java.awt.*;
import javax.swing.ImageIcon;

// Classe que representa um invasor (rbot) no jogo
public class Invader
{
    // Posicao e velocidade do rbot em pixels
    private int x, y;
    private int dx, dy;
    private boolean isActive;
    // Tamanho em pixels
    private int iw, ih;
    // Imagem do rbot
    private Image icon;
    // Area do painel do jogo ( para movimentar )
    private Dimension area;
    
    private double velocity;
    
    // Construtor, inicializa atributos e posiciona o rbot
    public Invader( Dimension a , double velocity)
    {
        area = a;
        icon = new ImageIcon( getClass().getResource( "/Sprites/invader.png" ) ).getImage();
        iw = icon.getWidth( null );
        ih = icon.getHeight( null );
        
        // x e y calculados usando a area
        x = (int) ( iw / 2 + Math.random() * ( a.width - iw ) );
        y = (int) ( ih / 2 + Math.random() * ( a.height - 250 - ih ) );
        
        // dx e dy randomicos
        while ( dx == 0 || dy == 0 ) {
            dx = 3 - (int) ( Math.random() * 6 * velocity );
            dy = 2 - (int) ( Math.random() * 4 * velocity );
            isActive = true;
        }
        
        this.velocity = velocity;
    }
    
    // Metodo de movimento do rbot, verificando se esta na area valida
    public void move()
    {
        if ( isActive ) {
            
            x += dx;
            y += dy;
            if ( x < iw / 2 ) { dx = -dx; x += dx; }
            if ( y < ih / 2 ) { dy = -dy; y += dy; }
            if ( x > area.width - iw / 2 ) { dx = -dx; x += dx; }
            if ( y > area.height -  ih / 2 - 28 ) { dy = -dy; y += dy; }
        }
        
    }
    
    // Metodo que desenha o rbot ( graficamente )
    public void draw( Graphics g )
    {
        if( isActive ) g.drawImage( icon, x - iw / 2, y - ih / 2, null );
    }
    
    public void desactive()
    {
        isActive = false;
    }
    
    public boolean isActive() { return isActive; }
    
    public int getX() { return x; }
    public int getY() { return y; }
}
