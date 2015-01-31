# UofT-hacks-15

Using this to share code

Main Activity.java

under package paste
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

replace the onCreate with 
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button inputButton = (Button) findViewById(R.id.inputButton);

        inputButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        TextView textInput =
                                (TextView)findViewById(R.id.textInput);

                        String input = textInput.getText().toString();

                        //Justin's Function here
                        //Andrew's Function also here

                        textInput.setText("");

                    }
                }
        );
    }
    
    In the xml file paste
    <EditText
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:id="@+id/textInput"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true"
        android:layout_marginTop="157dp" />

    <Button
        style="?android:attr/buttonStyleSmall"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Search"
        android:id="@+id/inputButton"
        android:layout_alignBottom="@+id/textInput"
        android:layout_toRightOf="@+id/textInput"
        android:layout_toEndOf="@+id/textInput" />

    <EditText
        android:layout_width="wrap_content"
        android:layout_height="200dp"
        android:id="@+id/outputText"
        android:layout_below="@+id/textInput"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true"
        android:layout_marginTop="30dp"
        android:text="Text goes here"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_alignParentEnd="true"
        android:background="#00000000"/>

