package com.example.popie.uoleventsdiary_admin.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popie.uoleventsdiary_admin.Activities.UpdateActivity;
import com.example.popie.uoleventsdiary_admin.Client.UserClient;
import com.example.popie.uoleventsdiary_admin.ManageEventsActivity;
import com.example.popie.uoleventsdiary_admin.Models.Event;
import com.example.popie.uoleventsdiary_admin.R;
import com.example.popie.uoleventsdiary_admin.Retrofit.Remote;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by popie on 11/3/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ContactViewHolder> {


    private Retrofit retrofit;
    private UserClient userClient;
    private Context context;
    private List<Event> events;


    public RecyclerViewAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, null);

        retrofit = Remote.getRetrofit();
        userClient = retrofit.create(UserClient.class);

        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ContactViewHolder holder, final int position) {


        final Event event = events.get(position);

        Picasso.with(context)
                .load(R.drawable.picture)
                .into(holder.circleImageView);

        holder.eventName.setText(event.getName());


        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Id: " + event.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, UpdateActivity.class);
                Gson gson = new Gson();
                String eventData = gson.toJson(event);
                intent.putExtra("eventData", eventData);
                context.startActivity(intent);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Id: " + event.getId(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true).setTitle("Alert").setMessage("Are you sure you want to delete this event?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Call<Void> call = userClient.deleteEvent(event.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    onRemove(position);
                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Response Failure", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Try again when network is strong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    void onRemove(int position) {
        events.remove(position);
        notifyDataSetChanged();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView eventName;
        Button buttonDelete;
        View v;

        public ContactViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circleImageView);
            eventName = itemView.findViewById(R.id.eventName);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            v = itemView.findViewById(R.id.cardView);


        }
    }
}
