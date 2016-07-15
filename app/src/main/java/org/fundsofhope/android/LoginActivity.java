package org.fundsofhope.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final String TAG = "LoginActivity";


    //    TODO Self Signup
//    SharedPreferences.Editor editor;
//    EditText _userName;
//    EditText _passWord;
//    Button _loginButton;
//    Button _signupButton;
//    int user_flag;
//    String email;
//    String password;
//    JSONObject jobj=null;
//    RadioButton user;
//    RadioButton ngo;
//    String URL;


    // TODO Google Sign-on
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 0;


    // TODO Old Google+ Sign-On
//    Person person;
//    private boolean mIsResolving = false;
//    private boolean mShouldResolve = false;
//
//    private static final int REQUEST_SIGNUP = 0;



    // TODO Facebook Sign-on
    CallbackManager callbackManager;
    int requestCode = 0;
    int loginIntent = 0;
    String postAction = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                googleSignIn();
                break;
            case R.id.sign_out_button:
                googlesignOut();
                break;
            case R.id.facebook_login:
                facebookLogin();
                break;
            case R.id.btn_login:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) view.findViewById(R.id.facebook_login);
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
//        loginButton = (LoginButton) findViewById(R.id.facebook_login);
        findViewById(R.id.facebook_login).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager,
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("hell", "entering profile");
                        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

                        final Profile profile = Profile.getCurrentProfile();

                        if (profile != null) {
                            makeGraphRequest(profile, accessToken);
                            Toast.makeText(LoginActivity.this, "name" + profile.getName()+profile.getFirstName()+profile.getLastName()+profile.getId(), Toast.LENGTH_LONG).show();
                        } else {
                            ProfileTracker profileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                    if (currentProfile != null) {
                                        makeGraphRequest(currentProfile, accessToken);
                                    }
                                    stopTracking();
                                }
                            };
                            profileTracker.startTracking();
                        }

                        Intent intent = getIntent();
                        requestCode = intent.getIntExtra("requestCode", 0);
                        loginIntent = intent.getIntExtra("intent", 0);
                        postAction = intent.getStringExtra("postAction");

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Could not sign in. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Could not sign in. Please try again.", Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    }

                });

//        TODO For Google+
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(new Scope(Scopes.PLUS_LOGIN))
//                .addScope(new Scope("email"))
//                .build();


//        editor = pref.edit();

//        TODO Self Sign-in
//        editor.putInt("flag", 0);
//        _userName = (EditText) findViewById(R.id.input_user);
//        _passWord = (EditText) findViewById(R.id.input_password);
//
//        _loginButton = (Button) findViewById(R.id.btn_login);
//        _signupButton = (Button) findViewById(R.id.btn_signup);
//        user=(RadioButton)findViewById(R.id.user);
//        ngo=(RadioButton)findViewById(R.id.ngo);
//        user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ngo.setEnabled(false);
//                user_flag = 1;
//            }
//        });
//        ngo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                user.setEnabled(false);
//                user_flag = 2;
//            }
//        });

//        _loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                email = _userName.getText().toString();
//                password = _passWord.getText().toString();
//                SharedPreferences mpref =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                editor = mpref.edit();
//                editor.putInt("user",user_flag);
//                editor.apply();
//                new LoginTask().execute("");
//            }
//        });

//        _signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
//            }
//        });

    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

    // Bypassing the login
