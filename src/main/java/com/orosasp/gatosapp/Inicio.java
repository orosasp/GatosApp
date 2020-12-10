package com.orosasp.gatosapp;

import java.io.IOException;
import javax.swing.JOptionPane;

public class Inicio {
    
    public static void main(String[] args) throws IOException {
        int opcionMenu= -1;
        String[] botones = {"1.Ver Gatos", "2.Ver favoritos", "3.Salir"};
        
        do {
            // Menu principal
            String opcion = (String) new JOptionPane().showInputDialog(null, "Gatitos Java", "Menu Principal", JOptionPane.INFORMATION_MESSAGE, null, botones, botones[0]);
            // Validamos que opcion selecciono el usuario
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    opcionMenu = i;
                }
            }
            
            switch(opcionMenu) {
                case 0:
                    GatosService.verGatos();
                    break;
                case 1:
                    Gatos gato = new Gatos();
                    GatosService.verFavoritos(gato.getApiKey());
                default:
                    break;
            }
                    
        } while (opcionMenu != 1);
    }
    
}
