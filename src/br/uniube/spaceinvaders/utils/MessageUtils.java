/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uniube.spaceinvaders.utils;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author bruno.brandao
 */
public class MessageUtils
{
    public static void showErrorMessage( String title, String message )
    {
        showErrorMessage( null, title, message );
    }
    
    public static void showErrorMessage( JComponent parent, String title, String message )
    {
        JOptionPane.showMessageDialog( parent, message, title, JOptionPane.ERROR_MESSAGE );
    }
}
