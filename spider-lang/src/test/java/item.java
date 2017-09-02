/**
 * Created by root on 2017/8/22.
 */
public class item {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "item{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
