package com.ajaysinghdewari.facebooklogin;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends FragmentActivity {

    private TextView tvfirst_name, tvlast_namee, tvfull_name, tvEmail;
    private CallbackManager callbackManager;
    LoginButton login_button;
    String email,name,first_name,last_name;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        tvfirst_name        = (TextView) findViewById(R.id.first_name);
        tvlast_namee        = (TextView) findViewById(R.id.last_name);
        tvfull_name         = (TextView) findViewById(R.id.full_name);
        tvEmail             = (TextView) findViewById(R.id.email);
        login_button        = (LoginButton) findViewById(R.id.fb_login_button);
        imageView           = (ImageView) findViewById(R.id.fb_profile_pic);

        login_button.setReadPermissions(Arrays.asList("public_profile","email")); //user_birthday, friends_birthday
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest   =   GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.d("JSON1", ""+response.getJSONObject().toString());
                        Log.d("JSON2", ""+object.toString());

                        try
                        {
                            String user_id=object.getString("id");
                            String profilePicUrl="http://graph.facebook.com/"+user_id+"/picture";
                            email       =   object.getString("email");
                            name        =   object.getString("name");
                            first_name  =   object.optString("first_name");
                            last_name   =   object.optString("last_name");

                            tvEmail.setText(email);
                            tvfirst_name.setText(first_name);
                            tvlast_namee.setText(last_name);
                            tvfull_name.setText(name);

                            Glide.with(MainActivity.this)
                                    .load(profilePicUrl)
                                    .into(imageView);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /*    private void printHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ajaysinghdewari.facebooklogin",
                    PackageManager.GET_SIGNATURES);
            Log.d("KeyHash:", "");
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("And The Hash key is====  "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Toast.makeText(MainActivity.this,"HashKey====="+Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }

        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }*/
}
