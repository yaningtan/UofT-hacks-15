package yaning.uofthacks15;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class Main extends ActionBarActivity {
    RequestQueue requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Button inputButton = (Button) findViewById(R.id.inputButton);

        inputButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView textInput =
                                (TextView)findViewById(R.id.textInput);

                        String input = textInput.getText().toString();


                        //Justin's Function here
                        // Just the first line for now
                        //separable means the phrase has one or more *'s in it
                        boolean separable = false;
                        for (int i = 0; i <= input.length()-1; i++)
                        {
                            if(input.charAt(i)== '*')
                            separable = true;
                        }

                        if(!separable)
                            displaySuggestions(input);
                        else if (input.charAt(input.length()-1) != '*')
                            displayAutocomplete(input);
                        else
                            textInput.setText("Not available yet.");

                    }
                }
        );
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

    private String query;
    public void changeText(String[] suggestions) {
        TextView textInput = (TextView)findViewById(R.id.textInput);
        query = textInput.getText().toString();
        if(suggestions.length != 0 )
            query = suggestions[0];
        textInput.setText(query);

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
                        TextView textInput = (TextView)findViewById(R.id.textInput);
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
