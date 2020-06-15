import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.HashMap;

public class TestGson {

    @Test
    public void f(){
        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("name","曹操");
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(hashMap);
        System.out.println(s);
        String mxhdoit = gson.toJson(new Person("mxhdoit", 21, 178));
        System.out.println(mxhdoit);
    }
}

class Person{
    String name;
    Integer age;
    Integer height;

    public Person(String name, Integer age, Integer height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }
}
