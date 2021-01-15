package com.example.kuba.keepitfoody;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Fragment used to display calendar. After clicking on a specific date
 * a user is being redirected to either nutrition calculator or shopping
 * list depending on the flag parameter.
 */
public class CalendarViewFragment extends Fragment {

    private OnCalendarViewInteractionListener listener;
    private CalendarView calendarView;
    private FoodyDatabaseHelper helper;
    private String flag;

    // Required empty public constructor
    public CalendarViewFragment() {}

    /**
     * Creates new instance of CalendarViewFragment class
     * with parameters.
     * @param flag
     * @return CalendarViewFragment
     */
    public static CalendarViewFragment newInstance(String flag) {
        CalendarViewFragment fragment = new CalendarViewFragment();
        Bundle args = new Bundle();
        args.putString(MenuActivity.FLAG, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.flag = getArguments().getString(MenuActivity.FLAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        helper = FoodyDatabaseHelper.getInstance(getContext());
        calendarView = view.findViewById(R.id.calendar);
        calendarView.setOnDayClickListener(eventDay -> {
            Calendar calendar = eventDay.getCalendar();
            String date = "";
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            date += year + "-";

            if (month < 10) {
                date += "0" + month + "-";
            } else {
                date += month + "-";
            }

            if (day < 10) {
                date += "0" + day;
            } else {
                date += day + "";
            }

            switch (flag) {
                case MenuActivity.FLAG_CALCULATOR:
                    onDayClicked(date);
                    break;
                case MenuActivity.FLAG_SHOPPING_LIST:
                    onDayClicked2(date);
                    break;
            }

        });

        // Setup Calendar
        setEventDays();

        return view;
    }

    /**
     * Method used to communicate with MenuActivity in order
     * to open nutrition calculator.
     * @param date
     */
    public void onDayClicked(String date) {
        if (listener != null) {
            listener.onCalendarViewInteraction(date);
        }
    }

    /**
     * Method used to communicate with MenuActivity in order
     * to open shopping list.
     * @param date
     */
    public void onDayClicked2(String date) {
        ArrayList<Meal> meals = helper.fetchMeals(date);

        if (meals.isEmpty()) {
            Toast.makeText(getContext(), "Brak zaplanowanych posiłków.", Toast.LENGTH_SHORT).show();
        } else {
            if (listener != null) {
                listener.onCalendarViewInteraction2(date);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnCalendarViewInteractionListener) {
            listener = (OnCalendarViewInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow communication between MenuActivity and potentially
     * other fragments contained in that activity.
     */
    public interface OnCalendarViewInteractionListener {
        void onCalendarViewInteraction(String date);
        void onCalendarViewInteraction2(String date);
    }

    /**
     * This method is called to fill calendar with events
     * that represent planned meals for given day.
     */
    private void setEventDays() {
        List<EventDay> events = new ArrayList<>();

        Cursor cursor = FoodyDatabaseHelper.getInstance(getContext()).getAllDays();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(FoodyDatabaseHelper.MEALS_COLUMN_DATE));
            String[] buffer = date.split("-");
            int year = Integer.valueOf(buffer[0]);
            int month = Integer.valueOf(buffer[1]);
            int day = Integer.valueOf(buffer[2]);
            long dateToMillis = new DateTime(year, month, day, 0, 0, DateTimeZone.UTC).getMillis();

            Date formattedDate = new Date(dateToMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);

            EventDay event = new EventDay(calendar, R.drawable.ic_vector_meal);
            events.add(event);
        }

        calendarView.setEvents(events);
    }

}
