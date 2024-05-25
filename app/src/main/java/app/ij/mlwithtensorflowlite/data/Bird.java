package app.ij.mlwithtensorflowlite.data;
import java.util.List;

public class Bird {
    private List<String> images;
    private String lengthMin;
    private String lengthMax;
    private String name;
    private String wingspanMin;
    private int id;
    private String wingspanMax;
    private String sciName;
    private List<String> region;
    private String family;
    private String order;
    private String status;
    private int total;
    private int page;
    private int pageSize;

    // Getters
    public List<String> getImages() {
        return images;
    }

    public String getLengthMin() {
        return lengthMin;
    }

    public String getLengthMax() {
        return lengthMax;
    }

    public String getName() {
        return name;
    }

    public String getWingspanMin() {
        return wingspanMin;
    }

    public int getId() {
        return id;
    }

    public String getWingspanMax() {
        return wingspanMax;
    }

    public String getSciName() {
        return sciName;
    }

    public List<String> getRegion() {
        return region;
    }

    public String getFamily() {
        return family;
    }

    public String getOrder() {
        return order;
    }

    public String getStatus() {
        return status;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}
