package com.wisewolf.midhilaarts.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.wisewolf.midhilaarts.Model.Faculity;
import com.wisewolf.midhilaarts.Model.Pack;
import com.wisewolf.midhilaarts.Model.PriceItem;
import com.wisewolf.midhilaarts.Model.Tools;

import java.util.ArrayList;
import java.util.List;

public class OfflineDatabase extends SQLiteOpenHelper {
    SQLiteDatabase dh;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Midhila_DB";

    public OfflineDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String topbanner = "CREATE TABLE topbanner(id varchar PRIMARY KEY ,image varchar     )";
        sqLiteDatabase.execSQL(topbanner);

        String bottombanner = "CREATE TABLE bottombanner(id varchar PRIMARY KEY ,image varchar     )";
        sqLiteDatabase.execSQL(bottombanner);



        String benifit = "CREATE TABLE benifit(id varchar PRIMARY KEY ,benifit varchar,pack varchar     )";
        sqLiteDatabase.execSQL(benifit);

        String Pack = "CREATE TABLE Pack(id varchar PRIMARY KEY , thumbnail  varchar         ,ratedBy  varchar         ,course_name  varchar         ,age_group  varchar         ,rating  varchar         ,benifit  varchar         ,faculity  varchar         ,vimeo_id  varchar         ,total_no_VIdeos  varchar         ,tools  varchar         ,used_by  varchar         ,price  varchar         ,id  varchar         ,course_description  varchar   )";
        sqLiteDatabase.execSQL(Pack);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

/*    public String insertinto_benifit(String benifit,String pack)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("[benifit]", benifit);
            values.put("[pack]", pack);

            dh.insert("benifit", null, values);
            dh.close();
            return "y";

        } catch (Exception e) {
            return e.toString();
        }


    }

    public String insertinto_topbanner(String image)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("[image]", image);

            dh.insert("topbanner", null, values);
            dh.close();
            return "y";

        } catch (Exception e) {
            return e.toString();
        }


    }

    public String insertinto_bottombanner(String image)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("[image]", image);

            dh.insert("bottombanner", null, values);
            dh.close();
            return "y";

        } catch (Exception e) {
            return e.toString();
        }


    }
    public String insertinto_Pack(          String thumbnail          ,String ratedBy          ,String course_name          ,String age_group          ,String rating          ,String benifit          ,String faculity          ,String vimeo_id          ,String total_no_VIdeos          ,String tools          ,String used_by          ,String price          ,String id          ,String course_description)
    {
        try {
            dh = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("[thumbnail]", thumbnail );
            values.put("[ratedBy]", ratedBy );
            values.put("[course_name]", course_name );
            values.put("[age_group]", age_group );
            values.put("[rating]", rating );
            values.put("[benifit]", benifit );
            values.put("[faculity]", faculity );
            values.put("[vimeo_id]", vimeo_id );
            values.put("[total_no_VIdeos]", total_no_VIdeos );
            values.put("[tools]", tools );
            values.put("[used_by]", used_by );
            values.put("[price]", price );
            values.put("[id]", id );
            values.put("[course_description]", course_description );

            dh.insert("Pack", null, values);
            dh.close();
            return "y";

        } catch (Exception e) {
            return e.toString();
        }


    }

    public List<String> topbanner() {
        dh = this.getReadableDatabase();
        String select = "select * from topbanner";
        Cursor cursor = dh.rawQuery(select, null);
        List<String> link = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String image;
                image=cursor.getString(cursor.getColumnIndex("image"));

                link.add(image);



            }
            while (cursor.moveToNext());

        }

        dh.close();
        return link;

    }

    public List<String> bottombanner() {
        dh = this.getReadableDatabase();
        String select = "select * from bottombanner";
        Cursor cursor = dh.rawQuery(select, null);
        List<String> link = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String image;
                image=cursor.getString(cursor.getColumnIndex("image"));

                link.add(image);



            }
            while (cursor.moveToNext());

        }

        dh.close();
        return link;

    }

    *//*public List<Pack> Pack() {
        dh = this.getReadableDatabase();

        String select = "select * from Pack ";
        Cursor cursor = dh.rawQuery(select, null);
        List<Pack> listPack=new ArrayList<>();

        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                String thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"));
                String ratedBy = cursor.getString(cursor.getColumnIndex("ratedBy"));
                String course_name = cursor.getString(cursor.getColumnIndex("course_name"));
                String age_group = cursor.getString(cursor.getColumnIndex("age_group"));
                String rating = cursor.getString(cursor.getColumnIndex("rating"));
                String benifit = cursor.getString(cursor.getColumnIndex("benifit"));
                String faculity = cursor.getString(cursor.getColumnIndex("faculity"));
                String vimeo_id = cursor.getString(cursor.getColumnIndex("vimeo_id"));
                String total_no_VIdeos = cursor.getString(cursor.getColumnIndex("total_no_VIdeos"));
                String tools = cursor.getString(cursor.getColumnIndex("tools"));
                String used_by = cursor.getString(cursor.getColumnIndex("used_by"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String course_description = cursor.getString(cursor.getColumnIndex("course_description"));
                Pack pack = new Pack(thumbnail
                    , ratedBy
                    , course_name
                    , age_group
                    , rating
                    , benifit
                    , faculity
                    , vimeo_id
                    , total_no_VIdeos
                    , tools
                    , used_by
                    , price
                    , id
                    , course_description);
                listPack.add(pack);


            }
            while (cursor.moveToNext());

        }

        dh.close();
        return listPack;

    }*//*

   public void deletetable(String name) {
        dh = this.getWritableDatabase();

        String query;
        query = "delete from "+name;
        Log.e("Query", query);
        dh.execSQL(query);



    }*/

}
