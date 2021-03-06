package br.uniube.spaceinvaders.objects;

import br.uniube.spaceinvaders.game.Direcao;
import java.awt.*;
import javax.swing.ImageIcon;

// class representa um tiro
public class Bullet
{
    // posicao do tiro em pixel
    private int x, y;
    private Direcao direcao;
    // tiro esta ativo?
    private boolean isActive;
    // tamanho do tiro em pixel
    private int iw, ih;
    // Imagem do tiro
    private Image icon;
    // area do painel do jogo
    private Dimension area;
    // projetil de uma bomba
    private boolean isBombBullet;
    
    public Bullet( Dimension a, int x, int y, Direcao dir )
    {
        this( a, x, y, dir, false );
    }
    
    // construtor, inicializa atributos e carrega a arma
    public Bullet( Dimension a, int x, int y, Direcao dir, boolean bombBullet )
    {
        area = a;
        icon = new ImageIcon( getClass().getResource( "/Sprites/bullet.png" ) ).getImage();
        iw = icon.getWidth( null );
        ih = icon.getHeight( null );
        // x e y passados direto como elemento
        this.x = x;
        this.y = y;
        direcao = dir;
        isActive = true;
        isBombBullet = bombBullet;
    }
    
    // metodo para movimentar projetil
    public void move()
    {
        if ( !isActive ) return;
        switch( direcao ) {
            
            case LEFT: { x -= 3; if ( x < 0 ) isActive = false; break; }
            case RIGHT: { x += 3; if ( x > area.width ) isActive = false; break; }
            case UP: { y -= 3; if ( y < 0 ) isActive = false; break; }
            case DOWN: { y += 3; if ( y > area.height - 100 ) isActive = false; break; }
        }
    }
    
    // Metodo que desenha o projetil ( graficamente )
    public void draw( Graphics g )
    {
        if( isActive ) g.drawImage( icon, x - iw / 2, y - ih / 2, null );
    }
    
    // projetil esta ativo?
    public boolean isActive() { return isActive; }
    
    // projetil de uma bomba
    public boolean isBombBullet() { return isBombBullet; }
    
    // verifica se o projetil esta proximo de um invasor
    public boolean hitIn( Invader i )
    {
        int ox = i.getX();
        int oy = i.getY();
        
        if ( Math.sqrt( (x - ox ) * ( x - ox ) + ( y -oy ) * ( y - oy ) ) < 15 ) {
            isActive = false;
            return true;
        } else {
            return false;
        }
    }
    
    public int getY(){ return y; }

    public int getX() {
        return x;
    }
}
