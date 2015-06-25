package com.pointrestapp.pointrest.adapters;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pointrestapp.pointrest.Constants;
import com.pointrestapp.pointrest.R;
import com.pointrestapp.pointrest.data.PuntiContentProvider;
import com.pointrestapp.pointrest.data.PuntiDbHelper;
import com.pointrestapp.pointrest.data.PuntiImagesDbHelper;
import com.pointrestapp.pointrest.data.SottocategoriaDbHelper;

public class PreferitiCursorAdapter extends CursorAdapter {

	public PreferitiCursorAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}

	private class ViewHolder{
		public TextView nome_preferito;
		public ImageView foto_preferito;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		
		View view = inflater.inflate(R.layout.element_preferiti_screen, null);
		ViewHolder vholder = new ViewHolder();
		
		vholder.nome_preferito = (TextView) view.findViewById(R.id.name_pi);
		vholder.foto_preferito = (ImageView) view.findViewById(R.id.pi_image);
		
		view.setTag(vholder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int preferitoNameColumnIndex = cursor.getColumnIndex(PuntiDbHelper.NOME);
		int preferitoIdColumnIndex = cursor.getColumnIndex(PuntiDbHelper._ID);
		ViewHolder vholder = (ViewHolder) view.getTag();
		
		vholder.nome_preferito.setText(cursor.getString(preferitoNameColumnIndex));
		
		Cursor c = context.getContentResolver()
				.query(PuntiContentProvider.PUNTI_IMAGES_URI, 
						new String[]{PuntiImagesDbHelper._ID + ""},
						PuntiImagesDbHelper.PUNTO_ID + "=?",
						new String[]{ cursor.getInt(preferitoIdColumnIndex) + "" },
						null);
		
		if(c.moveToFirst()){
			int imgIdOnRemoteDB = c.getInt(0);
			Glide.with(context).load(Constants.BASE_URL + "immagini/" + imgIdOnRemoteDB).placeholder(R.drawable.ic_place_black_36dp).crossFade().into(vholder.foto_preferito);
			
			c.close();
		}
	}
}
