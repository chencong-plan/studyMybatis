package testrole.model.entity;

import java.util.Date;

/**
 * @author chencong , Created in 2017/10/26 17:27
 */
public class SysRole {

    private Long id;
    private String roleName;
    private Integer enabled;
    private Long createBy;
    private Date createTime;

    private SysUser user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SysRole(Long id, String roleName, Integer enabled, Long createBy, Date createTime) {
        this.id = id;
        this.roleName = roleName;
        this.enabled = enabled;
        this.createBy = createBy;
        this.createTime = createTime;
    }

    public SysRole() {
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", enabled=" + enabled +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                '}';
    }
}
