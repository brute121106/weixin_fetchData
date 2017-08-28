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
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if(root==null){
            Log.d(TAG,"rootNode is null");
            return;
        }
        //获取订阅号好文章标题节点
        List<AccessibilityNodeInfo> dyhMsgNodes = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/fs");
        if(dyhMsgNodes!=null&&dyhMsgNodes.size()>0){
            String title="";
            for(AccessibilityNodeInfo msg:dyhMsgNodes){
                title+=msg.getText()+"\n";
            }
            System.out.println("订阅号标题内容\n"+title);
            Toast.makeText(GlobalApplication.getContext(), "订阅号标题内容-->"+title, Toast.LENGTH_SHORT).show();
            //点击订阅号标题
            //performClick(dyhMsgNodes.get(0));
            return;
        }

        //收到消息，获取红点数字节点，用DDMS查看红色数据点ID为 com.tencent.mm:id/ie
        List<AccessibilityNodeInfo> receiveNodes = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ie");
        if(receiveNodes!=null&&receiveNodes.size()>0){
            //点击红点进入--订阅号同理
            performClick(receiveNodes.get(0));
            return;
        }

        List<AccessibilityNodeInfo> msgNodes = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/io");
        if(msgNodes!=null&&msgNodes.size()>0){
            for(AccessibilityNodeInfo msg:msgNodes){
                System.out.println("输出聊天记录-->"+msg.getContentDescription());
            }
            return;
        }


    }
    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null)  return;
        if(nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
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
