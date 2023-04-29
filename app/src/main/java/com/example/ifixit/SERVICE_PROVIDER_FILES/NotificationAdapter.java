//package com.example.ifixit.SERVICE_PROVIDER_FILES;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.ifixit.R;
//
//import java.util.ArrayList;
//
//public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
//    Context context;
////    ArrayList<NotificationModel> notificationModel;
//
//        private ArrayList<String>names;
//        private ArrayList<String> addresses;
//
//
//
//    public NotificationAdapter(ArrayList<String> names,ArrayList<String> address){
//        this.names = names;
//        this.addresses = address;
//    }
//
//
//
//    @NonNull
//    @Override
//    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
//        //This is where you inflate the layout (Giving a look to our rows
//        return new MyViewHolder();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
//
//        //Assigning values to the views we created in the recycler_view_row layout files
//        //Based on the position of the recycler view
//        String name = names.get(position);
//        String address = addresses.get(position);
//
////        holder.nameTextView.setText(name);
////        holder.addressTextView.setText(address);
//
////        holder.declineRequest.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////            }
////        });
//
//
//        //holder.ImageView.setImageResource(notificationModel.get(position).getImage());
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        //The recycler view  just wants to know the number of items you want displayed
//        return names.size();
//
//
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//        //Grabbing the views from our recycler_view_row layout file
//        //Similar to onCreate method
//
//        ImageView imageView;
//        TextView customerName;
//        TextView template;
//        Button acceptRequest;
//        Button declineRequest;
//
//
//        public MyViewHolder(@NonNull View itemView){
//            super(itemView);
//
//            imageView = itemView.findViewById(R.id.userProfile);
//            customerName = itemView.findViewById(R.id.notificationName);
//            template = itemView.findViewById(R.id.template);
//            acceptRequest = itemView.findViewById(R.id.notificationAccept);
//            declineRequest = itemView.findViewById(R.id.notificationDecline);
//
//        }
//    }
//}
