import javax.swing.*;

public class InvaderApp
{
    public static void main( String args[] )
    {
        InvadersPanel sip = new InvadersPanel();
        JFrame frame = new JFrame();
        frame.getContentPane().add( sip );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.pack();
        frame.setVisible( true );
    }
}
