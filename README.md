### Mybatis学习笔记《Mybatis从入门到精通》
#### 1、namespace问题
> 在XxxMapper.xml的配置文件当中<mapper>根标签的namespace属性，当Mapper接口和xml文件进行关联的时候，命名空间namespace的值就需要配置成接口的全限定名称。
```java
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
}
```
>如上面的代码是userMapper的接口信息，包名为testrole.mapper，接口名是UserMapper，那么在UserMapper.xml配置文件的namespace中就需要这样配置其值了。包名+接口名
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="testrole.mapper.UserMapper">
</mapper>
```

#### 2、ResultMap和ResultType 
> 使用resultType和resultMap作为方法的返回值类型的不同。如果使用resultMap设置结果的映射，就需要在上面进行配置resultMap的内容。如下面所示：
```
<mapper>
<resultMap id="userMap" type="testrole.model.entity.SysUser">
    <id property="id" column="id"/>
    <result property="userName" column="user_name"/>
    <result property="userPassword" column="user_password"/>
    <result property="userEmail" column="user_email"/>
    <result property="userInfo" column="user_info"/>
    <result property="headImg" column="head_img" jdbcType="BLOB"/>
    <result property="createTime" column="create_time" jdbcType="TIMESTAMP"/>
</resultMap>

<select id="selectById" resultMap="userMap">
    SELECT * from sys_user where id = #{id}
</select>
</mapper>
```

> 如果使用resultType来设置返回值类型，需要在SQL语句当中为所有列名和属性名不一致的列设置别名，通过别名使最终查询结果列和resultType指定对象的属性名保持一致，从而实行自动映射。如下面代码所示：
```xml
<select id="selectAll" resultType="testrole.model.entity.SysUser">
select id ,
user_name userName ,
user_password userPassword,
user_email userEmail,
user_info userInfo ,
head_img headImg,
create_time createTime
from sys_user;
</select>
```
```java
// 所对应的SysUser实体类的属性如下
/**
 * @author chencong , Created in 2017/10/25 23:00
 */
public class SysUser {
    private Long id;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userInfo;
    private byte[] headImg;
    private Date createTime;
}
```
名称映射规则：
```markdown
通过设置resultMap当中的property属性和column属性的映射。
<result property="userName" column="user_name"/>
通过在SQL语句当中设置别名
user_name userName 
这两种方式都可以实现将查询列映射到对象属性的目的
注意：property属性或者别名要和对象属性的名字相同。但是在实际匹配过程当中，Mybatis会将两者都转换成大写形式，然后在判断是否相同。即property=“userName” 和 property=“username”都可以匹配到独享的userName属性上面。判断属性是否相同时候要使用USERNAME，因此设置property属性或者别名时候，不需要考虑大小写是否一致。但是为了方便阅读还是按照统一的命名规则来。
** 通过Mybatis的mapUnderscoreToCamelCase属性true，可以将下划线命名方式的数据库字段转换成java当中的驼峰命名。如下所示：

<!--设置Mybatis是否将数据库下划线命名转换成Java驼峰命名-->
<setting name="mapUnderscoreToCamelCase" value="true"/>

对应使用SQL别名方式就不需要指定别名为对象属性名了，如下所示：
<select id="selectAll" resultType="testrole.model.entity.SysUser">
    select id ,
        user_name ,
        user_password ,
        user_email ,
        user_info ,
        head_img ,
        create_time
    from sys_user;
</select>
```
>涉及到多表连接查询的时候，使用resultType解决方法：
 在对象的属性当中添加另外的对象作为属性，然后再select语句当中添加相应的对象的属性，具体代码如下所示：
```java
public class SysRole {

private Long id;
private String roleName;
private Integer enabled;
private Long createBy;
private Date createTime;

private SysUser user;  // 添加SysUser类的对象作为类SysRole的属性

// setter getter省略
}
```
>保证之后能够顺利输出user对象的属性值，在UserMapper.xml当中配置需要输出的user对象的属性值就行了，代码如下：
```xml
<select id="selectRolesByUserId" resultType="testrole.model.entity.SysRole">
select
    r.id,
    r.role_name ,
    r.enabled,
    r.create_by,
    r.create_time,
    u.user_name as "user.userName",
    u.user_password as "user.userPassword",
    u.user_email as "user.userEmail"
from sys_user u
    inner join sys_user_role ur on u.id = ur.user_id
    inner join sys_role r on ur.role_id = r.id    
