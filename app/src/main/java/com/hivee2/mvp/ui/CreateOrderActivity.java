package com.hivee2.mvp.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.hivee2.R;
import com.hivee2.content.Api;
import com.hivee2.content.Constant;
import com.hivee2.mvp.model.bean.CarBrandBean;
import com.hivee2.mvp.model.bean.CarModelBean;
import com.hivee2.mvp.model.bean.CarSeries;
import com.hivee2.mvp.model.bean.CityBean;
import com.hivee2.mvp.model.bean.CityListBean;
import com.hivee2.mvp.model.bean.UserdetialBean;
import com.hivee2.mvp.presenter.JsonFileReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

import static java.net.Proxy.Type.HTTP;

/**
 * Created by Lenovo on 2018/3/13.
 */

public class CreateOrderActivity extends Activity {
    private ImageView imageView2;
    private TextView title_name1;
    private RelativeLayout gonext;
    private RelativeLayout goback;
    private RelativeLayout choosezh;//选择账号
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private int nowid = 1;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView nnext;
    private TextView install_area;//用户名 门店账号
    private EditText borrower_name;//借款人姓名
    private EditText borrower_id;//借款人身份证
    private EditText borrower_phone;//借款人手机号
    private EditText car_vin;//车辆vin号
    private EditText car_num;//车牌号
    private EditText car_color;//车辆颜色
    private EditText ContactPerson;//门店联系人
    private EditText MobilePhone;//门店联系电话
    private RelativeLayout brand;
    private ArrayList<String> brandlist = new ArrayList<>();
    private ArrayList<String> stylelist = new ArrayList<>();
    private ArrayList<String> carlist = new ArrayList<>();
    private SharedPreferences sp = null;
    ArrayList<CarBrandBean.BrandListBean> brand_list;
    ArrayList<CarSeries.SeriesListBean> seriesListBeans;
    ArrayList<CarModelBean.ModelListBean> modelListBeans;

    private TextView tv_brand;
    private TextView tv_type;
    private RelativeLayout type;
    private RelativeLayout azrq;//安装日期
    private TextView install_date;//要求安装日期
    private RelativeLayout aztime;//安装时间
    private TextView aztimee;//要求安装时间
    private RelativeLayout install_add;//安装地址（省市区）
    private EditText et_install_address;//安装地址详细
    private TextView install_address;

