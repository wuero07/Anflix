package com.example.anflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DataCaptureDialog extends DialogFragment {

    private EditText etName, etAge;
    private RadioGroup rgGender;
    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_data_capture, container, false);

        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        rgGender = view.findViewById(R.id.rgGender);
        btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveData());

        return view;
    }

    private void saveData() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr) || selectedGenderId == -1) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Por favor, complete todos los campos.")
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        RadioButton selectedGender = rgGender.findViewById(selectedGenderId);
        String gender = selectedGender.getText().toString();


        String userKey = "user_" + System.currentTimeMillis();


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userKey + "_name", name);
        editor.putInt(userKey + "_age", age);
        editor.putString(userKey + "_gender", gender);
        editor.putString("current_user_key", userKey);
        editor.apply();

        dismiss();

        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
