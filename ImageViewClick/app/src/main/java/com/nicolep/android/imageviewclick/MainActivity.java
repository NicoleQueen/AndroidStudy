package com.nicolep.android.imageviewclick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;



public class MainActivity extends AppCompatActivity {
     ImageView searchView_Image;
     //Button btn_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * //方法：初始化View
         *     private void initView() {
         *         btn1 = (Button) findViewById(R.id.btn1);
         *        btn2 = (Button) findViewById(R.id.btn2);
         *    }
         *
         *     //方法：控件View的点击事件
         *     public void onClick(View v) {
         *         switch (v.getId()) {
         *        case R.id.btn1:
         *            Toast.makeText(MainActivity.this, "btn1", Toast.LENGTH_SHORT).show();
         *            break;
         *         case R.id.btn2:
         *             Toast.makeText(MainActivity.this, "btn2", Toast.LENGTH_SHORT).show();
         *             break;
         *
         *         default:
         *             break;
         *         }
         *     }
          */

        searchView_Image = (ImageView) findViewById(R.id.search);

        searchView_Image.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                startActivity(intent);
            }
        });
    }


}