    private CheckBox rb_yxsb;
    private CheckBox rb_wxsb;
    private EditText num_yx;
    private EditText num_wx;
    private RelativeLayout rl_dev_source;//设备来源
    private TextView dev_source;
    private ArrayList<String> dev_source_list = new ArrayList<>();
    private RelativeLayout rl_service_life;//服务年限
    private TextView service_life;
    private ArrayList<String> service_life_list = new ArrayList<>();
    int mYear, mMonth, mDay, mHour, mMinutes;
    String token = "";
    private final int SHOW_STARTTIME = 1;
    private final int SHOW_ENDTIME = 2;
    private ArrayList<CityListBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private ArrayList<CityBean> cityBeans = new ArrayList<>();
    private ArrayList<CityBean> cityBeans1 = new ArrayList<>();
    private ArrayList<CityBean> cityBeans2 = new ArrayList<>();
    private ArrayList<String> Pinvince = new ArrayList<>();//省
    private ArrayList<String> city = new ArrayList<>();//市
    private ArrayList<String> area = new ArrayList<>();//区
    private int level = 0;//当前省市区级别（0：省，1市，2区）
    private String ssq = "";//省市区
    private String citycode = "";
    private int brand_num;//车辆品牌id
    ArrayList<String> typel = new ArrayList<>();
    ArrayList<String> numl = new ArrayList<>();
    JSONArray AA = new JSONArray();
    long choosetime;
    int brand_id;//品牌编号

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        sp = getSharedPreferences("hive2", MODE_PRIVATE);
        token = sp.getString("Token", "");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        title_name1 = (TextView) findViewById(R.id.title_name1);
        gonext = (RelativeLayout) findViewById(R.id.gonext);
        goback = (RelativeLayout) findViewById(R.id.goback);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        nnext = (TextView) findViewById(R.id.nnext);
        borrower_name = (EditText) findViewById(R.id.borrower_name);
        borrower_id = (EditText) findViewById(R.id.borrower_id);
        borrower_phone = (EditText) findViewById(R.id.borrower_phone);
        brand = (RelativeLayout) findViewById(R.id.brand);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_type = (TextView) findViewById(R.id.tv_type);
        type = (RelativeLayout) findViewById(R.id.type);
        car_vin = (EditText) findViewById(R.id.car_vin);
        choosezh = (RelativeLayout) findViewById(R.id.choosezh);
        install_area = (TextView) findViewById(R.id.install_area);
        ContactPerson = (EditText) findViewById(R.id.ContactPerson);
        MobilePhone = (EditText) findViewById(R.id.MobilePhone);
        azrq = (RelativeLayout) findViewById(R.id.azrq);
        install_date = (TextView) findViewById(R.id.install_date);
        aztime = (RelativeLayout) findViewById(R.id.aztime);
        aztimee = (TextView) findViewById(R.id.aztimee);
        install_add = (RelativeLayout) findViewById(R.id.install_add);
        install_address = (TextView) findViewById(R.id.install_address);
        et_install_address = (EditText) findViewById(R.id.et_install_address);
        rl_dev_source = (RelativeLayout) findViewById(R.id.rl_dev_source);
        dev_source = (TextView) findViewById(R.id.dev_source);
        rl_service_life = (RelativeLayout) findViewById(R.id.rl_service_life);
        service_life = (TextView) findViewById(R.id.service_life);
        rb_yxsb = (CheckBox) findViewById(R.id.rb_yxsb);
        rb_wxsb = (CheckBox) findViewById(R.id.rb_wxsb);
        num_yx = (EditText) findViewById(R.id.num_yx);
        num_wx = (EditText) findViewById(R.id.num_wx);
        car_num = (EditText) findViewById(R.id.car_num);
        car_color = (EditText) findViewById(R.id.car_color);
    }

    private void initData() {
        RequestParams params = new RequestParams();
        params.addFormDataPart("token", sp.getString("Token", ""));
        HttpRequest.post(Api.GET_CAR_BRANDLIST, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i("jsonObject", jsonObject.toString());
                CarBrandBean carBrandBean = JSONObject.parseObject(jsonObject.toString(), CarBrandBean.class);
                brand_list = (ArrayList<CarBrandBean.BrandListBean>) carBrandBean.getBrand_list();
                brandlist.add("选择品牌");
                for (int i = 0; i < brand_list.size(); i++) {
                    CarBrandBean.BrandListBean brandListBean = brand_list.get(i);

                    brandlist.add(brand_list.get(i).getBrand_name());
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        dev_source.setText("需要安装师傅自带");
//        dev_source_list.add("请选择");
        dev_source_list.add("需要安装师傅自带");
        dev_source_list.add("自有库存");
        service_life_list.add("请选择");
        service_life_list.add("一年");
        service_life_list.add("二年");
        service_life_list.add("三年");
        String paramss = "{parent_id:0,tokenstring:'" + token + "'}";
        Log.i("jsonObject", paramss.toString());
        RequestParams params1 = new RequestParams();
        params1.addFormDataPart("param", paramss);
        HttpRequest.post(Api.APP_GetProvince, params1, new BaseHttpRequestCallback() {
            @Override
            protected void onSuccess(Object o) {
                super.onSuccess(o);
                Log.i("jsonObjectt", o.toString());
                JSONArray jsonArray = JSONObject.parseArray(o.toString());
                Log.i("jsonObjectt", jsonArray.toString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    CityBean cityBean = JSONObject.parseObject(jsonArray.get(i).toString(), CityBean.class);
                    cityBeans.add(cityBean);
                    Pinvince.add(cityBean.getName());
                }

            }
        });
        RequestParams params2 = new RequestParams();
        params2.addFormDataPart("userid", sp.getString(Constant.sp_userId, ""));
        HttpRequest.get(Api.GET_SESSION, params2, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                if (jsonObject.getBoolean("success")) {
                    //请求成功
                    String session = jsonObject.getJSONObject("object").getString("session");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("session", session);
                    Log.i("session", session);
                    editor.commit();
                } else {
                    Toast.makeText(CreateOrderActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);

            }
        });

    }

    private void initListener() {
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_name1.setText("新建工单");
        gonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                if (nowid == 1) {
                    //第一个页面
                    String borrower_name_input = borrower_name.getText().toString().trim();
                    if (borrower_name_input.isEmpty()) {
                        //输入框是空
                        Toast.makeText(CreateOrderActivity.this, "借款人姓名不能为空", Toast.LENGTH_SHORT).show();
                    } else if (borrower_id.getText().toString().trim().length() != 0 && borrower_id.getText().toString().trim().length() != 18) {
                        //人如果输入了身份证号，那么就必须是17位
                        //不是0位而且不是17位的情况
                        Toast.makeText(CreateOrderActivity.this, "请输入正确的身份证号", Toast.LENGTH_SHORT).show();
                    } else if (borrower_phone.getText().toString().trim().length() != 0 && borrower_phone.getText().toString().trim().length() != 11) {
                        //输入了手机号，但不是11位
                        //不是0位且不是11位
                        Toast.makeText(CreateOrderActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    } else {
                        //有信息可以进入到下一页
                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                        goback.setVisibility(View.VISIBLE);
                        tv2.setBackgroundResource(R.drawable.textview_border_circle);
                        nowid = nowid + 1;
                    }
                } else if (nowid == 2) {
                    //第二个页面
                    String vinid = car_vin.getText().toString().trim();
                    if (vinid.isEmpty()) {
                        Toast.makeText(CreateOrderActivity.this, "车辆vin号不能为空", Toast.LENGTH_SHORT).show();
                    } else if (vinid.length() != 17) {
                        Toast.makeText(CreateOrderActivity.this, "请输入正确的车架号", Toast.LENGTH_SHORT).show();
                    } else {
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.VISIBLE);
                        tv3.setBackgroundResource(R.drawable.textview_border_circle);
                        nowid = nowid + 1;
                        nnext.setText("提交");
                    }

                } else if (nowid == 3) {
                    //第三个页面
                    //提交
                    //只判断这个页面的必须值
                    ArrayList<HashMap<String, String>> typelist = new ArrayList<>();
                    if (rb_yxsb.isChecked()) {
                        //有线设备
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("type", "22");
                        hm.put("count", num_yx.getText().toString());
                        typelist.add(hm);
//                product_type=product_type+"{"+"type:22,count:"+num_yx.getText().toString()
                    }
                    if (rb_wxsb.isChecked()) {
                        //无线设备
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("type", "23");
                        hm.put("count", num_wx.getText().toString());
                        typelist.add(hm);
//                product_type=product_type+"{"+"type:22,count:"+num_yx.getText().toString()
                    }
                    Log.i("typelist", typelist.toString());
                    createOrder();

                }
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                if (nowid == 2) {
                    //第二个页面
                    ll2.setVisibility(View.GONE);
                    ll1.setVisibility(View.VISIBLE);
                    tv2.setBackgroundResource(R.drawable.textview_border_circle_white);
                    goback.setVisibility(View.GONE);
                    nowid = nowid - 1;
                } else if (nowid == 3) {
                    //第三个页面
                    ll3.setVisibility(View.GONE);
                    ll2.setVisibility(View.VISIBLE);
                    tv3.setBackgroundResource(R.drawable.textview_border_circle_white);
                    nnext.setText("下一步");
                    nowid = nowid - 1;
                }
            }
        });
        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CreateOrderActivity.this, BrandListActivity.class);
                startActivityForResult(intent, 2);
