import com.cn.fastcab.JwtApplication;
import com.cn.fastcab.controller.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/12/10 0010
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JwtApplication.class)
public class test {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void name() {
        Map<String, Object> map = jwtTokenUtil.generateToken("17877186956");
        System.out.println(map);
    }
}
