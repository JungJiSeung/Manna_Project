package com.example.manna_project.MainAgreementActivity_Util.Invited;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.manna_project.MainAgreementActivity;
import com.example.manna_project.MainAgreementActivity_Util.Promise;
import com.example.manna_project.R;
import com.example.manna_project.ShowDetailSchedule_Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Invited_List {
    ListView listView;
    Context context;
    ArrayList<Promise> arrayList;
    InvitedListAdapter invitedListAdapter;
    DatabaseReference DBRef;
    MainAgreementActivity mainAgreementActivity;

    public final static String TAG = "JS";

    public Invited_List(final Context context, ListView listView) {
        this.context = context;
        this.listView = listView;

        mainAgreementActivity = ((MainAgreementActivity)context);
        DBRef = FirebaseDatabase.getInstance().getReference();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ShowDetailSchedule_Activity.class);

                intent.putExtra("Mode", 1);
                intent.putExtra("MyInfo", mainAgreementActivity.getMyInfo());
                intent.putExtra("Promise_Info", invitedListAdapter.getItem(position));

                ((MainAgreementActivity) context).startActivityForResult(intent,ShowDetailSchedule_Activity.INVITED_PROMISE_CODE);
            }
        });


        arrayList = new ArrayList<>();

        setList(arrayList);


        invitedListAdapter = new InvitedListAdapter(this.getArrayList(), this.context, R.layout.activity_invited_list_item);

        setListAdpater();
        
//        loadDataFromFirebase();
    }

    public void setListItem() {

        Log.d(TAG, "Invited_List: " + mainAgreementActivity.getFirebaseCommunicator().getMyUid());


        arrayList.clear();

        for (Promise promise: mainAgreementActivity.getPromiseArrayList()) {
            Log.d(TAG, "setListItem: " + promise.getAcceptState().get(mainAgreementActivity.getFirebaseCommunicator().getMyUid()));
            if (promise.getAcceptState().get(mainAgreementActivity.getFirebaseCommunicator().getMyUid()) == Promise.INVITED) {
                arrayList.add(promise);
            }
        }

        // 방장인것 우선 정렬 이후 날짜 빠른 순
        Comparator<Promise> comparator = new Comparator<Promise>() {
            @Override
            public int compare(Promise o1, Promise o2) {
                try {
                    if (o1.getStartTime().getTimeInMillis() > o2.getStartTime().getTimeInMillis())
                        return -1;
                    else if(o1.getStartTime().getTimeInMillis() == o2.getStartTime().getTimeInMillis())
                        return 0;
                    else return 1;
                } catch (Exception e) {return 0;}

            }
        };

        if (arrayList!=null)
            Collections.sort(arrayList, comparator);

        getAcceptInvitationListAdapter().notifyDataSetChanged();
    }

    public void setList(ArrayList<Promise> arrayList) {
        this.arrayList = arrayList;
    }

    public void setListAdpater() {
        listView.setAdapter(invitedListAdapter);
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public ArrayList<Promise> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Promise> arrayList) {
        this.arrayList = arrayList;
    }

    public InvitedListAdapter getAcceptInvitationListAdapter() {
        return invitedListAdapter;
    }

    public void setAcceptInvitationListAdapter(InvitedListAdapter invitedListAdapter) {
        this.invitedListAdapter = invitedListAdapter;
    }
}
