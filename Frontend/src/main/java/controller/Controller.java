package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Controller {

    @FXML
    TextArea friends;

    @FXML
    public void initialize() {
        friends.setText(getUsers());
    }

    public String getUsers() {
        /**
         * SAA MUOKATA Tää funktio napattu olio-ohjelmoinnin kurssilt. Testinä
         * haetaan Text-Areaan kaikki käyttäjät endpointista. Idean olis
         * kuitenki hakee vaik kaverit tohon tulevaisuudessa.
         */
        URL myUrl;
        try {
            myUrl = new URL("http://localhost:8080/api/users");
        } catch (MalformedURLException e) {
            System.err.println(e);
            return "Error -- Ei yhteyttä Endpointtiin, koska serveri ei ole päällä";
        }

        try {

            InputStream istream = myUrl.openStream();

            InputStreamReader istreamreader = new InputStreamReader(istream);

            BufferedReader reader = new BufferedReader(istreamreader);

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();

        } catch (IOException e) {
            System.err.println(e);
            return "Error -- Ei yhteyttä Endpointtiin, koska serveri ei ole päällä";
        }
    }
}
