package com.example.anflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserSelectionDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_selection, container, false);

        ListView lvUsers = view.findViewById(R.id.lvUsers);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        Map<String, ?> allEntries = sharedPreferences.getAll();
        List<String> userNames = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().endsWith("_name")) {
                userNames.add(entry.getValue().toString());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userNames);
        lvUsers.setAdapter(adapter);

        lvUsers.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedName = userNames.get(position);
            String userKey = null;


            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (entry.getValue().equals(selectedName) && entry.getKey().endsWith("_name")) {
                    userKey = entry.getKey().replace("_name", "");
                    break;
                }
            }

            if (userKey != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("current_user_key", userKey);
                editor.apply();
                dismiss();
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