where u.id = #{userId}
</select>
```
#### 3、数据库字段类型和映射文件中字段属性问题
>MySQL数据库字段（时间）和Java对象属性的对应关系
     数据库的datetime类型可以存储DATE（日期部分 2017-10-27 时间部分为00:00:00）和TIMESTAMP两种类型的时间，不能够存储TIME类型的时间。如果需要存储TIME类型的时间，就需要将数据库字段类型转换成time。
 >> datetime类型对应Java属性类型配置如下：
```markdown
#{createTime , jdbcType=DATE}   // 数据库当中createTime对应类型为datetime
2017-10-27(Date)
或者
#{createTime , jdbcType=TIMESTAMP} 
2017-10-27 10:42:38.251(Timestamp)

datetime类型字段使用time类型的属性，如下配置就会出现异常
#{createTime , jdbcType=TIME}
Cause: com.mysql.jdbc.MysqlDataTruncation: Data truncation: Incorrect datetime value: '10:44:02' for column 'create_time' at row 1  //如果出现这样的异常就需要检查自己的字段类型是否匹配正确

```
#### 4、受影响行数和返回自增主键
> 在执行insert语句时候会返回一个int类型的值，该值为受影响的行数。
```markdown
//将新建对象插入数据库当中,返回result是受影响行数
int result = userMapper.insert(user);

<insert id="insert">
    insert into sys_user(
    id,user_name,user_password,user_email,user_info,head_img ,create_time
) values(    
    #{id},#{userName},#{userPassword},#{userEmail},
    #{userInfo},
    #{headImg , jdbcType=BLOB},
    #{createTime , jdbcType=TIMESTAMP}
)
</insert>
```
>注意：默认情况下SqlSessionFactory.openSession()是不自动提交的，需要我们自己手动执行commit进行提交数据库。
 那么，既然执行的insert语句返回值为“受影响的行数”，使用mybatis的insert方法如何得到新插入数据的主键呢？
 下面提供两种方法来完成。
>> 4.2 使用JDBC方式返回主键自增的值。
    首先在UserMapper的接口中定义如下插入数据的接口：
```markdown
/**
  * 新增用户信息 返回插入数据的主键
  * @param sysUser 将要添加的用户实体
  * @return 返回插入数据库记录的主键
  */
int insert2(SysUser sysUser);
```
>> 相对应的UserMapper.xml文件当中配置的insert2的代码如下：
```xml
<insert id="insert2" useGeneratedKeys="true" keyProperty="id">
    insert into sys_user(
        user_name,user_password,user_email,user_info,head_img ,create_time
    ) values(
        #{userName},#{userPassword},#{userEmail},
        #{userInfo},
        #{headImg , jdbcType=BLOB},    
        #{createTime , jdbcType=TIMESTAMP}
    )
</insert>
```
>主要相对于之前的insert方法添加了useGeneratedKeys=true和keyProperty=id的配置。
 配置 useGeneratedKeys="true"  mybatis会使用JDBC的getGeneratedKeys方法来取出数据库内部生成的主键。
 配置 keyProperty="id" 会将上一步得到的主键赋值给keyProperty属性配置的id属性
 注意：如果要设置多个属性时，使用逗号隔开，这种情况下通常还需要设置keyColumn属性，按顺序指定数据库列，这列的值和keyProperty配置的属性一一对应。
 由于需要使用数据库返回的主键值，在上面的配置文件上下部分都去掉了id列和#{id}属性

>> 4.2 上面使用的返回主键的方式只适用于支持主键自增的数据库。oracle不提供自增主键的功能，而是使用序列得到一个值，然后再将这个值赋值给id，再将数据插入数据库。
           对于这样的情况就提供了<selectKey>标签来获取主键的值，这种方式不仅仅适用于不提供主键自增的数据库，还适用于提供主键自增功能的数据库。

```xml
<insert id="insert3">
    insert into sys_user(
    user_name , user_password ,user_email,user_info ,head_img,create_time
)values(
    #{userName} ,#{userPassword} ,#{userEmail},#{userInfo},
    #{headImg , jdbcType=BLOB},
    #{createTime ,jdbcType=TIMESTAMP}
)
    /*设置selectKey来获取插入数据的主键*/
    <selectKey keyColumn="id" resultType="long" keyProperty="id" order="AFTER">
        select LAST_INSERT_ID()
    </selectKey>
