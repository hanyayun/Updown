package com.cnlive.updownfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnlive.updownfile.adapter.ShowDiretoryAdapter;
import com.cnlive.updownfile.bean.DirectoryBean;
import com.cnlive.updownfile.bean.FileBean;
import com.cnlive.updownfile.view.RefreshHeaderView;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.xiaoray.raysdk.RayLib;
import com.xiaoray.raysdk.constant.FileType;
import com.xiaoray.raysdk.utils.OkHttpUtils.HttpResult;
import com.xiaoray.raysdk.view.RayToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiretoryActivity extends AppCompatActivity {

    RecyclerView rcyDataShow;
    EditText editNewFolder;
    Button btnCreateNewFolder;
    TwinklingRefreshLayout refreshDataShow;
    private ShowDiretoryAdapter showFilesAdapter;
    private int page = 1;
    private boolean isLoadingFinished = true;
    private String folderPath = "/";
    private final int REQUEST_WRITE_CONTENT = 2;
    private final int QUERY_METHODE_ALLFILES = 0;
    private final int QUERY_METHODE_ONLY_DIRETORY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diretory);
        folderPath = getIntent().getStringExtra("path");
        init();
        initListener();
    }

    private void init() {
        rcyDataShow = findViewById(R.id.rcy_data_show);
        editNewFolder = findViewById(R.id.edit_new_folder);
        btnCreateNewFolder = findViewById(R.id.btn_create_new_folder);
        refreshDataShow = findViewById(R.id.refresh_data_show);
        refreshDataShow.setHeaderView(new RefreshHeaderView(this));
        showFilesAdapter = new ShowDiretoryAdapter(R.layout.item_directory, new ArrayList<FileBean>());
        rcyDataShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcyDataShow.setAdapter(showFilesAdapter);
        refreshDataShow.setEnableLoadmore(false);
        refreshDataShow.startRefresh();
    }

    private void initListener() {
        refreshDataShow.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getDiretorys(true);
            }
        });
        showFilesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDiretorys(false);
            }
        });
        showFilesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (showFilesAdapter.getData().get(position).getFile_type_Int() == FileType.TYPE_DIRECTORY) {
                    Intent intent = new Intent(DiretoryActivity.this, DiretoryActivity.class);
                    intent.putExtra("path", showFilesAdapter.getData().get(position).getPath() + showFilesAdapter.getData().get(position).getFname() + File.separator);
                    startActivity(intent);
                }

            }
        });
        btnCreateNewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editNewFolder.getText().toString())) {
                    RayToast.showShort("输入文件夹名称");
                    return;
                }
                RayLib.createNewFolderInCloud(editNewFolder.getText().toString(), DiretoryActivity.this, folderPath, new HttpResult() {
                    @Override
                    public void onError(int errorCode, String error) {

                    }

                    @Override
                    public void onResponseString(String s) {
                        refreshDataShow.startRefresh();
                    }
                });
            }
        });
    }


    private void getDiretorys(final boolean header) {
        if (!isLoadingFinished) {
            if (header) refreshDataShow.finishRefreshing();
            return;
        }
        isLoadingFinished = false;
        RayLib.getFilesByDirectory(QUERY_METHODE_ALLFILES, folderPath, page = header ? 1 : page, 50, 1, 1, this, new HttpResult() {
            @Override
            public void onError(int errorCode, String error) {
                if (header)
                    refreshDataShow.finishRefreshing();
                else
                    showFilesAdapter.loadMoreFail();
                isLoadingFinished = true;
            }

            @Override
            public void onResponseString(String s) {
                isLoadingFinished = true;
                if (header) {
                    showFilesAdapter.setNewData(parseResult(s));
                    refreshDataShow.finishRefreshing();
                } else {
                    if (parseResult(s) != null)
                        showFilesAdapter.addData(parseResult(s));
                    else
                        showFilesAdapter.loadMoreEnd();
                }
                page++;

            }
        });
    }

    private List<FileBean> parseResult(String json) {
        try {
            Gson gson = new Gson();
            DirectoryBean bean = gson.fromJson(json, DirectoryBean.class);
            if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                return bean.getData();
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
