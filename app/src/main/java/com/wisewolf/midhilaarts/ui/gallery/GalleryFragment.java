package com.wisewolf.midhilaarts.ui.gallery;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.Gallery;
import com.wisewolf.midhilaarts.GalleryHead;
import com.wisewolf.midhilaarts.GalleryImgAdapter;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {
    RecyclerView headList,imageList;
    int pageLength=0,pageWidth;
    AlertDialog.Builder builder ;
    AlertDialog dialog;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        headList=root.findViewById(R.id.headList);
        imageList=root.findViewById(R.id.imageList);
        try {
            builBuilder();
            callPackage();
        } catch (Exception e) {
            e.printStackTrace();
        }


        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Gallery");
            }
        });
        return root;
    }

    private void builBuilder() {
        builder  = new  AlertDialog.Builder(getContext());
        // create an alert builder
        builder.setCancelable(false);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alert_loading, null);
        builder.setView(customLayout);
        // add a button
        // create and show the alert dialog
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    private void callPackage() {
        dialog.show();
        final RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<List<Gallery>> call = service.getGallery();
        try {


            call.enqueue(new Callback<List<Gallery>>() {
                @Override
                public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {

                    int a =response.code();
                    if (a == 200) {
                        dialog.dismiss();
                        calcHandView();
                        //TODO add data addDb(response.body());
                        GalleryHead galleryHead = new   GalleryHead(response.body(), headList,pageWidth,pageLength ,new GalleryHead.OnItemClickListener() {
                            @Override
                            public void onItemClick(String s, int position) {
                                GalleryImgAdapter galleryImgAdapter=new GalleryImgAdapter(pageWidth,pageLength,response.body().get(position).getUrl(), imageList, new GalleryImgAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String s) {

                                    }
                                });
                                imageList.setAdapter(galleryImgAdapter);
                                LinearLayoutManager galleryImglayoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                galleryImglayoutManager.scrollToPositionWithOffset(2, 20);
                                imageList.setLayoutManager(galleryImglayoutManager);

                            }

                        });

                        headList.setAdapter(galleryHead);
                        LinearLayoutManager galleryHeadlayoutManager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        headList.setLayoutManager(galleryHeadlayoutManager);

                        if (response.body() != null){
                            getImages(response.body());
                        }

                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<List<Gallery>> call, Throwable t) {
                    String a;
                }
            });
            String a;
        } catch (Exception e) {
            String a;
        }

    }

    private void getImages(List<Gallery> body) {

        List<String> gallery =new ArrayList<>();
        for (int i=0;i<body.get(0).getUrl().size();i++){
            gallery.add(body.get(0).getUrl().get(i));
        }

        GalleryImgAdapter galleryImgAdapter=new GalleryImgAdapter(pageWidth,pageLength,gallery, imageList, new GalleryImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String s) {

            }
        });
        imageList.setAdapter(galleryImgAdapter);
        LinearLayoutManager galleryImglayoutManager
            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        imageList.setLayoutManager(galleryImglayoutManager);
    }

    private void calcHandView() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        pageWidth=size.x;
        pageLength=size.y;
    }
}
