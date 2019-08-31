package com.nicolep.android.carouselimage;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.view.PagerAdapter;
import android.widget.Toast;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * https://www.youhutong.com/index.php/article/index/176.html
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /**************************************************************/
    // 布局显示轮播图的ViewPager
    private ViewPager mViewPaper;
    // 布局中显示图片标题的TextView
    private TextView title;
    // 布局中显示点的3个点的对应View
    private List<View> dots;
    /***************************************************************/
    // 用户存放动太创建出来的ImageView
    private List<ImageView> images;
    // 在values文件假下创建了ids.xml文件，并定义了5张轮播图对应的id
    private int[] ids = new int[]{R.id.bn1,R.id.bn2,R.id.bn3};
    // 存放图片资源的数组
    private int[] urls = new int[]{R.drawable.qinghua,R.drawable.beijin,R.drawable.school};
    // 存放图片的标题
    private String[]  titles = new String[]{"标题1","标题2","标题3"};
    /**************************************************************/
    // 记录当前显示的图片序号（用于自动轮播。计算得到下一张要显示图片的序号）
    private int currentItem = 0;
    // 记录上一个点的位置    （用于改变之前点的样式）
    private int oldPosition = 0;
    private ViewPagerAdapter adapter;
    // 线程对象
    private ScheduledExecutorService scheduledExecutorService;
    /**************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 第一步：初始化控件对象
        initView();
        // 第二步：初始化数据（1：定义图片id数组 2：定义图片资源数组  3：创建ImageView控件）
        images = new ArrayList<ImageView>();
        for(int i = 0; i < urls.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setId(ids[i]);                        // 设置图片ID
            imageView.setOnClickListener(this);             // 设置监听事件（点击事件）
            imageView.setBackgroundResource(urls[i]);       // 设置图片资源
            imageView.setTag("这是图片："+i+i+i+i+i+i+i+i); // 设置图片Tag
            images.add(imageView);

        }
        // 第三步：自定义Adapter（适配器）
        adapter = new ViewPagerAdapter();
        // 第四步：设置显示（已经初步完成。可以手划切换图片了）
        mViewPaper.setAdapter(adapter);
        // 第五步： 给ViewPager添加监听事件，   当ViewPager中的图片发生变化时调用
        mViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            // position表式发生改变后，当前显示的序号（也就是第几张）
            public void onPageSelected(int position) {
                //设置对应的标题
                title.setText(titles[position]);
                //也把对应的点变成当前显示
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                //把之前点的显示样式设回另一个样式
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                // 记录值
                oldPosition = position;
                currentItem = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){}
        });
        // 第六步：实现自动轮播。切换图片显示（线程实现，请看下面的方法）
    }

    /**
     *   第一步的方法（初始化控件对象）
     */
    private void initView() {
        // 获取ViewPager控件
        mViewPaper = (ViewPager) findViewById(R.id.vp);
        // 获取TextView控件
        title = (TextView) findViewById(R.id.title);
        // 获取显示点的view控件（在图片改变时好相应改变点的样式）
        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot_0));
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));

    }

    /**
     *   第三步的方法（自定义Adapter）
     */
    private class ViewPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return images.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(images.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }
    }
    /***********************************第六步的方法（实现自动轮播）********************************/
    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    protected void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 延迟2秒后开始执行，然后等显示完毕图片后，等待3秒后，再显示下一张。
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPageTask(),2,3, TimeUnit.SECONDS);
    }

    /**
     *  子线程类（ViewPageTask），实现Runnable线程接口
     */
    private class ViewPageTask implements Runnable{
        @Override
        public void run() {
            // 设置下一张要显示的图片
            currentItem = (currentItem + 1) % urls.length;
            // 去更新UI，显示目标图片（这里不需要传值过去了所以写0）
            mHandler.sendEmptyMessage(0);
        }
    }
    /**
     * 接收子线程传递过来的数据，然后配合主线程更新UI
     */
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            // 显示指定currentItem的图片
            mViewPaper.setCurrentItem(currentItem);
        };
    };

    /**
     *   附加点击事件（第二步中设置的）
     */
    @Override
    public void onClick(View v) {
        // 获取当前点击对象的Tag值
        String str = v.getTag().toString();
        switch (v.getId()) {
            case R.id.bn1:
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.bn2:
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;
            case R.id.bn3:
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