//                OptionsPickerView pickerView = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
//                    @Override
//                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                        //当点击的时候触发的事件
//                        tv_brand.setText(brandlist.get(options1));
//                        tv_type.setText("请选择车辆型号");
//                        if (options1 == 0) {
//                            //选择品牌
//                            carlist.clear();
//                        } else {
//                            doNetWork(brand_list.get(options1 - 1).getBrand_id());
//                            brand_num= brand_list.get(options1 - 1).getBrand_id();
//                        }
//
//                    }
//                }).setTitleText("").setDividerColor(Color.BLUE)
//                        .setTextColorCenter(Color.GRAY)
//                        .setContentTextSize(18)
//                        .setOutSideCancelable(false).build();
//                pickerView.setPicker(brandlist);
//                pickerView.show();
            }
        });
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_brand.getText().toString().trim().isEmpty() || tv_brand.getText().toString().trim().equals("选择品牌")) {
                    Toast.makeText(CreateOrderActivity.this, "请先选择车辆品牌", Toast.LENGTH_SHORT).show();
                } else if (carlist.size() == 0) {
                    Toast.makeText(CreateOrderActivity.this, "该品牌下没有车辆型号", Toast.LENGTH_SHORT).show();
                } else {
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            //当点击的时候触发的事件
//                        tv_brand.setText(carlist.get(options1));
                            Log.i("a123", seriesListBeans.get(options1).getSeries_id() + "");
                            doNetWork1(seriesListBeans.get(options1).getSeries_id());
                        }
                    }).setTitleText("").setDividerColor(Color.BLUE)
                            .setTextColorCenter(Color.GRAY)
                            .setContentTextSize(18)
                            .setOutSideCancelable(false).build();
                    pickerView.setPicker(carlist);
                    pickerView.show();
                }

            }
        });
        azrq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar ca = Calendar.getInstance();
                mYear = ca.get(Calendar.YEAR);
                mMonth = ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);
                mHour = ca.get(Calendar.HOUR_OF_DAY);
                mMinutes = ca.get(Calendar.MINUTE);
                showDialog(SHOW_STARTTIME);

