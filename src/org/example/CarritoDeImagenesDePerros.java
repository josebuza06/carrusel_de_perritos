package org.example;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CarritoDeImagenesDePerros {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final int MIN_IMAGENES = 10;
    private static final int MAX_IMAGENES = 20;

    public static void main(String[] args) throws IOException {
        // Obtener la raza de perro seleccionada por el usuario
        String raza = JOptionPane.showInputDialog("Ingrese la raza de perro que desea ver: ");

        // Descargar 10 imágenes aleatorias
        int numeroDeImagenes = new Random().nextInt(MAX_IMAGENES - MIN_IMAGENES + 1) + MIN_IMAGENES;
        Image[] imagenes = new Image[numeroDeImagenes];
        for (int i = 0; i < numeroDeImagenes; i++) {
            imagenes[i] = descargarImagen(raza);
        }

        // Mostrar las imágenes en un carrusel
        JFrame frame = new JFrame();
        JLabel label = new JLabel();
        frame.add(label);
        frame.setSize(800, 600);
        frame.setVisible(true);

        int imagenActual = 0;
        while (true) {
            label.setIcon(new ImageIcon(imagenes[imagenActual]));
            frame.repaint();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            imagenActual = (imagenActual + 1) % numeroDeImagenes;
        }
    }

    private static Image descargarImagen(String raza) throws IOException {
        URL url = new URL("https://api.thedogapi.com/v1/images/random");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("x-api-key", API_KEY);
        if (raza != null) {
            connection.setRequestProperty("breed", raza);
        }
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            InputStream inputStream = connection.getInputStream();
            return ImageIO.read(inputStream);
        } else {
            throw new IOException(String.format("Error al descargar la imagen: %d", responseCode));
        }
    }
}

