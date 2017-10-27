package testrole.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import testrole.model.entity.SysRole;
import testrole.model.entity.SysUser;

import java.util.Date;
import java.util.List;

/**
 * @author chencong , Created in 2017/10/26 17:09
 */
public class UserMapperTest extends BaseMapperTest {

    @Test
    public void testSelectById() {
        //获取SqlSession对象
        SqlSession sqlSession = getSqlSession();
        try {
            //获取一个UserMapper的接口
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 调用接口当中selectById()方法 查询id=1 的用户
            SysUser sysUser = userMapper.selectById(1l);
            // 查询结果sysUser 不为空
            Assert.assertNotNull(sysUser);
            // userName = admin
            Assert.assertEquals("admin", sysUser.getUserName());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectAll() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> userList = userMapper.selectAll();
            Assert.assertNotNull(userList);  // 判断对象不为空
            Assert.assertTrue(userList.size() > 0);  // 判断结果userList大于0
            for (SysUser user : userList) {
                System.out.println(user);
            }
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectRolesByUserId() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysRole> roleList = userMapper.selectRolesByUserId(1l);
            Assert.assertNotNull(roleList);
            // Assert.assertTrue(roleList.size() > 0);
            for (SysRole sysRole : roleList) {
                System.out.println(sysRole);
            }
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = getSqlSession();
        SysUser user = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //创建一个user对象
            user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@ccoder.cc");
            user.setUserInfo("test info");
            //正常情况下应该放入一张图片在headImg中
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());
            //将新建对象插入数据库当中,返回result是受影响行数
            // int result = userMapper.insert(user);
            int result = userMapper.insert(user);  // 这里的insert2方法是指定返回主键的方法
            Assert.assertEquals(1, result);  // 直插入一行数据
            Assert.assertNotNull(user.getId()); // id 为null 没有给id赋值 并没有配置会写id的值

        } finally {
            //为了不影响其他测试 这里选择回滚
            // 因为默认的SQLSession.openSession 是不自动提交的 因此不手动执行commit也是不会提交到数据库当中的,只是存在于session缓存当中
            sqlSession.rollback();
            sqlSession.close();
            //   System.out.println(user);
        }
        System.out.println(user);
    }

    @Test
    public void testUpdateById() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //从数据库当中查询一个user对象
            SysUser user = userMapper.selectById(1L);
            Assert.assertEquals("admin", user.getUserName());
            //修改用户名
            user.setUserName("admin_test");
            // 修改邮箱
            user.setUserEmail("test@ccoder.cc");
            // 更新数据 这一返回的值是受影响的行数
            int result = userMapper.updateById(user);
            Assert.assertEquals(1, result);  // 只更新了一条数据
            Assert.assertEquals("admin_test", userMapper.selectById(1L).getUserName());  // 更新之后的名字是“admin_test”
            System.out.println(user);
        } finally {
            // 不影响其他的测试 这里选择进行回滚
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testDeleteById() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 从数据库当中查询对象 根据 id = 1
            SysUser user = userMapper.selectById(1l);
            Assert.assertNotNull(user);  // 第一次查询 查询到了 不为null
            //调用方法进行删除该对象
            //Assert.assertEquals(1,userMapper.deleteById(user.getId()));
            // 使用deleteById重载的方法进行测试删除
            Assert.assertEquals(1, userMapper.deleteById(user));
            //  第二次查询 已经删除了 为null
            Assert.assertNull(userMapper.selectById(1l));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSelectRolesByUserIdAndRoleEnabled() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 调用方法selectRolesByUserIdAndRoleEnabled 查询用户的角色
            //List<SysRole> userList = userMapper.selectRolesByUserIdAndRoleEnabled(1L, 1);
            SysUser user = new SysUser();
            user.setId(1l);
            SysRole role = new SysRole();
            role.setEnabled(1);
            List<SysRole> roleList = userMapper.selectRolesByUserAndRole(user, role);
            // Assert.assertNotNull(userList);  // 查询结果不为空
            //ssert.assertTrue(userList.size() > 0);
            for (SysRole sysRole : roleList) {
                System.out.println(sysRole);
            }
        } finally {
            sqlSession.close();
        }
    }


}
