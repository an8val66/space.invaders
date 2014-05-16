package br.uniube.spaceinvaders.objects;

import br.uniube.spaceinvaders.game.Direcao;
import java.awt.*;
import javax.swing.ImageIcon;

// Classe que representa o defensor no jogo
public class Shooter
{
    private static int VELOCITY = 2;
    
    // Posicao do defensor em pixels
    private int x, y;
    // Tamanho do defensor em pixels
    private int iw, ih;
    // Imagem do defensor
    private Image icon;
    // area do painel do jogo ( para controlar movimentos )
    private Dimension area;
    
    // Construtor, inicializa atributos e posiciona o shooter
    public Shooter( Dimension a )
    {
        area = a;
        icon = new ImageIcon( getClass().getResource( "/Sprites/nave.png" ) ).getImage();
        iw = icon.getWidth( null );
        ih = icon.getHeight( null );
        
        // x e y iniciais centrados na area de movimentacao
        x = (int)( iw / 2 + ( a.width - iw ) / 2 );
        y = (int)( a.height - 75 + ih / 2 );
    }
    
    public void move( Direcao dir )
    {
        if ( dir == null ) return;
        switch( dir ) {
            case LEFT: { if ( x > iw / 2) x-=VELOCITY; break; }
            case RIGHT: { if ( x < area.width - iw / 2 ) x+=VELOCITY; break; }
            case UP: { if ( y > area.height - 100 + ih / 2) y-=VELOCITY; break; }
            case DOWN: { if ( y < area.height - ih / 2 - 28) y+=VELOCITY; break; }
        }
    }
    
    // Metodo para desenhar o defensor ( graficamente )
    public void draw( Graphics g )
    {
        g.drawImage( icon, x - iw / 2, y - ih / 2, null );
    }
    
    // verifica se o shooter esta proximo de um invasor
    public boolean hitIn( Invader i )
    {
        int ox = i.getX();
        int oy = i.getY();
        
        if ( Math.sqrt( (x - ox ) * ( x - ox ) + ( y -oy ) * ( y - oy ) ) < 35) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return iw; }
    public int getHeight() { return ih; }
}
