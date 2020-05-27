package io.lrcores;


import io.lrcores.common.utils.RedisUtils;
import io.lrcores.modules.sys.dao.SysUserDao;
import io.lrcores.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void contextLoads() {
        SysUserEntity user = new SysUserEntity();
        user.setEmail("123456@qq.com");
        redisUtils.set("user", user);

        System.out.println(ToStringBuilder.reflectionToString(redisUtils.get("user", SysUserEntity.class)));
    }


    @Autowired
    SysUserDao sysUserDao;
    @Test
    public void testAddUser() {
        SysUserEntity user = new SysUserEntity();
        user.setEmail("1234523426@qq.com");
        user.setUsername("zha344ddfddn44ksl");
        sysUserDao.insert(user);

    }


}
