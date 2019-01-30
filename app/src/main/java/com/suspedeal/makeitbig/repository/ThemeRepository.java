package com.suspedeal.makeitbig.repository;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.suspedeal.makeitbig.addTheme.OnAddUrlToObjectListener;
import com.suspedeal.makeitbig.addTheme.OnSetDownloadUrlCallback;
import com.suspedeal.makeitbig.addTheme.OnThemeAddedToDBCallback;
import com.suspedeal.makeitbig.model.BigText;

public class ThemeRepository implements IThemeRepository {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference themesRef = mDatabase.getReference("themes");
    private StorageReference storage = FirebaseStorage.getInstance().getReference();

    private String lastUid;

    @Override
    public void addThemeToDB(final OnThemeAddedToDBCallback listener, BigText bigText) {
        //after adding new theme to DB, copy the uid and create in storage a png file with the same name (ex: -LXG1kfeGs4ANKqYzRBW.png)
        final String uid = themesRef.push().getKey();
        bigText.setUid(uid);
        themesRef.child(uid).setValue(bigText, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    listener.onFailed(databaseError.getMessage());
                } else {
                    lastUid = uid;
                    listener.onSuccess(uid);
                }
            }
        });
    }

    @Override
    public void getDownloadUrl(final OnAddUrlToObjectListener listener) {

            //no new theme added because we reset the lastUid when we set the donwloadUrl on the object
            if(lastUid.equals("")){
                listener.onFailed("Url already added to object");
                return;
            }

            //search for background in storage with same "lastUid" when added theme
            storage.child(lastUid + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    setDownloadUrl(new OnSetDownloadUrlCallback() {
                        @Override
                        public void onSuccess() {
                            listener.onSuccess();
                        }

                        @Override
                        public void onFailed(String error) {
                            listener.onFailed(error);
                        }
                    }, uri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    listener.onFailed(exception.getMessage());
                }
            });
    }

    private void setDownloadUrl(final OnSetDownloadUrlCallback listener, final String uri) {

        Query query = themesRef.orderByChild("uid").equalTo(lastUid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot themeSnapshot : dataSnapshot.getChildren()) {
                    BigText theme = themeSnapshot.getValue(BigText.class);
                    if (theme != null) {
                        themesRef.child(lastUid).child("backgroundUrl").setValue(uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                lastUid = "";
                                listener.onSuccess();
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError.getMessage());
            }
        });

        //reset lastUid to avoid pressing setDownloadUrl again after imageUrl has already been set.

    }

}
