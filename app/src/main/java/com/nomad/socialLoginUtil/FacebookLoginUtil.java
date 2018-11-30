package com.nomad.socialLoginUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FacebookLoginUtil {
    Activity activity;
    CallbackManager callbackManager;
    AccessToken accessToken;
    FacebookLoginListener facebookLoginListener;

    /*call this constructor in onCreate() of Activity*/
    public FacebookLoginUtil(final Activity activity, final FacebookLoginListener facebookLoginListener) {
        this.activity = activity;
        this.facebookLoginListener = facebookLoginListener;
        callbackManager = CallbackManager.Factory.create();

        accessToken = AccessToken.getCurrentAccessToken();
        if (LoginManager.getInstance() != null){
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                final String userId = loginResult.getAccessToken().getUserId();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try{
                                    // Application code
                                    String email = object.getString("email");
                                    facebookLoginListener.onFacebookLoginSuccess(email, userId);
                                   // String birthday = object.getString("birthday"); // 01/31/1980 format
                                }catch (JSONException exception){
                                    facebookLoginListener.onFacebookLoginError(exception.getMessage());
                                }


                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                //facebookLoginListener.onFacebookLoginSuccess(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                facebookLoginListener.onFacebookLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                facebookLoginListener.onFacebookLoginError(error.getMessage());
            }
        });
    }

    /*call this method with Array.asList(YOUR ACCESS ITEMS) when the login button clicked*/
    public void loginWithFacebook(List accessList) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
    }

    /*get CallbackManager and use in onActivityResult as
     * super.getCallBackManager.onActivityResult(...)*/
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void logoutFromFacebook() {
        LoginManager.getInstance().logOut();
    }

    /*get these call backs*/
    public interface FacebookLoginListener {
        void onFacebookLoginSuccess(String email, String userId);

        void onFacebookLoginCancel();

        void onFacebookLoginError(String error);

        void onFacebookLogoutCompleted();
    }
}
