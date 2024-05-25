package app.ij.mlwithtensorflowlite.data;
import java.util.Comparator;
import java.util.List;

import app.ij.mlwithtensorflowlite.data.Bird;

public class BirdImageComparator implements Comparator<Bird> {
    @Override
    public int compare(Bird bird1, Bird bird2) {
        // Sort birds with images first
        boolean hasImage1 = bird1.getImages() != null && !bird1.getImages().isEmpty();
        boolean hasImage2 = bird2.getImages() != null && !bird2.getImages().isEmpty();

        // If both birds have images or both birds don't have images, preserve the original order
        if (hasImage1 == hasImage2) {
            return 0;
        } else if (hasImage1) {
            return -1; // bird1 comes first
        } else {
            return 1; // bird2 comes first
        }
    }
}

