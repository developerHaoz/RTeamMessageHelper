package com.developerhaoz.rteammessagehelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通讯录的 Adapter
 *
 * @author Haoz
 * @date 2017/11/11.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<ContactBean> mContactBeanList;
    public static Map<Integer, Boolean> mCheckMap = new HashMap<>();
    private int mType;
    public static final int TYPE_SENDMESSAGE = 0;
    public static final int TYPE_CONTACTS = 1;
    private OnContactClickListener mListener;

    public ContactsAdapter(List<ContactBean> contactBeanList, int type) {
        this.mContactBeanList = contactBeanList;
        this.mType = type;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.tvName.setText(mContactBeanList.get(position).getName());
        final LinearLayout linearLayout = holder.mLinearLayout;
        final CheckBox checkBox = holder.checked;
        if(TYPE_SENDMESSAGE == mType){
            sendMessageType(checkBox, position, linearLayout);
        }

        if(TYPE_CONTACTS == mType){
            checkBox.setVisibility(View.GONE);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick();
                }
            });
        }

    }

    private void sendMessageType(final CheckBox checkBox, final int position, LinearLayout linearLayout) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckMap.put(position, true);
                }else{
                    mCheckMap.remove(position);
                }
            }
        });

        checkBox.setChecked(mCheckMap.containsKey(position));

        if(MainActivity.isAll){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (GsonUtil.isEmpty(mContactBeanList)) {
            return 0;
        }
        return mContactBeanList.size();
    }

    public interface OnContactClickListener{
        void onClick();
    }

    public void setOnClickListener(OnContactClickListener listener){
        this.mListener = listener;
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        CheckBox checked;
        LinearLayout mLinearLayout;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.contact_ll);
            tvName = (TextView) itemView.findViewById(R.id.contact_tv_name);
            checked = (CheckBox) itemView.findViewById(R.id.contact_cb_check);
        }
    }
}
