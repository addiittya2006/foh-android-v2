package org.fundsofhope.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.fundsofhope.android.config.ApiInterface;
import org.fundsofhope.android.config.ServiceGenerator;
import org.fundsofhope.android.model.SignupStatus;
import org.fundsofhope.android.util.NetworkUtilities;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final String TAG = "LoginActivity";
    public String name;
    public String email;
    public String phoneNo;


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
            case R.id.btn_signup:
                Intent intent1 = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(intent1);
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
        findViewById(R.id.btn_signup).setOnClickListener(this);

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
                            Toast.makeText(LoginActivity.this, "name" + profile.getName() + profile.getFirstName() + profile.getLastName() + profile.getId(), Toast.LENGTH_LONG).show();
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
        } else {
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
            register(acct.getDisplayName(),  acct.getEmail(),"0123456789",acct.getIdToken());
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
                register(firstName + lastName, email, "99000900", fbId);
                Toast.makeText(LoginActivity.this, firstName + lastName + birthday + email + gender + name + fbId + fbPic + ageRangeMax + ageRangeMin, Toast.LENGTH_LONG).show();

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "name, id, email, gender, age_range, birthday, bio, first_name, last_name");
        request.setParameters(bundle);
        request.executeAsync();
    }

    private void register(String name, String email, String phoneNo, String fbCred) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        String serverURL1 = "http://api.fundsofhope.org/signup/";
        new LongOperation2().execute(serverURL1);
        /*
        if (NetworkUtilities.isInternet(LoginActivity.this)) {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            apiInterface.signup(name,email,phoneNo,
                    new retrofit.Callback<SignupStatus>() {
                        @Override
                        public void success(SignupStatus signupStatus, Response response) {

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("TAG", "Response : Failure " + error.getMessage());
                        }
                    });
        } else {
//            DebugLog.d(getString(R.string.error_no_internet_connection));
        }*/

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
    private class LongOperation2 extends AsyncTask<String, Void, SignupStatus> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        // private String Content;
        private String Error = null;
        private SignupStatus result;
        String studentId;
        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        String data = "";

        int sizeData = 0;

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            // Start Progress Dialog (Message)
            // String studentid="";

            // Intent intent = getIntent();

            // if (intent != null) {

            // emailId = intent.getStringExtra("emailId");

            // }*/

            Dialog.setMessage("Please wait..");
            Dialog.show();
            // try{
            // Set Request parameter
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("phoneNo", phoneNo);
            jsonObject.addProperty("email", email);


            Gson gson2 = new Gson();

            String jsonString = gson2.toJson(jsonObject);
            Log.i("hell", jsonString);
            // data +=
            // "{\"order\":{\"instructions\":\"\",\"paymentMethod\":\"COD\",\"items\":[{\"itemId\":962,\"name\":\"Cottage Cheese & Grilled Veggies Salad\",\"smallImageUrl\":\"/static/21/962_Cottage_Cheese_Salad200x200.jpg\",\"price\":249,\"itemType\":\"Veggies\",\"instructions\":\"instructions Abc\",\"quantity\":1},{\"itemId\":867,\"name\":\"Greek Salad\",\"smallImageUrl\":\"/static/21/867_Greek_Salad200x200.jpg\",\"price\":219,\"itemType\":\"Veggies\",\"instructions\":\"ABC\",\"quantity\":1}],\"deliveryCharges\":30,\"discountAmount\":117,\"discountPercentage\":25,\"finalOrderAmount\":397,\"discountList\":[{\"id\":4,\"name\":\"Corprate Discount\",\"category\":\"Discount\",\"type\":\"PERCENTAGE\",\"value\":25}],\"deliveryDateTime\":\"12-7-2015 19:45\"},\"customer\":{\"name\":\"hhhhh null\",\"phone\":9540095277,\"email\":\"rahul@cookedspecially.com\",\"address\":\"nvdiv eiv iwr\",\"deliveryArea\":\"DLF Phase 3\",\"city\":\"Gurgaon\",\"id\":9970}}";
            // data += "{" + "\"phoneNumber\"" + ":\"" +
            // mobileno.getText().toString()
            // + "\"}";
            data = jsonString;
            // data +=
            // "%7B%22order%22%3A%7B%22instructions%22%3A%22%22%2C%22paymentMethod%22%3A%22COD%22%2C%22items%22%3A%5B%7B%22itemId%22%3A962%2C%22name%22%3A%22Cottage+Cheese+%26+Grilled+Veggies+Salad%22%2C%22smallImageUrl%22%3A%22%2Fstatic%2F21%2F962_Cottage_Cheese_Salad200x200.jpg%22%2C%22price%22%3A249%2C%22itemType%22%3A%22Veggies%22%2C%22instructions%22%3A%22instructions+Abc%22%2C%22quantity%22%3A1%7D%2C%7B%22itemId%22%3A867%2C%22name%22%3A%22Greek+Salad%22%2C%22smallImageUrl%22%3A%22%2Fstatic%2F21%2F867_Greek_Salad200x200.jpg%22%2C%22price%22%3A219%2C%22itemType%22%3A%22Veggies%22%2C%22instructions%22%3A%22ABC%22%2C%22quantity%22%3A1%7D%5D%2C%22deliveryCharges%22%3A30%2C%22discountAmount%22%3A117%2C%22discountPercentage%22%3A25%2C%22finalOrderAmount%22%3A397%2C%22discountList%22%3A%5B%7B%22id%22%3A4%2C%22name%22%3A%22Corprate+Discount%22%2C%22category%22%3A%22Discount%22%2C%22type%22%3A%22PERCENTAGE%22%2C%22value%22%3A25%7D%5D%2C%22deliveryDateTime%22%3A%2212-7-2015+19%3A45%22%7D%2C%22customer%22%3A%7B%22name%22%3A%22hhhhh+null%22%2C%22phone%22%3A9540095277%2C%22email%22%3A%22rahul%40cookedspecially.com%22%2C%22address%22%3A%22nvdiv+eiv+iwr%22%2C%22deliveryArea%22%3A%22DLF+Phase+3%22%2C%22city%22%3A%22Gurgaon%22%2C%22id%22%3A9970%7D%7D";
        }

        // Call after onPreExecute method
        protected SignupStatus doInBackground(String... urls) {


            HttpURLConnection httpcon;

            try {

                httpcon = (HttpURLConnection) ((new URL("http://api.fundsofhope.org/signup/").openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.connect();

                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();
                Log.i("hel", String.valueOf(httpcon.getErrorStream())+httpcon.getResponseMessage()+httpcon.getResponseCode());


                BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                Gson gson2 = new Gson();
                Log.i("he;;", sb.toString());

                result = gson2.fromJson(sb.toString(), SignupStatus.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Append Server Response To Content String


            /*****************************************************/
            return result;
        }
        protected void onPostExecute(SignupStatus response) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

          Log.i("response", String.valueOf(response)+Error);
            if(response.getStatus().equals("success") || response.getStatus().equals("User updated"))
            {
                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this,response.getStatus(),Toast.LENGTH_LONG).show();
            }



                // Show Response Json On Screen (activity)
                // uiUpdate.setText(Content);

                /****************** Start Parse Response JSON Data *************/

                // String OutputData = ""
//                               	SharedPreferences sp = getApplicationContext()









        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

    }
}
