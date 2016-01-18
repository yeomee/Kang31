package com.eastflag.kang.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eastflag.kang.Constant;
import com.eastflag.kang.KangApplication;
import com.eastflag.kang.R;
import com.eastflag.kang.dto.MemberVO;
import com.eastflag.kang.dto.MoimVO;
import com.eastflag.kang.dto.PositionVo;
import com.eastflag.kang.utils.PreferenceUtil;
import com.eastflag.kang.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 회원 등록 화면
 */
public class Fragment111 extends Fragment {
    private final int MODE_REG = 0;
    private final int MODE_MODIFY = 1;

    private View mView;
    private AQuery mAq;

    @Bind(R.id.menu1) Button mMenu1;
    @Bind(R.id.menu2) Button mMenu2;
    @Bind(R.id.menu3) Button mMenu3;

    @Bind(R.id.mb_name) EditText mb_name;
    @Bind(R.id.mb_position) Spinner mb_position;
    @Bind(R.id.mb_pn) EditText mb_pn;
    @Bind(R.id.mb_add) EditText mb_add;
    @Bind(R.id.mb_enter_ymd) TextView mb_enter_ymd;
    @Bind(R.id.mb_actions) EditText mb_actions;
    @Bind(R.id.submit) Button submit;
    @Bind(R.id.delete) Button delete;

    private ArrayList<PositionVo> mPositionList = new ArrayList<PositionVo>();
    private ArrayAdapter<PositionVo> mAdaptor;

    private String mTitle;

    private MoimVO mMoimVo;
    private MemberVO mMemberVo;
    private String mbName;
    private String mbPosition;
    private String mbPn;
    private String mbAddr;
    private String mbActions;

    private int screenMode;

    public Fragment111() {
        // Required empty public constructor
    }

    public Fragment111(MoimVO moimVo) {
        this.mMoimVo = moimVo;
        screenMode = MODE_REG;
    }

    public Fragment111(MoimVO moimVo, MemberVO memberVo) {
        this.mMoimVo = moimVo;
        this.mMemberVo = memberVo;
        screenMode = MODE_MODIFY;
    }

//    public static Fragment111 newInstance(String m_id, String mb_name, String mb_position,
//                                          String mb_pn, String mb_addr, String mb_actions) {
//        Fragment111 frag = new Fragment111();
//        Bundle args = new Bundle();
//        args.putString("m_id", m_id);
//        args.putString("mb_name", mb_name);
//        args.putString("mb_position", mb_position);
//        args.putString("mb_pn", mb_pn);
//        args.putString("mb_addr", mb_addr);
//        args.putString("mb_actions", mb_actions);
//        frag.setArguments(args);
//        return frag;
//    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        mId = getArguments().getString("m_id");
//        mbName = getArguments().getString("mb_name");
//        mbPosition = getArguments().getString("mb_position");
//        mbPn = getArguments().getString("mb_pn");
//        mbAddr = getArguments().getString("mb_addr");
//        mbActions = getArguments().getString("mb_actions");
//
//        mView = View.inflate(getActivity(), R.layout.fragment_111, null);
//        mAq = new AQuery(mView);
//        ButterKnife.bind(this, mView);
//
//        if(TextUtils.isEmpty(mbName)) {
//            mTitle = "회원 등록";
//        } else {
//            mTitle = "회원 수정";
//            //버튼
//            submit.setText("회원 수정");
//            delete.setVisibility(View.VISIBLE);
//
//            mb_name.setText(mbName);
//            int selected = 0;
//            if("회장".equals(mbPosition)) {
//                selected = 0;
//            } else if("총무".equals(mbPosition)) {
//                selected = 1;
//            } if("회원".equals(mbPosition)) {
//                selected = 2;
//            }
//            mb_position.setSelection(selected);
//            mb_pn.setText(mbPn);
//            mb_add.setText(mbAddr);
//            mb_actions.setText(mbActions);
//        }
//
//        Dialog dialog = new AlertDialog.Builder(getActivity())
//                .setTitle(mTitle)
//                .setView(mView)
//                .create();
//        return dialog;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_111, container, false);
        mAq = new AQuery(mView);
        ButterKnife.bind(this, mView);

        mAdaptor = new ArrayAdapter<PositionVo>(getActivity(), R.layout.spinner, mPositionList);
        mb_position.setAdapter(mAdaptor);

        getPositionList();

        if(screenMode == MODE_REG) { // 등록 모드
            mMenu2.setSelected(true);
        }
        else { // 수정 모드
            mMenu3.setSelected(true);

            submit.setText("회원 수정");
            delete.setVisibility(View.VISIBLE);

            mb_name.setText(mMemberVo.getMb_name());
            for(int i = 0; i < mAdaptor.getCount(); i++) {
                PositionVo positionVo = mAdaptor.getItem(i);
                if(positionVo.getPo_name().equals(mMemberVo.getMy_position())) {
                    mb_position.setSelection(i);
                    break;
                }
            }
            mb_pn.setText(mMemberVo.getMb_pn());
            mb_add.setText(mMemberVo.getMb_add());
            mb_enter_ymd.setText(mMemberVo.getMb_enter_ymd());
            mb_actions.setText(mMemberVo.getMb_action());
        }

        mMenu1.setOnClickListener(mMenuClick);
        //mMenu2.setOnClickListener(mMenuClick);
        //mMenu3.setOnClickListener(mMenuClick);

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submit();
//            }
//        });

