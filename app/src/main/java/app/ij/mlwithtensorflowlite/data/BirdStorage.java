package app.ij.mlwithtensorflowlite.data;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BirdStorage {

    private static final String BIRD_PREFS = "bird_prefs";
    private static final String KEY_BIRDS = "birds";

    public static List<Bird> getAllBirds(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BIRD_PREFS, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_BIRDS, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<Bird>>(){}.getType();
            return new Gson().fromJson(json, listType);
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveBird(Context context, Bird bird) {
        List<Bird> birds = getAllBirds(context);
        birds.add(bird);
        saveBirds(context, birds);
    }

    public static void removeBird(Context context, int birdId) {
        List<Bird> birds = getAllBirds(context);
        for (int i = 0; i < birds.size(); i++) {
            if (birds.get(i).getId() == birdId) {
                birds.remove(i);
                break;
            }
        }
        saveBirds(context, birds);
    }

    private static void saveBirds(Context context, List<Bird> birds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BIRD_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(birds);
        editor.putString(KEY_BIRDS, json);
        editor.apply();
    }
}
