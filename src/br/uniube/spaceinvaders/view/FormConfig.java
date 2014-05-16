/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uniube.spaceinvaders.view;

import br.uniube.spaceinvaders.game.Configs;
import br.uniube.spaceinvaders.utils.MessageUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import jvisual.components.panels.DynamicPanel;


public class FormConfig extends JFrame{
    
    private static FormConfigPanel configPanel;
    
    
    public FormConfig() {
        super("Configurações");
        try{
            configPanel = new FormConfigPanel(this);
            add(configPanel);
            pack();
        } catch (Exception ex){
            MessageUtils.showErrorMessage(null, "Não foi possível abrir as configurações. \n" + ex.getMessage(), "Erro");
            ex.printStackTrace();
        } 
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
        
        if (b){
          configPanel.carregarConfigs();
        }
    }
    
    

    private class FormConfigPanel extends DynamicPanel{
        
        JFrame frame;
        
        JButton btnCancelar;
        JButton btnSalvar;
        JCheckBox chkMovimentoPersistente;
        
        public FormConfigPanel(JFrame frame) throws Exception{
            this.frame = frame;
            build(frame);
           //pack();
        }

        public void initComponents() {
            btnSalvar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    salvar();
                    frame.setVisible(false);
                }
            });
            
            btnCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                }
            });
        }
        
        private void salvar(){
            Configs.isMovimentoPersistente = chkMovimentoPersistente.isSelected();
        }
        
        public void carregarConfigs(){
            chkMovimentoPersistente.setSelected(Configs.isMovimentoPersistente);
        }
        
    }
    
}
