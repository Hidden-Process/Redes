package es.uma.rysd.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ClientInfoStatus;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import es.uma.rysd.entities.TokenResponse;
import es.uma.rysd.entities.Category;
import es.uma.rysd.entities.CategoryResponse;
import es.uma.rysd.entities.Question;
import es.uma.rysd.entities.QuestionResponse;

public class TriviaClient {
    private String token = null;


    private final String app_name = "OpenTrivia";

    private final String url_api = "https://opentdb.com/";
    private final String get_token = url_api + "api_token.php?command=request";
    private final String categories = url_api + "api_category.php";
    private final String questions = url_api + "api.php";

    public TriviaClient(){
        super();

        try{
            URL client = new URL(get_token);
            HttpsURLConnection connection = (HttpsURLConnection) client.openConnection();


            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setRequestProperty("User-Agent", app_name);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            if(connection.getResponseCode() != HttpsURLConnection.HTTP_OK) System.out.println("Error checkeando la conexión del cliente");
            else {
                Gson parser = new Gson();
                InputStream in = connection.getInputStream();
                TokenResponse r = parser.fromJson(new InputStreamReader(in), TokenResponse.class);
                token = r.token;
            }
        } catch(MalformedURLException e1){
            System.out.println("Error URL malformada en el cliente " + e1.getMessage());
        } catch(IOException e2){
            System.out.println("Error al abrir la conexión en el cliente " + e2.getMessage());
        }
    }

    Category [] getCategories(){
        try{
            URL categoria = new URL(categories);
            HttpsURLConnection connection = (HttpsURLConnection) categoria.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setRequestProperty("User-Agent", app_name);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            if(connection.getResponseCode() != HttpsURLConnection.HTTP_OK)  System.out.println("Error checkeando la conexión de categorias.");
            else {
                Gson parser = new Gson();
                InputStream in = connection.getInputStream();
                CategoryResponse r = parser.fromJson(new InputStreamReader(in), CategoryResponse.class);
                return r.trivia_categories;
            }
        } catch(MalformedURLException e3){
            System.out.println("Error URL malformada en las categorias " + e3.getMessage());
        } catch(IOException e4){
            System.out.println("Error al abrir la conexión en las categorias " + e4.getMessage());
        }
        return null;
    }

    Question [] getQuestions(Integer amount, Integer category, String difficulty){

        try{

            URL cat;

            if(category == null) cat = new URL(questions + "?amount=" + amount + "&difficulty=" + URLEncoder.encode(difficulty, "UTF-8") + "&encode=url3986&token=" + token);
            else cat = new URL(questions + "?amount=" + amount + "&difficulty=" + URLEncoder.encode(difficulty, "UTF-8") + "&category=" + category + "&encode=url3986&token=" + token);

            URL cuestiones = cat;
            HttpsURLConnection connection = (HttpsURLConnection) cuestiones.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setRequestProperty("User-Agent", app_name);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            if(connection.getResponseCode() != HttpsURLConnection.HTTP_OK)  System.out.println("Error checkeando la conexión de cuestiones.");
            else {
                Gson parser = new Gson();
                InputStream in = connection.getInputStream();
                QuestionResponse q = parser.fromJson(new InputStreamReader(in), QuestionResponse.class);

                return q.results;
            }

        } catch(MalformedURLException e5){
            System.out.println("Error URL malformada en las cuestiones " + e5.getMessage());
        } catch(IOException e6){
            System.out.println("Error al abrir la conexión en las cuestiones " + e6.getMessage());
        }
        return null;
    }
}
