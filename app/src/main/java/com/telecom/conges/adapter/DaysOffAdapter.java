package com.telecom.conges.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.telecom.conges.R;
import com.telecom.conges.data.models.DaysOff;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DaysOffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DaysOff> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public DaysOffAdapter(Context context, List<DaysOff> items) {
        this.items = items;
        ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_days_off, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    private String formattedDate(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z"));
        return DateTimeFormat.forPattern("E dd MMM").withLocale(Locale.FRENCH).print(localDate);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final DaysOff daysOff = items.get(position);
            view.title.setText(daysOff.getName());
            view.date.setText(formattedDate(daysOff.getDateStart()) + " - " + formattedDate(daysOff.getDateEnd()));
//            Tools.displayImageOriginal(ctx, view.image, daysOff.image);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, daysOff, position);
                }
            });
        }
    }

    // Replace the contents of a view (invoked by the layout manager)

    private void onMoreButtonClick(final View view, final DaysOff p, int position) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item, position);
                return true;
            }
        });
        popupMenu.inflate(R.menu.ferie_menu);
        popupMenu.show();
    }

    public void addData(List<DaysOff> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void updateAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, DaysOff obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, DaysOff obj, MenuItem item, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView date;
        public ImageButton more;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
            more = (ImageButton) v.findViewById(R.id.more);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

}