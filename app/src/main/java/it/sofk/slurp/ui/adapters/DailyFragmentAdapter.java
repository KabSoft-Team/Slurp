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
    private FoodInstance[] foodInstances = new FoodInstance[0];

    @NonNull
    @Override
    public DailyFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DailyItemBinding binding = DailyItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodInstance foodInstance = foodInstances[position];
        holder.binding.dailyFoodname.setText(foodInstance.getFoodType().toUpperCase());
        holder.binding.ratingBar.setRating((float) foodInstance.getPortionConsumed());

        holder.binding.dailyPlusbutton.setOnClickListener((View) -> {
            if (foodInstance.getPortionConsumed() == 5.0) return;

            foodInstance.setPortionConsumed(foodInstance.getPortionConsumed() + 0.5);
            holder.binding.ratingBar.setRating((float)foodInstance.getPortionConsumed());
            if (clickListener != null) clickListener.onPlusClick(foodInstance);
        });

        holder.binding.dailyMinusbutton.setOnClickListener((View) -> {
            if (foodInstance.getPortionConsumed() == 0.0) return;

            foodInstance.setPortionConsumed(foodInstance.getPortionConsumed() - 0.5);
            holder.binding.ratingBar.setRating((float)foodInstance.getPortionConsumed());
            if (clickListener != null) clickListener.onMinusClick(foodInstance);
        });
    }

    @Override
    public int getItemCount() {
        return foodInstances.length;
    }

    public void submitData(List<FoodInstance> data) {
        if (foodInstances.length == 0) {
            foodInstances = data.toArray(foodInstances);
            this.notifyDataSetChanged();
        }
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
