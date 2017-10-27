package testrole.model.entity;

/**
 * @author chencong , Created in 2017/10/25 23:08
 */
public class SysUserRolePrivilege {

    private Long roleId;
    private Long privilegeId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public SysUserRolePrivilege(Long roleId, Long privilegeId) {
        this.roleId = roleId;
        this.privilegeId = privilegeId;
    }

    public SysUserRolePrivilege() {
    }

    @Override
    public String toString() {
        return "SysUserRolePrivilege{" +
                "roleId=" + roleId +
                ", privilegeId=" + privilegeId +
                '}';
    }
}
