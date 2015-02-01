package yaning.uofthacks15;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.text.style.*;
import android.text.*;
import android.content.Context;
import android.widget.ImageView;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.BitmapFactory;

import android.graphics.Bitmap;
import java.io.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URL;
import java.util.Arrays;

public class Main extends ActionBarActivity {
    RequestQueue requests;
    //RandomWord rw = new RandomWord(getAssets());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().commit();
        }

        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);
        Button createButton = (Button) findViewById(R.id.createButton);
        Button googleButton = (Button) findViewById(R.id.googleButton);

        searchButton.setEnabled(true);

        clearButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView textInput =
                                (TextView) findViewById(R.id.textInput);

                        textInput.setText("");
                    }
                }
        );

        searchButton.setOnClickListener(
                new Button.OnClickListener() {

                    public void onClick(View v) {
                        TextView textInput =
                                (TextView)findViewById(R.id.textInput);

                        String input = textInput.getText().toString();

                        // Just the first line for now
                        //separable means the phrase has one or more *'s in it
                        boolean separable = false;
                        for (int i = 0; i <= input.length()-1; i++)
                        {
                            if(input.charAt(i)== '*')
                            separable = true;
                        }

                        if(!separable) {
                            displaySuggestions(input);
                        } else {
                            displayAutocomplete(input);
                        }

                        //displaySuggestedImage(input);

                    }
                }
        );

        createButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        displayRandom();
                    }
                }
        );

        googleButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView textInput =
                                (TextView)findViewById(R.id.textInput);

                        launchBrowser(textInput.getText().toString());
                    }
        });

        requests = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Google search result for the phrase
    public void launchBrowser(String phrase) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("https://google.com/#q=" + phrase));
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String query;
    public void changeText(String[] suggestions) {
        TextView textInput = (TextView)findViewById(R.id.textInput);
       /* query = textInput.getText().toString();
        if(suggestions.length != 0 )
            query = suggestions[0];
        textInput.setText(query);*/


        SpannableStringBuilder builder = new SpannableStringBuilder();

        //query = textInput.getText().toString();
        //query = "*";
        //query = query.replaceFirst("\\*", suggestions[0]);

        //String original_word = sep(textInput.getText().toString());
        String original_word = textInput.getText().toString();

        SpannableString wordSpan= new SpannableString(original_word);
        wordSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, original_word.length(), 0);
        builder.append(wordSpan);

        String suggested_word = suggestions[0];
        String rest_word = suggested_word.substring(original_word.length(), suggested_word.length());
        SpannableString sugSpan= new SpannableString(rest_word);
        sugSpan.setSpan(new ForegroundColorSpan(Color.GRAY), 0, rest_word.length(), 0);
        builder.append(sugSpan);

        //query = builder.toString();
        // Apply formatting to the suggestion
        //textInput = (TextView)findViewById(R.id.textInput);

        // textInput.setText(builder, BufferType.SPANNABLE);
        //textInput.setText(original_word + " " +  rest_word + suggested_word.length() );

        textInput.setText(builder, TextView.BufferType.SPANNABLE);
        //textInput.setText(original_word + rest_word + suggested_word.length() );


    }

    public static String sep(String s)
    {
        int l = s.indexOf(" *");
        if (l >0)
        {
            return s.substring(0, l);
        }
        return "";

    }

    public void displaySuggestions(String input) {
        String url = "http://suggestqueries.google.com/complete/search?q=&client=android";
        String[] suggestions;

        // Parse the query and turn it into url
        input = input.replaceAll(" ", "+");
        input = input.replaceAll("\"", "%22");

        url = url.replace("q=", "q=" + input);

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String[] suggestions = response.get(1).toString().split("\\[\"|\".\"|\"\\]");

                            if (suggestions.length > 1) {// Update the text field
                                suggestions = Arrays.copyOfRange(suggestions, 1, suggestions.length - 1);
                                changeText(suggestions);
                            } else{
                                Log.d("Unsearchable:", " failed");
                            }


                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requests.add(jsArrayRequest);
    }

    public void displaySuggestedImage(String input) {
        // Find the first relevant image
        String url = "https://www.google.ca/search?tbm=isch&tbs=itp:photo&q=";
        //String url = "https://www.flickr.com/search?text=";
        //String url = "https://c1.staticflickr.com/9/8669/16405780632_8a9c3d13a9.jpg";
        String[] suggestions;
        ImageView mImageView;

        // Parse the query and turn it into url
        input = input.replaceAll(" ", "+");
        input = input.replaceAll("\"", "%22");

        url += input;

        // Find the first image
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        try {
                            //InputStream is = (InputStream) new URL(ImageParser.parse(response)).getContent();
                            //Bitmap bitmap = BitmapFactory.decodeStream(is);
                            String s = ImageParser.parse(response);
                            System.out.println(s);
                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                            if (bitmap == null)
                                throw new NullPointerException();
                            imageView.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requests.add(stringRequest);

        /*ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        displaySuggestedImage();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        ImageView mImageView = (ImageView) Main.this.findViewById(R.id.imageView);
                        mImageView.setImageResource(0); // R.drawable.image_load_error
                    }
                });

        requests.add(request);*/
    }

    public void displayRandom() {

        // clear
        TextView textInput =
                (TextView) findViewById(R.id.textInput);
        textInput.setText("");

        //String s1 = rw.getString();
        //String s2 = rw.getString();

        //displayAutocomplete(s1 + " * " + s2);
    }

    public void displayAutocomplete(String query) {
        String url = "";
        try {
           url = "http://google.com/search?q=" + URLEncoder.encode('"' + query + '"', "UTF-8");
        } catch (UnsupportedEncodingException e) {}


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView textInput = (TextView) findViewById(R.id.textInput);
                        textInput.setText(ResultParser.parse(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requests.add(stringRequest);
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            return rootView;
//        }
//    }
}
