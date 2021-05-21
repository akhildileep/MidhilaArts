package com.wisewolf.midhilaarts.ui.aboutUs;

import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Model.Transactions;
import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.RetrofitClientInstance;
import com.wisewolf.midhilaarts.TransactionAdapter;
import com.wisewolf.midhilaarts.ui.gallery.TransactionViewModel;

import java.util.List;

import mehdi.sakout.aboutpage.AboutPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUs extends Fragment {
    RecyclerView transactionList;
    int pageLength = 0, pageWidth;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private com.wisewolf.midhilaarts.ui.aboutUs.AboutUsViewModel aboutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutViewModel =
            ViewModelProviders.of(this).get(AboutUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        return new AboutPage(getContext())
            .isRTL(false)
            .setDescription(getString(R.string.app_description))
            .addEmail("midhilaart@gmail.com", "Email")
            .addFacebook("midhilaartlearningapp")
            .addInstagram("midhilaart")
            .addPlayStore("com.wisewolf.midhilaarts")
            .addWebsite("http://midhilaarts.com/")
            .addYoutube("UCtD-VE7pJqy1pgomgRqZuNw")
            .setImage(R.drawable.midhila)
            .create();

    }

}
