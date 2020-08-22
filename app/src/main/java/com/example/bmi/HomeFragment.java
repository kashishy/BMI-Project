package com.example.bmi;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.zip.DeflaterOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private View view;
    //private Spinner activitySpinner;
    //private TextView bmrText, totalEnergyText, perdayCalText, todayCalText, activityLevelText;
    private EditText genderText, ageText, weightText, heightText;
    private EditText bmrText, totalEnergyText, perdayCalText, activityLevelText, todayCalText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDataReference, perdayCalorieReference, todayCalorieReference;// activityLevelReference;
    private int selectedItem = -1;
    private static String TAG = "Home Fragment : ";
    private String gender, perdayCalorie, todayCalorie, activityLevel;
    private double weight, height;
    private int age;
    private ProgressBar progressBar;
    private ImageView imageSuggestion;
    private TextView headingSuggestion, textSuggestion;
    private String[] levels = {"No Exercise", "Light Exercise", "Moderate Exercise", "Heavy Exercise", "Very Heavy Exercise"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Initialised();

        userDataReference.keepSynced(true);
        //perdayCalorieReference.keepSynced(true);
        //todayCalorieReference.keepSynced(true);

        RetrieveData();

    }

    public void Initialised() {

        activityLevelText = view.findViewById(R.id.activityLevel_home_frag_id);
        bmrText = view.findViewById(R.id.bmr_home_frag_id);
        totalEnergyText = view.findViewById(R.id.totalenergy_home_frag_id);
        progressBar = view.findViewById(R.id.progressBar_home_frag_id);
        genderText = view.findViewById(R.id.gender_home_frag_id);
        ageText = view.findViewById(R.id.age_home_frag_id);
        weightText = view.findViewById(R.id.weight_home_frag_id);
        heightText = view.findViewById(R.id.height_home_frag_id);
        perdayCalText = view.findViewById(R.id.calIntake_home_frag_id);
        todayCalText = view.findViewById(R.id.todayCalIntake_home_frag_id);
        imageSuggestion = view.findViewById(R.id.imageSuggestion_id);
        headingSuggestion = view.findViewById(R.id.headingSuggestion_id);
        textSuggestion = view.findViewById(R.id.textSuggestion_id);

        firebaseAuth = FirebaseAuth.getInstance();
        userDataReference = FirebaseDatabase.getInstance().getReference("users_data").child(firebaseAuth.getCurrentUser().getUid());
        perdayCalorieReference = FirebaseDatabase.getInstance().getReference("perday_calorie");
        todayCalorieReference = FirebaseDatabase.getInstance().getReference("today_calorie");
        //activityLevelReference = FirebaseDatabase.getInstance().getReference("activity_level");
    }

    public void RetrieveData()
    {
        progressBar.setVisibility(View.VISIBLE);

        /*activityLevelReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Log.d(TAG, "In activity level");
                    //activityLevel = dataSnapshot.child("activity").getValue().toString().trim();
                    selectedItem = Integer.parseInt(dataSnapshot.child("activity").getValue().toString().trim());
                    //String[] levels = getResources().getStringArray(R.array.activity_array);
                    activityLevel = levels[selectedItem];
                }
                else
                {
                    Log.d(TAG, "In activity level not present");
                    activityLevel = "Not Recorded";
                }
                activityLevelText.setText(activityLevel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    gender = dataSnapshot.child("gender").getValue().toString().trim();
                    weight = Double.parseDouble(dataSnapshot.child("weight").getValue().toString().trim());
                    height = Double.parseDouble(dataSnapshot.child("height").getValue().toString().trim());
                    age = Integer.parseInt(dataSnapshot.child("age").getValue().toString().trim());
                    activityLevel = dataSnapshot.child("activity").getValue().toString().trim();
                    if (activityLevel.equals("Select")) {
                        selectedItem = -1;
                    } else if(activityLevel.equals("No Exercise")) {
                        selectedItem = 0;
                    } else if (activityLevel.equals("Light Exercise")){
                        selectedItem = 1;
                    }else if (activityLevel.equals("Moderate Exercise")){
                        selectedItem = 2;
                    }else if (activityLevel.equals("Heavy Exercise")){
                        selectedItem = 3;
                    }else{
                        selectedItem = 4;
                    }
                    Log.d(TAG + "Gender  ", gender);
                    Log.d(TAG + "Weight  ", Double.toString(weight));
                    Log.d(TAG + "Height  ", Double.toString(height));
                    Log.d(TAG + "Age  ", Integer.toString(age));

                    double bmr = calculateBMR(gender, weight, height, age);
                    double calorie = calorieNeed(bmr);
                    double hei = height/100;
                    hei = hei*hei;
                    double bmi = weight/hei;
                    UpdateUI(gender, age, weight, height, activityLevel, bmr, calorie, bmi);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "No data found for this user", Toast.LENGTH_SHORT).show();
            }
        });

        perdayCalorieReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Log.d(TAG, "In perday calorie intake");
                    perdayCalorie = dataSnapshot.child("calorie").getValue().toString().trim() + " kcal";
                }
                else
                {
                    Log.d(TAG, "In perday calorie intake not present");
                    perdayCalorie = "Not Recorded";
                }
                perdayCalText.setText(perdayCalorie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        todayCalorieReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    todayCalorie = dataSnapshot.child("calorie").getValue().toString().trim() + " kcal";
                }
                else
                {
                    todayCalorie = "Not Recorded";
                }
                todayCalText.setText(todayCalorie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public double calculateBMR(String gender, double weight, double height, int age)
    {
        //Toast.makeText(getContext(), "Before BMR Calculate", Toast.LENGTH_SHORT).show();

        double bmr = 0.0;
        //Log.d(TAG + "BMR After ", Double.toString(bmr));

        /*Log.d(TAG + "Gender : ", gender);
        Log.d(TAG + "Weight : ", Double.toString(weight));
        Log.d(TAG + "Height : ", Double.toString(height));
        Log.d(TAG + "Age : ", Integer.toString(age));*/

        bmr = (10* weight) + (6.25* height) - (5* age);
        if(gender.equals("Male"))
        {
            bmr = bmr - 5;
        }
        else if(gender.equals("Female"))
        {
            bmr = bmr - 161;
        }
        //Toast.makeText(getContext(), "After BMR Calculate "+ bmr, Toast.LENGTH_SHORT).show();
        return bmr;
    }

    public double calorieNeed(double bmr)
    {
        //Toast.makeText(getContext(), "Before Calorie Calculate "+selectedItem, Toast.LENGTH_SHORT).show();
        double activityFactor = 0.0;

        if(selectedItem == -1)
        {
            return 0.0;
        }
        else
        {
            if(selectedItem == 0)
            {
                activityFactor = 1.2;
            }
            else if(selectedItem == 1)
            {
                activityFactor = 1.375;
            }
            else if(selectedItem == 2)
            {
                activityFactor = 1.55;
            }
            else if(selectedItem == 3)
            {
                activityFactor = 1.725;
            }
            else if(selectedItem == 4)
            {
                activityFactor = 1.9;
            }

        }
        double calorie = bmr * activityFactor;
        //Toast.makeText(getContext(), "After BMR Calculate "+ calorie, Toast.LENGTH_SHORT).show();
        return calorie;
    }

    public void UpdateUI(String gender, int age, double weight, double height, String activityLevel, double bmr, double calorie, double bmi)
    {
        //Toast.makeText(getContext(), "Before Update UI", Toast.LENGTH_SHORT).show();
        genderText.setText(gender);
        ageText.setText(String.format("%1$s",age));
        weightText.setText(String.format("%1$sKg", weight));
        heightText.setText(String.format("%1$scm",height));
        activityLevelText.setText(activityLevel);
        bmrText.setText(Double.toString(bmr));
        if(calorie == 0)
        {
            totalEnergyText.setText("Not Recorded");
        }
        else
        {
            totalEnergyText.setText(String.format("%1$s kcal/day", calorie));
        }
        if (bmi<=18.5)
        {
            imageSuggestion.setImageResource(R.drawable.underweight);
            headingSuggestion.setText(Html.fromHtml("<h5>Underweight</h5>"));
            textSuggestion.setText(Html.fromHtml("<h5>Some key components of a diet for weight gain may include:</h5><ol type=\"1\"><li><b>Adding snacks:</b>High-protein and whole-grain carbohydrate snacks can help a person gain weight. Examples include peanut butter crackers, protein bars, trail mix, pita chips and hummus, or a handful of almonds.</li><li><b>Eating several small meals a day:</b>Sometimes a person may be underweight because they cannot tolerate eating large meals. Instead, a person can eat several small meals throughout the day.</li><li><b>Incorporating additional foods:</b>A person can add calorie-dense food sources to their existing diet, such as putting slivered almonds on top of cereal or yogurt, sunflower or chia seeds on a salad or soup, or nut butter on whole-grain toast.</li><li><b>Avoiding empty calories:</b>Eating high-calorie foods may cause a person to gain weight, but they also have excess fats that could affect a person's heart and blood vessels. A person should avoid foods that are high in sugar and salt.</li></ol>"));
        }
        else if (bmi>18.5 && bmi<25)
        {
            imageSuggestion.setImageResource(R.drawable.healthy);
            headingSuggestion.setText(Html.fromHtml("<h5>Healthy</h5>"));
            textSuggestion.setText(Html.fromHtml("<h5>For adults:</h5>A healthy diet includes the following:<ol type=\"1\"><li>Fruit, vegetables, legumes (e.g. lentils and beans), nuts and whole grains (e.g. unprocessed maize, millet, oats, wheat and brown rice).</li><li>At least 400 g (i.e. five portions) of fruit and vegetables per day, excluding potatoes, sweet potatoes, cassava and other starchy roots.</li><li>Less than 10% of total energy intake from free sugars, which is equivalent to 50 g (or about 12 level teaspoons) for a person of healthy body weight consuming about 2000 calories per day, but ideally is less than 5% of total energy intake for additional health benefits. Free sugars are all sugars added to foods or drinks by the manufacturer, cook or consumer, as well as sugars naturally present in honey, syrups, fruit juices and fruit juice concentrates.</li><li>Less than 30% of total energy intake from fats. Unsaturated fats (found in fish, avocado and nuts, and in sunflower, soybean, canola and olive oils) are preferable to saturated fats (found in fatty meat, butter, palm and coconut oil, cream, cheese, ghee and lard) and trans-fats of all kinds, including both industrially-produced trans-fats (found in baked and fried foods, and pre-packaged snacks and foods, such as frozen pizza, pies, cookies, biscuits, wafers, and cooking oils and spreads) and ruminant trans-fats (found in meat and dairy foods from ruminant animals, such as cows, sheep, goats and camels). It is suggested that the intake of saturated fats be reduced to less than 10% of total energy intake and trans-fats to less than 1% of total energy intake. In particular, industrially-produced trans-fats are not part of a healthy diet and should be avoided.</li><li>Less than 5  g of salt (equivalent to about one teaspoon) per day. Salt should be iodized.</li>\n" +
                    "</ol><h5>For infants and young children:</h5><ol type=\"1\"><li>In the first 2 years of a child’s life, optimal nutrition fosters healthy growth and improves cognitive development. It also reduces the risk of becoming overweight or obese and developing NCDs later in life.</li><li><h6>Advice on a healthy diet for infants and children is similar to that for adults, but the following elements are also important:</h6><ol type=\"i\"><li>Infants should be breastfed exclusively during the first 6 months of life.</li><li>Infants should be breastfed continuously until 2 years of age and beyond.</li><li>From 6 months of age, breast milk should be complemented with a variety of adequate, safe and nutrient-dense foods. Salt and sugars should not be added to complementary foods.</li></ol></li></ol>"));
        }
        else {
            imageSuggestion.setImageResource(R.drawable.overweight);
            headingSuggestion.setText(Html.fromHtml("<h5>Overweight</h5>"));
            textSuggestion.setText(Html.fromHtml("<h4>Some key components of a diet for weight loss may include:</h4><ol type=\"1\"><li><b>Low-Calorie Diet:</b> The most established way for an obese person to lose weight is by focusing on diet and cutting calories. According to the National Heart, Lung and Blood Institute, you should cut your caloric intake by about 500 to 1,000 calories a day to lose one to two pounds a week. Even if you’re eager to shed your obese status, resist the temptation to cut back on calories drastically. Generally speaking, women can lose weight safely by consuming 1,000 to 1,200 calories a day, and men should aim for 1,200 to 1,600 calories a day. However, these calorie ranges are just a guide; speak to a doctor about the best way for you to lose weight. Diets of fewer than 800 calories a day require doctor supervision. A healthy low-calorie diet plan avoids saturated and trans fats, cholesterol, too much sodium and added sugars. It includes low-fat dairy products; lean proteins, such as fish, poultry and beans; whole grains; and fruits and vegetables.</li><li><b>Low-Carbohydrate Diet:</b> If counting calories sounds like an obstacle to losing weight, a low-carbohydrate diet plan might work better for you. Research published in 2003 in “The New England Journal of Medicine” found that a low-carb diet created more weight loss in obese participants than a conventional diet during the first six months -- though the differences were the same after one year. Additionally, a study published in the “Annals of Internal Medicine” in 2004 found that a low-carb diet plan led to greater weight loss and improved triglyceride and cholesterol levels in obese participants. On a low-carb diet plan, you restrict carbohydrates -- particularly high-glycemic varieties that affect your blood sugar -- in favor of eating more protein and fat. While eating this way, you’ll avoid grains such as bread, pasta, rice and oats; some high-sugar fruits; root vegetables; and foods with added sugar, such as candy, ice cream and desserts.</li><li><b>Portion Sizes:</b> No matter which diet plan you choose, losing a significant amount of weight requires a keen eye on determining and adhering to proper portion sizes. Cut back on portions to eat less food and balance your caloric intake. Start by weighing and measuring everything you eat, registered dietitian LuAnn Berry says on Health.com. Other tricks include using a smaller plate for your meals, because it holds less food, and ensuring that you read the nutrition label for serving sizes and adjust your consumption as necessary.</li><li><b>Combine Diet with Exercise:</b> Although diet is a vital part of losing weight, a study published in 2003 in the “Journal of the American Medical Association” found that a lifestyle overhaul consisting of both a healthy diet plan and physical activity was the most successful approach to losing weight for obese patients. The American College of Sports Medicine says an obese person should focus on low-intensity aerobic activity with the goal of increasing duration and frequency, rather than intensity. It recommends four to five days of exercise for 30 to 60 minutes; break these up into three 10-minute sessions if you were previously sedentary. Appropriate activities include walking, swimming or cycling, all of which put minimal stress on the joints.</li></ol>"));
        }
        //Toast.makeText(getContext(), "After Update UI", Toast.LENGTH_SHORT).show();
    }
}