//    protected void successlog(View v){
//        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//        startActivity(intent);
//        finish();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void googlesignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            Toast.makeText(LoginActivity.this, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void makeGraphRequest(Profile profile, final AccessToken accessToken) {

        final String name = profile.getName();
        final String fbId = profile.getId();
        final Uri fbPic = profile.getProfilePictureUri(150, 150);

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                final String email = user.optString("email");
                final String gender = user.optString("gender");
                String ageRangeMin = "";
                String ageRangeMax = "";
                try {
                    final JSONObject ageRange = user.getJSONObject("age_range");
                    ageRangeMin = ageRange.optString("min");
                    ageRangeMax = ageRange.optString("max");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String firstName = user.optString("first_name");
                final String lastName = user.optString("last_name");
                final String birthday = user.optString("birthday");
                Toast.makeText(LoginActivity.this,firstName+lastName+birthday+email+gender+name+fbId+fbPic+ageRangeMax+ageRangeMin, Toast.LENGTH_LONG).show();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "name, id, email, gender, age_range, birthday, bio, first_name, last_name");
        request.setParameters(bundle);
        request.executeAsync();
    }


    // TODO Google+ Login Extras
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.d("SplashActivity", "onConnectionFailed:" + connectionResult);
//
//        if (!mIsResolving && mShouldResolve) {
//            if (connectionResult.hasResolution()) {
//                try {
//                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
//                    mIsResolving = true;
//                } catch (IntentSender.SendIntentException e) {
//
//                    mIsResolving = false;
//                    mGoogleApiClient.connect();
//                }
//            } else {
//                showErrorDialog(connectionResult);
//            }
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            if (resultCode != RESULT_OK) {
//                mShouldResolve = false;
//            }
//            mIsResolving = false;
//            mGoogleApiClient.connect();
//        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//        }
//    }

//    public void googleLogin() {
//        Log.i("hell", "enterd google login");
//        person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//        if (person != null) {
//            Log.i("hell", "fetching details");
//            final String googleId = person.getId();
//            final String name = person.getDisplayName();
//            final String pic = person.getImage().getUrl();
//            final String birthday = person.getBirthday() != null ? person.getBirthday() : "";
//            Person.AgeRange ageRange = person.getAgeRange();
//            final int ageRangeMin = ageRange != null ? ageRange.getMin() : 0;
//            final int ageRangeMax = ageRange != null ? ageRange.getMax() : 0;
//            final String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//            final String gender = (person.getGender() == Person.Gender.MALE) ? "male" : "female";
//            final String firstName = person.getName().getGivenName();
//            final String lastName = person.getName().getFamilyName();
//            prefs = getSharedPreferences("application_settings", 0);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putString("googleId", googleId);
//            editor.putString("name", name);
//            editor.putString("pic", pic + "&sz=200");
//            editor.putString("email", email);
//            editor.putString("birthday", birthday);
//            editor.putString("ageRangeMin", String.valueOf(ageRangeMin));
//            editor.putString("ageRangeMax", String.valueOf(ageRangeMax));
//            editor.putString("gender", gender);
//            editor.apply();
//            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//            finish();
//            Toast.makeText(LoginActivity.this,name+pic+birthday+email+firstName+lastName,Toast.LENGTH_LONG).show();
//        }
//        else
//            Toast.makeText(LoginActivity.this,"Error login", Toast.LENGTH_LONG).show();
//    }

//    public void googleLogin(View v) {
//        mShouldResolve = true;
//        mGoogleApiClient.connect();
//    }

//    private void showErrorDialog(ConnectionResult connectionResult) {
//        int errorCode = connectionResult.getErrorCode();
//
//        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
//            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
//                    new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            mShouldResolve = false;
//                        }
//                    }).show();
//        } else {
//            mShouldResolve = false;
//        }
//    }

    //    TODO Self Sign-in AsyncTask & Functions
    //    private class LoginTask extends AsyncTask<String, Integer, JSONObject> {
//
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Authenticating...");
//            progressDialog.show();
//        }
//
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            try{
//                Log.i(TAG,"entered try()");
//
//    Toast.makeText(getApplicationContext(), "Please wait,connecting to server",Toast.LENGTH_LONG).show();
//                Log.i(TAG,"entered toast()");
//                Log.i(TAG,email);
//                Log.i(TAG,password);
//                if(user_flag==1)
//                    URL="http://fundsofhope.herokuapp.com/user/login";
//                else if(user_flag==2)
//                    URL="http://fundsofhope.herokuapp.com/ngo/login";
//                Log.i(TAG, "in response handler");
//
//                List<NameValuePair> data = new ArrayList<NameValuePair>();
//                if(user_flag==1)data.add(new BasicNameValuePair("username", email));
//                else if(user_flag==2)data.add(new BasicNameValuePair("ngoid", email));
//                data.add(new BasicNameValuePair("password", password));
//
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//
//                HttpPost httpPost = new HttpPost(URL);
//                httpPost.setEntity(new UrlEncodedFormEntity(data));
//
//                HttpResponse httpResponse = httpClient.execute(httpPost);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                Log.i(TAG,"executed");
//                InputStream is = httpEntity.getContent();
//                Log.i(TAG, "in strict mode");
//
//                try {
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line;
//                    while ((line = reader.readLine()) != null)
//                    {
//                        sb.append(line).append("\n");
//                    }
//                    is.close();
//                    String page_output = sb.toString();
//                    jobj=new JSONObject(page_output);
//                    Log.i("LOG", "page_output --> " + page_output);
//                } catch (Exception e) {
//                    Log.e("Buffer Error", "Error converting result " + e.toString());
//                }
//
//                Log.i(TAG,"request executed");
//
//            } catch (IOException ignored) {}
//            Log.i(TAG, "returning response");
//            return jobj;
//        }
//
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//        }
//
//        protected void onPostExecute(JSONObject result){
//            Log.i(TAG, "Entered on post execute");
//            progressDialog.dismiss();
//
//
//            Toast.makeText(LoginActivity.this,"length="+result.length()+result, Toast.LENGTH_LONG).show();
//
//
//            try {
//                if(result.getBoolean("login")) {
//                    SharedPreferences.Editor editor;

//                    SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    editor = pref.edit();
//
//                    String token=result.getString("token");
//
//                    String message=result.getString("message");
//                    editor.putInt("flag", 1);
//                    editor.putInt("user",user_flag);
//
//
//                    editor.putString("token", token);
//                    editor.putString("email",email);
//                    editor.putString("pass",password);
//                    editor.apply();
//
//                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
//                    System.out.println("token is"+token);
//                    Log.i(TAG, "Entered if");
//                    onLoginSuccess();
//                    successlog();
//                }
//                else if (!result.getBoolean("login")) {
//                    onLoginFailed();
//                }
//                else{
//                    Toast.makeText(LoginActivity.this, "Can't Connect", Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }

    //    public void onLoginSuccess() {
//        _loginButton.setEnabled(true);
//        finish();
//    }

//    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "SplashActivity failed", Toast.LENGTH_LONG).show();
//        _loginButton.setEnabled(true);
//    }

//    public boolean validate() {
//        boolean valid = true;
//        String password = _passWord.getText().toString();
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passWord.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            _passWord.setError(null);
//        }
//        return valid;
//    }

}
