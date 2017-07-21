package com.rahulsamples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SocialLoginActivity extends AppCompatActivity {

    private static final String LOGTAG = SocialLoginActivity.class.getSimpleName();
    private static String ACCESS_TOKEN;
    @Bind(R.id.tv_linked_in)
    TextView tv_linked_in;
    @Bind(R.id.tv_social_id)
    TextView tv_social_id;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.tv_profile_pic)
    TextView tv_profile_pic;
    @Bind(R.id.tv_email)
    TextView tv_email;
    @Bind(R.id.tv_Name)
    TextView tv_Name;

    private static final String host = "api.linkedin.com";
    //    private static final String topCardUrl = "https://" + host + "/v1/people/~?format=json:" +
    // private static final String url = "https://" + host + "/v1/people/~:(id,email-address,first-name,,last-name,public-profile-url,picture-url,picture-urls::(original))";
    // String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name)";
    private static final String url = "https://" + host + "/v1/people/~:" + "(id,email-address,formatted-name,phone-numbers,picture-urls::(original))";

    //String url = "https://api.linkedin.com/v1/people/~";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        ButterKnife.bind(this);
        setListener();

        // Method to check Internet connectivity
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                // System.out.println("Online: " +(testInet("https://firstjob.co.in")/*|| testInet("google.com") || testInet("amazon.com")*/));
                System.out.println("Online: " +isReachableByPing("firstjob.co.in"));
            }
        });
        thread.start();

    }

    // Method to check Internet connectivity
    public static boolean isReachableByPing(String host) {
        try{
            String cmd = "";
            if(System.getProperty("os.name").startsWith("Windows")) {
                // For Windows
                cmd = "ping -n 1 " + host;
            } else {
                // For Linux and OSX
                cmd = "ping -c 1 " + host;
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            if(myProcess.exitValue() == 0) {

                return true;
            } else {

                return false;
            }

        } catch( Exception e ) {

            e.printStackTrace();
            return false;
        }
    }

    // Method to check Internet connectivity
    public static boolean testInet(String site) {
        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress(site,80);
        try {
            sock.connect(addr,3000);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {sock.close();}
            catch (IOException e) {}
        }
    }

    private void setListener() {
        tv_linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISessionManager.getInstance(getApplicationContext()).init(SocialLoginActivity.this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        login();
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }, true);
            }
        });
    }

    // Authenticate with linkedin and intialize Session.
    public void login() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse s) {
                String fullName = "";
                String socialId = "";
                String email = "";
                String profile_url = "";

                JSONObject object = s.getResponseDataAsJson();

                if (object != null) {

                    try {
                        if (object.getString("emailAddress") != null && object.getString("emailAddress").length() > 0) {
                            email = object.getString("emailAddress");
                            tv_email.setText("Email: "+email);
                        }else{
                            tv_email.setText("Email: ");
                        }


                        if (object.getString("formattedName") != null && object.getString("formattedName").length() > 0) {
                            fullName = object.getString("formattedName");
                            tv_Name.setText("Name: "+fullName);

                        }else{
                            tv_Name.setText("Name: ");
                        }

                        if (object.getString("id") != null && object.getString("id").length() > 0) {
                            socialId = object.getString("id");
                            tv_social_id.setText("Social Id: "+socialId);
                        }else{
                            tv_social_id.setText("Social Id: ");

                        }

                        if (object.getJSONObject("pictureUrls").getJSONArray("values") != null) {

                            int size = object.getJSONObject("pictureUrls").getJSONArray("values").length();
                            for (int i = 0; i < size; i++) {
                                profile_url = object.getJSONObject("pictureUrls").getJSONArray("values").getString(0);
                            }
                            tv_profile_pic.setText("Profile URL: "+profile_url);
                        }else{
                            tv_profile_pic.setText("Profile URL: ");

                        }

                        System.out.println("))))))))  Email: " + email + "  FullName: " + fullName + " SocialId:" + socialId +" Profile URL: "+profile_url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LISessionManager.getInstance(getApplicationContext()).clearSession();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                Toast.makeText(SocialLoginActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("Error: " + error.toString());
            }
        });


    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }
}
