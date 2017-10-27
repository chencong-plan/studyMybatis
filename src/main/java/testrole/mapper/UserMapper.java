package testrole.mapper;

import org.apache.ibatis.annotations.Param;
import testrole.model.entity.SysRole;
import testrole.model.entity.SysUser;

import java.util.List;

/**
 * @author chencong , Created in 2017/10/26 15:52
 */
public interface UserMapper {

    /**
     * 通过id查询用户
     * @param id 将要查询的id
     * @return  返回该用户实体
     */
    SysUser selectById(Long id);


    /**
     * 查询全部用户信息
     * @return 返回全部用户list集合
     */
    List<SysUser> selectAll();

    /**
     * 通过用户id获取该用户拥有的所有权限
     * @param userId  将要查询的用户id
     * @return  返回该用户的权限集合
     */
    List<SysRole> selectRolesByUserId(Long userId);


    /**
     * 新增用户
     * @param sysUser 将要添加的用户实体
     * @return 返回受影响的行数
     */
    int insert(SysUser sysUser);

    /**
     * 新增用户信息 返回插入数据的主键
     * @param sysUser 将要添加的用户实体
     * @return 返回插入数据库记录的主键
     */
    int insert2(SysUser sysUser);

    /**
     * 新增用户---使用selectkey的方式返回新增数据的主键
     *
     * @param sysUser 将要插入数据的实体对象
     * @return 返回该数据在数据库当中的主键
     */
    int insert3(SysUser sysUser);

    /**
     * 根据主键更新用户信息
     *
     * @param sysUser 将要进行更新的实例对象
     * @return 返回受影响的行数
     */
    int updateById(SysUser sysUser);

    /**
     * 通过主键id删除数据
     *
     * @param id 将要删除数据的主键第
     * @return 返回手影响的行数
     */
    int deleteById(Long id);

    /**
     * 提供第二种根据对象进行删除的方法
     *
     * @param user 将要删除的对象
     * @return 返回此次操作影响的行数
     */
    int deleteById(SysUser user);

    /**
     * 根据用户id和角色的enabled状态获取用户的角色
     *
     * @param userId  将要查找的用户id
     * @param enabled 该用户的enabled状态
     * @return 返回list集合
     */
    List<SysRole> selectRolesByUserIdAndRoleEnabled(@Param("userId") Long userId, @Param("enabled") Integer enabled);

    /**
     * @param user
     * @param role
     * @return
     */
    List<SysRole> selectRolesByUserAndRole(@Param("user") SysUser user, @Param("role") SysRole role);
}