        return mView;
    }

    public void getPositionList() {
        String url;
        url = Constant.HOST + Constant.API_111_SCENE;

        Log.d("LDK", "url:" + url);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pn", Util.getMdn(getActivity())); //전화번호
        params.put("paid", Util.getAndroidId(getActivity())); //안드로이드 아이디
        params.put("token", PreferenceUtil.getInstance(getActivity()).getToken()); //폰모델
        params.put("m_id", mMoimVo.getM_id());
        Log.d("LDK", params.toString());

        mAq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
            try {
                if (status.getCode() != 200) {
                    Log.d("LDK", "status:" + status.getCode());
                    Util.showToast(getActivity(), "서버 오류가 발생하였습니다.");
                    return;
                }
                Log.d("LDK", object.toString(1));
                //데이터 존재하지 않음

                //데이터 존재하지 않음
                PositionVo position = new PositionVo();
                position.setPo_cd("-1");
                position.setPo_name("직책을 선택하세요.");
                mPositionList.add(position);

                if (object.getInt("result") == 0) {
                    String scname_msg = object.getString("scname_msg1");
                    //title.setText(scname_msg);

                    JSONArray array = object.getJSONArray("position");
                    for(int i = 0; i < array.length(); ++i) {
                        JSONObject json = array.getJSONObject(i);
                        position = new PositionVo();

                        position.setPo_cd(json.getString("po_cd"));
                        position.setPo_name(json.getString("po_name"));

                        mPositionList.add(position);
                    }

                    if(screenMode == MODE_REG) {
                        mb_position.setSelection(0);
                    }
                    else {
                        for(int i = 0; i < mAdaptor.getCount(); i++) {
                            PositionVo positionVo = mAdaptor.getItem(i);
                            if(positionVo.getPo_name().equals(mMemberVo.getMy_position())) {
                                mb_position.setSelection(i);
                                break;
                            }
                        }
                    }

                    mAdaptor.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        });
    }

    @OnClick(R.id.submit)
    public void submit() {
        if(TextUtils.isEmpty(mb_name.getText())) {
            Util.showToast(getActivity(), "회원 이름을 입력하세요");
            return;
        }

        if(TextUtils.isEmpty(mb_position.getSelectedItem().toString())) {
            Util.showToast(getActivity(), "회원 직책을 선택하세요");
            return;
        }

        if(TextUtils.isEmpty(mb_pn.getText())) {
            Util.showToast(getActivity(), "회원 전화번호를 입력하세요");
            return;
        }

        if(TextUtils.isEmpty(mb_enter_ymd.getText())) {
            Util.showToast(getActivity(), "회원 가입일을 입력하세요");
            return;
        }

        String url;
        if(screenMode == MODE_REG) {
            url = Constant.HOST + Constant.API_111;
        }
        else {
            url = Constant.HOST + Constant.API_112;
        }

        Log.d("LDK", "url:" + url);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pn", Util.getMdn(getActivity())); //전화번호
        params.put("paid", Util.getAndroidId(getActivity())); //안드로이드 아이디
        params.put("token", PreferenceUtil.getInstance(getActivity()).getToken()); //폰모델
        params.put("m_id", mMoimVo.getM_id());
        params.put("mb_name", mb_name.getText().toString());
        params.put("mb_position", ((PositionVo)mb_position.getSelectedItem()).getPo_cd());
        params.put("mb_pn", mb_pn.getText().toString());
        params.put("mb_add", mb_add.getText().toString());
        params.put("mb_enter_ymd", mb_enter_ymd.getText().toString());
        params.put("mb_actions", mb_actions.getText().toString());
        if(screenMode == MODE_MODIFY) {
            params.put("mb_id", mMemberVo.getMb_id());
        }
        Log.d("LDK", params.toString());

        mAq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
            try {
                if (status.getCode() != 200) {
                    Log.d("LDK", "status:" + status.getCode());
                    Util.showToast(getActivity(), "서버 오류가 발생하였습니다.");
                    return;
                }
                Log.d("LDK", object.toString(1));
                //데이터 존재하지 않음
                if (object.getInt("result") == 0) {
                    if(screenMode == MODE_REG) {
                        Util.showToast(getActivity(), "등록 되었습니다");
                    }
                    else {
                        Util.showToast(getActivity(), "수정 되었습니다");
                    }
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.container, new Fragment110(mMoimVo)).commitAllowingStateLoss();
                } else {
                    Util.showToast(getActivity(), object.getString("scname_msg"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        });
    }

    @OnClick(R.id.mb_enter_ymd) void enter_ymd() {
        final GregorianCalendar calendar = new GregorianCalendar();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mb_enter_ymd.setText(String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth));
        }
    };

    public void showMenu(int selected) {
        mMenu1.setSelected(false);
        mMenu2.setSelected(false);
        //mMenu3.setSelected(false);
        switch(selected) {
            case 1:
                mMenu1.setSelected(true);
                break;
            case 2:
                mMenu2.setSelected(true);
                break;
        }
    }

    View.OnClickListener mMenuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            KangApplication.sApp.soundButton();
            mMenu1.setSelected(false);
            mMenu2.setSelected(false);
            //mMenu3.setSelected(false);

            Fragment mFragment;

            switch(v.getId()) {
                case R.id.menu1:
                    mMenu1.setSelected(true);
                    mFragment = new Fragment110(mMoimVo);
                    getFragmentManager().beginTransaction().replace(R.id.container, mFragment).commitAllowingStateLoss();
                    break;
//                case R.id.menu2:
//                    mMenu2.setSelected(true);
//                    mFragment = new Fragment200();
//                    mFm.beginTransaction().replace(R.id.container, mFragment).commitAllowingStateLoss();
//                    break;
//                case R.id.menu3:
//                    mMenu3.setSelected(true);
//                    break;
                default:
                    break;
            }
        }
    };


}
