import java.awt.*;
import javax.swing.ImageIcon;

// Classe que representa o defensor no jogo
public class Shooter
{
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
        y = (int)( a.height - 100 + ih / 2 );
    }
    
    public void move( Direcao dir )
    {
        if ( dir == null ) return;
        switch( dir ) {
            
            case LEFT: { x--; if ( x < iw / 2 ) x = iw / 2; break; }
            case RIGHT: { x++; if ( x > area.width - iw / 2 ) x = area.width - iw / 2; break; }
            case UP: { y--; if ( y < area.height - 100 + ih / 2 ) y = area.height - 100 + ih / 2; break; }
            case DOWN: { y++; if ( y > area.height - ih / 2 ) y = area.height - ih / 2; break; }
        }
    }
    
    // Metodo para desenhar o defensor ( graficamente )
    public void draw( Graphics g )
    {
        g.drawImage( icon, x - iw / 2, y - ih / 2, null );
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return iw; }
    public int getHeight() { return ih; }
}
