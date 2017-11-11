package com.developerhaoz.rteammessagehelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

/**
 * 通讯录的 Adapter
 *
 * @author Haoz
 * @date 2017/11/11.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>{

    private List<ContactBean> mContactBeanList;
    private CheckListener mListener;

    public ContactsAdapter(List<ContactBean> contactBeanList){
        this.mContactBeanList = contactBeanList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.tvName.setText(mContactBeanList.get(position).getName());
        final CheckBox checkBox = holder.checked;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.check(position, checkBox.isChecked());
            }
        });

    }

    public void setCheckListener(CheckListener listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
//        if(isEmpty(mContactBeanList)){
//            return 0;
//        }
        return mContactBeanList.size();
    }

    private boolean isEmpty(Collection collection){
        if(collection == null || collection.size() == 0){
            return true;
        }
        return false;
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        CheckBox checked;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.contact_tv_name);
            checked = (CheckBox) itemView.findViewById(R.id.contact_cb_check);
        }
    }

    interface CheckListener{
        void check(int position, boolean isCheck);
    }
}
