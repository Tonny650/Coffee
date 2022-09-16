package com.thisname.coffee.activitys.providers;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thisname.coffee.activitys.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserProvider {
    private CollectionReference mCollectionReference;

    public UserProvider() {
        mCollectionReference = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot>getUser(String id){
        return mCollectionReference.document(id).get();
    }

    public Task<Void> create(User user){
        return mCollectionReference.document(user.getId()).set(user);

    }

    public Task<Void> update(User user){
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        return mCollectionReference.document(user.getId()).update(map);
    }


}
