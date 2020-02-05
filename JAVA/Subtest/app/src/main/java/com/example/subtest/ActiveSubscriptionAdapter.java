package com.example.subtest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActiveSubscriptionAdapter extends RecyclerView.Adapter<ActiveSubscriptionAdapter.ViewHolder> {
    private ArrayList<SubscriptionElements> mList;
    private Context mContext;

    public ActiveSubscriptionAdapter(ArrayList<SubscriptionElements> list,Context context) {
        mList = list;
        mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_active_subscription_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("Test","Bind view holder called");
        holder.mSubsMerchantName.setText(mList.get(position).getMerchant());
        String idText = mContext.getResources().getString(R.string.id,mList.get(position).getSubID());
        holder.mSubsID.setText(idText);
        if(!TextUtils.isEmpty(mList.get(position).getNextPaymentAmount())){
            if(!TextUtils.isEmpty(mList.get(position).getNextPaymentDate())){
                String paymentDetailText = mContext.getResources().getString(R.string.subscription_payment) + " " + mContext.getResources().getString(R.string.rupees) + mList.get(position).getNextPaymentAmount();
                String date =mContext.getResources().getString(R.string.subscription_date) + " " + mList.get(position).getNextPaymentDate();
                String statusText = paymentDetailText + " " + date;
                holder.mNextDueDate.setText(statusText);
            } else {
                String paymentDetailText = mContext.getResources().getString(R.string.subscription_payment) + " " + mContext.getResources().getString(R.string.rupees) + mList.get(position).getNextPaymentAmount();
                holder.mNextDueDate.setText(paymentDetailText);
            }
        } else {
            holder.mNextDueDate.setVisibility(View.GONE);
        }
        Picasso.get().load(mList.get(position).getImageResource()).into(holder.mMerchantLogo);

    }

    @Override
    public int getItemCount() {
        return (mList.isEmpty() || mList==null ) ? 0 : mList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mSubsMerchantName,mSubsID,mNextDueDate;
        private ImageView mMerchantLogo;

        public ViewHolder(final View view) {
            super(view);
            mSubsMerchantName =  view.findViewById(R.id.tv_merchant_title);
            mSubsID =  view.findViewById(R.id.tv_merchant_subscription_id);
            mNextDueDate =  view.findViewById(R.id.tv_merchant_payment_status);
            mMerchantLogo =  view.findViewById(R.id.iv_merchant_logo);
        }
    }

}