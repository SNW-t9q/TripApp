package com.example.tripapp.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tripapp.Adapter.PopularAdapter;
import com.example.tripapp.Bean.ApiResponse;
import com.example.tripapp.Bean.PopularItem;
import com.example.tripapp.R;
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

public class MineFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PopularAdapter adapter;
    private ImageView ivHead;
    private final List<PopularItem> popularList = new ArrayList<>();
    private int isMine = 1;

    private OkHttpClient client = new OkHttpClient();
    private Call currentCall;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private final String url = "http://8.138.243.249:8080/spots";
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;


    public MineFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);

        initView(root);
        setLayoutManager();

        fetchPopularData();
        initEvent();

        return root;
    }

    private void initView(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progressBar);
        ivHead = root.findViewById(R.id.iv_head);
    }

    private void setLayoutManager() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 设置边距，让左右卡片部分可见
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(150, 0, 150, 0);

        // 添加 PagerSnapHelper 让滑动停在卡片中心
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // 添加滚动监听，实现缩放 + 层级叠加效果
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int centerX = recyclerView.getWidth() / 2;
                // 遍历可见的 item
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View child = recyclerView.getChildAt(i);
                    // 计算每个 item 的中心位置
                    float childCenterX = (child.getLeft() + child.getRight()) / 2f;
                    // 距离中心的偏移量
                    float distance = Math.abs(centerX - childCenterX);
                    float scale = 1 - (distance / recyclerView.getWidth()) * 0.3f;
                    // 缩放比例：距离中心越远越小
                    child.setScaleY(scale);
                    child.setScaleX(scale);
                    // 调整层级（越接近中心越上层）
                    child.setTranslationZ(scale * 10);
                }
            }
        });


        adapter = new PopularAdapter(requireContext(), popularList,isMine);
        recyclerView.setAdapter(adapter);
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
                        progressBar.setVisibility(View.VISIBLE);
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


    private void initEvent() {
        requestMyPermission();
    }

    private void requestMyPermission() {
        // 注册权限申请
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(getContext(), "需要相册权限才能选择头像", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 注册选择图片
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            // 使用 Glide 加载并裁剪成圆形
                            Glide.with(MineFragment.this)
                                    .load(uri)
                                    .circleCrop() // 裁剪成圆形
                                    .into(ivHead);
                        }
                    }
                }
        );

        // 点击头像时触发
        ivHead.setOnClickListener(v -> checkPermissionAndOpenGallery());

    }

    /**
     * 检查权限并打开相册
     */
    private void checkPermissionAndOpenGallery() {
        // Android 13+ 使用 READ_MEDIA_IMAGES
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            // Android 12 及以下使用 READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    /**
     * 打开系统相册
     */
    private void openGallery() {
        pickImageLauncher.launch("image/*");
    }
}