//                datePicker
            }
        });
        aztime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(SHOW_ENDTIME);
            }
        });
        choosezh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CreateOrderActivity.this, ChooseaAccountActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        install_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level = 0;
                ssq = "";
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //点击确定的时候，获取当前的信息
                        int pinid = cityBeans.get(options1).getId();
                        ssq = ssq + Pinvince.get(options1);
                        level = level + 1;
                        showopv(pinid);
                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false)
                        .build();
                pvOptions.setPicker(Pinvince);//三级选择器
                pvOptions.show();

            }
        });
        rl_dev_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionsPickerView pickerView = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //当点击的时候触发的事件
                        dev_source.setText(dev_source_list.get(options1));


                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false).build();
                pickerView.setPicker(dev_source_list);
                pickerView.show();
            }
        });
        rl_service_life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionsPickerView pickerView = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //当点击的时候触发的事件
                        service_life.setText(service_life_list.get(options1));


                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false).build();
                pickerView.setPicker(service_life_list);
                pickerView.show();
            }
        });
        rb_yxsb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //选中
                    num_yx.setFocusable(true);
                    num_yx.setFocusableInTouchMode(true);
                    num_yx.setText("1");
                } else {
                    num_yx.setText("");
                    num_yx.setFocusable(false);
                }
            }
        });
        rb_wxsb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("onCheckedChanged", b + "2");
                if (b) {
                    //选中
                    num_wx.setFocusable(true);
                    num_wx.setFocusableInTouchMode(true);
                    num_wx.setText("1");
                } else {
                    num_wx.setText("");
                    num_wx.setFocusable(false);
                }
            }
        });
    }

    private void showopv(int pinid) {
        city.clear();
        area.clear();
        cityBeans1.clear();
        cityBeans2.clear();
        String paramss = "{parent_id:" + pinid + ",tokenstring:'" + token + "'}";
        Log.i("jsonObject", paramss.toString());
        RequestParams params1 = new RequestParams();
        params1.addFormDataPart("param", paramss);
        HttpRequest.post(Api.APP_GetProvince, params1, new BaseHttpRequestCallback() {
            @Override
            protected void onSuccess(Object o) {
                super.onSuccess(o);
                Log.i("jsonObjectt", o.toString());
                JSONArray jsonArray = JSONObject.parseArray(o.toString());
                Log.i("jsonObjectt", jsonArray.toString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (level == 1) {
                        CityBean cityBean = JSONObject.parseObject(jsonArray.get(i).toString(), CityBean.class);
                        cityBeans1.add(cityBean);
                        city.add(cityBean.getName());


                    } else if (level == 2) {
                        CityBean cityBean = JSONObject.parseObject(jsonArray.get(i).toString(), CityBean.class);
                        cityBeans2.add(cityBean);
                        area.add(cityBean.getName());

                    }
                }
                if (level == 1) {
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            //当点击的时候触发的事件
                            int pinid = cityBeans1.get(options1).getId();
                            level = level + 1;
                            ssq = ssq + city.get(options1);
                            showopv(pinid);

                        }
                    }).setTitleText("").setDividerColor(Color.BLUE)
                            .setTextColorCenter(Color.GRAY)
                            .setContentTextSize(18)
                            .setOutSideCancelable(false).build();
                    pickerView.setPicker(city);
                    pickerView.show();
                } else if (level == 2) {
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            //当点击的时候触发的事件
                            ssq = ssq + area.get(options1);
                            install_address.setText(ssq);
                            citycode = cityBeans2.get(options1).getCode();

                        }
                    }).setTitleText("").setDividerColor(Color.BLUE)
                            .setTextColorCenter(Color.GRAY)
                            .setContentTextSize(18)
                            .setOutSideCancelable(false).build();
                    pickerView.setPicker(area);
                    pickerView.show();
                }


            }
        });

    }

    private void createOrder() {
        //先判断必填项有没有填写
        String st_install_area = install_area.getText().toString().trim();
        String st_ContactPerson = ContactPerson.getText().toString().trim();
        String st_MobilePhone = MobilePhone.getText().toString().trim();
        String st_install_date = install_date.getText().toString().trim();
        String st_install_address = install_address.getText().toString().trim();
        String st_et_install_address = et_install_address.getText().toString().trim();
        String st_dev_source = dev_source.getText().toString().trim();
        String st_service_life = service_life.getText().toString().trim();

        if (st_install_area.isEmpty() || st_install_area.equals("请选择账号")) {
            //没填账号
            Toast.makeText(CreateOrderActivity.this, "请选择账号", Toast.LENGTH_SHORT).show();
        } else if (st_ContactPerson.isEmpty()) {
            //没填门店联系人
            Toast.makeText(CreateOrderActivity.this, "请输入门店联系人", Toast.LENGTH_SHORT).show();
        } else if (st_MobilePhone.isEmpty()) {
            //没填门店联系电话
            Toast.makeText(CreateOrderActivity.this, "请输入门店联系电话", Toast.LENGTH_SHORT).show();
        } else if (st_MobilePhone.length() != 11) {
            //门店联系电话不是11位
            Toast.makeText(CreateOrderActivity.this, "请输入正确的门店联系电话", Toast.LENGTH_SHORT).show();
        } else if (st_install_date.isEmpty() || st_install_date.equals("请选择")) {
            //没填要求安装时间
            Toast.makeText(CreateOrderActivity.this, "请输入要求安装日期", Toast.LENGTH_SHORT).show();
        } else if (st_install_address.isEmpty() || st_et_install_address.isEmpty() || st_install_address.equals("请选择")) {
            //没填安装地址
            Toast.makeText(CreateOrderActivity.this, "请输入安装地址", Toast.LENGTH_SHORT).show();
        } else if (choosetime < new Date().getTime()) {
            //选了安装日期，安装时间填了，判断是否早于当前时间
            Toast.makeText(CreateOrderActivity.this, "安装时间不能早于当前时间", Toast.LENGTH_SHORT).show();
        } else if (st_dev_source.isEmpty() || st_dev_source.equals("请选择")) {
            //没设备来源
            Toast.makeText(CreateOrderActivity.this, "请选择设备来源", Toast.LENGTH_SHORT).show();
        } else if (st_service_life.isEmpty() || st_service_life.equals("请选择")) {
            //没设备来源
            Toast.makeText(CreateOrderActivity.this, "请选择设备购买年限", Toast.LENGTH_SHORT).show();
        } else if (rb_wxsb.isChecked() == false && rb_yxsb.isChecked() == false) {
            //选择框全是false
            Toast.makeText(CreateOrderActivity.this, "请选择设备", Toast.LENGTH_SHORT).show();
        } else if (rb_wxsb.isChecked() && num_wx.getText().toString().trim().isEmpty()) {
            //无线选择框是true，但是输入框没值
            Toast.makeText(CreateOrderActivity.this, "请输入无线设备数量", Toast.LENGTH_SHORT).show();
        } else if (rb_yxsb.isChecked() && num_yx.getText().toString().trim().isEmpty()) {
            //无线选择框是true，但是输入框没值
            Toast.makeText(CreateOrderActivity.this, "请输入有线设备数量", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("posturl", "onStart");
            AsyncHttpClient client = new AsyncHttpClient();
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            try {
                jsonObject.put("session", sp.getString("session", ""));
                jsonObject.put("city_code", citycode);
                jsonObject.put("borrower_name", borrower_name.getText().toString().trim());
                jsonObject.put("borrower_id", borrower_id.getText().toString().trim());
                jsonObject.put("borrower_phone", borrower_phone.getText().toString().trim());
                jsonObject.put("car_num", car_num.getText().toString().trim());
                jsonObject.put("car_vin", car_vin.getText().toString().trim());
                if (tv_brand.getText().equals("选择品牌") || tv_brand.getText().equals("请选择车辆品牌")) {
                    //没选车辆品牌
                } else {
                    jsonObject.put("car_brand", brand_num);//车辆品牌
                }
                if (tv_type.getText().toString().trim().equals("请选择车辆型号")) {

                } else {
                    jsonObject.put("car_model", tv_type.getText().toString().trim());//车辆型号
                }
                jsonObject.put("car_color", car_color.getText().toString().trim());
                jsonObject.put("install_area", install_area.getText().toString().trim());
                String strr = st_install_address + st_et_install_address;
//                String strUTF81 = URLDecoder.decode(strr, "UTF-8");
//                String strUTF8 = new String(strr.getBytes("UTF-8"),"ISO8859-1");
                jsonObject.put("install_address", strr);
                jsonObject.put("install_date", install_date.getText().toString().trim());
                if (aztimee.getText().toString().trim().equals("请选择")) {

                } else {
                    jsonObject.put("install_time", aztimee.getText().toString().trim());//安装时间
                }
                jsonObject.put("install_cont_per", ContactPerson.getText().toString().trim());
                jsonObject.put("install_cont_pho", MobilePhone.getText().toString().trim());


                if (dev_source.getText().toString().trim().equals("需要安装师傅自带")) {
                    jsonObject.put("dev_source", 1);
                } else {
                    jsonObject.put("dev_source", 2);
                }
                if (service_life.getText().toString().equals("一年")) {
                    jsonObject.put("service_life", 1);//服务年限
                } else if (service_life.getText().toString().equals("二年")) {
                    jsonObject.put("service_life", 2);//服务年限
                } else {
                    jsonObject.put("service_life", 3);//服务年限
                }

                ArrayList<HashMap<String, String>> typelist = new ArrayList<>();

                if (rb_yxsb.isChecked()) {
                    //有线设备
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("type", "22");
                    hm.put("count", num_yx.getText().toString());
                    typelist.add(hm);
                    typel.add("2");
                    numl.add(num_yx.getText().toString());

                    JSONObject jo1 = new JSONObject();
                    jo1.put("type", "2");
                    jo1.put("count", num_yx.getText().toString());
                    AA.add(jo1);
//                product_type=product_type+"{"+"type:22,count:"+num_yx.getText().toString()
                }
                if (rb_wxsb.isChecked()) {
                    //无线设备
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("type", "23");
                    hm.put("count", num_wx.getText().toString());
                    typelist.add(hm);
                    typel.add("1");
                    numl.add(num_wx.getText().toString());
                    JSONObject jo1 = new JSONObject();
                    jo1.put("type", "1");
                    jo1.put("count", num_wx.getText().toString());
                    AA.add(jo1);
//                product_type=product_type+"{"+"type:22,count:"+num_yx.getText().toString()
                }

                String str = typelist.toString();
                String str1 = str.replaceAll("=", ":");
                Log.i("typelist", str1);


                Log.i("AA", AA.toJSONString());
//             product_type=[{"type": "1","count": "1"},{"type": "2","count": "3"}]
//                jsonObject.put("product_type","product_type");
                jsonObject.put("product_type", AA);//设备信息

//                Log.i("AA", "["+jo1.toString().replace("\"", "'")+"]");
//                jsonObject.put("product_type", "["+jo1.toString().replace("\"", "'")+"]");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //正式提交参数


//            RequestParams paramss=new RequestParams();
//            paramss.addFormDataPart("session",sp.getString("session",""));
//            paramss.addFormDataPart("city_code",citycode);
//            paramss.addFormDataPart("borrower_name",borrower_name.getText().toString().trim());//借款人名字
//            paramss.addFormDataPart("borrower_id",borrower_id.getText().toString().trim());//身份证
//            paramss.addFormDataPart("borrower_phone",borrower_phone.getText().toString().trim());//借款人手机号
//            paramss.addFormDataPart("car_num",car_num.getText().toString().trim());//车牌号
//            paramss.addFormDataPart("car_vin",car_vin.getText().toString().trim());/in
//            if(tv_brand.getText().equals("选择品牌")||tv_brand.getText().equals("请选择车辆品牌")){
//                //没选车辆品牌
//            }else{
//                paramss.addFormDataPart("car_brand",brand_num);//车辆品牌
//            }
//            if(tv_type.getText().toString().trim().equals("请选择车辆型号")){
//
//            }else{
//                paramss.addFormDataPart("car_model",tv_type.getText().toString().trim());//车辆型号
//            }
//            paramss.addFormDataPart("car_color",car_color.getText().toString().trim());//车辆颜色
//            paramss.addFormDataPart("install_area",install_area.getText().toString().trim());//选择账号
//            paramss.addFormDataPart("install_address",st_install_address+st_et_install_address);//安装地址
//            paramss.addFormDataPart("install_date",install_date.getText().toString().trim());//安装日期
//            if(aztimee.getText().toString().trim().equals("请选择")){
//
//            }else{
//                paramss.addFormDataPart("install_time",aztimee.getText().toString().trim());//安装时间
//            }
//            paramss.addFormDataPart("install_cont_per",ContactPerson.getText().toString().trim());//门店联系人
//            paramss.addFormDataPart("install_cont_pho",MobilePhone.getText().toString().trim());//门店联系电话
//            if(dev_source.getText().toString().trim().equals("需要安装师傅自带")){
//                paramss.addFormDataPart("dev_source",1);//自带
//            }else{
//                paramss.addFormDataPart("dev_source",2);//自带
//            }
//            if(service_life.getText().toString().equals("一年")){
//                paramss.addFormDataPart("service_life",1);//服务年限
//            }else if(service_life.getText().toString().equals("二年")){
//                paramss.addFormDataPart("service_life",2);//服务年限
//            }else{
//                paramss.addFormDataPart("service_life",3);//服务年限
//            }
//            String product_type="[";
//            ArrayList<HashMap<String,String>>typelist=new ArrayList<>();
//            if(rb_yxsb.isChecked()){
//                //有线设备
//                HashMap<String,String>hm=new HashMap<>();
//                hm.put("type","22");
//                hm.put("count",num_yx.getText().toString());
//                typelist.add(hm);
////                product_type=product_type+"{"+"type:22,count:"+num_yx.getText().toString()
//            }
//            if(rb_wxsb.isChecked()){
//                //无线设备
//                HashMap<String,String>hm=new HashMap<>();
//                hm.put("type","23");
//                hm.put("count",num_wx.getText().toString());
//                typelist.add(hm);
////                product_type=product_type+"{"+"type:22,count:"+num_yx.getText().toString()
//            }
//
//            String str = typelist.toString();
//            String str1 = str.replaceAll("=",":");
//            Log.i("typelist", str1);
////             product_type=[{"type": "16","count": "1"},{"type": "2","count": "3"}]
//            paramss.addFormDataPart("product_type","[{type: '2', count: '1'}, {type: '1', count: '1'}]");//安装时间
////            paramss.setRequestBody("text/plain; charset=UTF-8","text/plain; charset=UTF-8");
//
//
//


            StringEntity entity = null;
            try {
                Log.i("jsonObject", jsonObject.toString());
                String jss = jsonObject.toString();
//                String str1 = jss.replaceAll("\"[","[");
//                str1=str1.replaceAll("]\"","]");

                String[] split = jss.split("\"\\[");
                String[] splitt = split[1].split("\\]\"");
                String mid = AA.toString();
                Log.i("mid", mid);
                for (int i = 0; i < typel.size(); i++) {

                }
//                jss=split[0]+"[{"+"type"+":"+2+",count:"+"1}]"+splitt[1];
                jss = split[0] + mid + splitt[1];
//                jss = URLDecoder.decode(jss, "UTF-8");
                jss = new String(jss.getBytes("UTF-8"), "ISO8859-1");
                Log.i("jsonObject", jss);
                entity = new StringEntity(jss);
                entity.setContentType(new BasicHeader("Content-Type", "application/json"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            client.post(CreateOrderActivity.this, Api.CREATE_ORDER, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String json = new String(bytes).trim().toString();
                    Log.i("querySettleList", json);
//                    String json=new String(bytes,"UTF-8").trim().toString();
                    JSONTokener jsonTokener = new JSONTokener(json);
                    try {
                        org.json.JSONObject ob = (org.json.JSONObject) jsonTokener.nextValue();
                        if (ob.getBoolean("success")) {
                            Toast.makeText(CreateOrderActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CreateOrderActivity.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }
            });


//            Log.i("paramss", paramss.toString());
//            HttpRequest.post(Api.CREATE_ORDER,paramss,new JsonHttpRequestCallback(){
//                @Override
//                protected void onSuccess(Headers headers, JSONObject jsonObject) {
//                    super.onSuccess(headers, jsonObject);
//                    Log.i("onSuccess", jsonObject.toString());
//                    if(jsonObject.getBoolean("success")){
//                        Toast.makeText(CreateOrderActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(CreateOrderActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(int errorCode, String msg) {
//                    super.onFailure(errorCode, msg);
//                    Log.i("onFailure", msg);
//                }
//
//                @Override
//                public void onStart() {
//                    super.onStart();
//                    Log.i("onFailure", "onStart");
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    Log.i("onFailure", "onFinish");
//                }
//            });

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case SHOW_STARTTIME:
                final DatePickerDialog dpd = new DatePickerDialog(this, null, mYear, mMonth, mDay);
                DatePicker dp1 = dpd.getDatePicker();
                dp1.setMinDate(System.currentTimeMillis() - 1000);
                //给确定按钮增加点击事件
                dpd.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //获取弹出框上面的时间
                        DatePicker dp = dpd.getDatePicker();
                        mYear = dp.getYear();
                        mMonth = dp.getMonth();
                        mDay = dp.getDayOfMonth();
//                        dp.setMinDate(new Date().getTime());
//                        dp.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
//
//                            @Override
//                            public void onDateChanged(DatePicker view, int year,
//                                                      int monthOfYear, int dayOfMonth) {
//
//                                if (isDateAfter(view)) {
//                                    Calendar mCalendar = Calendar.getInstance();
//                                    view.init(mCalendar.get(Calendar.YEAR),
//                                            mCalendar.get(Calendar.MONTH),
//                                            mCalendar.get(Calendar.DAY_OF_MONTH), this);
//                                }
//                            }
//
//                            private boolean isDateAfter(DatePicker tempView) {
//                                Calendar mCalendar = Calendar.getInstance();
//                                Calendar tempCalendar = Calendar.getInstance();
//                                tempCalendar.set(tempView.getYear(), tempView.getMonth(),
//                                        tempView.getDayOfMonth(), 0, 0, 0);
//                                if (tempCalendar.after(mCalendar))
//                                    return true;
//                                else
//                                    return false;
//                            }
//                        });
                        //展示
                        display();
                    }
                });
                return dpd;
            case SHOW_ENDTIME:
                String str;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//得到当前的时间
                Date curDate = new Date(System.currentTimeMillis());
                str = formatter.format(curDate);


                Calendar mCalendar = Calendar.getInstance();
                int nowhour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int nowminute = mCalendar.get(Calendar.MINUTE);
                final TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mHour = i;
                        mMinutes = i1;
                        Log.i("time", mHour + ":" + mMinutes);
                        String lastHour = "";
                        String lastMinutes = "";
                        if (mHour < 10) {
                            lastHour = "0" + mHour;
                        } else {
                            lastHour = "" + mHour;
                        }
                        if (mMinutes < 10) {
                            lastMinutes = "0" + mMinutes;
                        } else {
                            lastMinutes = "" + mMinutes;
                        }
                        Log.i("time", lastHour + ":" + lastMinutes);
                        aztimee.setText(lastHour + ":" + lastMinutes);
                        //获取时间段
                        SimpleDateFormat f4 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        try {
                            String format3 = install_date.getText().toString().trim() + " " + lastHour + ":" + lastMinutes;
                            Date chosedate = f4.parse(format3);
                            choosetime = chosedate.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, nowhour, nowminute, true);
                return tpd;
        }
        return null;
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    private void display() {
        int month = mMonth + 1;
        String month_new = "";
        String day = "";
        if (month < 10) {
            month_new = "0" + month;
        } else {
            month_new = month + "";
        }
        if (mDay < 10) {
            day = "0" + mDay;
        } else {
            day = mDay + "";
        }
        install_date.setText(new StringBuffer().append(mYear).append("-").append(month_new).append("-").append(day));

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                OptionsPickerView pickerView1 = new OptionsPickerView.Builder(CreateOrderActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //当点击的时候触发的事件
                        tv_type.setText(stylelist.get(options1));
                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false).build();
                pickerView1.setPicker(stylelist);
                pickerView1.show();
            }
            super.handleMessage(msg);

        }
    };

    public class MyThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    }

    private void doNetWork1(int series_id) {
        Log.i("aaaa13", series_id + "");
        stylelist.clear();
        RequestParams params = new RequestParams();
        params.addFormDataPart("token", sp.getString("Token", ""));
        params.addFormDataPart("seriesId", series_id);
        HttpRequest.post(Api.GET_CAR_MODEL, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i("aaaa13", "123: ");
                CarModelBean carModelBean = JSONObject.parseObject(jsonObject.toString(), CarModelBean.class);
                modelListBeans = (ArrayList<CarModelBean.ModelListBean>) carModelBean.getModel_list();

                for (int i = 0; i < modelListBeans.size(); i++) {
                    CarModelBean.ModelListBean modelListBean = modelListBeans.get(i);
                    stylelist.add(modelListBean.getModel_name());


                }
                new MyThread().start();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void doNetWork(int brand_id) {
        carlist.clear();
        RequestParams params = new RequestParams();
        params.addFormDataPart("token", sp.getString("Token", ""));
        params.addFormDataPart("brandId", brand_id);
        HttpRequest.post(Api.GET_CAR_SERIES, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                CarSeries carSeries = JSONObject.parseObject(jsonObject.toString(), CarSeries.class);
                seriesListBeans = (ArrayList<CarSeries.SeriesListBean>) carSeries.getSeries_list();
                for (int i = 0; i < seriesListBeans.size(); i++) {
                    CarSeries.SeriesListBean seriesListBean = seriesListBeans.get(i);
                    carlist.add(seriesListBean.getSeries_name());
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 2) {
            if (requestCode == 1) {
                String Name = data.getStringExtra("Name");
                String ID = data.getStringExtra("ID");
                //设置结果显示框的显示数值
                install_area.setText(Name);
//                Toast.makeText(CreateOrderActivity.this,Name+ID,Toast.LENGTH_SHORT).show();
                //获取门店信息
                achievemd(ID);
            }
        }
        if (resultCode == 3) {
            if (requestCode ==2) {
                carlist.clear();
                String brand_name = data.getStringExtra("brand_name");
                brand_id = data.getIntExtra("brand_id",1);
                //设置结果显示框的显示数值
                tv_brand.setText(brand_name);
//                Toast.makeText(CreateOrderActivity.this,Name+ID,Toast.LENGTH_SHORT).show();
                //获取当前品牌下的车系
                tv_type.setText("请选择车辆型号");
                            doNetWork(brand_id);
                            brand_num= brand_id;
                Log.i("brand_id", brand_id+"/");

            }
        }
    }

    private void achievemd(String mdid) {
        RequestParams params = new RequestParams();
        params.addFormDataPart("UserID", mdid);
        params.addFormDataPart("TokenString", token);
        HttpRequest.post(Api.GET_USER_INFO, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                UserdetialBean userdetialBean = JSONObject.parseObject(jsonObject.toString(), UserdetialBean.class);
                UserdetialBean.ResponseCustomerBean responseCustomerBean = userdetialBean.getResponse_Customer();
//                if(responseCustomerBean.getContactPerson().trim().isEmpty()){
//
//                }else{
                ContactPerson.setText(responseCustomerBean.getContactPerson().trim());
                MobilePhone.setText(responseCustomerBean.getMobilePhone().trim());
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
