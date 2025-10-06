package com.example.tripapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.tripapp.Adapter.PopularAdapter;
import com.example.tripapp.R;
import com.example.tripapp.Bean.ApiResponse;
import com.example.tripapp.Bean.PopularItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PopularAdapter adapter;
    private final List<PopularItem> popularList = new ArrayList<>();

    private OkHttpClient client = new OkHttpClient();
    private Call currentCall;

    // 你的接口（换成真实地址）
    private final String url = "http://8.138.243.249:8080/spots";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycleview); // 注意你的布局里 id 是 recycleview
        progressBar = root.findViewById(R.id.ProgressBar);

        // 瀑布流，2列，竖向
        StaggeredGridLayoutManager lm =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);

        adapter = new PopularAdapter(requireContext(), popularList);
        recyclerView.setAdapter(adapter);

        fetchPopularData();

        return root;
    }

    private void fetchPopularData() {
        progressBar.setVisibility(View.VISIBLE);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        currentCall = client.newCall(request);
        currentCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "网络请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "";
                Log.d("HomeFragment", "response body = " + body);

                // 用 Gson 解析外层 ApiResponse<List<PopularItem>>
                try {
                    Gson gson = new Gson();
                    Type apiType = new TypeToken<ApiResponse<List<PopularItem>>>() {}.getType();
                    ApiResponse<List<PopularItem>> apiResp = gson.fromJson(body, apiType);

                    final List<PopularItem> data = (apiResp != null && apiResp.data != null)
                            ? apiResp.data : new ArrayList<>();

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            popularList.clear();
                            popularList.addAll(data);
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                            if (popularList.isEmpty()) {
                                Toast.makeText(getContext(), "没有热门数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "解析失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消正在进行的网络请求，防止回调访问已经销毁的 UI
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
    }
}

