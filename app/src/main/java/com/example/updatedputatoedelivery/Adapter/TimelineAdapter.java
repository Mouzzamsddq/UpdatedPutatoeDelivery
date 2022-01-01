package com.example.updatedputatoedelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.updatedputatoedelivery.Models.OrderStatus;
import com.example.updatedputatoedelivery.Models.TimeLineModel;
import com.example.updatedputatoedelivery.Models.TimelineAttributes;
import com.example.updatedputatoedelivery.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class TimelineAdapter  extends RecyclerView.Adapter<TimelineAdapter.MyHolder>{


    private Context context;
    private List<TimeLineModel> mFeedList;


    public TimelineAdapter(Context context, List<TimeLineModel> mFeedList) {
        this.context = context;
        this.mFeedList = mFeedList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        TimeLineModel timeLineModel = mFeedList.get(position);


        holder.timelineView.setEndLineColor(ContextCompat.getColor(context,R.color.putatoeGreenColor),1);
        holder.timelineView.setStartLineColor(ContextCompat.getColor(context , R.color.putatoeGreenColor),0);

        if(timeLineModel.getStatus() == OrderStatus.INACTIVE)
        {
              holder.timelineView.setMarker(ContextCompat.getDrawable(context , R.drawable.ic_marker_inactive),ContextCompat.getColor(context,R.color.putatoeGreenColor));

        }
        else if(timeLineModel.getStatus() == OrderStatus.ACTIVE)
        {
            holder.timelineView.setMarker(ContextCompat.getDrawable(context , R.drawable.ic_marker_active),ContextCompat.getColor(context , R.color.putatoeGreenColor));
        }
        else
        {
            holder.timelineView.setMarker(ContextCompat.getDrawable(context , R.drawable.ic_marker),ContextCompat.getColor(context, R.color.putatoeGreenColor));
        }

        holder.dateTextView.setText(timeLineModel.getDate());
        holder.messageTextView.setText(timeLineModel.getMessage());






    }


    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder {

        private TextView dateTextView;
        private TextView messageTextView;
        private TimelineView timelineView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);




            //init all the views
            timelineView = itemView.findViewById(R.id.timeline);
            dateTextView = itemView.findViewById(R.id.text_timeline_date);
            messageTextView = itemView.findViewById(R.id.text_timeline_title);
//            timelineView.setMarkerSize(mAttributes.markerSize);
//            timelineView.setMarkerColor(mAttributes.markerColor);
//            timelineView.setMarkerInCenter(mAttributes.markerInCenter);
//            timelineView.setMarkerPaddingLeft(mAttributes.markerLeftPadding);
//            timelineView.setMarkerPaddingTop(mAttributes.markerTopPadding);
//            timelineView.setMarkerPaddingRight(mAttributes.markerRightPadding);
//            timelineView.setMarkerPaddingBottom(mAttributes.markerBottomPadding);
//            timelineView.setLinePadding(mAttributes.linePadding);
//
//            timelineView.setLineWidth(mAttributes.lineWidth);
//            timelineView.setStartLineColor(mAttributes.startLineColor,);


        }
    }
}
