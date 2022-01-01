package com.example.updatedputatoedelivery.Fragments;

import android.animation.LayoutTransition;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.updatedputatoedelivery.Adapter.TimelineAdapter;
import com.example.updatedputatoedelivery.Models.OrderStatus;
import com.example.updatedputatoedelivery.Models.TimeLineModel;
import com.example.updatedputatoedelivery.R;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements OnMapReadyCallback {



    //reference variable of toolbar
    private Toolbar toolbar;

    //reference of recycler view
    private RecyclerView trackOrderRecyclerView;
    //reference of adapter
    private TimelineAdapter timelineAdapter;
    //reference of list
    private List<TimeLineModel> mDataList;

    //reference of timeline view
    private TimelineView timelineView;
    private ImageView expandArrowImageView;
    private RelativeLayout trackOrderRelativeLayout1;
    private LinearLayout trackOrderRelativeLayout2;
    private RelativeLayout orderStatusLayout;
    private ImageView shrinkImageView;



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //change title of the action bar when home fragment is visible
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Track Order");


        //init views
        initViews(view);
        setDataListItems();
        timelineAdapter = new TimelineAdapter(getContext() , mDataList);
        trackOrderRecyclerView.setAdapter(timelineAdapter);


        //set animation with layout
        orderStatusLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        TransitionManager.beginDelayedTransition(trackOrderRelativeLayout1,new AutoTransition());
        trackOrderRelativeLayout1.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);





        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        //when click on expand arrow
        expandArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //change visibility of the layout
                trackOrderRelativeLayout1.setVisibility(View.VISIBLE);
                trackOrderRelativeLayout2.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                trackOrderRecyclerView.setLayoutManager(linearLayoutManager);



            }
        });

        //when click on shrink arrow
        shrinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change visibility of the layouts
                trackOrderRelativeLayout1.setVisibility(View.GONE);
                trackOrderRelativeLayout2.setVisibility(View.VISIBLE);
            }
        });










        return view;
    }

    //set data into data list
    private  void setDataListItems() {

        mDataList.add( new TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));
    }

    private  void initViews(View view)
    {
        mDataList = new ArrayList<>();
        trackOrderRecyclerView = view.findViewById(R.id.trackOrderRecyclerView);

        //init time line view
        timelineView = view.findViewById(R.id.timeline);
        timelineView.setStartLineColor(ContextCompat.getColor(getContext(), R.color.putatoeGreenColor),1);
        timelineView.setEndLineColor(ContextCompat.getColor(getContext(),R.color.putatoeGreenColor),0);
        timelineView.setMarker(ContextCompat.getDrawable(getContext(),R.drawable.marker),ContextCompat.getColor(getContext(),R.color.putatoeGreenColor));
        expandArrowImageView = view.findViewById(R.id.expandArrowImageView);
        trackOrderRelativeLayout1 = view.findViewById(R.id.trackOrderRelativeLayout);
        trackOrderRelativeLayout2 = view.findViewById(R.id.trackOrderRelativeLayout2);
        orderStatusLayout = view.findViewById(R.id.orderStatusLayout);
        shrinkImageView = view.findViewById(R.id.shrinkArrowImageView);




    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        MarkerOptions markerOptions = new MarkerOptions().title("origin").position(new LatLng(0,0));
        googleMap.addMarker(markerOptions);



    }
}