package com.ilyes.myapplication.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ilyes.myapplication.R;
import com.ilyes.myapplication.databinding.CityWeatherRowBinding;
import com.ilyes.myapplication.model.CityWeather;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class CitiesWeatherListAdapter extends RecyclerView.Adapter<CitiesWeatherListAdapter.MyViewHolder> {
    private final Context context;
    private List<CityWeather> cityWeatherList;


    public CitiesWeatherListAdapter(Context context) {
        this.context = context;
        this.cityWeatherList = new ArrayList<>();
    }

    public void setCityWeatherList(List<CityWeather> cityWeatherList) {
        this.cityWeatherList = cityWeatherList;
    }

    @NonNull
    @Override
    public CitiesWeatherListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CityWeatherRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.city_weather_row, parent, false
        );
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesWeatherListAdapter.MyViewHolder holder, int position) {
        CityWeather cityWeather = this.cityWeatherList.get(position);
        holder.binding.city.setText(cityWeather.name);

        holder.binding.temperature.setText(String.format("%d°C", Double.valueOf(cityWeather.main.temp).intValue()));
        if (cityWeather.weather.size() > 0) {
            CityWeather.Weather weather = cityWeather.weather.get(0);
            holder.binding.description.setText(StringUtils.capitalize(weather.description));
            Glide.with(context)
                    .load(MessageFormat.format(context.getString(R.string.iconpattern),weather.icon))
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .error(R.drawable.ic_launcher_background)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.binding.picto);
        }
    }

    @Override
    public int getItemCount() {
        return this.cityWeatherList != null ? this.cityWeatherList.size() : 0;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CityWeatherRowBinding binding;

        public MyViewHolder(CityWeatherRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }
}
