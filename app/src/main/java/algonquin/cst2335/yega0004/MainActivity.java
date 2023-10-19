package algonquin.cst2335.yega0004;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Mohammadhassan Yeganeshenas
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**This holds the text at the center of the screen*/
    private TextView tv = null;
    /**This holds the password at the below position of the TextView*/
    private EditText et = null;
    /**This hold the Login button in the down of the page*/
    private Button bt = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editTextPass);
        Button bt = findViewById(R.id.loginButton);

        bt.setOnClickListener(clk -> {
            String password = et.getText().toString();
            boolean isPasswordComplex = checkPasswordComplexity(password);

            if (isPasswordComplex){
                tv.setText("Your password meets the requirements");
            }else {
                tv.setText("You shall not pass!");
            }
        });
    }


    /**
     * This function Checks if my password is strong enough.
     *
     * @param password The String object that we are checking
     * @return Returns true if the password is strong enough
     */
    Boolean checkPasswordComplexity(String password) {
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialSymbol = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if ("#$%^&*!@?".contains(String.valueOf(c))) {
                hasSpecialSymbol = true;
            }
        }

        if (!hasUpperCase) {
            showToast("Your password does not have an upper case letter");
            return false;
        } else if (!hasLowerCase) {
            showToast("Your password does not have a lower case letter");
            return false;
        } else if (!hasDigit) {
            showToast("Your password does not have a number");
            return false;
        } else if (!hasSpecialSymbol) {
            showToast("Your password does not have a special symbol (#$%^&*!@?)");
            return false;
        }


        return true; //only get here if they're all true

    }

    /** check if there is a special character for    the password complexity
     *
     * @param c The String object that we are checking
     * @return Returns true if it has a special character
     */
    private boolean isSpecialCharacter(char c){
        switch (c){
            case '#':
            case '*':
            case '?':
            case '!':
            case '@':
            case '$':
            case '%':
            case '^':
            case '&':
                return true;
            default:
                return false;
        }
    }

    /** A method for setting the Toast message
     *
     * @param message Give us the message that we are going to put as a toast
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
