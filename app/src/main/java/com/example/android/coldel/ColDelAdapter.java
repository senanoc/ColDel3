package com.example.android.coldel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ColDelAdapter extends RecyclerView.Adapter<ColDelAdapter.ColDelViewHolder> {

    private List<DocumentSnapshot> mColDelSnapshots = new ArrayList<>();

    public ColDelAdapter () {

        CollectionReference colDelRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH);

        colDelRef.orderBy(Constants.KEY_RID, Query.Direction.DESCENDING).limit(50).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "Listening Failed");
                    return;
                }
                mColDelSnapshots = documentSnapshots.getDocuments();
                notifyDataSetChanged();
            }
        });

    }

    @NonNull
    @Override
    public ColDelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coldel_itemview, parent,false);
        return new ColDelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColDelViewHolder colDelViewHolder, int i) {
        DocumentSnapshot ds = mColDelSnapshots.get(i);
        String name = (String) ds.get(Constants.KEY_NAME);
        String addr = (String) ds.get(Constants.KEY_ADDR);
        String rid = (String) ds.get(Constants.KEY_RID);
        String order = (String) ds.get(Constants.KEY_ORDER);
        colDelViewHolder.mNameTextView.setText(name);
        colDelViewHolder.mAddrTextView.setText(addr);
        colDelViewHolder.mRIDTextView.setText(rid);
        colDelViewHolder.mOrderTextView.setText(order);

    }

    @Override
    public int getItemCount() {
        return mColDelSnapshots.size();
    }


    class ColDelViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mAddrTextView;
        private TextView mRIDTextView;
        private TextView mOrderTextView;

        public ColDelViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.itemview_name);
            mAddrTextView = itemView.findViewById(R.id.itemview_addr);
            mRIDTextView = itemView.findViewById(R.id.itemview_RID);
            mOrderTextView = itemView.findViewById(R.id.itemview_order);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot ds = mColDelSnapshots.get(getAdapterPosition());
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ColDelDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_DOC_ID,ds.getId());
                    context.startActivity(intent);
                }
            });

        }


    }

}
