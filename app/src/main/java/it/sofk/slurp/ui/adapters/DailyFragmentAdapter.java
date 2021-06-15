package it.sofk.slurp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.sofk.slurp.database.entity.FoodInstance;
import it.sofk.slurp.databinding.DailyItemBinding;

public class DailyFragmentAdapter extends RecyclerView.Adapter<DailyFragmentAdapter.ViewHolder> {

    private ClickListener clickListener;

    private final AsyncListDiffer<FoodInstance> listDiffer = new AsyncListDiffer(this, new DiffUtil.ItemCallback<FoodInstance>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodInstance oldItem, @NonNull FoodInstance newItem) {
            return oldItem.getDate().equals(newItem.getDate()) && oldItem.getFoodType().equals(newItem.getFoodType());
        }
        @Override
        public boolean areContentsTheSame(@NonNull FoodInstance oldItem, @NonNull FoodInstance newItem) {
            boolean match = oldItem.getFoodType().equals(newItem.getFoodType())
                    && oldItem.getDate().equals(newItem.getDate())
                    && oldItem.getPortionConsumed() == newItem.getPortionConsumed();
            return match;
        }
    });

    @NonNull
    @Override
    public DailyFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DailyItemBinding binding = DailyItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodInstance instance = listDiffer.getCurrentList().get(position);
        holder.binding.dailyFoodname.setText(instance.getFoodType().toUpperCase());
        holder.binding.ratingBar.setRating((float) instance.getPortionConsumed());


        holder.binding.dailyPlusbutton.setOnClickListener((View) -> {
            if (instance.getPortionConsumed() == 5.0) return;

            instance.setPortionConsumed(instance.getPortionConsumed() + 0.5);
            if (clickListener != null) clickListener.onPlusClick(instance);
        });

        holder.binding.dailyMinusbutton.setOnClickListener((View) -> {
            if (instance.getPortionConsumed() == 0) return;

            instance.setPortionConsumed(instance.getPortionConsumed() - 0.5);
            if (clickListener != null) clickListener.onMinusClick(instance);
        });
    }

    @Override
    public int getItemCount() {
        return listDiffer.getCurrentList().size();
    }

    public void submitData(List<FoodInstance> data) {
        listDiffer.submitList(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final DailyItemBinding binding;

        public ViewHolder(@NonNull DailyItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onPlusClick(FoodInstance foodInstance);
        void onMinusClick(FoodInstance foodInstance);
    }
}
