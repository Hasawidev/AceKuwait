package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Category;

import java.util.ArrayList;

public class SubcategoryExpandableAdapter extends ExpandableRecyclerAdapter<SubcategoryExpandableAdapter.CategoryItem> {
    public static final int TYPE_CATEGORY = 1001;
    ArrayList<Category> categoryArrayList = new ArrayList<>();

    public SubcategoryExpandableAdapter(Context context, ArrayList<Category> categories) {
        super(context);
        this.categoryArrayList = categories;
//        setItems(categoryArrayList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.layout_subcategory, parent));
            case TYPE_CATEGORY:
            default:
                return new InnerCategoryViewHolder(inflate(R.layout.layout_subcategory, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_CATEGORY:
            default:
                ((InnerCategoryViewHolder) holder).bind(position);
                break;
        }
    }

    public static class CategoryItem extends ExpandableRecyclerAdapter.ListItem {
        public String Text;

        public CategoryItem(String categoryHeaderName) {
            super(TYPE_HEADER);
            Text = categoryHeaderName;
        }

//        public CategoryItem(String cateogryName) {
//            super(TYPE_CATEGORY);
//            Text = cateogryName;
//        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.imageView20));

            name = (TextView) view.findViewById(R.id.tvSubCategoryName);
        }

        public void bind(int position) {
            super.bind(position);

            name.setText(visibleItems.get(position).Text);
        }
    }

    public class InnerCategoryViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView category;

        public InnerCategoryViewHolder(View view) {
            super(view);

            category = (TextView) view.findViewById(R.id.tvSubCategoryName);
        }

        public void bind(int position) {
            category.setText(category.getText());
        }

    }
}
