import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Bomb
{
    // posicao da bomba em pixel
    private int x, y;
    private Direcao direcao;
    // bomba esta ativo?
    private boolean isActive;
    // tamanho do bomba em pixel
    private int iw, ih;
    // Imagem do bomba
    private Image icon;
    // area do painel do jogo
    private Dimension area;
    
    // construtor, inicializa atributos e carrega a arma
    public Bomb( Dimension a, int x, int y, Direcao dir )
    {
        area = a;
        icon = new ImageIcon( getClass().getResource( "/Sprites/bomb.png" ) ).getImage();
        iw = icon.getWidth( null );
        ih = icon.getHeight( null );
        // x e y passados direto como elemento
        this.x = x;
        this.y = y;
        direcao = dir;
        isActive = true;
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
        //y = y - 3;
        //if ( y <= 0 ) isActive = false;
    }
    
    // Metodo que desenha o projetil ( graficamente )
    public void draw( Graphics g )
    {
        if( isActive ) g.drawImage( icon, x - iw / 2, y - ih / 2, null );
    }
    
    // bomba esta ativo?
    public boolean isActive() { return isActive; }
    
    public void deactive() { isActive = false; }
    
    // verifica se a bomba esta proximo de um invasor
    public boolean hitIn( Invader i )
    {
        int ox = i.getX();
        int oy = i.getY();
        if ( Math.sqrt( (x - ox ) * ( x - ox ) + ( y -oy ) * ( y - oy ) ) < 25 ) {

            return true;
        } else {
            
            return false;
        }
    }
    
    // explosao da bomba ( retorna projeteis )
    public ArrayList<Bullet> explode()
    {
        ArrayList<Bullet> newShoots = new ArrayList<Bullet>( 4 );
        
        if ( isActive ) {
            
            newShoots.add( new Bullet( area, x, y, Direcao.LEFT ) );
            newShoots.add( new Bullet( area, x, y, Direcao.RIGHT ) );
            newShoots.add( new Bullet( area, x, y, Direcao.UP ) );
            newShoots.add( new Bullet( area, x, y, Direcao.DOWN ) );
        
            isActive = false;
        }

        return newShoots;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
}
