import com.xpu.dao.ImageDao;
import com.xpu.model.Image;
import org.junit.Test;

import java.util.List;

public class TestImageDao {

    @Test
    public void f1(){
        ImageDao dao = new ImageDao();
//        Image image = new Image();
//        image.setImageName("selfphoto");
//        image.setSize(100);
//        image.setContentType("image/png");
//        image.setPath("./data/1.png");
//        image.setUploadTime("20200216");
//        image.setMd5("1223344");
//        System.out.println(image);
//        dao.insert(image);
//
//        List<Image> images = dao.selectAll();
//        for (Image m:images){
//            System.out.println(m);
//        }
        Image image = dao.selectOne(1);
        System.out.println(image);
//        dao.deleteOne(1);
    }
}
