package com.orosasp.gatosapp;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class GatosService {

    public static void verGatos() throws IOException {
        //1 Vamos a traer losd atos de la API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search?")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        
        String elJason = response.body().string();
        
        // Cortar los corchetes
        elJason = elJason.substring(1, elJason.length());
        elJason = elJason.substring(0, elJason.length()- 1);
        
        // Crear objeto de la clase Gson
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJason, Gatos.class);
         
        // Redimencionar en caso de ser neceesario
        try {
            Image image = null;
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);
            ImageIcon fondoGato = new ImageIcon(image);
            if (fondoGato.getIconWidth() > 800) {
                // Redimencionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }
            String menu = "Opciones: \n"
                    + "1. Ver otra imagen\n"
                    + "2. Favorito\n"
                    + "3. Volver al menu\n";
            String[] botones = {"Ver otra imagen", "Favorito", "Volver al menu"};
            String idGato = String.valueOf(gatos.getId());
            String opcion = (String) JOptionPane.showInputDialog(null, menu, idGato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);
            int seleccion = -1;
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    seleccion = i;
                }
            }
            switch(seleccion) {
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public static void favoritoGato(Gatos gato) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n  \"image_id\": \""+ gato.getId() +"\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void verFavoritos(String apikey) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("x-api-key", apikey)
                .build();
        Response response = client.newCall(request).execute();

        // Guardamos el String con la respuesta
        String elJason = response.body().string();

        //Creamos el Objeto Gson
        Gson gson = new Gson();

        GatosFav[] gatosArray = gson.fromJson(elJason, GatosFav[].class);

        if (gatosArray.length > 0) {
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max - min) + 1)) + min;
            int indice = aleatorio - 1;

            GatosFav gatoFav = gatosArray[indice];

            // Redimencionar en caso de ser neceesario
            try {
                Image image = null;
                URL url = new URL(gatoFav.image.getUrl());
                image = ImageIO.read(url);
                ImageIcon fondoGato = new ImageIcon(image);
                if (fondoGato.getIconWidth() > 800) {
                    // Redimencionamos
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }
                String menu = "Opciones: \n"
                        + "1. Ver otra imagen\n"
                        + "2. Eliminar Favorito\n"
                        + "3. Volver al menu\n";
                String[] botones = {"Ver otra imagen", "Eliminar Favorito", "Volver al menu"};
                String idGato = String.valueOf(gatoFav.getId());
                String opcion = (String) JOptionPane.showInputDialog(null, menu, idGato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);
                int seleccion = -1;
                for (int i = 0; i < botones.length; i++) {
                    if (opcion.equals(botones[i])) {
                        seleccion = i;
                    }
                }
                switch (seleccion) {
                    case 0:
                        verFavoritos(apikey);
                        break;
                    case 1:
                        borrarFavorito(gatoFav);
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println(e);
            }

        }
    }

    public static void borrarFavorito(GatosFav gatoFav) {
        try {
            OkHttpClient client = new OkHttpClient();
            
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + gatoFav.getId())
                    .delete(null)
                    .addHeader("x-api-key", gatoFav.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
