package com.example.kuba.keepitfoody;

import com.example.kuba.keepitfoody.DailyMealsFragment.*;
import com.squareup.picasso.Picasso;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Fragment is used to display currently logged in user's
 * personal information and enable it's modification.
 */
public class UserProfileFragment extends Fragment implements
        MenuItem.OnActionExpandListener {

    private User user;
    private ActivityFactorAdapter activityFactorAdapter;
    private Uri imageUri;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private ImageView imageViewProfilePicture;
    private EditText editName;
    private EditText editEmail;
    private TextView editDate;
    private EditText editWeight;
    private EditText editHeight;
    private RadioButton radioButtonFemale;
    private RadioButton radioButtonMale;
    private Spinner spinner;

    private OnUserProfileInteractionListener listener;

    public static UserProfileFragment newInstance(User user, String imageUri) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(MenuActivity.USER, user);
        args.putString(MenuActivity.IMAGE, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            this.user = getArguments().getParcelable(MenuActivity.USER);
            this.imageUri = Uri.parse(getArguments().getString(MenuActivity.IMAGE));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editDate = view.findViewById(R.id.editDate);
        editWeight = view.findViewById(R.id.editWeight);
        editHeight = view.findViewById(R.id.editHeight);
        radioButtonFemale =  view.findViewById(R.id.radioButtonFemale);
        radioButtonMale =  view.findViewById(R.id.radioButtonMale);
        spinner = view.findViewById(R.id.spinner);
        imageViewProfilePicture = view.findViewById(R.id.imageViewProfilePicture);

        User user = FoodyDatabaseHelper.getInstance(getContext()).fetchUser();

        if (imageUri.toString().isEmpty()) {
            if (!user.getProfilePicture().isEmpty() && !user.getProfilePicture().equals("null")) {
                String url = "https://192.168.134.62/api" + user.getProfilePicture();
                Picasso.with(getContext()).load(url).fit().centerInside().into(imageViewProfilePicture);
            }
        } else {
            imageViewProfilePicture.setImageURI(imageUri);
        }

        imageViewProfilePicture.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Otwieram galerię...", Toast.LENGTH_SHORT).show();
            openGallery();
        });

        editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    getActivity(),
                    //android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    onDateSetListener,
                    year,
                    month,
                    day);

            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        onDateSetListener = (view1, year, month, dayOfMonth) -> {
            String date;
            month += 1;

            if(month < 10) {
                date = year + "-0" + month + "-" + dayOfMonth;
            }
            else {
                date = year + "-" + month + "-" + dayOfMonth;
            }

            this.user.setDateOfBirth(date);
            editDate.setText(DailyMealsFragment.formatDate(date));
        };

        // Apply profile info on fragment
        setProfile();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserProfileInteractionListener) {
            listener = (OnUserProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveAction:
                if (validate()) {
                    saveProfileInfo();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    /**
     * Load a collection of ActivityFactor type objects.
     * @return
     */
    private ArrayList<ActivityFactor> getActivityFactors() {
        ArrayList<ActivityFactor> activityFactors = new ArrayList<>();
        activityFactors.add(new ActivityFactor(0,"Leżący stan chorobowy", 1.2, 1.2));
        activityFactors.add(new ActivityFactor(1, "Bardzo lekka aktywność", 1.3, 1.3));
        activityFactors.add(new ActivityFactor(2, "Lekka aktywność", 1.6, 1.5));
        activityFactors.add(new ActivityFactor(3, "Średnia aktywność", 1.7, 1.6));
        activityFactors.add(new ActivityFactor(4, "Duża aktywność", 2.1, 1.9));
        activityFactors.add(new ActivityFactor(5, "Bardzo duża aktywność", 2.4, 2.2));
        return activityFactors;
    }

    /**
     * Prepare user data to display.
     */
    private void setProfile() {
        editName.setText(user.getName());
        editEmail.setText(user.getEmail());

        if (user.getDateOfBirth().equals("null")) {
            editDate.setText("RRRR-MM-DD");
        } else {
            editDate.setText(DailyMealsFragment.formatDate(user.getDateOfBirth()));
        }

        if (user.getWeight() == 0.0) {
            editWeight.setText("");
        } else {
            editWeight.setText(String.valueOf(user.getWeight()));
        }

        if (user.getHeight() == 0.0) {
            editHeight.setText("");
        } else {
            editHeight.setText(String.valueOf((int)user.getHeight()));
        }

        // ActivityFactor
        ArrayList<ActivityFactor> activityFactors = getActivityFactors();
        activityFactorAdapter = new ActivityFactorAdapter(getContext(), activityFactors);
        spinner.setAdapter(activityFactorAdapter);

        // Sex
        if(user.isSex()) { // Male
            radioButtonMale.setChecked(true);
            if (user.getActivityFactor() == 1.2) {
                spinner.setSelection(0);
            } else if (user.getActivityFactor() == 1.3) {
                spinner.setSelection(1);
            } else if (user.getActivityFactor() == 1.6) {
                spinner.setSelection(2);
            } else if (user.getActivityFactor() == 1.7) {
                spinner.setSelection(3);
            } else if (user.getActivityFactor() == 2.1) {
                spinner.setSelection(4);
            } else if (user.getActivityFactor() == 2.4) {
                spinner.setSelection(5);
            } else {
                spinner.setSelection(0);
            }
        }
        else { // Female
            radioButtonFemale.setChecked(true);
            if (user.getActivityFactor() == 1.2) {
                spinner.setSelection(0);
            } else if (user.getActivityFactor() == 1.3) {
                spinner.setSelection(1);
            } else if (user.getActivityFactor() == 1.5) {
                spinner.setSelection(2);
            } else if (user.getActivityFactor() == 1.6) {
                spinner.setSelection(3);
            } else if (user.getActivityFactor() == 1.9) {
                spinner.setSelection(4);
            } else if (user.getActivityFactor() == 2.2) {
                spinner.setSelection(5);
            } else {
                spinner.setSelection(0);
            }
        }

    }

    /**
     * Save changes to modified user and send to server.
     */
    private void saveProfileInfo() {
        // Save changes
        user.setName(editName.getText().toString());

        if (!editWeight.getText().toString().isEmpty()) {
            user.setWeight(Double.valueOf(editWeight.getText().toString()));
        }

        if (!editHeight.getText().toString().isEmpty()) {
            user.setHeight(Double.valueOf(editHeight.getText().toString()));
        }

        ActivityFactor factor = (ActivityFactor) spinner.getSelectedItem();

        if (radioButtonMale.isChecked()) { // Male
            user.setSex(true);
            user.setActivityFactor(factor.getMaleFactor());
        } else if (radioButtonFemale.isChecked()) { // Female
            user.setSex(false);
            user.setActivityFactor(factor.getFemaleFactor());
        }

        // Use interface to save changes
        sendUserBack(user);
    }

    /**
     * Validate text fields.
     * @return
     */
    private boolean validate() {

        if (editName.getText().toString().isEmpty()) {
            editName.setError("Imię nie może być puste.");
            return false;
        }

        if (editWeight.getText().toString().isEmpty()) {
            editWeight.setError("Waga nie może być pusta.");
            return false;
        }

        if (editHeight.getText().toString().isEmpty()) {
            editHeight.setError("Wzrost nie może być pusty.");
            return false;
        }

        if (user.getDateOfBirth().isEmpty() || user.getDateOfBirth().equals("null")) {
            Toast.makeText(getContext(), "Wypełnij datę urodzenia.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(gallery, MenuActivity.REQUEST_IMAGE);
    }

    /**
     * This interface must be implemented in main activity
     * in order to receive and send back USER type object.
     */
    public interface OnUserProfileInteractionListener {
        void onUserProfileInteraction(User user);
    }

    public void sendUserBack(User user) {
        if (listener != null) {
            listener.onUserProfileInteraction(user);
        }
    }
}
