package hyj.weixin_fetchdata.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

import hyj.weixin_fetchdata.GlobalApplication;


public class WxListeningService extends AccessibilityService {
    private static final String TAG="WxListeningService";

    /**
     * 监听方法入口
     * 执行思路：
     * 1、收到消息，监听到红色点、点击
     * 2、进入订阅号获取消息标题列表、点击
     * 3、点击标题进入正文 获取到文章连接，退出继续监听（拿到链接在后台http请求爬取正文内容）
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if(root==null){
            Log.d(TAG,"rootNode is null");
            return;
        }

        //1、收到消息，监听红点提示、点击    用DDMS查看红色数据点ID为 com.tencent.mm:id/ajb
        List<AccessibilityNodeInfo> receiveNodes = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ajb");
        if(receiveNodes!=null&&receiveNodes.size()>0){
            //点击红点订阅号
            performClick(receiveNodes.get(0));
            return;
        }

        //2、进入订阅号获取消息标题
        List<AccessibilityNodeInfo> dyhMsgNodes = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/fs");
        if(dyhMsgNodes!=null&&dyhMsgNodes.size()>0){
            String title="";
            for(AccessibilityNodeInfo msg:dyhMsgNodes){
                title+=msg.getText()+"\n";
            }
            System.out.println("订阅号标题内容\n"+title);
            Toast.makeText(GlobalApplication.getContext(), "订阅号标题内容\n"+title, Toast.LENGTH_SHORT).show();
            //点击订阅号第一条内容标题
            performClick(dyhMsgNodes.get(0));
            return;
        }
        //3、点击标题进入正文 获取到文章连接，退出继续监听（拿到链接在后台http请求爬取正文内容）


    }
    public  boolean performClick(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null)  return false;
        if(nodeInfo.isClickable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
        return false;
    }
    public static void startAppByPackName(String packageName,String activity){
        Intent intent = new Intent();
        ComponentName cmp=new ComponentName(packageName,activity);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        GlobalApplication.getContext().startActivity(intent);
    }
    @Override
    protected void onServiceConnected() {
        //启动微信
        Toast.makeText(GlobalApplication.getContext(), "启动微信", Toast.LENGTH_SHORT).show();
        startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
    }
    @Override
    public void onInterrupt() {

    }
}
