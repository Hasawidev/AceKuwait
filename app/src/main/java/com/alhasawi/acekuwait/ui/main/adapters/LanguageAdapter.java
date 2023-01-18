package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.AppLanguage;
import com.alhasawi.acekuwait.databinding.LayoutLanguageAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    ArrayList<AppLanguage> languageArrayList;
    Context context;

    public LanguageAdapter(Context context, ArrayList<AppLanguage> languages) {
        this.context = context;
        this.languageArrayList = languages;
    }

    public static void setsSelected(int sSelected) {
        sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutLanguageAdapterItemBinding languageAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_language_adapter_item, parent, false);
        return new ViewHolder(languageAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppLanguage selectedLanguage = languageArrayList.get(position);
        if (selectedLanguage.isSelected())
            holder.languageAdapterItemBinding.imageView40.setImageDrawable(context.getResources().getDrawable(R.drawable.language_select));
        else
            holder.languageAdapterItemBinding.imageView40.setImageDrawable(context.getResources().getDrawable(R.drawable.language_unselect));
        if (sSelected == position) {
            holder.languageAdapterItemBinding.imageView40.setImageDrawable(context.getResources().getDrawable(R.drawable.language_select));
        } else {
            holder.languageAdapterItemBinding.imageView40.setImageDrawable(context.getResources().getDrawable(R.drawable.language_unselect));
        }
        holder.languageAdapterItemBinding.tvLanguageName.setText(selectedLanguage.getName());
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }


    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return languageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutLanguageAdapterItemBinding languageAdapterItemBinding;

        public ViewHolder(@NonNull LayoutLanguageAdapterItemBinding languageAdapterItemBinding) {
            super(languageAdapterItemBinding.getRoot());
            this.languageAdapterItemBinding = languageAdapterItemBinding;
            languageAdapterItemBinding.constraintLayout29.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sSelected = getAdapterPosition();
            languageArrayList.get(getAdapterPosition()).setSelected(true);
            sClickListener.onItemClickListener(getAdapterPosition(), v);
        }
    }
}
