/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uniube.spaceinvaders.view;

import br.uniube.spaceinvaders.game.Game;
import java.awt.Dimension;
import java.awt.MenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JMenuItem;
import jvisual.components.panels.DynamicPanel;
import jvisual.components.panels.VerticalPanel;

public class InvaderApp extends DynamicPanel{
    
     // dimensoes do game
    private static final int largura = 800;
    private static final int altura = 600;
    
    VerticalPanel painelJogo;
    Game game;
    
    JMenuItem mnuConfig;
    JMenuItem mnuSair;
    
    public static void main( String args[] )
    {
        try{
            JFrame frame = new JFrame("Space Invaders");
            
            InvaderApp sip = new InvaderApp();
            sip.build(frame);
            
            frame.getContentPane().add( sip );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.pack();
            frame.setVisible( true );
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao iniciar o jogo: \n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public InvaderApp() throws Exception {
        setPreferredSize(new Dimension( largura, altura ));
    }

    public void initComponents() {
        game = new Game();
        painelJogo.add(game);
        
        mnuConfig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        
        mnuSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
