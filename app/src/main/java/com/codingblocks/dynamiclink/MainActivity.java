package com.codingblocks.dynamiclink;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.appinvite.FirebaseAppInvite;
    public class MainActivity extends AppCompatActivity implements
            GoogleApiClient.OnConnectionFailedListener,
            View.OnClickListener {

        private static final String TAG = MainActivity.class.getSimpleName();
        private static final int REQUEST_INVITE = 0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Button button =findViewById(R.id.inviteButton);
            button.setOnClickListener(this);


            createdynamiclink();
            FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData data) {
                            if (data == null) {
                                Log.d(TAG, "getInvitation: no data");
                                return;
                            }
                            Uri deepLink = data.getLink();
                            FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                            if (invite != null) {
                                String invitationId = invite.getInvitationId();
                            }

                            Log.d(TAG, "deepLink:" + deepLink);
                            if (deepLink != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setPackage(getPackageName());
                                intent.setData(deepLink);

                                startActivity(intent);
                            }
                            // [END_EXCLUDE]
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "getDynamicLink:onFailure", e);
                        }
                    });
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
            showMessage(getString(R.string.google_play_services_error));
        }

        private void onInviteClicked() {
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build();
            startActivityForResult(intent, REQUEST_INVITE);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

            if (requestCode == REQUEST_INVITE) {
                if (resultCode == RESULT_OK) {

                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                    for (String id : ids) {
                        Log.d(TAG, "onActivityResult: sent invitation " + id);
                    }
                } else {
                    showMessage(getString(R.string.send_failed));

                }
            }
        }
        void createdynamiclink() {

            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://www.google.com/"))
                    .setDomainUriPrefix("https://manisha.page.link")
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                    .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                    .buildDynamicLink();

            Uri dynamicLinkUri = dynamicLink.getUri();

        }

        private void showMessage(String msg) {
            Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.inviteButton) {
                onInviteClicked();
            }
        }
    }
