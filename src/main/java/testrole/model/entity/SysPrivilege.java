package testrole.model.entity;

/**
 * @author chencong , Created in 2017/10/25 23:06
 */
public class SysPrivilege {

    private Long id;
    private String privilegeName;
    private String privilegeUrl;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeUrl() {
        return privilegeUrl;
    }

    public void setPrivilegeUrl(String privilegeUrl) {
        this.privilegeUrl = privilegeUrl;
    }

    public SysPrivilege(Long id, String privilegeName, String privilegeUrl) {
        this.id = id;
        this.privilegeName = privilegeName;
        this.privilegeUrl = privilegeUrl;
    }

    public SysPrivilege() {
    }

    @Override
    public String toString() {
        return "SysPrivilege{" +
                "id=" + id +
                ", privilegeName='" + privilegeName + '\'' +
                ", privilegeUrl='" + privilegeUrl + '\'' +
                '}';
    }
}
