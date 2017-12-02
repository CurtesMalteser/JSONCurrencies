package com.curtesmalteser.jsoncurrencies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

/**
 * Created by anton on 29/11/2017.
 */

/*public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.CurrenciesAdapterViewHolder>{

    private CurrenciesModel mCm;

    public CurrenciesAdapter() {

    }

    public class CurrenciesAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mCurrencyTV;

        public CurrenciesAdapterViewHolder (View view) {
            super(view);
            mCurrencyTV = (TextView) view.findViewById(R.id.tv_currency);
        }

    }

    @Override
    public CurrenciesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_single_currency;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new CurrenciesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrenciesAdapterViewHolder holder, int position) {
        //String currencyForSelectDay = mCm.[position];
    }
}*/
