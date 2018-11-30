package com.nomad.socialLoginUtil;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class GoogleLoginUtil {
    //Context context;
    GoogleSignInAccount googleSignInAccount;
    GoogleSignInClient googleSignInClient;

    Activity activity;
    GoogleSignInListener googleSingInListener;
    List<String> googleSignInResultList = null;
    /*call the constructor in onCreate() method of the Activity  */
    public GoogleLoginUtil(Activity activity, GoogleSignInListener googleSignInListener){
        this.activity = activity;
        this.googleSingInListener = googleSignInListener;
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity);

    }
    /*call this method to get intent and call startActivityForResult(intent, YOUR_REQ_CODE)*/
    public Intent getGoogleSignInIntent(){
        return  googleSignInClient.getSignInIntent();
    }

    /*call this method in onActivityResult and pass the Intent data received here*/

    public List<String> getGoogleSignInResult(Intent data){
        GoogleSignInAccount result = null;
        googleSignInResultList = new ArrayList<>();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            result = task.getResult(ApiException.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        if (result!=null)
        {
            googleSignInResultList.add(result.getId());
            googleSignInResultList.add(result.getIdToken());
            googleSignInResultList.add(result.getEmail());
            googleSignInResultList.add(result.getDisplayName());
        }

        return googleSignInResultList;
    }

    /*call this method on logout button click  and get a call back onLogoutFromGoogleComplete*/
    public void logoutFromGoogleAccount(){

        googleSignInClient.signOut().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                googleSingInListener.onLogoutFromGoogleComplete();
            }
        });


    }

    public interface GoogleSignInListener{
        void onLogoutFromGoogleComplete();
    }
}


