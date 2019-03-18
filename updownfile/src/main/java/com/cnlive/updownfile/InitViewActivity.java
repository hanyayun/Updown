package com.cnlive.updownfile;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnlive.media.picker.ImagePicker;
import com.cnlive.media.picker.PickerConfig;
import com.cnlive.media.picker.entity.Media;
import com.cnlive.updownfile.adapter.ShowFilesAdapter;
import com.cnlive.updownfile.bean.FileBean;
import com.cnlive.updownfile.util.Constant;
import com.cnlive.updownfile.util.DownloadUtil;
import com.cnlive.updownfile.util.UploadUtil;
import com.xiaoray.raysdk.RayLib;
import com.xiaoray.raysdk.entity.bean.LocalCloudPath;
import com.xiaoray.raysdk.listener.ConnectCloudListener;
import com.xiaoray.raysdk.service.TaskDownloadService;
import com.xiaoray.raysdk.service.TaskUploadService;
import com.xiaoray.raysdk.utils.OkHttpUtils.CloudRequest;

import java.util.ArrayList;

import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class InitViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView up_task;
    private TextView down_task;
    private TextView show_data;
    private TextView enter_file;
    private EditText cloud_path;
    public static final int REQUEST_PICKER = 0X1001;
    private final int REQUEST_WRITE_CONTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_view);
        initView();
        RayLib.init(this);
        RayLib.connectCloud(InitViewActivity.this, "a4:11:63:61:00:02", "8656", "625624",
                "3D6B5148-98D8-4642-9909-45AEBBEEC8E0", new ConnectCloudListener() {
                    @Override
                    public void connectSuc() {
                        Toast.makeText(InitViewActivity.this, "连接设备成功,IP:" + CloudRequest.getInstance()
                                .getIp(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void error(int error, String errorInfo) {
                        Toast.makeText(InitViewActivity.this, "连接设备失败:" + error, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void response(Object object) {

                    }
                });
    }

    private void initView(){
        up_task = findViewById(R.id.up_task);
        down_task = findViewById(R.id.down_task);
        show_data = findViewById(R.id.show_data);
        enter_file = findViewById(R.id.enter_file);
        cloud_path = findViewById(R.id.cloud_path);
        cloud_path.setVisibility(View.GONE);

        up_task.setOnClickListener(this);
        down_task.setOnClickListener(this);
        show_data.setOnClickListener(this);
        enter_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.up_task) {
            startPickerImageAndVideo();
        } else if (i == R.id.down_task) {
            int permissionCheck = ContextCompat.checkSelfPermission(InitViewActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(InitViewActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_CONTENT);
            } else if (permissionCheck == PERMISSION_GRANTED) {
                cloud_path.setVisibility(View.VISIBLE);
                if("".equals(cloud_path.getText().toString().trim())){
                    Toast.makeText(InitViewActivity.this, "请下载文件目录:" , Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                addDownload("");
            }
        } else if (i == R.id.show_data) {
        } else if (i == R.id.enter_file) {
            Intent intent=new Intent(InitViewActivity.this, DiretoryActivity.class);
            intent.putExtra("path","/");
            startActivity(intent);
        }
    }


    /**
     * 从相册选择图片或者视频
     */
    private void startPickerImageAndVideo() {
        ImagePicker imagePicker = new ImagePicker.Builder()
                .setSelectGif(true)
                .maxNum(9)
                .selectMode(PickerConfig.PICKER_IMAGE_VIDEO)
                .maxVideoSize(Constant.MEDIA_MAX_SIZE)
                .maxImageSize(Constant.IMAGE_MAX_SIZE)
//                .cachePath(CacheUtil.getConversationCachePath())
                .builder();
        imagePicker.start(this, REQUEST_PICKER, PickerConfig.DEFAULT_RESULT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PICKER:
                if(data == null){
                    return;
                }
                ArrayList<Media> selectList = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                ArrayList lists = new ArrayList();
                for (int i = 0; i < selectList.size(); i++) {
                    LocalCloudPath localCloudPath = new LocalCloudPath();
                    localCloudPath.setLocal(selectList.get(i).path);
                    localCloudPath.setCloud("/");
                    lists.add(localCloudPath);
                }
                UploadUtil.xiaoRayUploadFile(this,lists);
                Toast.makeText(this, "正在开始上传", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * 文件下载
     */
    private void addDownload(String cloudPath) {
        ArrayList<LocalCloudPath> arrayLists = new ArrayList<>();
        String localPath = Environment.getExternalStorageDirectory().toString();
        LocalCloudPath localCloudPath = new LocalCloudPath();
        localCloudPath.setCloud(cloudPath);
        localCloudPath.setLocal(localPath);
        arrayLists.add(localCloudPath);
        DownloadUtil.addDownload(InitViewActivity.this,arrayLists);
    }
}
