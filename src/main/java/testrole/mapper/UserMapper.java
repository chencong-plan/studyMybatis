package testrole.mapper;

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
}