</insert>
```
  selectKey标签的keyColumn、keyProperty和上面的useGeneratedKeys的用法含义相同，这里的resultType用户设置返回值类型。order属性的设置和使用的数据库有关。
在MySQL数据库当中，order属性设置为AFTER，因为当前记录的主键值在insert语句执行成功后才能够获取到。
在Oracle数据库当中，order属性设置为BEFORE，因为Oracle中需要先从序列获取值，然后将值作为主键插入数据库当中。
>selectKey标签当中属性的介绍，可以看出selectKey当中是一个独立的SQL语句进行获取到当前数据库的主键。    
     下面介绍一些支持主键自增的数据库配置：

>
<table>
    <tr>
        <td>DB2</td>
        <td>VALUES IDENTITY_VAL_LOCAL( )</td>
    </tr>
    <tr>
        <td>MySQL</td>
        <td> SELECT LAST_INSERT_ID( )</td>
    </tr>
    <tr>
        <td> SQL SERVER </td>
        <td>SELECT SCOPE_IDENTITY( )</td>
    </tr>
    <tr>
        <td>CLOUDSCAPE</td>
        <td>VALUES IDENTITY_VAL_LOCAL( )</td>
    </tr>
    <tr>
        <td>DERBY</td>
        <td>VALUES IDENTITY_VAL_LOCAL( )</td>
    </tr>
    <tr>
        <td>DB2</td>
        <td>VALUES IDENTITY_VAL_LOCAL( )</td>
    </tr>
    <tr>
        <td>HSQLDB</td>
        <td>CALL IDENTITY( )</td>
    </tr>
    <tr>
        <td>SYBASE</td>
        <td>SELECT @@IDENTITY</td>
    </tr>
    <tr>
        <td>DB2_MF</td>
        <td>SELECT IDENTITY_VAL_LOCAL( ) FROM SYSIBM.SYSDUMMY1( )</td>
    </tr>
    <tr>
        <td>INFORMIX</td>
        <td>select dbinfo('sqlca.sqlerrdl') from systables where tabid = 1</td>
    </tr> 
</table>

#### 5、多个接口参数的用法
> 对于之前我们用到接口当中的方法时候，都是只有一个参数，当有多个参数的时候也是对应使用JavaBean进行的（insert update方法）。
> 但是在实际开发当中这样肯定是不行的，实际开发当中在一个接口当中肯定会有多个参数的，我们也不能为两三个参数去创建一个JavaBean方法（比较麻烦）。
> 那么现在，就来说一下怎么解决这个问题。在Mybatis当中提供了两种方法解决：① 使用Map类型作为参数 ② 使用@Param注解

① 使用Map方法
> 使用Map方式作为参数的方法，实际上就是通过Map当中的Key来映射XML当中SQL使用的参数的名字，value来存放参数值。需要多个参数的时候，通过map的key-value进行传递参数。
> 由于这种方式需要自己手动创建map以及对参数进行赋值，相对来说还是比较麻烦的。着重还是使用@param方法
```
    @Test
    public void testSelectRolesByUserIdAndRoleEnabled() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 调用方法selectRolesByUserIdAndRoleEnabled 查询用户的角色
            List<SysRole> userList = userMapper.selectRolesByUserIdAndRoleEnabled(1L, 1);
            Assert.assertNotNull(userList);  // 查询结果不为空
            Assert.assertTrue(userList.size() > 0);
        } finally {
            sqlSession.close();
        }
    }
```
> 如上面代码所示，调用方法selectRolesByUserIdAndRoleEnabled() 然后传递参数，相应的配置文件如下：
```xml
<select id="selectRolesByUserIdAndRoleEnabled" resultType="testrole.model.entity.SysRole">
        select
          r.id,
          r.role_name roleName,
          r.enabled ,
          r.create_by createBy,
          r.create_time createTime
        from sys_user u
          inner join sys_user_role ur on u.id = ur.user_id
          inner join sys_role r on ur.role_id = r.id
        where u.id = #{userId} and r.enabled = #{enabled}
    </select>
```
但是执行的过程后，发现出现这样的错误信息：
```markdown
    org.apache.ibatis.exceptions.PersistenceException: 
    ### Error querying database.  Cause: org.apache.ibatis.binding.BindingException: Parameter 'userId' not found. Available parameters are [0, 1, param1, param2]
    ### Cause: org.apache.ibatis.binding.BindingException: Parameter 'userId' not found. Available parameters are [0, 1, param1, param2]
```
这个错误信息表示xml可用的参数只用0,1,param1 ,param2,没有userId.0和1 ， param1 和param2 都是Mybatis根据参数位置自定义的名字，这是如果将xml当中的#{userId}改为#{0}或者 #{param1}也是可以的。
虽然说这样做可以，为了方便理解。但是实际开发当中不建议这样操作。
> 
```java
  where u.id = #{0} and r.enabled = #{1}
```
② 使用@Param注解方式



