package com.curtesmalteser.jsoncurrencies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;
import com.curtesmalteser.jsoncurrencies.model.FlagModel;
import com.curtesmalteser.jsoncurrencies.utilities.FlagUtils;

import java.util.ArrayList;

/**
 * Created by António "Curtes Malteser" Bastião on 29/11/2017.
 */


// COMPLETED - Create the adapter CurrenciesAdapter that extends RecyclerView.Adapter<CurrenciesAdapter.CurrenciesViewHolder>
public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.CurrenciesViewHolder>{

    private static final String TAG = CurrenciesAdapter.class.getSimpleName();

    // COMPLETED - add and an ArrayList<CurrenciesModel> that will hold the data to poppulate the viewHolder
    private ArrayList<CurrenciesModel> mCurrenciesArrayList;

    final private ListItemClickListener mOnClickListener;

    // COMPLETED (1) Add an interface called ListItemClickListener
    // COMPLETED (2) Within that interface, define a void method called onListItemClick that takes the model
    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(CurrenciesModel currenciesModel);
    }

    // COMPLETED - create a constructor for Currencies adapter
    public CurrenciesAdapter(ArrayList<CurrenciesModel> currenciesModelArrayList,
                             ListItemClickListener listener) {
        this.mCurrenciesArrayList = currenciesModelArrayList;
        this.mOnClickListener = listener;
    }

    // COMPLETED - override onCreateViewHolder and return a viewHolder object
    @Override
    public CurrenciesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // COMPLETED - inflate the view using the LayoutInflater
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_single_currency;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatly = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediatly);
        CurrenciesViewHolder viewHolder = new CurrenciesViewHolder(view);

        return viewHolder;
    }

    // COMPLETED - override the method onBindViewHolder and call the method bind and pass the position
    @Override
    public void onBindViewHolder(CurrenciesViewHolder holder, int position) {

        Log.d(TAG, "Position: #" + position);
        holder.bind(position);

    }

    // COMPLETED - override the method getItemCount and return the number of items (
    @Override
    public int getItemCount() {
        return mCurrenciesArrayList.size();
    }

    // COMPLETED - Create the class CurrenciesViewHolder that extends RecyclerView.ViewHolder
    public class CurrenciesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // COMPLETED - Add the views member varibles
        ImageView mImgFlag;
        ImageView mImgSign;
        TextView mTvCoin;
        TextView mTvCurrency;

        // COMPLETED - Create a constructor for CurrenciesViewHolder
        public CurrenciesViewHolder(View itemView) {
            super(itemView);

            // COMPLETED - Set the views using the itemView.findViewById()
            mImgFlag = itemView.findViewById(R.id.image_flag);
            mImgSign = itemView.findViewById(R.id.image_sign);
            mTvCoin = itemView.findViewById(R.id.tv_coin);
            mTvCurrency = itemView.findViewById(R.id.tv_currency);

            itemView.setOnClickListener(this);
        }

        // COMPLETED - Add the method bind that accepts an Integer as parameter
        void bind(int listIndex) {
            
            CurrenciesModel model = mCurrenciesArrayList.get(listIndex);

            FlagModel flagModel = FlagUtils.selectFlag(model.getCurrency());

            mImgFlag.setImageResource(flagModel.getFlag());
            mImgSign.setImageResource(flagModel.getSign());
            mTvCoin.setText(model.getCurrency());
            mTvCurrency.setText(String.valueOf(model.getRate()));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            // Get the object From the ArrayList based on the clickedPosition
            CurrenciesModel currenciesModel = mCurrenciesArrayList.get(clickedPosition);
            mOnClickListener.onListItemClick(currenciesModel);

        }
    }
}
